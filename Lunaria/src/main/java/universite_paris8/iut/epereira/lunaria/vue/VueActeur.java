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
import universite_paris8.iut.epereira.lunaria.modele.acteurs.Ennemis.Adepte;
import universite_paris8.iut.epereira.lunaria.modele.acteurs.Ennemis.Ennemi;
import universite_paris8.iut.epereira.lunaria.modele.acteurs.Hero;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class VueActeur {
    private Acteur a;
    private Controleur controleur;
    private final Map<Acteur, String> animationState = new HashMap<>();
    private final Map<Acteur, Timeline> animations = new HashMap<>();
    private final Map<Acteur, ImageView> sprites = new HashMap<>();
    private final Map<Acteur, Timeline> idleAnimations = new HashMap<>();
    private final Map<Acteur, Timeline> jumpAnimations = new HashMap<>();
    private final Map<Acteur, Timeline> attackAnimations = new HashMap<>();
    private Image[] heroJumpFrames;
    private Image[] moutonFrames;
    private Image[] heroFrames;
    private Image[] ennemiFrames;
    private Image[] heroIdleFrames;
    private Image[] heroAttackFrames;
    public VueActeur(Acteur a, Controleur controleur){
        this.a = a;
        this.controleur = controleur;
        prechargerImages();
        ajouterActeurVue();
    }
    private void prechargerImages() {
        // Préchargement des images du héros pour la marche
        heroFrames = new Image[4];
        heroFrames[0] = new Image(getClass().getResourceAsStream("/universite_paris8/iut/epereira/lunaria/DossierMap/Hero/walk0.png"));
        heroFrames[1] = new Image(getClass().getResourceAsStream("/universite_paris8/iut/epereira/lunaria/DossierMap/Hero/walk1.png"));
        heroFrames[2] = new Image(getClass().getResourceAsStream("/universite_paris8/iut/epereira/lunaria/DossierMap/Hero/walk2.png"));
        heroFrames[3] = new Image(getClass().getResourceAsStream("/universite_paris8/iut/epereira/lunaria/DossierMap/Hero/walk3.png"));

        // Préchargement des images du héros pour l'idle
        heroIdleFrames = new Image[4];
        heroIdleFrames[0] = new Image(getClass().getResourceAsStream("/universite_paris8/iut/epereira/lunaria/DossierMap/Hero/idle0.png"));
        heroIdleFrames[1] = new Image(getClass().getResourceAsStream("/universite_paris8/iut/epereira/lunaria/DossierMap/Hero/idlea.png"));
        heroIdleFrames[2] = new Image(getClass().getResourceAsStream("/universite_paris8/iut/epereira/lunaria/DossierMap/Hero/idleb.png"));
        heroIdleFrames[3] = new Image(getClass().getResourceAsStream("/universite_paris8/iut/epereira/lunaria/DossierMap/Hero/idlec.png"));

        // Préchargement des images du héros pour le saut
        heroJumpFrames = new Image[6];
        heroJumpFrames[0] = new Image(getClass().getResourceAsStream("/universite_paris8/iut/epereira/lunaria/DossierMap/Hero/jump0.png"));
        heroJumpFrames[1] = new Image(getClass().getResourceAsStream("/universite_paris8/iut/epereira/lunaria/DossierMap/Hero/jump1.png"));
        heroJumpFrames[2] = new Image(getClass().getResourceAsStream("/universite_paris8/iut/epereira/lunaria/DossierMap/Hero/jump2.png"));
        heroJumpFrames[3] = new Image(getClass().getResourceAsStream("/universite_paris8/iut/epereira/lunaria/DossierMap/Hero/jump3.png"));
        heroJumpFrames[4] = new Image(getClass().getResourceAsStream("/universite_paris8/iut/epereira/lunaria/DossierMap/Hero/jump4.png"));
        heroJumpFrames[5] = new Image(getClass().getResourceAsStream("/universite_paris8/iut/epereira/lunaria/DossierMap/Hero/jump5.png"));

        //Pré des images attack
        heroAttackFrames = new Image[5];
        heroAttackFrames[0] = new Image(getClass().getResourceAsStream("/universite_paris8/iut/epereira/lunaria/DossierMap/Hero/attack0.png"));
        heroAttackFrames[1] = new Image(getClass().getResourceAsStream("/universite_paris8/iut/epereira/lunaria/DossierMap/Hero/attack1.png"));
        heroAttackFrames[2] = new Image(getClass().getResourceAsStream("/universite_paris8/iut/epereira/lunaria/DossierMap/Hero/attack2.png"));
        heroAttackFrames[3] = new Image(getClass().getResourceAsStream("/universite_paris8/iut/epereira/lunaria/DossierMap/Hero/attack3.png"));
        heroAttackFrames[4] = new Image(getClass().getResourceAsStream("/universite_paris8/iut/epereira/lunaria/DossierMap/Hero/attack4.png"));

        // Préchargement des images d'ennemi
        ennemiFrames = new Image[1];
        ennemiFrames[0] = new Image(getClass().getResourceAsStream("/universite_paris8/iut/epereira/lunaria/DossierMap/Adepte/idle1.png"));

        // Préchargement des images du mouton
        moutonFrames = new Image[1];
        moutonFrames[0] = new Image(getClass().getResourceAsStream("/universite_paris8/iut/epereira/lunaria/DossierMap/Mouton.png"));
    }
    public ImageView creerSprite() {
        ImageView imageView = new ImageView();

        // Dimensions du sprite
        imageView.setFitWidth(48);
        imageView.setFitHeight(48);

        // Créer les animations selon le type d'acteur
        if (a instanceof Hero) {
            // Définir l'image initiale (frame idle)
            imageView.setImage(heroIdleFrames[0]);
            imageView.setId("Hero");

            // Animation de marche
            final int[] walkFrameIndex = {0};
            Timeline walkAnimation = new Timeline(
                    new KeyFrame(Duration.millis(150), e -> {
                        walkFrameIndex[0] = (walkFrameIndex[0] + 1) % heroFrames.length;
                        imageView.setImage(heroFrames[walkFrameIndex[0]]);
                    })
            );
            walkAnimation.setCycleCount(Animation.INDEFINITE);

            // Animation d'idle
            final int[] idleFrameIndex = {0};
            Timeline idleAnimation = new Timeline(
                    new KeyFrame(Duration.millis(250), e -> {
                        idleFrameIndex[0] = (idleFrameIndex[0] + 1) % heroIdleFrames.length;
                        imageView.setImage(heroIdleFrames[idleFrameIndex[0]]);
                    })
            );
            idleAnimation.setCycleCount(Animation.INDEFINITE);

            // Animation de saut
            final int[] jumpFrameIndex = {0};
            Timeline jumpAnimation = new Timeline(
                    new KeyFrame(Duration.millis(200), e -> {
                        jumpFrameIndex[0] = (jumpFrameIndex[0] + 1) % heroJumpFrames.length;
                        imageView.setImage(heroJumpFrames[jumpFrameIndex[0]]);
                    })
            );
            jumpAnimation.setCycleCount(Animation.INDEFINITE);

            // Animation d'attaque - NOUVEL AJOUT
            Timeline attackAnimation = new Timeline();
            for (int i = 0; i < heroAttackFrames.length; i++) {
                final int frameIndex = i;
                KeyFrame keyFrame = new KeyFrame(
                        Duration.millis(100 * i), // Timing séquentiel pour les frames d'attaque
                        e -> imageView.setImage(heroAttackFrames[frameIndex])
                );
                attackAnimation.getKeyFrames().add(keyFrame);
            }
            // Ajouter un keyframe final pour revenir à l'animation précédente
            attackAnimation.getKeyFrames().add(new KeyFrame(
                    Duration.millis(100 * heroAttackFrames.length),
                    e -> {
                        // Revenir à l'animation idle à la fin de l'attaque
                        idleAnimation.play();
                        animationState.put(a, "idle");
                    }
            ));
            attackAnimation.setCycleCount(1); // L'attaque ne joue qu'une seule fois

            // Démarrer l'animation d'idle par défaut
            idleAnimation.play();
            animationState.put(a, "idle");

            // Stocker les animations
            animations.put(a, walkAnimation);
            idleAnimations.put(a, idleAnimation);
            jumpAnimations.put(a, jumpAnimation);
            attackAnimations.put(a, attackAnimation); // NOUVEL AJOUT



        } else if (a instanceof Adepte){
            // Pour les ennemis (animation simple) - inchangé
            imageView.setImage(ennemiFrames[0]);
            imageView.setId("Ennemis");

            final int[] frameIndex = {0};
            Timeline animation = new Timeline(
                    new KeyFrame(Duration.millis(150), e -> {
                        frameIndex[0] = (frameIndex[0] + 1) % ennemiFrames.length;
                        imageView.setImage(ennemiFrames[frameIndex[0]]);
                    })
            );
            animation.setCycleCount(Animation.INDEFINITE);
            animation.play();

            // Stocker l'animation
            animations.put(a, animation);
        }
        else {
            imageView.setImage(moutonFrames[0]);
            imageView.setId("Mouton");

            final int[] frameIndex = {0};
            Timeline animation = new Timeline(
                    new KeyFrame(Duration.millis(150), e -> {
                        frameIndex[0] = (frameIndex[0] + 1) % moutonFrames.length;
                        imageView.setImage(moutonFrames[frameIndex[0]]);
                    })
            );
            animation.setCycleCount(Animation.INDEFINITE);
            animation.play();

            // Stocker l'animation
            animations.put(a, animation);
        }

        // Lier les propriétés de position
        imageView.translateXProperty().bind(a.x.subtract(imageView.getFitWidth() / 2));
        imageView.translateYProperty().bind(a.y.subtract(imageView.getFitHeight() / 2));

        return imageView;
    }
    public void mettreAJourAnimation(Acteur acteur, double vitesseX) {
        ImageView sprite = sprites.get(acteur);

        if (sprite == null) return;

        // Orienter le sprite selon la direction
        if (vitesseX < 0) {
            sprite.setScaleX(-1); // Gauche
        } else if (vitesseX > 0) {
            sprite.setScaleX(1);  // Droite
        }

        // Pour le héros seulement
        if (acteur instanceof Hero) {
            Timeline walkAnimation = animations.get(acteur);
            Timeline idleAnimation = idleAnimations.get(acteur);
            Timeline jumpAnimation = jumpAnimations.get(acteur);
            Timeline attackAnimation = attackAnimations.get(acteur);
            String currentState = animationState.getOrDefault(acteur, "idle");

            Hero hero = (Hero) acteur;

            // Vérifier si le héros attaque - Priorité sur les autres animations
            boolean isAttacking = hero.getActions().get(6);

            // Vérifier si le héros est en train de sauter
            boolean isJumping = !hero.auSol;

            // Décider quelle animation jouer - En donnant priorité à l'attaque
            if (isAttacking) {
                // Si l'animation d'attaque n'est pas déjà en cours
                if (!currentState.equals("attack")) {
                    // Arrêter toutes les autres animations
                    walkAnimation.stop();
                    idleAnimation.stop();
                    jumpAnimation.stop();

                    // Jouer l'attaque
                    attackAnimation.play();
                    animationState.put(acteur, "attack");
                }
            } else if (isJumping) {
                // Si le héros saute et n'est pas déjà en animation de saut
                if (!currentState.equals("jump")) {
                    walkAnimation.stop();
                    idleAnimation.stop();
                    jumpAnimation.play();
                    animationState.put(acteur, "jump");
                }
            } else if (Math.abs(vitesseX) > 0) {
                // Si le héros marche et n'est pas déjà en animation de marche
                if (!currentState.equals("walk")) {
                    jumpAnimation.stop();
                    idleAnimation.stop();
                    walkAnimation.play();
                    animationState.put(acteur, "walk");
                }
            } else {
                // Si le héros est immobile et n'est pas déjà en animation d'idle
                if (!currentState.equals("idle")) {
                    walkAnimation.stop();
                    jumpAnimation.stop();
                    idleAnimation.play();
                    animationState.put(acteur, "idle");
                }
            }
        }
    }
    public void supprimerActeurVue(Acteur acteur) {
        ImageView sprite = sprites.get(acteur);
        Timeline walkAnimation = animations.get(acteur);
        Timeline idleAnimation = idleAnimations.get(acteur);
        Timeline jumpAnimation = jumpAnimations.get(acteur);
        Timeline attackAnimation = attackAnimations.get(acteur);

        if (sprite != null) {
            controleur.getTabJeu().getChildren().remove(sprite);
            sprites.remove(acteur);
        }

        if (walkAnimation != null) {
            walkAnimation.stop();
            animations.remove(acteur);
        }

        if (idleAnimation != null) {
            idleAnimation.stop();
            idleAnimations.remove(acteur);
        }

        if (jumpAnimation != null) {
            jumpAnimation.stop();
            jumpAnimations.remove(acteur);
        }

        if (attackAnimation != null) {
            attackAnimation.stop();
            attackAnimations.remove(acteur);
        }

        animationState.remove(acteur);
    }
    public void ajouterActeurVue() {
        ImageView sprite = creerSprite();
        sprites.put(a, sprite);
        controleur.getTabJeu().getChildren().add(sprite);
    }
    public void attaqueHero() {
        if (!controleur.getEnv().getHero().attackOnCooldown) {
            // Activer l'action d'attaque qui sera détectée par mettreAJourAnimation
            controleur.getEnv().getHero().getActions().set(6, true);
            controleur.getEnv().getHero().agit();

            // Obtenir les références au héros et à son animation d'attaque
            Hero hero = controleur.getEnv().getHero();
            Timeline attackAnimation = attackAnimations.get(hero);

            // Jouer directement l'animation d'attaque
            if (attackAnimation != null) {
                // Arrêter les autres animations en cours
                if (animations.get(hero) != null) animations.get(hero).stop();
                if (idleAnimations.get(hero) != null) idleAnimations.get(hero).stop();
                if (jumpAnimations.get(hero) != null) jumpAnimations.get(hero).stop();

                // Jouer l'animation d'attaque
                attackAnimation.play();
                animationState.put(hero, "attack");
            }

            // Réinitialiser l'action d'attaque après un court délai
            Platform.runLater(() -> {
                controleur.getEnv().getHero().getActions().set(6, false);
            });

            controleur.getEnv().getHero().attackOnCooldown = true;

            Timeline attackCooldownTimer = new Timeline(
                    new KeyFrame(Duration.seconds(1), e -> {
                        controleur.getEnv().getHero().attackOnCooldown = false;
                        System.out.println("Attaque dispo");
                    })
            );
            attackCooldownTimer.setCycleCount(1);
            attackCooldownTimer.play();

        } else {
            System.out.println("COOLDOWN");
        }
    }

    //à commenter
    public void attaqueEnnemis() {
        // UTILISER UNE COPIE pour éviter ConcurrentModificationException
        List<Acteur> acteursCopie = new ArrayList<>(controleur.getEnv().getActeurs());

        for (Acteur acteur : acteursCopie) {
            if (acteur instanceof Ennemi && !acteur.attackOnCooldown) {
                acteur.agit();
                acteur.attackOnCooldown = true;

                final Acteur finalActeur = acteur;
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

    public Map<Acteur, ImageView> getSprites() {
        return sprites;
    }

    public Acteur getActeur() {
        return a;
    }
}
