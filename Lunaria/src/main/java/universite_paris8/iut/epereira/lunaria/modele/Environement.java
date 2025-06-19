package universite_paris8.iut.epereira.lunaria.modele;

import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import universite_paris8.iut.epereira.lunaria.controleur.GestionInventaire;
import universite_paris8.iut.epereira.lunaria.modele.Craft.*;
import universite_paris8.iut.epereira.lunaria.modele.acteurs.Ennemis.Adepte;
import universite_paris8.iut.epereira.lunaria.modele.acteurs.Ennemis.Aigle;
import universite_paris8.iut.epereira.lunaria.modele.acteurs.Hero;
import universite_paris8.iut.epereira.lunaria.modele.acteurs.mobPassif.Aleksa;
import universite_paris8.iut.epereira.lunaria.modele.acteurs.mobPassif.Mouton;
import universite_paris8.iut.epereira.lunaria.modele.acteurs.mobPassif.PNJ;
import universite_paris8.iut.epereira.lunaria.modele.items.Consommables.Planche;
import universite_paris8.iut.epereira.lunaria.modele.items.Consommables.Terre;
import universite_paris8.iut.epereira.lunaria.modele.items.Equipements.Armes.EpeeEnBois;
import universite_paris8.iut.epereira.lunaria.modele.items.Equipements.Outils.Hache;
import universite_paris8.iut.epereira.lunaria.modele.items.Equipements.Outils.Pioche;
import universite_paris8.iut.epereira.lunaria.modele.items.Equipements.Outils.PiocheBois;

import javax.crypto.spec.PSource;
import java.security.cert.CertificateNotYetValidException;
import java.util.ArrayList;
import java.util.List;

public class Environement {
    private Terrain terrain;
    private Hero hero;
    private char cylceJourNuit;
    private int width;
    private int height;
    private ArrayList<Acteur> acteurs;
    //rajouter une liste de spawns
    private ArrayList<PNJ> pnjs;
    private int compteurFaim = 0;
    private final int INTERVALLE_FAIM = 140;
    private List<Acteur> acteursASupprimer = new ArrayList<>();
    // Spawner pour les ennemis
    private Adepte spawnerAdepte;
    private Aigle spawnerAigle;
    private BooleanProperty etatJour;    //permet de savoir si on est le jour ou la nuit
    //true=jour, false=nuit
    private Inventaire marchand;
    private ArrayList<Craft> listeCraft;
    //private ObservableList<Craft> craftingListVue;

    public Environement(int width, int height) {
        this.terrain = new Terrain(width / ConfigurationJeu.TAILLE_TUILE, height / ConfigurationJeu.TAILLE_TUILE);
        this.hero = new Hero(this);
        hero.initialiserHero();
        pnjs = new ArrayList<>();
        acteurs = new ArrayList<>();
        //craftingListVue = FXCollections.observableArrayList();
        listeCraft = new ArrayList<>();

        listeCraft.add(new CraftHacheBois(this));
        listeCraft.add(new CraftPiocheBois(this));
        listeCraft.add(new CraftHachePierre(this));
        listeCraft.add(new CraftPiochePierre(this));

        acteurs.add(new Mouton(20, 1, this, 500, 400));

        acteurs.add(new Aleksa(100, 1, this, 800, 400));

        acteurs.add(hero);

        this.height = height;
        this.width = width;

        spawnerAdepte = new Adepte(1, 1, 1, 50, this, hero, 0, 0,5);
        spawnerAigle = new Aigle(1, 1, 1, 50, this, hero, 0, 0,5);
        this.etatJour =new SimpleBooleanProperty(true);
        initTest();
        spawnerAdepte = new Adepte(1, 1, 1, 50, this, hero, 0, 0,5);
        this.etatJour.set(true);
    }

    public void update() { //faire agir tout le monde et supprimer les morts
        spawnerAdepte.spawner();
        spawnerAigle.spawner();
        spawnerAdepte.spawner(); //faire plutot une classe qui gere les spawns (reservoir de spawn dans env)
        supprimerActeursMarques();
        getHero().saciete();
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


    public void updateCraft(){
//        System.out.println("update craft");
//        for(int i = 0; i < listeCraft.size();i++){
//            if(listeCraft.get(i).craftable() && !craftingListVue.contains(listeCraft.get(i))){
//                craftingListVue.add(listeCraft.get(i));
//            } else if (craftingListVue.contains(listeCraft.get(i))) {
//                craftingListVue.remove(listeCraft.get(i));
//            }
//
//        }
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
        for (Acteur a : this.acteurs) {
            if (a.getId().equals(id)) {
                return a;
            }
        }
        return null;
    }

    public void ajouter(Acteur a) {
        acteurs.add(a);
    }

    public void retirer(Acteur a) {
        acteurs.remove(a);
    }

    public int getWidth() {
        return width;
    }

//    public ObservableList<Craft> getCraftingList() {
//        return craftingListVue;
//    }


    public ArrayList<Craft> getListeCraft() {
        return listeCraft;
    }

    public char getCylceJourNuit() {
        return cylceJourNuit;
    }

    public Hero getHero() {
        return hero;
    }

    public PNJ getPNJ(int id) {
        return pnjs.get(id);
    }

    public ArrayList<PNJ> getPNJs() {
        for (Acteur pnj : getActeurs()) {
            if (pnj instanceof PNJ)
                pnjs.add((PNJ) pnj);
        }
        return pnjs;
    }

    public Terrain getTerrain() {
        return terrain;
    }

    public Inventaire getMarchand() {
        return marchand;
    }

    public void setMarchand(Inventaire marchand) {
        this.marchand = marchand;
        System.out.println("inv updated");
    }

    public boolean verifCasser(int x, int y) {
        Item itemEquipe = this.getHero().getInv().getItemEquipeSousFormeItem();

        if (itemEquipe == null) {
            return false;
        }

        int typeBloc = this.getTerrain().getTableau().get(getTerrain().getPos(x, y));
        return itemEquipe.peutCasser(typeBloc);
    }

    public void changerEtatJour() {
        if (etatJour.getValue())
            etatJour.set(false);
        else
            etatJour.set(true);
    }

    public BooleanProperty getEtatJour(){
        return etatJour;
    }



    public void initTest() {
        Item item = new Terre();
        hero.getInv().getListeditem().set(19, item);
        hero.getInv().getQuantite()[19].set(10);

    }

}