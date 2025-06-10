package universite_paris8.iut.epereira.lunaria.modele;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import universite_paris8.iut.epereira.lunaria.controleur.GestionInventaire;
import universite_paris8.iut.epereira.lunaria.modele.acteurs.Ennemis.Adepte;
import universite_paris8.iut.epereira.lunaria.modele.acteurs.Hero;
import universite_paris8.iut.epereira.lunaria.modele.acteurs.mobPassif.Mouton;
import universite_paris8.iut.epereira.lunaria.modele.items.Consommables.Planche;
import universite_paris8.iut.epereira.lunaria.modele.items.Equipements.Armes.EpeeEnBois;
import universite_paris8.iut.epereira.lunaria.modele.items.Equipements.Outils.Hache;
import universite_paris8.iut.epereira.lunaria.modele.items.Equipements.Outils.Pioche;

import java.util.ArrayList;
import java.util.List;

public class Environement {
    private Terrain terrain;
    private Hero hero;
    private char cylceJourNuit;
    private int width;
    private int height;
    private ArrayList<Acteur> acteurs;
    private List<Acteur> acteursASupprimer = new ArrayList<>();
    // Spawner pour les ennemis
    private Adepte spawnerAdepte;

    public Environement(int width, int height){
        this.terrain = new Terrain(width/ConfigurationJeu.TAILLE_TUILE,height/ConfigurationJeu.TAILLE_TUILE);
        this.hero = new Hero(this);
        initialiserHero();
        acteurs = new ArrayList<>();

        acteurs.add(new Mouton(20, 1, this, 200, 400));

        acteurs.add(hero);

        this.height = height;
        this.width = width;

        spawnerAdepte = new Adepte(1, 1, 1, 50, this, hero, 0, 0);
    }

    public void update() { //faire agir tout le monde et supprimer les morts
        spawnerAdepte.spawner();

        supprimerActeursMarques();
    }

    public void marquerPourSuppression(Acteur acteur) {
        acteursASupprimer.add(acteur);
    }

    public void supprimerActeursMarques() {
        for (Acteur acteur : acteursASupprimer) {
            acteurs.remove(acteur);
        }
        acteursASupprimer.clear();
    }

    // GETTER :
    public List<Acteur> getActeursASupprimer() {
        return new ArrayList<>(acteursASupprimer);
    }

    public int getHeight() {
        return height;
    }

    public ArrayList<Acteur> getActeurs() {
        return acteurs;
    }

    public Acteur getActeur(String id) {
        for(Acteur a:this.acteurs){
            if(a.getId().equals(id)){
                return a;
            }
        }
        return null;
    }

    public void ajouter(Acteur a){
        acteurs.add(a);
    }

    public void retirer(Acteur a){
        acteurs.remove(a);
    }

    public int getWidth() {
        return width;
    }

    public char getCylceJourNuit() {
        return cylceJourNuit;
    }

    public Hero getHero() {
        return hero;
    }

    public Terrain getTerrain() {
        return terrain;
    }



    public boolean estPositionOccupeeParActeur(int tuileX, int tuileY) {
        for (Acteur acteur : getActeurs()) {
            if (acteur instanceof Hero) {
                int heroX = (int) (acteur.getPosX() / ConfigurationJeu.TAILLE_TUILE);
                int heroY = (int) (acteur.getPosY() / ConfigurationJeu.TAILLE_TUILE);

                if ((tuileX == heroX - 1 || tuileX == heroX || tuileX == heroX + 1) && (tuileY == heroY - 1 || tuileY == heroY || tuileY == heroY + 1)) {
                    return true;
                } else {
                    int acteurX = (int) (acteur.getPosX() / ConfigurationJeu.TAILLE_TUILE);
                    int acteurY = (int) (acteur.getPosY() / ConfigurationJeu.TAILLE_TUILE);

                    if (tuileX == acteurX && tuileY == acteurY) {
                        return true;
                    }
                }
            }
        }
        return false;
    }

    public boolean verifCasser(int x, int y){
        //cas 1: casser la terre ou la pierre

        if(this.getHero().getInv().getItemEquipeSousFormeItem().getId()==31 && (this.getTerrain().getTableau().get(getTerrain().getPos(x,y))==1 || this.getTerrain().getTableau().get(getTerrain().getPos(x,y))==2 || this.getTerrain().getTableau().get(getTerrain().getPos(x,y))==3 || this.getTerrain().getTableau().get(getTerrain().getPos(x,y))==4)){
            return true;
        }
        //cas 2: casser le bois

        else if (this.getHero().getInv().getItemEquipeSousFormeItem().getId()==30 && (this.getTerrain().getTableau().get(getTerrain().getPos(x,y))==5 || this.getTerrain().getTableau().get(getTerrain().getPos(x,y))==6)){
            return true;
        }
        else {
            return false;
        }
    }

    public void initialiserHero(){
        this.getHero().getInv().ajouterItem(new EpeeEnBois());
        this.getHero().getInv().ajouterItem(new Pioche());
        this.getHero().getInv().ajouterItem(new Hache());
        this.getHero().getInv().equiperItem(0);
    }




}