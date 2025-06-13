package universite_paris8.iut.epereira.lunaria.modele.acteurs;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Platform;
import javafx.collections.ObservableList;
import javafx.util.Duration;
import universite_paris8.iut.epereira.lunaria.modele.*;
import universite_paris8.iut.epereira.lunaria.modele.acteurs.Ennemis.Ennemi;
import universite_paris8.iut.epereira.lunaria.modele.items.Consommables.Planche;

import java.util.ArrayList;

public class Hero extends Acteur {
    private InventaireJoueur inv;
    private ArrayList<Boolean> actions;
    private boolean haut = false, bas = false, droite = false, gauche = false, inventaire = false, pause = false, attaque = false;
    private int range, degat;
    private Item souris;
    private int quantiteItem;

    public Hero(Environement env) {
        super(env);
        actions = new ArrayList<>();
        inv = new InventaireJoueur();
        remplirAction();
        range = 5;
        degat = 10;
        souris = null;
        quantiteItem = 0;
    }

    public void remplirAction() {
        actions.add(haut);
        actions.add(bas);
        actions.add(droite);
        actions.add(gauche);
        actions.add(inventaire);
        actions.add(pause);
        actions.add(attaque);
    }

    @Override
    public void deplacement() {
        haut = actions.get(0);
        droite = actions.get(2);
        gauche = actions.get(3);

        appliquerGravite();

        if (haut && auSol) {
            vitesseY = SAUT;
            auSol = false;
        }

        deplacerVerticalement();

        double deltaX = 0;
        if (gauche) deltaX -= getVitesseX();
        if (droite) deltaX += getVitesseX();
        deplacerHorizontalement(deltaX);
    }

    public boolean peutAttaquer() {
        return !attackOnCooldown;
    }

    public boolean executerAttaque() {
        if (!peutAttaquer()) {
            System.out.println("COOLDOWN");
            return false;
        }

        // Marquer le début du cooldown - L'attaque se lance toujours
        attackOnCooldown = true;

        // Logique d'attaque - vérifier s'il y a des ennemis touchés
        boolean ennemisATouches = false;
        ArrayList<Acteur> ennemisASupprimer = new ArrayList<>();

        for (Acteur acteur : getEnv().getActeurs()) {
            if (acteur instanceof Ennemi) {
                double distanceX = Math.abs(getPosX() - acteur.getPosX());
                double distanceY = Math.abs(getPosY() - acteur.getPosY());
                double distance = Math.sqrt(distanceX * distanceX + distanceY * distanceY);

                if (distance <= range) {
                    acteur.setPv(acteur.getPv() - degat);
                    System.out.println("Ennemi touché ! Points de vie restants: " + acteur.getPv());
                    ennemisATouches = true;

                    if (acteur.getPv() <= 0) {
                        System.out.println("Ennemi vaincu !");
                        ennemisASupprimer.add(acteur);
                    }
                }
            }
        }

        // Supprimer les ennemis morts
        for (Acteur ennemi : ennemisASupprimer) {
            ennemi.estMort();
        }

        // Message si aucun ennemi touché
        if (!ennemisATouches) {
            System.out.println("Attaque dans le vide !");
        }

        // Démarrer le cooldown
        demarrerCooldown();

        // Retourne toujours true car l'attaque s'est lancée
        return true;
    }

    private void demarrerCooldown() {
        Timeline cooldownTimer = new Timeline(
                new KeyFrame(Duration.seconds(1), e -> {
                    this.attackOnCooldown = false;
                    System.out.println("Attaque dispo");
                })
        );
        cooldownTimer.setCycleCount(1);
        cooldownTimer.play();
    }
    @Override
    public void agit() {

    }

    public InventaireJoueur getInv() {
        return inv;
    }
    public int getRange() {
        return range;
    }

    public int getDegat() {
        return degat;
    }

    public ArrayList<Boolean> getActions() {
        return actions;
    }

    public boolean estDansRange(int tuileX, int tuileY) {
        int heroX = (int) (getPosX() / ConfigurationJeu.TAILLE_TUILE);
        int heroY = (int) (getPosY() / ConfigurationJeu.TAILLE_TUILE);
        int range = getRange(); // en nombre de cases

        int distanceX = Math.abs(tuileX - heroX);
        int distanceY = Math.abs(tuileY - heroY);

        return distanceX <= range && distanceY <= range;
    }

    public void casserArbre(int nbreBuches,int x, int y){
        Item planche = new Planche();
        for (int i = 0; i < nbreBuches; i++) {
            getInv().ajouterItem(planche, 1);
            // Supprimer la tuile du terrain
            getEnv().getTerrain().changerTuile(0, x, y - i);
            //optionnel, juste pour voir l'avancé
            System.out.println("+1 " + planche.getNom());
        }
        // Afficher le total de cet item dans l'inventaire
        int totalItem = getInv().compterItem(planche.getNom());
        System.out.println("Total " + planche.getNom() + " : " + totalItem);
    }

    public void casserBloc(ObservableList<Integer> terrain, int tuileX, int tuileY) {
        Item item = Item.getItemPourTuile(getEnv().getTerrain().getTableau().get(getEnv().getTerrain().getPos(tuileX, tuileY)));

        if (getEnv().getTerrain().getTableau().get(getEnv().getTerrain().getPos(tuileX, tuileY)) == 5) {
            casserArbre(getEnv().getTerrain().compterArbreAuDessus(tuileX, tuileY), tuileX, tuileY);
        } else if (getEnv().getTerrain().getTableau().get(getEnv().getTerrain().getPos(tuileX, tuileY)) != 5) {
            getInv().ajouterItem(item, 1);
            // Supprimer la tuile du terrain
            getEnv().getTerrain().changerTuile(0, tuileX, tuileY);
            //optionnel, juste pour voir l'avancé
            System.out.println("+1 " + item.getNom());
            // Afficher le total de cet item dans l'inventaire
            int totalItem = getInv().compterItem(item.getNom());
            System.out.println("Total " + item.getNom() + " : " + totalItem);
        }
    }
    public void placerBloc(int tuileX, int tuileY) {
        if (getEnv().estPositionOccupeeParActeur(tuileX, tuileY)) {
            System.out.println("Impossible de placer un bloc sur un acteur !");
        } else {
            int positionEquipe = getInv().getItemEquipe();

            if (positionEquipe != -1) {
                Item item = getInv().getListeditem().get(positionEquipe);

                // Vérification que l'item existe et peut être placé
                if (item != null && item.getPeutEtrePlace()) {
                    // Placer le bloc sur le terrain
                    getEnv().getTerrain().changerTuile(item.getId(), tuileX, tuileY);
                    // controleur.getGestionMap().chargerTiles(env.getTerrain());

                    // Retirer un item de l'inventaire
                    getInv().retirerItem(positionEquipe, 1);

                }
            }
        }
    }


    public int getQuantiteItem() {
        return quantiteItem;
    }

    public Item getSouris() {
        return souris;
    }

    public void setSouris(Item souris) {
        this.souris = souris;
    }

    public void setQuantiteItem(int quantiteItem) {
        this.quantiteItem = quantiteItem;
    }

    public boolean sourisVide() {
        System.out.println("item dans souris: " + souris.toString());
        System.out.println("Quantite Souris:" + quantiteItem);
        return souris.getId()<0;
    }
}