package universite_paris8.iut.epereira.lunaria.DossierControleur;

import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.TextArea;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.TilePane;
import javafx.scene.shape.Circle;
import javafx.util.Duration;
import universite_paris8.iut.epereira.lunaria.modele.*;
import universite_paris8.iut.epereira.lunaria.modele.acteurs.Ennemis.Ennemi;
import universite_paris8.iut.epereira.lunaria.modele.acteurs.Hero;


import java.net.URL;
import java.util.*;

public class Controleur implements Initializable {
    @FXML
    private GridPane inventaireGridPane;  //gridPane qui contient les emplacement d'inventaire

    @FXML
    private TilePane tilePaneId; //tilePane pour poesr nos bloc et les casser

    @FXML //pouor afficher les item dans l'inventaire
    private ImageView imageInv1, imageInv2, imageInv3, imageInv4, imageInv5, imageInv6, imageInv7, imageInv8, imageInv9;

    @FXML //Texte Pause quand on arrete le jeux
    private TextArea pauseID;

    @FXML //le Pane qui contient tout
    private Pane tabJeu;

    @FXML //Afficher le fond
    private ImageView background;

    //contient l'item dans la souris
    private Item tempItemSouris;

    //lier acteur avec leur sprites et animation
    private final Map<Acteur, String> animationState = new HashMap<>();
    private final Map<Acteur, Timeline> animations = new HashMap<>();
    private final Map<Acteur, ImageView> sprites = new HashMap<>();
    private final Map<Acteur, Timeline> idleAnimations = new HashMap<>();
    private final Map<Acteur, Timeline> jumpAnimations = new HashMap<>();

    private Image[] heroJumpFrames;

    //Environement modele
    private Image[] heroFrames;
    private Image[] ennemiFrames;
    private Image[] heroIdleFrames;
    private Environement env;

    //Controler la map
    private GestionnaireMap gestionMap;

    //boolean popur savoir si l'inventaire est ouvert ou pas.
    private boolean inventaireBooleanOvert = true;

    //garde quel slot de l'inventaire est selectioné
    private int isSelectedInHand = 0;

    //game loop
    private Timeline gameLoop;


    @Override //initialization
    public void initialize(URL url, ResourceBundle resourceBundle) {
        //Création de l'environement de la taille de l'écren
        env = new Environement(ConfigurationJeu.WIDTH_SCREEN, ConfigurationJeu.HEIGHT_SCREEN);

        //initialiser ce que contient la souris a null
        tempItemSouris = null;


        prechargerImages();
        //appelle de la methode serInvVisible
        setInvVisible(false);

        //régler les taille des different Pane
        tabJeu.setPrefWidth(ConfigurationJeu.WIDTH_SCREEN);
        tabJeu.setPrefHeight(ConfigurationJeu.HEIGHT_SCREEN);

        tilePaneId.setPrefWidth(ConfigurationJeu.WIDTH_SCREEN);
        tilePaneId.setPrefHeight(ConfigurationJeu.HEIGHT_TILES);

        background.setFitWidth(ConfigurationJeu.WIDTH_SCREEN);
        background.setFitHeight(ConfigurationJeu.HEIGHT_SCREEN);

        //initialisation du gestionaire de la map
        gestionMap = new GestionnaireMap(tilePaneId, env);

        //ajouter tous les acteurs dans la vue
        for (Acteur a : env.getActeurs()) {
            ajouterActeurVue(a);
        }

        //démarage du jeux
        configurerEvenements();
        creerBoucleDeJeu();
        Platform.runLater(() -> {
            gestionMap.chargerTiles(env.getTerrain());
        });

        Platform.runLater(() -> {
            tabJeu.requestFocus();
            demarrer();
        });
    }

    private void chargerItemDansMain() {
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

        // Préchargement des images d'ennemi
        ennemiFrames = new Image[2];
        ennemiFrames[0] = new Image(getClass().getResourceAsStream("/universite_paris8/iut/epereira/lunaria/DossierMap/Adepte/idle1.png"));
        ennemiFrames[1] = new Image(getClass().getResourceAsStream("/universite_paris8/iut/epereira/lunaria/DossierMap/Adepte/idle1.png"));
    }


    //faire apparaitre/disparaitre l'inventaire
    private void setInvVisible(Boolean bool) {
        inventaireGridPane.setVisible(bool);
        inventaireGridPane.setDisable(!bool);
        for (int i = 0; i < 9; i++) {
            inventaireGridPane.getChildren().get(i).setVisible(bool);
        }
    }

