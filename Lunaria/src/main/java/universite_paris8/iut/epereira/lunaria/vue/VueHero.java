package universite_paris8.iut.epereira.lunaria.vue;

import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Platform;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.util.Duration;
import universite_paris8.iut.epereira.lunaria.controleur.Controleur;
import universite_paris8.iut.epereira.lunaria.modele.Acteur;
import universite_paris8.iut.epereira.lunaria.modele.acteurs.Hero;

import java.util.HashMap;
import java.util.Map;

public class VueHero extends VueActeur {
    // Maps spécifiques au héros (plusieurs animations) - ✅ Initialisées dans le constructeur
    private Map<Acteur, Timeline> idleAnimations;
    private Map<Acteur, Timeline> jumpAnimations;
    private Map<Acteur, Timeline> attackAnimations;

    // Images spécifiques au héros
    private Image[] heroFrames;
    private Image[] heroIdleFrames;
    private Image[] heroJumpFrames;
    private Image[] heroAttackFrames;

    public VueHero(Hero hero, Controleur controleur) {
        super(hero, controleur); // Appel du constructeur parent

        // ✅ Initialiser les maps APRÈS super()
        this.idleAnimations = new HashMap<>();
        this.jumpAnimations = new HashMap<>();
        this.attackAnimations = new HashMap<>();

        // ✅ Maintenant initialiser la vue (tous les champs sont prêts)
        initialiserVue();
    }

    @Override
    protected void prechargerImages() {
        // Images de marche
        heroFrames = new Image[4];
        heroFrames[0] = new Image(getClass().getResourceAsStream("/universite_paris8/iut/epereira/lunaria/DossierMap/Hero/walk0.png"));
        heroFrames[1] = new Image(getClass().getResourceAsStream("/universite_paris8/iut/epereira/lunaria/DossierMap/Hero/walk1.png"));
        heroFrames[2] = new Image(getClass().getResourceAsStream("/universite_paris8/iut/epereira/lunaria/DossierMap/Hero/walk2.png"));
        heroFrames[3] = new Image(getClass().getResourceAsStream("/universite_paris8/iut/epereira/lunaria/DossierMap/Hero/walk3.png"));

        // Images d'idle
        heroIdleFrames = new Image[4];
        heroIdleFrames[0] = new Image(getClass().getResourceAsStream("/universite_paris8/iut/epereira/lunaria/DossierMap/Hero/idle0.png"));
        heroIdleFrames[1] = new Image(getClass().getResourceAsStream("/universite_paris8/iut/epereira/lunaria/DossierMap/Hero/idlea.png"));
        heroIdleFrames[2] = new Image(getClass().getResourceAsStream("/universite_paris8/iut/epereira/lunaria/DossierMap/Hero/idleb.png"));
        heroIdleFrames[3] = new Image(getClass().getResourceAsStream("/universite_paris8/iut/epereira/lunaria/DossierMap/Hero/idlec.png"));

        // Images de saut
        heroJumpFrames = new Image[6];
        heroJumpFrames[0] = new Image(getClass().getResourceAsStream("/universite_paris8/iut/epereira/lunaria/DossierMap/Hero/jump0.png"));
        heroJumpFrames[1] = new Image(getClass().getResourceAsStream("/universite_paris8/iut/epereira/lunaria/DossierMap/Hero/jump1.png"));
        heroJumpFrames[2] = new Image(getClass().getResourceAsStream("/universite_paris8/iut/epereira/lunaria/DossierMap/Hero/jump2.png"));
        heroJumpFrames[3] = new Image(getClass().getResourceAsStream("/universite_paris8/iut/epereira/lunaria/DossierMap/Hero/jump3.png"));
        heroJumpFrames[4] = new Image(getClass().getResourceAsStream("/universite_paris8/iut/epereira/lunaria/DossierMap/Hero/jump4.png"));
        heroJumpFrames[5] = new Image(getClass().getResourceAsStream("/universite_paris8/iut/epereira/lunaria/DossierMap/Hero/jump5.png"));

        // Images d'attaque
        heroAttackFrames = new Image[5];
        heroAttackFrames[0] = new Image(getClass().getResourceAsStream("/universite_paris8/iut/epereira/lunaria/DossierMap/Hero/attack0.png"));
        heroAttackFrames[1] = new Image(getClass().getResourceAsStream("/universite_paris8/iut/epereira/lunaria/DossierMap/Hero/attack1.png"));
        heroAttackFrames[2] = new Image(getClass().getResourceAsStream("/universite_paris8/iut/epereira/lunaria/DossierMap/Hero/attack2.png"));
        heroAttackFrames[3] = new Image(getClass().getResourceAsStream("/universite_paris8/iut/epereira/lunaria/DossierMap/Hero/attack3.png"));
        heroAttackFrames[4] = new Image(getClass().getResourceAsStream("/universite_paris8/iut/epereira/lunaria/DossierMap/Hero/attack4.png"));
    }

