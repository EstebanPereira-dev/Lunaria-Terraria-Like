package universite_paris8.iut.epereira.lunaria.modele;

public class Terrain {
    private int width;
    private int height;
    private int[][] terrain;
    private boolean[][] tangibilite;

    public Terrain(int width, int height) {
        this.width = width;
        this.height = height;
        this.terrain = new int[][] {
                {4, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 4},
                {4, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 4},
                {4, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 4},
                {4, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 4},
                {4, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 4},
                {4, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 4},
                {4, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 4, 4, 4, 4, 4, 4, 4, 4, 0, 0, 4},
                {4, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 4, 5, 5, 5, 5, 5, 5, 4, 0, 0, 4},
                {4, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 4, 5, 5, 5, 5, 5, 5, 4, 0, 0, 4},
                {4, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 4, 5, 5, 5, 5, 5, 5, 4, 0, 0, 4},
                {4, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 4, 5, 5, 5, 5, 5, 5, 4, 0, 0, 4},
                {4, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 5, 5, 5, 5, 5, 5, 5, 4, 0, 0, 4},
                {2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2},
                {1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1},
                {1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1},
                {1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1},
                {1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1},
                {1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1},
                {1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1}
        };

        initTangibilite();
    }

    private void initTangibilite() {
        this.tangibilite = new boolean[height][width];

        for (int i = 0; i < height; i++) {
            for (int j = 0; j < width; j++) {
                tangibilite[i][j] = (terrain[i][j] != 0);
            }
        }
    }

    public void setTerrain(int[][] terrain) {
        this.terrain = terrain;
        initTangibilite();
    }


    public boolean estTangible(int ligneY, int colonneX) {
        if (ligneY < 0 || ligneY >= height || colonneX < 0 || colonneX >= width) {
            return false;
        }
        return tangibilite[ligneY][colonneX];
    }

    public boolean estEnCollision(Acteur acteur, int tailleTuile) {
        double centreX = acteur.x.get();
        double centreY = acteur.y.get();
        double rayon = 10;

        int minTileX = Math.max(0, (int)((centreX - rayon) / tailleTuile));
        int maxTileX = Math.min(width - 1, (int)((centreX + rayon) / tailleTuile));
        int minTileY = Math.max(0, (int)((centreY - rayon) / tailleTuile));
        int maxTileY = Math.min(height - 1, (int)((centreY + rayon) / tailleTuile));

        for (int y = minTileY; y <= maxTileY; y++) {
            for (int x = minTileX; x <= maxTileX; x++) {
                if (estTangible(y, x)) {
                    double tileX = x * tailleTuile;
                    double tileY = y * tailleTuile;

                    double distX = Math.abs(centreX - (tileX + tailleTuile/2));
                    double distY = Math.abs(centreY - (tileY + tailleTuile/2));

                    if (distX <= (tailleTuile/2 + rayon) && distY <= (tailleTuile/2 + rayon)) {
                        return true;
                    }
                }
            }
        }

        return false;
    }


    public boolean estAuSol(Acteur acteur, int tailleTuile) {
        double centreX = acteur.x.get();
        double centreY = acteur.y.get();
        double rayon = 10;

        double py = centreY + rayon + 1;

        int tuileX = (int) (centreX / tailleTuile);
        int tuileY = (int) (py / tailleTuile);

        return estTangible(tuileY, tuileX);
    }

    public int getWidth() {
        return this.width;
    }

    public int getHeight() {
        return this.height;
    }

    public int[][] getTerrain() {
        return terrain;
    }

    public boolean[][] getTangibilite() {
        return tangibilite;
    }
}