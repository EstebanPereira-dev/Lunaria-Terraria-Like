package universite_paris8.iut.epereira.lunaria.modele.acteurs;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Platform;
import javafx.collections.ObservableList;
import javafx.util.Duration;
import universite_paris8.iut.epereira.lunaria.modele.*;
import universite_paris8.iut.epereira.lunaria.modele.acteurs.Ennemis.Ennemi;
import universite_paris8.iut.epereira.lunaria.modele.items.Consommables.Buisson;
import universite_paris8.iut.epereira.lunaria.modele.items.Consommables.Herbe;
import universite_paris8.iut.epereira.lunaria.modele.items.Consommables.Planche;
import universite_paris8.iut.epereira.lunaria.modele.items.Consommables.Terre;
import universite_paris8.iut.epereira.lunaria.modele.items.Equipements.Armes.EpeeEnBois;
import universite_paris8.iut.epereira.lunaria.modele.items.Equipements.Outils.Hache;
import universite_paris8.iut.epereira.lunaria.modele.items.Equipements.Outils.Pioche;

import java.util.ArrayList;

public class Hero extends Acteur {
    private Inventaire inv;
    private ArrayList<Boolean> actions;
    private boolean haut = false, bas = false, droite = false, gauche = false, inventaire = false, pause = false, attaque = false, interraction = false;
    private int range, degat, faim;
    private int compteurFaim = 0;
    private int compteurRegen = 0;
    private final int INTERVALLE_REGEN = 250;
    private final int INTERVALLE_FAIM = 150;
    private Item souris;
    private int quantiteItem;

    public Hero(Environement env) {
        super(env);
        actions = new ArrayList<>();
        inv = new Inventaire();
        remplirAction();
        range = 16;
        degat = 10;
        souris = null;
        quantiteItem = 0;
        faim = 200;
    }

    public void remplirAction() {
        actions.add(haut);
        actions.add(bas);
        actions.add(droite);
        actions.add(gauche);
        actions.add(inventaire);
        actions.add(pause);
        actions.add(attaque);
        actions.add(interraction);
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
        boolean acteursATouches = false;
        ArrayList<Acteur> acteursASupprimer = new ArrayList<>();

        for (Acteur acteur : getEnv().getActeurs()) {
            // AJOUT : Empêcher le héros de s'attaquer lui-même
            if (acteur == this) {
                continue; // Passer au prochain acteur
            }

            double distanceX = Math.abs(getPosX() - acteur.getPosX());
            double distanceY = Math.abs(getPosY() - acteur.getPosY());
            double distance = Math.sqrt(distanceX * distanceX + distanceY * distanceY);

            if (distance <= range) {
                acteur.setPv(acteur.getPv() - degat);
                System.out.println("Ennemi touché ! Points de vie restants: " + acteur.getPv());
                acteursATouches = true;

                if (acteur.getPv() <= 0) {
                    setEcu(ecu.get() + acteur.getEcu());
                    System.out.println("Ennemi vaincu !");
                    acteursASupprimer.add(acteur);
                }
            }
        }

        // Supprimer les ennemis morts
        for (Acteur acteur : acteursASupprimer) {
            acteur.estMort();
        }

        // Message si aucun ennemi touché
        if (!acteursATouches) {
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

    @Override
    public void loot() {

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
        // Récupérer le type de bloc une seule fois
        int typeBloc = getEnv().getTerrain().getTableau().get(getEnv().getTerrain().getPos(tuileX, tuileY));

        // Vérifier que l'outil peut casser ce bloc (sécurité)
        Item outilEquipe = getInv().getItemEquipeSousFormeItem();
        if (!outilEquipe.peutCasser(typeBloc)) {
            System.out.println("Votre outil ne peut pas casser ce type de bloc !");
            return;
        }

        // Traitement spécial pour les arbres (bloc 5)
        if (typeBloc == 5) {
            casserArbre(getEnv().getTerrain().compterArbreAuDessus(tuileX, tuileY), tuileX, tuileY);
        } else {
            // Traitement normal pour les autres blocs
            casserBlocNormal(typeBloc, tuileX, tuileY);
        }
    }

    private void casserBlocNormal(int typeBloc, int tuileX, int tuileY) {
        // Créer l'item correspondant au bloc cassé selon le type
        Item itemRecolte;
        switch (typeBloc) {
            case 1: itemRecolte = new Terre(); break;
            case 2: itemRecolte = new Herbe(); break;
            case 3: itemRecolte = new Buisson(); break;
            default:itemRecolte = new Planche(); break;
        }

        // Ajouter l'item à l'inventaire
        getInv().ajouterItem(itemRecolte, 1);

        // Supprimer la tuile du terrain
        getEnv().getTerrain().changerTuile(0, tuileX, tuileY);

        // Messages informatifs
        System.out.println("+1 " + itemRecolte.getNom());
        int totalItem = getInv().compterItem(itemRecolte.getNom());
        System.out.println("Total " + itemRecolte.getNom() + " : " + totalItem);
    }
    public void placerBloc(int tuileX, int tuileY) {
        if (getEnv().estPositionOccupeeParActeur(tuileX, tuileY)) {
            System.out.println("Impossible de placer un bloc sur un acteur !");
        } else {
            int positionEquipe = getInv().getItemEquipe();

            if (positionEquipe != -1) {
                Item item = getInv().getListeditem().get(positionEquipe);
                int typeBloc = getEnv().getTerrain().getTableau().get(getEnv().getTerrain().getPos(tuileX, tuileY));

                // Vérification que l'item existe et peut être placé
                if (item != null && item.peutEtrePlaceSur(typeBloc)) { // ✅ Correction ici
                    // Placer le bloc sur le terrain
                    getEnv().getTerrain().changerTuile(item.getId(), tuileX, tuileY);
                    // controleur.getGestionMap().chargerTiles(env.getTerrain());

                    // Retirer un item de l'inventaire
                    getInv().retirerItem(positionEquipe, 1);
                }
            }
        }
    }
    public void saciete(){
        compteurFaim++;
        if (compteurFaim >= INTERVALLE_FAIM) {
            compteurFaim = 0; // Reset du compteur

            if (getFaim() == 0) {
                setPv(getPv() - 1);
            } else {
                setFaim(getFaim() - 1);
            }
        }
        compteurRegen++;
        if (compteurRegen >= INTERVALLE_REGEN){
            compteurRegen = 0;
            if (faim > 150 && getPv() < 100){
                setPv(getPv()+1);
            }
        }
    }
    public void initialiserHero(){
        getInv().ajouterItem(new EpeeEnBois());
        getInv().ajouterItem(new Pioche());
        getInv().ajouterItem(new Hache());
        getInv().equiperItem(0);
    }
    public void restaurerFaim(int nutriment){
        faim += nutriment;
    }

    public void setEcu(int ecu) {
        this.ecu.set(ecu);
    }

    public Inventaire getInv() {
        return inv;
    }
    public int getRange() {
        return range;
    }

    public int getDegat() {
        return degat;
    }

    public int getFaim() {
        return faim;
    }

    public void setFaim(int faim) {
        this.faim = faim;
    }

    public ArrayList<Boolean> getActions() {
        return actions;
    }

    public boolean verifAttaque(){
        Item itemEquipe = getInv().getItemEquipeSousFormeItem();
        if (itemEquipe == null) {
            return false;
        }
        return itemEquipe.getId() == 20 || itemEquipe.getId() == 21;
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
//        System.out.println("item dans souris: " + souris.toString());
//        System.out.println("Quantite Souris:" + quantiteItem);
        return souris == null;
    }
}