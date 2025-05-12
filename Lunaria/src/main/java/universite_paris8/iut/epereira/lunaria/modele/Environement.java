package universite_paris8.iut.epereira.lunaria.modele;

public class Environement {
    private char cylceJourNuit;
    private int width;
    private int height;

    public Environement(int width, int height){
        this.height = height;
        this.width = width;
    }


    // GETTER :
    public int getHeight() {
        return height;
    }

    public int getWidth() {
        return width;
    }

    public char getCylceJourNuit() {
        return cylceJourNuit;
    }



}
