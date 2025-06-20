package universite_paris8.iut.epereira.lunaria.vue;

import javafx.animation.Timeline;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import universite_paris8.iut.epereira.lunaria.controleur.Controleur;
import universite_paris8.iut.epereira.lunaria.modele.Acteur;

public class VuePNJ extends VueActeur {
    private Image[] pnjFrames;


    public VuePNJ(Acteur pnj, Controleur controleur) {
        super(pnj, controleur);
        initialiserVue();
    }

    @Override
    protected void prechargerImages() {
        pnjFrames = new Image[1];
        pnjFrames[0] = new Image(getClass().getResourceAsStream("/universite_paris8/iut/epereira/lunaria/DossierMap/Aleksa.png"));
    }

    @Override
    protected Image[] getFramesForAnimation(String animationType) {
        return pnjFrames;
    }

    @Override
    protected String getActeurType() {
        return "pnj";
    }

    @Override
    protected ImageView creerSprite() {
        ImageView imageView = creerImageViewBase(pnjFrames[0]);
        Timeline animation = creerAnimationCyclique(imageView, pnjFrames, 150);
        animation.play();
        stockerAnimation("idle", animation);
        return imageView;
    }

    @Override
    public void mettreAJourAnimation(Acteur acteur, double vitesseX) {
        ImageView sprite = sprites.get(acteur);
        if (sprite == null) return;
        orienterSprite(sprite, vitesseX);
        sprite.setOnMouseClicked(e -> acteur.agit());
    }
}
