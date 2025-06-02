package universite_paris8.iut.epereira.lunaria.modele;


public class Terrain {
    private int width;
    private int height;
    private int[][] tableau;
    private boolean[][] tangibilite;

    private static final int[] TUILES_NON_TANGIBLES = {0,3,5};

    public Terrain(int width, int height) {
        this.width = width;
        this.height = height;
        initTableau(ConfigurationJeu.WIDTH_TILES,ConfigurationJeu.HEIGHT_TILES);

        initTangibilite();
    }
    public void initTableau(int width, int height) {
        this.width = width;
        this.height = height;
        this.tableau = new int [height][width];

        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                tableau[y][x] = 0;
            }
        }


        int profondeurTerre = 15;
        int hauteurHerbe = 1;
        int profondeurPierre=6;

        for (int y = height - profondeurTerre; y < height; y++) {
            for (int x = 0; x < width; x++) {
                if (y == height - profondeurTerre) {
                    tableau[y][x] = 2;
                } else if(y > height - profondeurPierre) {
                    tableau[y][x] = 4;
                }
                else {
                    tableau[y][x] = 1;
                }
            }
        }

        for (int x = 0; x < width; x += 5) { // TOUS LES 5 BLOCS
            if (Math.random() < 0.5) { // 50% de chance d'avoir un buisson
                tableau[height - profondeurTerre - 1][x] = 3; //3 = BUISSON
            }
        }
        creerArbre(34,39);


        initTangibilite();
    }

    public void creerArbre(int x,int y){
        tableau[x][y]=5;
        tableau[x-1][y]=5;
        tableau[x-2][y]=5;
        tableau[x-3][y]=5;
    }

    private void initTangibilite() {
        this.tangibilite = new boolean[height][width];

        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                tangibilite[y][x] = !estTypeNonTangible(tableau[y][x]);
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

    public int[][] getTableau() {
        return tableau;
    }

    public void changerTuile(int blocDeRemplacement, int x, int y) {
            this.getTableau()[y][x] = blocDeRemplacement;
            updateTangibilite();
            System.out.println("Le bloc en x=" + x + " et en y=" + y + " est remplacé par un bloc de type " + blocDeRemplacement);

            //test permettant d afficher le tableau
          /*  for (int i = 0; i < this.getTableau().length; i++) {
                for (int j = 0; j < this.getTableau()[i].length; j++)
                    System.out.print(this.getTableau()[i][j]);
                System.out.println("");
            }*/


    }
    public int compterArbreAuDessus(int x, int y){ //compte le nombre de buches de bois en haut de la tuile selectionnée
        int compteur=1;
        for (int i= 1; i<3; i++){            //3=taille max d un arbre
            if (getTableau()[y-i][x]==5){
                compteur++;
            }
        }
        return compteur;
    }


    public void setTableau(int[][] tableau) {
        this.tableau = tableau;
    }


}