    @Override
    protected String getActeurType() {
        return "Hero";
    }

    @Override
    protected Image[] getFramesForAnimation(String animationType) {
        switch (animationType) {
            case "walk": return heroFrames;
            case "idle": return heroIdleFrames;
            case "jump": return heroJumpFrames;
            case "attack": return heroAttackFrames;
            default: return heroIdleFrames;
        }
    }

    @Override
    protected ImageView creerSprite() {
        ImageView imageView = creerImageViewBase(heroIdleFrames[0]);

        creerAnimationsHero(imageView);

        Timeline idleAnimation = idleAnimations.get(acteur);
        idleAnimation.play();
        animationState.put(acteur, "idle");

        return imageView;
    }

    private void creerAnimationsHero(ImageView imageView) {

        // Animation de marche
        Timeline walkAnimation = creerAnimationCyclique(imageView, heroFrames, 150);
        stockerAnimation("walk", walkAnimation);

        // Animation d'idle
        Timeline idleAnimation = creerAnimationCyclique(imageView, heroIdleFrames, 250);
        idleAnimations.put(acteur, idleAnimation);

        // Animation de saut
        Timeline jumpAnimation = creerAnimationCyclique(imageView, heroJumpFrames, 200);
        jumpAnimations.put(acteur, jumpAnimation);

        // Animation d'attaque
        Timeline attackAnimation = creerAnimationSequentielle(imageView, heroAttackFrames, 100, () -> {
            idleAnimation.play();
            animationState.put(acteur, "idle");
        });
        attackAnimations.put(acteur, attackAnimation);
    }

    @Override
    public void mettreAJourAnimation(Acteur acteur, double vitesseX) {
        ImageView sprite = sprites.get(acteur);
        if (sprite == null) return;

        // ✅ UTILISE la méthode commune d'orientation
        orienterSprite(sprite, vitesseX);

        Hero hero = (Hero) acteur;
        Timeline walkAnimation = animations.get(acteur);
        Timeline idleAnimation = idleAnimations.get(acteur);
        Timeline jumpAnimation = jumpAnimations.get(acteur);
        Timeline attackAnimation = attackAnimations.get(acteur);
        String currentState = animationState.getOrDefault(acteur, "idle");

        boolean isAttacking = hero.getActions().get(6);
        boolean isJumping = !hero.auSol;

        // ✅ PROTECTION : Ne pas interrompre l'animation d'attaque si elle est en cours
        boolean isAttackAnimationPlaying = currentState.equals("attack") &&
                attackAnimation != null &&
                attackAnimation.getStatus() == Animation.Status.RUNNING;

        // Logique de priorité des animations
        if (isAttacking && !currentState.equals("attack")) {
            arreterToutesAnimationsHero();
            attackAnimation.play();
            animationState.put(acteur, "attack");
        } else if (isAttackAnimationPlaying) {
            return;
        } else if (isJumping && !currentState.equals("jump")) {
            arreterToutesAnimationsHero();
            jumpAnimation.play();
            animationState.put(acteur, "jump");
        } else if (Math.abs(vitesseX) > 0 && !currentState.equals("walk")) {
            arreterToutesAnimationsHero();
            walkAnimation.play();
            animationState.put(acteur, "walk");
        } else if (Math.abs(vitesseX) == 0 && !currentState.equals("idle") && !isAttacking && !isJumping) {
            arreterToutesAnimationsHero();
            idleAnimation.play();
            animationState.put(acteur, "idle");
        }
    }

    private void arreterToutesAnimationsHero() {
        animations.get(acteur).stop();
        idleAnimations.get(acteur).stop();
        jumpAnimations.get(acteur).stop();
        attackAnimations.get(acteur).stop();
    }

    public void attaquer() {
        Hero hero = (Hero) acteur;
        if (!hero.attackOnCooldown) {
            hero.getActions().set(6, true);
            hero.agit();

            Timeline attackAnimation = attackAnimations.get(hero);
            if (attackAnimation != null) {
                arreterToutesAnimationsHero();
                attackAnimation.play();
                animationState.put(hero, "attack");
            }

            Platform.runLater(() -> hero.getActions().set(6, false));

            hero.attackOnCooldown = true;
            Timeline cooldownTimer = new Timeline(
                    new KeyFrame(Duration.seconds(1), e -> {
                        hero.attackOnCooldown = false;
                        System.out.println("Attaque dispo");
                    })
            );
            cooldownTimer.setCycleCount(1);
            cooldownTimer.play();
        } else {
            System.out.println("COOLDOWN");
        }
    }
}