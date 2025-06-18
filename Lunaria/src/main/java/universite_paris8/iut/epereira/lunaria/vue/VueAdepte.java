package universite_paris8.iut.epereira.lunaria.vue;

import javafx.animation.Timeline;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import universite_paris8.iut.epereira.lunaria.controleur.Controleur;
import universite_paris8.iut.epereira.lunaria.modele.Acteur;
import universite_paris8.iut.epereira.lunaria.modele.acteurs.Ennemis.Ennemi;

public class VueAdepte extends VueActeur {
    private Image[] ennemiFrames;

    public VueAdepte(Ennemi ennemi, Controleur controleur) {
        super(ennemi, controleur);
        initialiserVue();
    }

    @Override
    protected void prechargerImages() {
        ennemiFrames = new Image[1];
        ennemiFrames[0] = new Image(getClass().getResourceAsStream("/universite_paris8/iut/epereira/lunaria/DossierMap/Adepte/idle1.png"));
    }

    @Override
    protected Image[] getFramesForAnimation(String animationType) {
        return ennemiFrames; // Simple pour l'ennemi
    }

    @Override
    protected String getActeurType() {
        return "Ennemi";
    }

    @Override
    protected ImageView creerSprite() {

        ImageView imageView = creerImageViewBase(ennemiFrames[0]);

        Timeline animation = creerAnimationCyclique(imageView, ennemiFrames, 150);
        animation.play();
        stockerAnimation("idle", animation);

        return imageView;
    }

    @Override
    public void mettreAJourAnimation(Acteur acteur, double vitesseX) {
        ImageView sprite = sprites.get(acteur);
        if (sprite == null) return;

        orienterSprite(sprite, vitesseX);


    }
}