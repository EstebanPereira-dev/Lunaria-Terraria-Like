package universite_paris8.iut.epereira.lunaria.modele;


import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

public class Terrain {
    private int width;
    private int height;
    //private int[][] tableau;
    private ObservableList<Integer> tableau;
    private boolean[][] tangibilite;

    private static final int[] TUILES_NON_TANGIBLES = {0,3,5};

    /*
    formules:
    passer tableau => liste:
    (y * longueur) + x = posListe

    passer Liste => Tableau:
    y = Math.floor(posListe/longueur)
    x = posListe%longueur

     */

    public Terrain(int width, int height) {
        this.width = width;
        this.height = height;
        tableau = FXCollections.observableArrayList();
        initTableau(ConfigurationJeu.WIDTH_TILES,ConfigurationJeu.HEIGHT_TILES);

        initTangibilite();
    }




    public void initTableau(int width, int height) {
        this.width = width;
        this.height = height;
        //this.tableau = new int [height][width];

//        for (int y = 0; y < height*width; y++) {
//            //tableau[y][x] = 0;
//            tableau.add(0);
//        }


        int profondeurTerre = 15;
        int hauteurHerbe = 1;
        int profondeurPierre=6;
        int profondeurCiel=ConfigurationJeu.HEIGHT_TILES-profondeurTerre-hauteurHerbe-profondeurPierre;

        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {

                if (y < profondeurCiel) {
                    tableau.add(0);// ciel
                } else if (y < profondeurCiel + hauteurHerbe) {
                    tableau.add(2); // herbe
                } else if (y < profondeurCiel + hauteurHerbe + profondeurTerre) {
                    tableau.add(1); // terre
                } else {
                    tableau.add(4); // pierre
                }
            }
        }



        for (int x = 0; x < width; x += 5) { // TOUS LES 5 BLOCS
            if (Math.random() < 0.5) { // 50% de chance d'avoir un buisson
                tableau.set(getPos(x,height-hauteurHerbe-profondeurTerre-profondeurPierre - 1),3); //3 = BUISSON
            }
        }
        creerArbre(20,27);


        initTangibilite();
    }

    public int tabY(int pos){
        return (int)Math.floor(pos/ConfigurationJeu.WIDTH_TILES);
    }
    public int tabX(int pos){
        return (int)pos%ConfigurationJeu.WIDTH_TILES;
    }

    public int getPos(int x, int y){
        return (int) y*width+x;
    }

  public void creerArbre(int x,int y) {
      tableau.set((getPos(x,y)),5);
      tableau.set((getPos(x,y-1)),5);
      tableau.set((getPos(x,y-2)),5);
      tableau.set((getPos(x,y-3)),5);
      tableau.set((getPos(x,y-4)),5);
      tableau.set((getPos(x,y-5)),5);
      tableau.set((getPos(x,y-6)),5);
      tableau.set((getPos(x,y-7)),5);

  }

    private void initTangibilite() {
        this.tangibilite = new boolean[height][width];

        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                tangibilite[y][x] = !estTypeNonTangible(tableau.get(getPos(x,y)));
            }
        }
    }

    private boolean estTypeNonTangible(int type) {
        for (int nonTangible : TUILES_NON_TANGIBLES) {
            if (type == nonTangible) {
                return true;
            }
        }
        return false;
    }

    public boolean estEnCollision(Acteur acteur) {
        double centreX = acteur.getPosX();
        double centreY = acteur.getPosY();
        double rayon = 17;

        // Calculer les limites de tuiles à vérifier
        int minTileX = Math.max(0, (int) ((centreX - rayon) / ConfigurationJeu.TAILLE_TUILE));
        int maxTileX = Math.min(getWidth() - 1, (int) ((centreX + rayon) / ConfigurationJeu.TAILLE_TUILE));
        int minTileY = Math.max(0, (int) ((centreY - rayon) / ConfigurationJeu.TAILLE_TUILE));
        int maxTileY = Math.min(getHeight() - 1, (int) ((centreY + rayon) / ConfigurationJeu.TAILLE_TUILE));

        // Vérifier les tuiles aux bords du cercle de collision
        for (int y = minTileY; y <= maxTileY; y++) {
            for (int x = minTileX; x <= maxTileX; x++) {
                if (tangibilite[y][x]) {
                    double tuileCentreX = x * ConfigurationJeu.TAILLE_TUILE + ConfigurationJeu.TAILLE_TUILE / 2;
                    double tuileCentreY = y * ConfigurationJeu.TAILLE_TUILE + ConfigurationJeu.TAILLE_TUILE / 2;

                    // Calcul de distance
                    double distX = Math.abs(centreX - tuileCentreX);
                    double distY = Math.abs(centreY - tuileCentreY);

                    // Test de collision avec une petite marge de tolérance
                    if (distX < (ConfigurationJeu.TAILLE_TUILE / 2 + rayon - 2) && distY < (ConfigurationJeu.TAILLE_TUILE / 2 + rayon - 2)) {
                        return true;
                    }
                }
            }
        }
        return false;
    }

    public boolean estAuSol(Acteur a) {
        double centreX = a.getPosX();
        double centreY = a.getPosY();
        double rayon = 17;

        double testY = centreY + rayon + 1;

        int[] pointsToCheck = {-5, 0, 5};

        for (int offset : pointsToCheck) {
            double testX = centreX + offset;
            int tileX = (int) (testX / ConfigurationJeu.TAILLE_TUILE);
            int tileY = (int) (testY / ConfigurationJeu.TAILLE_TUILE);

            if (tileY >= 0 && tileY < getHeight() && tileX >= 0 && tileX < getWidth()) {
                if (tangibilite[tileY][tileX]) {
                    return true;
                }
            }
        }

        return false;
    }
    // UTILE POUR POSER DES BLOCS ETC
    public void setTangible(int x, int y, boolean estTangible) {
        if (x >= 0 && x < width && y >= 0 && y < height) {
            tangibilite[y][x] = estTangible;
        }
    }

    public boolean estTangible(int x, int y) {
        if (x >= 0 && x < width && y >= 0 && y < height) {
            return tangibilite[y][x];
        }
        return false;
    }

    public void updateTangibilite() {
        initTangibilite();
    }

    public int getWidth() {
        return this.width;
    }

    public int getHeight() {
        return this.height;
    }

    public ObservableList<Integer> getTableau() {
        return tableau;
    }

    public void changerTuile(int blocDeRemplacement, int x, int y) {
        int pos = getPos(x, y);
        if (pos >= 0 && pos < tableau.size()) {
            tableau.set(pos, blocDeRemplacement);
            updateTangibilite();
            System.out.println("Le bloc en x=" + x + " et en y=" + y + " est remplacé par un bloc de type " + blocDeRemplacement);

            //test permettant d afficher le tableau
          /*  for (int i = 0; i < this.getTableau().length; i++) {
                for (int j = 0; j < this.getTableau()[i].length; j++)
                    System.out.print(this.getTableau()[i][j]);
                System.out.println("");
            }*/
        }
    }

    public int compterArbreAuDessus(int x, int y){ //compte le nombre de buches de bois en haut de la tuile selectionnée
        int compteur=1;
        while(tableau.get(getPos(x,y-compteur))==5){            //3=taille max d un arbre
                compteur++;
        }
        return compteur;
    }





}