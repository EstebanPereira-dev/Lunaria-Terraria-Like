package universite_paris8.iut.epereira.lunaria.modele;


import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import static universite_paris8.iut.epereira.lunaria.modele.Acteur.random;

public class Terrain {
    private int width;
    private int height;
    private ObservableList<Integer> tableau;
    private boolean[][] tangibilite;

    private static final int[] TUILES_NON_TANGIBLES = {0, 3, 5,7};

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
        initTableau(ConfigurationJeu.WIDTH_TILES, ConfigurationJeu.HEIGHT_TILES);

        initTangibilite();
    }
    public void initTableau(int width, int height) {
        this.width = width;
        this.height = height;

        tableau.clear();

        int profondeurTerre = 6;
        int hauteurHerbe = 1;
        int profondeurPierre = 13;
        int profondeurCiel = ConfigurationJeu.HEIGHT_TILES - profondeurTerre - hauteurHerbe - profondeurPierre;

        // CRÉER UN LÉGER RELIEF
        int[] hauteurSol = new int[width];
        int niveauSolBase = profondeurCiel + hauteurHerbe;

        // Générer un léger relief naturel (max +/- 4 blocs)
        for (int x = 0; x < width; x++) {
            double ondulation = 2 * Math.sin(x * 0.05) + 1 * Math.sin(x * 0.1);
            hauteurSol[x] = niveauSolBase + (int)ondulation;

            // Garder dans des limites raisonnables
            hauteurSol[x] = Math.max(niveauSolBase - 3, Math.min(niveauSolBase + 3, hauteurSol[x]));
        }

        // GÉNÉRATION DU TERRAIN DE BASE
        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                int niveauSol = hauteurSol[x];

                if (y < niveauSol) {
                    tableau.add(0); // ciel
                } else if (y == niveauSol) {
                    tableau.add(2); // herbe
                } else if (y < niveauSol + profondeurTerre) {
                    tableau.add(1); // terre
                } else {
                    tableau.add(4); // pierre
                }
            }
        }

        // Quelques poches de pierre dans la terre
        for (int i = 0; i < 5; i++) {
            int x = 3 + (int)(Math.random() * (width - 6));
            int y = niveauSolBase + 3 + (int)(Math.random() * 6);
            creerPochePierreSimple(x, y);
        }


        // Forêt principale - 8 arbres bien espacés
        for (int i = 0; i < 8; i++) {
            int x = 8 + i * (width / 9);
            if (x >= 2 && x < width - 2) {
                int surfaceY = hauteurSol[x] - 1;
                if (estPositionLibre(x, surfaceY, 1)) {
                    int hauteurArbre = 6 + (int)(Math.random() * 3); // 6-8 blocs
                    creerArbre(x, surfaceY, hauteurArbre);
                }
            }
        }

        // Quelques arbres supplémentaires
        for (int i = 0; i < 4; i++) {
            int x = 5 + (int)(Math.random() * (width - 10));
            int surfaceY = hauteurSol[x] - 1;
            if (estPositionLibre(x, surfaceY, 3)) {
                int hauteurArbre = 5 + (int)(Math.random() * 3);
                creerArbre(x, surfaceY, hauteurArbre);
            }
        }

        // BUISSONS
        for (int x = 0; x < width; x += 4) {
            int surfaceY = hauteurSol[x] - 1;
            if (Math.random() < 0.3 && estPositionLibre(x, surfaceY, 1)) {
                tableau.set(getPos(x, surfaceY), 3); // buisson
            }
        }


        // Quelques branches tombées
        for (int i = 0; i < 3; i++) {
            int x = (int)(Math.random() * width);
            int surfaceY = hauteurSol[x] - 1;
            if (x >= 0 && x < width && tableau.get(getPos(x, surfaceY)) == 0) {
                tableau.set(getPos(x, surfaceY), 5); // bois
            }
        }

        initTangibilite();
    }


    private void creerPochePierreSimple(int centreX, int centreY) {
        if (centreX < 1 || centreX >= width-1 || centreY < 1 || centreY >= height-1) {
            return;
        }

        // Petite poche 3x3
        for (int dy = -1; dy <= 1; dy++) {
            for (int dx = -1; dx <= 1; dx++) {
                int x = centreX + dx;
                int y = centreY + dy;

                if (x >= 0 && x < width && y >= 0 && y < height && Math.random() < 0.7) {
                    tableau.set(getPos(x, y), 4); // pierre
                }
            }
        }
    }

    private boolean estPositionLibre(int x, int y, int largeur) {
        if (x < 0 || y < 0 || x + largeur > width || y >= height) {
            return false;
        }

        for (int i = 0; i < largeur; i++) {
            if (tableau.get(getPos(x + i, y)) != 0) {
                return false;
            }
        }
        return true;
    }

    private void creerArbre(int x, int y, int hauteur) {
        if (x < 0 || x >= width || y < 0 || y >= height) {
            return;
        }

        // Tronc
        for (int i = 0; i < hauteur; i++) {
            int posY = y - i;
            if (posY >= 0 && posY < height) {
                tableau.set(getPos(x, posY), 5); // bois
            }
        }

        // Feuillage simple
        int topY = y - hauteur;
        if (topY >= 0 && topY < height) {
            // Sommet
            tableau.set(getPos(x, topY), 7);
            if (x > 0) tableau.set(getPos(x - 1, topY), 7);
            if (x < width - 1) tableau.set(getPos(x + 1, topY), 7);

            // Couche du milieu
            if (topY + 1 < height) {
                if (x > 0) tableau.set(getPos(x - 1, topY + 1), 7);
                if (x < width - 1) tableau.set(getPos(x + 1, topY + 1), 7);
                if (x > 1) tableau.set(getPos(x - 2, topY + 1), 7);
                if (x < width - 2) tableau.set(getPos(x + 2, topY + 1), 7);
            }
        }
    }


    public int getPos(int x, int y) {
        if (x < 0 || x >= width || y < 0 || y >= height) {
            return 0;
        }
        return y * width + x;
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
        while(tableau.get(getPos(x,y-compteur))==5){
                compteur++;
        }
        return compteur;
    }
}