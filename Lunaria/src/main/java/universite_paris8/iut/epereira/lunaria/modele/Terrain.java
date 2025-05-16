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
        double centreX = acteur.x.get();
        double centreY = acteur.y.get();
        double rayon = 10;

        // Les fonctions Math.max/min assurent que le hero reste dans les limites du terrain.

        int minTileX = Math.max(0, (int) ((centreX - rayon) / TAILLE_TUILE));
        int maxTileX = Math.min(getWidth() - 1, (int) ((centreX + rayon) / TAILLE_TUILE));
        int minTileY = Math.max(0, (int) ((centreY - rayon) / TAILLE_TUILE));
        int maxTileY = Math.min(getHeight() - 1, (int) ((centreY + rayon) / TAILLE_TUILE));

        //Si la tuile est solide, on calcule :
        //La position centrale de la tuile (tuileCentreX, tuileCentreY)
        //La distance entre le centre de l'acteur et le centre de la tuile (distX, distY)

        for (int y = minTileY; y <= maxTileY; y++) {
            for (int x = minTileX; x <= maxTileX; x++) {
                if (getTableau()[y][x] != 0 && getTableau()[y][x] != 5) {
                    double tuileCentreX = x * TAILLE_TUILE + TAILLE_TUILE / 2;
                    double tuileCentreY = y * TAILLE_TUILE + TAILLE_TUILE / 2;

                    double distX = Math.abs(centreX - tuileCentreX);
                    double distY = Math.abs(centreY - tuileCentreY);

                    if (distX < (TAILLE_TUILE / 2 + rayon - 2) && distY < (TAILLE_TUILE / 2 + rayon - 2)) {
                        return true;
                    }
                }
            }
        }
        return false;
    }
    public boolean estAuSol(Acteur a) {
        double centreX = a.x.get();
        double testY = a.y.get() + 11; // 10 (rayon) + 1

        int tileX = (int) (centreX / TAILLE_TUILE);
        int tileY = (int) (testY / TAILLE_TUILE);

        return tileY >= 0 && tileY < getHeight() && tileX >= 0 && tileX < getWidth() &&
                getTableau()[tileY][tileX] != 0 &&
                getTableau()[tileY][tileX] != 5;
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