    //pour chaque entré de touche
    private void configurerEvenements() {
        tabJeu.setOnKeyPressed(this::gererTouchePressee);
        tabJeu.setOnKeyReleased(this::gererToucheRelachee);
    }




    public void dansLaMain(){

    }

    //gestionaire de l'inventaire
    @FXML
    public void inv(ActionEvent event) {
        Node source = (Node) event.getSource();
        Integer row = GridPane.getRowIndex(source);
        Integer col = GridPane.getColumnIndex(source);


        Item temp = env.getHero().getInv().getIteminList(row * 3 + col);

        //vérifie si l'emplacement sur le quel on click est occupé
        //si occupé, echangé ce que contient la souris avec le contenue de l'emplacement
        //sinon, juste placer ce que contient la souris dans l'emplacement
        if (temp != null) {
            Item switch2 = temp;
            env.getHero().getInv().addItem(row * 3 + col, tempItemSouris);
            tempItemSouris = switch2;
        } else {
            env.getHero().getInv().addItem(row * 3 + col, tempItemSouris);
        }

        selectItem(row * 3 + col);


        //charger ce que contient la case par une image de l'item, vide si null

        //imageInv1.setImage(new Image("/universite_paris8/iut/epereira/lunaria/DossierMap/Background.png"));

        //((Node) event.getSource()).setStyle("-fx-background-image: url("+getClass().getResource("/universite_paris8/iut/epereira/lunaria/DossierMap/Background.png")+")");
        //((Node) event.getSource()).setOpacity(1.0);

        //inventaireGridPane.getChildren().get(row * 3 + col).setStyle();


    }

    @FXML//si la souris est cliquer attaquer
    public void clicSouris() {
        attaqueHero();
    }

    @FXML //rien
    public void plusClicSouris() {
    }

    //gere quand les touche sont appuyé, faire une action
    private void gererTouchePressee(KeyEvent event) {
        switch (event.getCode()) {
            case SPACE, Z: //sauter
                env.getHero().getActions().set(0, true);
                break;
            case Q: //aller a gauche
                env.getHero().getActions().set(3, true);
                break;
            case D: //aller a droite
                env.getHero().getActions().set(2, true);
                break;
            case ESCAPE: //mettre le jeux en pause
                env.getHero().getActions().set(5, !env.getHero().getActions().get(5));
                if (env.getHero().getActions().get(5)) {
                    pauseID.setVisible(true);
                    arreter();
                } else {
                    demarrer();
                    pauseID.setVisible(false);
                }
                break;
            case I: //ouvrire ou fermer l'inventaire
                if (env.getHero().getActions().get(5)) {

                } else {
                    env.getHero().getActions().set(4, !env.getHero().getActions().get(4));
                    if (inventaireBooleanOvert) {
                        setInvVisible(true);
                        inventaireBooleanOvert = !inventaireBooleanOvert;
                    } else {
                        setInvVisible(false);
                        inventaireBooleanOvert = !inventaireBooleanOvert;
                    }

                }
                break;

            case DIGIT1:
                selectItem(0);
                break;

            case DIGIT2:
                selectItem(1);
                break;

            case DIGIT3:
                selectItem(2);
                break;

            case DIGIT4:
                selectItem(3);
                break;

            case DIGIT5:
                selectItem(4);
                break;

            case DIGIT6:
                selectItem(5);
                break;

            case DIGIT7:
                selectItem(6);
                break;

            case DIGIT8:
                selectItem(7);
                break;

            case DIGIT9:
                selectItem(8);
                break;

        }
    }

    public Item selectItem(int i) {

        if (!inventaireBooleanOvert) {


        //System.out.println(inventaireGridPane.getChildren().get(i).getStyle());

        if (inventaireGridPane.getChildren().get(i).getStyle().equals("-fx-background-color: white")) {
            //System.out.println("change en jaune");
            inventaireGridPane.getChildren().get(i).setStyle("-fx-background-color: yellow");
            //System.out.println(inventaireGridPane.getChildren().get(i).getStyle());
        } else {
            if (inventaireGridPane.getChildren().get(i).getStyle().toString().equals("-fx-background-color: yellow")) {
                //System.out.println("change en blanc");
                inventaireGridPane.getChildren().get(i).setStyle("-fx-background-color: white");
                return null;
                //System.out.println(inventaireGridPane.getChildren().get(i).getStyle());
            }
        }
        if (isSelectedInHand != i) {
            inventaireGridPane.getChildren().get(isSelectedInHand).setStyle("-fx-background-color: white");
        }
        isSelectedInHand = i;
        return env.getHero().getInv().getIteminList(i);
        }
        else{
            return env.getHero().getInv().getIteminList(isSelectedInHand);
        }

    }

