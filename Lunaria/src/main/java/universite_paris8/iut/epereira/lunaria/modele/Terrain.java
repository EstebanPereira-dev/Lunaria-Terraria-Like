package universite_paris8.iut.epereira.lunaria.modele;

public class Terrain {
    private int width;
    private int height;
    private int[][] terrain;

    public Terrain(int width, int height){
        this.width=width;
        this.height=height;
        this.terrain=new int[width][height];
    }

    public void setTerrain(){
      for (int ligne= 0;ligne<this.width;ligne++) {
          for (int colonne = 0; colonne < this.height; colonne++) {

          }
      }
    }
    public int getWidth(){
        return this.width;
    }

    public int getHeight() {
        return this.height;
    }

    public int[][] getTerrain() {
        return terrain;
    }

    public String toString(){
        String s="";
        for (int i=0; i<this.width;i++){
            for (int j=0; j<this.height;j++) {
                s=s+this.terrain[i][j];
            }
            s=s+ "\n";
            }
        return s;
    }

}
