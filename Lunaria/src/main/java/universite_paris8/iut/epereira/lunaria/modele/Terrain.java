package universite_paris8.iut.epereira.lunaria.modele;

import javafx.scene.control.Tab;

public class Terrain {
    private int width;
    private int height;
    private int[][] tableau;
    private boolean[][] tangibilite;
    private int TAILLE_TUILE;

    public Terrain(int width, int height,int TAILLE_TUILE) {
        this.TAILLE_TUILE = TAILLE_TUILE;
        this.width = width;
        this.height = height;
        this.tableau = new int[][] {
                {4, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 4},
                {4, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 4},
                {4, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 4},
                {4, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 4},
                {4, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 4},
                {4, 0, 0, 0, 0, 4, 4, 4, 4, 4, 4, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 4},
                {4, 0, 0, 0, 0, 4, 5, 5, 5, 5, 4, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 4},
                {4, 0, 0, 0, 0, 4, 5, 5, 5, 5, 4, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 4},
                {4, 0, 0, 0, 0, 4, 5, 5, 5, 5, 4, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 4},
                {4, 0, 0, 0, 0, 4, 5, 5, 5, 5, 4, 4, 0, 0, 0, 0, 0, 0, 0, 0, 0, 4},
                {4, 0, 0, 0, 0, 4, 5, 5, 5, 5, 5, 5, 0, 0, 0, 0, 0, 0, 0, 0, 0, 4},
                {2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 0, 0, 0, 2, 2, 2, 2},
                {1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 2, 2, 2, 1, 1, 1, 1},
                {1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1},
                {1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1},
                {1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1}
        };

        initTangibilite();
    }

    public boolean estEnCollision(Acteur acteur) {
        double centreX = acteur.getPosX();
        double centreY = acteur.getPosY();
        double rayon = 10;

        // Calculer les limites de tuiles à vérifier
        int minTileX = Math.max(0, (int) ((centreX - rayon) / TAILLE_TUILE));
        int maxTileX = Math.min(getWidth() - 1, (int) ((centreX + rayon) / TAILLE_TUILE));
        int minTileY = Math.max(0, (int) ((centreY - rayon) / TAILLE_TUILE));
        int maxTileY = Math.min(getHeight() - 1, (int) ((centreY + rayon) / TAILLE_TUILE));

        // Vérifier les tuiles aux bords du cercle de collision
        for (int y = minTileY; y <= maxTileY; y++) {
            for (int x = minTileX; x <= maxTileX; x++) {
                // Vérifier uniquement les tuiles solides (pas d'air ni de bois)
                if (getTableau()[y][x] != 0 && getTableau()[y][x] != 5) {
                    double tuileCentreX = x * TAILLE_TUILE + TAILLE_TUILE / 2;
                    double tuileCentreY = y * TAILLE_TUILE + TAILLE_TUILE / 2;

                    // Calcul de distance optimisé
                    double distX = Math.abs(centreX - tuileCentreX);
                    double distY = Math.abs(centreY - tuileCentreY);

                    // Test de collision avec une petite marge de tolérance
                    if (distX < (TAILLE_TUILE / 2 + rayon - 2) && distY < (TAILLE_TUILE / 2 + rayon - 2)) {
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
        double rayon = 10;

        double testY = centreY + rayon + 1;

        int[] pointsToCheck = {-5, 0, 5};

        for (int offset : pointsToCheck) {
            double testX = centreX + offset;
            int tileX = (int) (testX / TAILLE_TUILE);
            int tileY = (int) (testY / TAILLE_TUILE);

            if (tileY >= 0 && tileY < getHeight() && tileX >= 0 && tileX < getWidth()) {
                int tileType = getTableau()[tileY][tileX];
                if (tileType != 0 && tileType != 5) {
                    return true;
                }
            }
        }

        return false; // Aucun point n'est au sol
    }

    private void initTangibilite() {
        this.tangibilite = new boolean[height][width];

        for (int i = 0; i < height; i++) {
            for (int j = 0; j < width; j++) {
                tangibilite[i][j] = (tableau[i][j] != 0);
            }
        }
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

}