    //gere quand la touche est relacher
    private void gererToucheRelachee(KeyEvent event) {
        switch (event.getCode()) {
            case SPACE, Z:
                env.getHero().getActions().set(0, false);
                break;
            case Q:
                env.getHero().getActions().set(3, false);
                break;
            case D:
                env.getHero().getActions().set(2, false);
                break;
        }
    }

    //création de la boucle de jeux
    private void creerBoucleDeJeu() {
        gameLoop = new Timeline(
                new KeyFrame(Duration.millis(15), e -> miseAJourJeu())
        );
        gameLoop.setCycleCount(Animation.INDEFINITE);
    }

    //gere ce qui se passe toute les tick
    private void miseAJourJeu() {
        List<Acteur> acteursCopie = new ArrayList<>(env.getActeurs());

        List<Acteur> acteursASupprimer = env.getActeursASupprimer();
        for (Acteur acteur : acteursASupprimer) {
            supprimerActeurVue(acteur);
        }

        env.supprimerActeursMarques();
        for (Acteur a : acteursCopie) {
            // Stocker la position avant le déplacement
            double oldX = a.getPosX();

            // Appliquer le déplacement
            a.deplacement();

            // Calculer la vitesse effective (changement de position)
            double deltaX = a.getPosX() - oldX;

            // Mettre à jour l'animation en fonction du mouvement
            mettreAJourAnimation(a, deltaX);

            if (a instanceof Ennemi)
                attaqueEnnemis();
        }


    }

    //à commenter
    public void attaqueHero() {
        if (!env.getHero().attackOnCooldown) {
            env.getHero().getActions().set(6, true);
            env.getHero().attaque();

            Platform.runLater(() -> {
                env.getHero().getActions().set(6, false);
            });

            env.getHero().attackOnCooldown = true;

            Timeline attackCooldownTimer = new Timeline(
                    new KeyFrame(Duration.seconds(3), e -> {
                        env.getHero().attackOnCooldown = false;
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
        for (Acteur acteur : env.getActeurs()) {
            if (acteur instanceof Ennemi && !acteur.attackOnCooldown) {
                acteur.attaque();
                acteur.attackOnCooldown = true;

                final Acteur finalActeur = acteur;
                Timeline attackCooldownTimerEnnemi = new Timeline(
                        new KeyFrame(Duration.seconds(5), e -> {
                            if (!env.getActeursASupprimer().contains(finalActeur)) {
                                finalActeur.attackOnCooldown = false;
                            }
                        })
                );
                attackCooldownTimerEnnemi.setCycleCount(1);
                attackCooldownTimerEnnemi.play();
            }
        }
    }

    public ImageView creerSprite(Acteur a) {
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

            // Démarrer l'animation d'idle par défaut
            idleAnimation.play();
            animationState.put(a, "idle");

            // Stocker les animations
            animations.put(a, walkAnimation);
            idleAnimations.put(a, idleAnimation);
            jumpAnimations.put(a, jumpAnimation);
        } else {
            // Pour les ennemis (animation simple)
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

        // Lier les propriétés de position
        imageView.translateXProperty().bind(a.x.subtract(imageView.getFitWidth() / 2));
        imageView.translateYProperty().bind(a.y.subtract(imageView.getFitHeight() / 2));

        return imageView;
    }

    private void mettreAJourAnimation(Acteur acteur, double vitesseX) {
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
            String currentState = animationState.getOrDefault(acteur, "idle");

            Hero hero = (Hero) acteur;

            // Vérifier si le héros est en train de sauter
            boolean isJumping = !hero.auSol;

            // Décider quelle animation jouer
            if (isJumping) {
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

        if (sprite != null) {
            tabJeu.getChildren().remove(sprite);
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

        animationState.remove(acteur);
    }

    // Démarrage
    public void demarrer() {
        gameLoop.play();
    }

    // Pause
    public void arreter() {
        gameLoop.stop();
    }

    // Modifiez la méthode ajouterActeurVue
    //ajoute un acteur dans la vue
    public void ajouterActeurVue(Acteur acteur) {
        ImageView sprite = creerSprite(acteur);
        sprites.put(acteur, sprite);
        tabJeu.getChildren().add(sprite);
    }
}