package universite_paris8.iut.epereira.lunaria.modele.acteurs.Ennemis;

import universite_paris8.iut.epereira.lunaria.modele.Acteur;
import universite_paris8.iut.epereira.lunaria.modele.ConfigurationJeu;
import universite_paris8.iut.epereira.lunaria.modele.Environement;
import universite_paris8.iut.epereira.lunaria.modele.acteurs.Hero;

import static universite_paris8.iut.epereira.lunaria.modele.ConfigurationJeu.rdm;

public class Adepte extends Ennemi {

    private static long dernierSpawn = 0;

    public Adepte(int pv, int v, int degat, int range, Environement env, Hero hero, double x, double y) {
        super(pv, v, degat, range, env, hero, x, y);
    }

    @Override
    public void deplacement() {
        int aleatoire = rdm.nextInt(250);
        appliquerGravite();
        if (getPosX() < hero.getPosX())
            deplacerHorizontalement(vitesseX);
        else
            deplacerHorizontalement(-vitesseX);
        if (auSol && aleatoire <= 1)
            vitesseY = SAUT;
        deplacerVerticalement();
    }

    @Override
    public void agit() {
        double distanceX = Math.abs(getPosX() - hero.getPosX());
        double distanceY = Math.abs(getPosY() - hero.getPosY());
        double distance = Math.sqrt(distanceX * distanceX + distanceY * distanceY);

        if (distance <= range) {
            hero.setPv(hero.getPv() - getDegat());
            System.out.println("Hero touché ! Points de vie restants: " + hero.getPv());

            if (hero.getPv() <= 0) {
                System.out.println("Hero vaincu !");
                hero.estMort();
            }
        }
    }

    @Override
    public boolean conditionApparation() {
        long maintenant = System.currentTimeMillis();
        return (maintenant - dernierSpawn) > 90000 &&
                rdm.nextInt(100) < 30; // 30% de chance
    }

    @Override
    public void spawner() {
        if (!conditionApparation()) return;

        double x = rdm.nextDouble() * getEnv().getWidth();
        double y = trouverHauteurSol(x) - 20; // 20 pixels au-dessus du sol

        int pv = 15 + rdm.nextInt(10);
        int vitesse = 1 + rdm.nextInt(2);
        int degats = 8 + rdm.nextInt(5);

        Adepte nouvelAdepte = new Adepte(pv, vitesse, degats, range, getEnv(), hero, x, y);
        getEnv().ajouter(nouvelAdepte);
        dernierSpawn = System.currentTimeMillis();
    }

    private double trouverHauteurSol(double x) {
        int tileX = (int) (x / ConfigurationJeu.TAILLE_TUILE);
        int[][] terrain = getEnv().getTerrain().getTableau();

        // Parcourir de haut en bas pour trouver le premier bloc tangible
        for (int tileY = 0; tileY < terrain.length; tileY++) {
            if (getEnv().getTerrain().estTangible(tileX, tileY)) {
                return tileY * ConfigurationJeu.TAILLE_TUILE;
            }
        }

        return getEnv().getHeight(); // Si pas de sol trouvé, spawner en bas
    }
}