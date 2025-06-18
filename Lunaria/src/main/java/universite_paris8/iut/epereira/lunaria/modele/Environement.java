package universite_paris8.iut.epereira.lunaria.modele;

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
    private ArrayList<PNJ> pnjs;
    private int compteurFaim = 0;
    private final int INTERVALLE_FAIM = 140;
    private List<Acteur> acteursASupprimer = new ArrayList<>();
    // Spawner pour les ennemis
    private Adepte spawnerAdepte;
    private Aigle spawnerAigle;
    private boolean etatJour;    //permet de savoir si on est le jour ou la nuit
    //true=jour, false=nuit
    private Inventaire marchand;
    private ArrayList<Craft> listeCraft;
    private ObservableList<Craft> craftingListVue;

    public Environement(int width, int height) {
        this.terrain = new Terrain(width / ConfigurationJeu.TAILLE_TUILE, height / ConfigurationJeu.TAILLE_TUILE);
        this.hero = new Hero(this);
        hero.initialiserHero();
        pnjs = new ArrayList<>();
        acteurs = new ArrayList<>();
        craftingListVue = FXCollections.observableArrayList();
        listeCraft = new ArrayList<>();

        listeCraft.add(new CraftPiocheBois(this));
        listeCraft.add(new CraftPiochePierre(this));
        listeCraft.add(new CraftHacheBois(this));
        listeCraft.add(new CraftHachePierre(this));

        acteurs.add(new Mouton(20, 1, this, 200, 400));

        acteurs.add(new Aleksa(100, 1, this, 300, 400));

        acteurs.add(hero);

        this.height = height;
        this.width = width;

        spawnerAdepte = new Adepte(1, 1, 1, 50, this, hero, 0, 0,5);
        spawnerAigle = new Aigle(1, 1, 1, 50, this, hero, 0, 0,5);
        this.etatJour =true;
        initTest();
        spawnerAdepte = new Adepte(1, 1, 1, 50, this, hero, 0, 0);
        this.etatJour = true;
    }

    public void update() { //faire agir tout le monde et supprimer les morts
        spawnerAdepte.spawner();
        spawnerAigle.spawner();
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
        for(int i = 0; i < listeCraft.size();i++){
            if(listeCraft.get(i).craftable()){
                craftingListVue.add(listeCraft.get(i));
            } else if (craftingListVue.contains(craftingListVue.get(i))) {
                craftingListVue.remove(craftingListVue.get(i));
            }

        }
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

    public ObservableList<Craft> getCraftingList() {
        return craftingListVue;
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
        if (etatJour)
            etatJour = false;
        else
            etatJour = true;
    }

    public boolean getEtatJour() {
        return etatJour;
    }


    //interaction inventaire hero

    //click gauche echange l'item dans la souris avec l'item dans l'inventaire avec les quantite
    public void clickPrimaire(int index) {
        Item itemTemp = hero.getSouris();
        int quantiteTemp = hero.getQuantiteItem();
        System.out.println(itemTemp);
        System.out.println(quantiteTemp);
        //si on appuie sur une case non null avec un item dans la souris
        if (hero.getSouris() != null && hero.getInv().getListeditem().get(index) != null) {
            //si l'item dans la souris et l'item dans l'inventaire est le meme
            if (hero.getSouris().getId() == hero.getInv().getListeditem().get(index).getId()) {
                hero.setSouris(null);
                //si en ajoutant la quantite de la souris dans l'inventaire, possibilité de dépasser la taille max
                if ((hero.getInv().getQuantite()[index].getValue() + hero.getQuantiteItem()) > (hero.getInv().getListeditem().get(index).getStackMax()) ) {

                    hero.setQuantiteItem((hero.getQuantiteItem() + hero.getInv().getQuantite()[index].getValue()) - hero.getInv().getListeditem().get(index).getStackMax());
                    hero.getInv().getQuantite()[index].set(hero.getInv().getListeditem().get(index).getStackMax());
                } else {
                    hero.getInv().getQuantite()[index].set(hero.getInv().getQuantite()[index].getValue() + hero.getQuantiteItem());
                    hero.setQuantiteItem(0);
                }
            }
        } else {

            hero.setQuantiteItem(hero.getInv().getQuantite()[index].getValue());
            hero.setSouris(hero.getInv().getListeditem().get(index));

            hero.getInv().getQuantite()[index].set(quantiteTemp);
            hero.getInv().getListeditem().set(index, itemTemp);
        }
    }

    //click droit avec la souris vide pour prendre la moitier des item dans l'invnetaire et le mettre dans la souris
    public void clickSecondaireVide(int index) {
        int qt = hero.getInv().getQuantite()[index].getValue();

        if (hero.sourisVide() && hero.getInv().getQuantite()[index].getValue() > 1 && hero.getInv().getListeditem().get(index) != null) {

            hero.setSouris(hero.getInv().getListeditem().get(index));

            hero.setQuantiteItem((int) Math.floorDiv(qt, 2) + Math.floorMod(qt, 2));
            hero.getInv().getQuantite()[index].set(Math.floorDiv(qt, 2));
        } else if (hero.getInv().getQuantite()[index].getValue() == 1) {
            clickPrimaire(index);
        }
    }

    //click droit avec la souris qui contient des item pose 1 item dans l'inventaire
    public void clickSecondairePlein(int index) {
//        System.out.println("entrer 1");
//        System.out.println(hero.getSouris());
//        System.out.println(hero.sourisVide());
        if (!hero.sourisVide()) {
            if (hero.getQuantiteItem() == 1) {
                clickPrimaire(index);
            }
            //System.out.println("entrer 2");
            if (hero.getInv().getListeditem().get(index) == null) {
                //System.out.println("entrer 3");
                hero.getInv().getListeditem().set(index, hero.getSouris());
                hero.getInv().getQuantite()[index].setValue(1);
                hero.setQuantiteItem(hero.getQuantiteItem() - 1);
            } else {
                //System.out.println("entrer 4");
                if(hero.getInv().getListeditem().get(index) != null){
                    if (hero.getInv().getListeditem().get(index).getId() == hero.getSouris().getId()) {
                        //System.out.println("entrer 5");
                        hero.getInv().getQuantite()[index].set(hero.getInv().getQuantite()[index].getValue() + 1);
                        hero.setQuantiteItem(hero.getQuantiteItem() - 1);
                    }
                }
            }
            if (hero.getQuantiteItem() == 0) {
                //System.out.println("entrer 6");
                hero.setSouris(null);
            }
        }
    }


    public void initTest() {
        Item item = new Terre();
        hero.getInv().getListeditem().set(19, item);
        hero.getInv().getQuantite()[19].set(10);

    }

}