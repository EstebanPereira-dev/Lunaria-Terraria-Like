package universite_paris8.iut.epereira.lunaria.vue;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.util.Duration;
import universite_paris8.iut.epereira.lunaria.controleur.Controleur;
import universite_paris8.iut.epereira.lunaria.modele.Acteur;
import universite_paris8.iut.epereira.lunaria.modele.acteurs.Ennemis.Ennemi;
import universite_paris8.iut.epereira.lunaria.modele.acteurs.Hero;

public class VueEnnemi extends VueActeur {
    private Image[] ennemiFrames;

    public VueEnnemi(Ennemi ennemi, Controleur controleur) {
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

    public void attaquer() { // Modele
        Ennemi ennemi = (Ennemi) acteur;

        if (!ennemi.attackOnCooldown) {
            Hero hero = controleur.getEnv().getHero();
            double distanceAuHero = calculerDistance(ennemi, hero);
            double porteeAttaque = ((Ennemi) acteur).getRange();

            if (distanceAuHero <= porteeAttaque) {
                ennemi.agit();
                ennemi.attackOnCooldown = true;

                // Timer de cooldown
                final Acteur finalActeur = ennemi;
                Timeline attackCooldownTimerEnnemi = new Timeline(
                        new KeyFrame(Duration.seconds(5), e -> {
                            if (!controleur.getEnv().getActeursASupprimer().contains(finalActeur)) {
                                finalActeur.attackOnCooldown = false;
                            }
                        })
                );
                attackCooldownTimerEnnemi.setCycleCount(1);
                attackCooldownTimerEnnemi.play();
            }
        }
    }

    private double calculerDistance(Acteur acteur1, Acteur acteur2) {
        double deltaX = acteur1.getPosX() - acteur2.getPosX();
        double deltaY = acteur1.getPosY() - acteur2.getPosY();
        return Math.sqrt(deltaX * deltaX + deltaY * deltaY);
    }
}