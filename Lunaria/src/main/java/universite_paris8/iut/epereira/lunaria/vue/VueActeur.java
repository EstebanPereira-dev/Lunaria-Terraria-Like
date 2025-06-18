package universite_paris8.iut.epereira.lunaria.vue;

import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.util.Duration;
import universite_paris8.iut.epereira.lunaria.controleur.Controleur;
import universite_paris8.iut.epereira.lunaria.modele.Acteur;

import java.util.HashMap;
import java.util.Map;

public abstract class VueActeur {
    protected Acteur acteur;
    protected Controleur controleur;
    protected final Map<Acteur, String> animationState = new HashMap<>();
    protected final Map<Acteur, Timeline> animations = new HashMap<>();
    protected final Map<Acteur, ImageView> sprites = new HashMap<>();

    public VueActeur(Acteur acteur, Controleur controleur) {
        this.acteur = acteur;
        this.controleur = controleur;
        prechargerImages();
    }

    protected final void initialiserVue() {
        ajouterActeurVue();
    }

    // Méthodes abstraites
    protected abstract void prechargerImages();
    protected abstract Image[] getFramesForAnimation(String animationType);
    protected abstract String getActeurType();
    public abstract void mettreAJourAnimation(Acteur acteur, double vitesseX);


    protected ImageView creerImageViewBase(Image imageInitiale) {
        ImageView imageView = new ImageView();
        imageView.setFitWidth(48);
        imageView.setFitHeight(48);
        imageView.setImage(imageInitiale);
        imageView.setId(getActeurType());

        // Lier les propriétés de position (factorisation !)
        imageView.translateXProperty().bind(acteur.x.subtract(imageView.getFitWidth() / 2));
        imageView.translateYProperty().bind(acteur.y.subtract(imageView.getFitHeight() / 2));

        return imageView;
    }

    protected Timeline creerAnimationCyclique(ImageView imageView, Image[] frames, int dureeMs) {
        final int[] frameIndex = {0};
        Timeline animation = new Timeline(
                new KeyFrame(Duration.millis(dureeMs), e -> {
                    frameIndex[0] = (frameIndex[0] + 1) % frames.length;
                    imageView.setImage(frames[frameIndex[0]]);
                })
        );
        animation.setCycleCount(Animation.INDEFINITE);
        return animation;
    }

    protected Timeline creerAnimationSequentielle(ImageView imageView, Image[] frames, int dureeParFrame, Runnable callback) {
        Timeline animation = new Timeline();
        for (int i = 0; i < frames.length; i++) {
            final int frameIndex = i;
            KeyFrame keyFrame = new KeyFrame(
                    Duration.millis(dureeParFrame * i),
                    e -> imageView.setImage(frames[frameIndex])
            );
            animation.getKeyFrames().add(keyFrame);
        }

        // Ajouter le callback à la fin
        if (callback != null) {
            animation.getKeyFrames().add(new KeyFrame(
                    Duration.millis(dureeParFrame * frames.length),
                    e -> callback.run()
            ));
        }

        animation.setCycleCount(1);
        return animation;
    }

    protected void orienterSprite(ImageView sprite, double vitesseX) {
        if (vitesseX < 0) {
            sprite.setScaleX(-1); // Gauche
        } else if (vitesseX > 0) {
            sprite.setScaleX(1);  // Droite
        }
    }

    protected void stockerAnimation(String nom, Timeline animation) {
        animations.put(acteur, animation);
    }

    protected void arreterToutesAnimations() {
        animations.values().forEach(Timeline::stop);
    }

    private final void ajouterActeurVue() {
        ImageView sprite = creerSprite();
        sprites.put(acteur, sprite);

        controleur.getCamera().ajouterActeurAuMonde(sprite);
    }

    public final void supprimerActeurVue(Acteur acteur) {
        ImageView sprite = sprites.get(acteur);
        if (sprite != null) {

            controleur.getCamera().retirerActeurDuMonde(sprite);
            sprites.remove(acteur);
        }

        arreterToutesAnimations();
        animations.clear();
        animationState.remove(acteur);
    }

    protected abstract ImageView creerSprite();

    // Getters
    public Map<Acteur, ImageView> getSprites() {
        return sprites;
    }

    public Acteur getActeur() {
        return acteur;
    }

}