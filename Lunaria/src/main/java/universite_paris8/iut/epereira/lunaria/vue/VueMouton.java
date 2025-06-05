package universite_paris8.iut.epereira.lunaria.vue;

import javafx.animation.Timeline;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import universite_paris8.iut.epereira.lunaria.controleur.Controleur;
import universite_paris8.iut.epereira.lunaria.modele.Acteur;

public class VueMouton extends VueActeur {
    private Image[] moutonFrames;

    public VueMouton(Acteur mouton, Controleur controleur) {
        super(mouton, controleur);
        initialiserVue();
    }

    @Override
    protected void prechargerImages() {
        moutonFrames = new Image[1];
        moutonFrames[0] = new Image(getClass().getResourceAsStream("/universite_paris8/iut/epereira/lunaria/DossierMap/Mouton.png"));
    }

    @Override
    protected Image[] getFramesForAnimation(String animationType) {
        return moutonFrames;
    }

    @Override
    protected String getActeurType() {
        return "Mouton";
    }

    @Override
    protected ImageView creerSprite() {
        ImageView imageView = creerImageViewBase(moutonFrames[0]);
        Timeline animation = creerAnimationCyclique(imageView, moutonFrames, 150);
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