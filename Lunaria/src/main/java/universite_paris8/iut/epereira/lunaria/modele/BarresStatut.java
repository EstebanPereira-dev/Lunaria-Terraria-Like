package universite_paris8.iut.epereira.lunaria.modele;

import javafx.animation.FadeTransition;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.scene.effect.Glow;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.util.Duration;

public class BarresStatut extends Pane {

    // Classe interne pour une barre individuelle
    private static class BarreIndividuelle extends Pane {
        private Rectangle barreFond;
        private Rectangle barreValeur;
        private Text label;
        private int valeurMax;
        private int valeurActuelle;
        private Color couleurPrincipale;
        private String nom;


        public BarreIndividuelle(String nom, int valeurMax, int largeur, int hauteur, Color couleurPrincipale) {
            this.nom = nom;
            this.valeurMax = valeurMax;
            this.valeurActuelle = valeurMax;
            this.couleurPrincipale = couleurPrincipale;

            // Label de la barre
            label = new Text(nom + ": " + valeurActuelle + "/" + valeurMax);
            label.setFont(Font.font(12));
            label.setFill(Color.WHITE);

            // Fond gris
            barreFond = new Rectangle(largeur, hauteur);
            barreFond.setFill(Color.DARKGRAY);
            barreFond.setStroke(Color.BLACK);
            barreFond.setStrokeWidth(1);

            // Barre de valeur
            barreValeur = new Rectangle(largeur, hauteur);
            barreValeur.setFill(couleurPrincipale);

            // Positionnement
            label.setY(-5); // Au-dessus de la barre
            barreFond.setY(5);
            barreValeur.setY(5);

            getChildren().addAll(label, barreFond, barreValeur);
            setPrefSize(largeur, hauteur + 20); // +20 pour le label
        }

        public void mettreAJour(int nouvelleValeur) {
            int ancienneValeur = this.valeurActuelle;

            // Limiter la valeur entre 0 et max
            this.valeurActuelle = Math.max(0, Math.min(nouvelleValeur, valeurMax));

            // Mettre à jour le label
            label.setText(nom + ": " + valeurActuelle + "/" + valeurMax);

            // Calculer la largeur proportionnelle
            double ratio = (double) valeurActuelle / valeurMax;
            double nouvelleWidth = barreFond.getWidth() * ratio;
            barreValeur.setWidth(nouvelleWidth);

            // Changer la couleur selon le niveau
            Color couleurActuelle;
            if (ratio < 0.25) {
                couleurActuelle = Color.RED;
            } else if (ratio < 0.5) {
                couleurActuelle = Color.ORANGE;
            } else {
                couleurActuelle = couleurPrincipale;
            }
            barreValeur.setFill(couleurActuelle);

            // Effets visuels
            if (valeurActuelle < ancienneValeur) {
                // Perte de valeur - flash rouge
                FadeTransition flash = new FadeTransition(Duration.millis(150), barreValeur);
                flash.setFromValue(0.4);
                flash.setToValue(1.0);
                flash.play();
            } else if (valeurActuelle > ancienneValeur) {
                // Gain de valeur - effet glow
                barreValeur.setEffect(new Glow(0.8));
                Timeline removeGlow = new Timeline(
                        new KeyFrame(Duration.millis(500), e -> barreValeur.setEffect(null))
                );
                removeGlow.play();
            }
        }

        public int getValeurActuelle() {
            return valeurActuelle;
        }

        public int getValeurMax() {
            return valeurMax;
        }
    }

    // Les deux barres
    private BarreIndividuelle barreVie;
    private BarreIndividuelle barreFaim;

    public BarresStatut(int pvMax, int faimMax, int largeur, int hauteurBarre) {
        VBox container = new VBox(10); // Espacement de 10 pixels entre les barres

        // Créer les barres
        barreVie = new BarreIndividuelle("Vie", pvMax, largeur, hauteurBarre, Color.LIMEGREEN);
        barreFaim = new BarreIndividuelle("Faim", faimMax, largeur, hauteurBarre, Color.CHOCOLATE);

        container.getChildren().addAll(barreVie, barreFaim);
        getChildren().add(container);

        setPrefSize(largeur, (hauteurBarre + 30) * 2); // +30 pour labels et espacement
    }

    // Méthodes publiques pour mettre à jour les barres
    public void mettreAJourVie(int nouveauxPV) {
        barreVie.mettreAJour(nouveauxPV);
    }

    public void mettreAJourFaim(int nouvelleFaim) {
        barreFaim.mettreAJour(nouvelleFaim);
    }

    public void mettreAJour(int nouveauxPV, int nouvelleFaim) {
        mettreAJourVie(nouveauxPV);
        mettreAJourFaim(nouvelleFaim);
    }

    // Getters
    public int getVieActuelle() {
        return barreVie.getValeurActuelle();
    }

    public int getVieMax() {
        return barreVie.getValeurMax();
    }

    public int getFaimActuelle() {
        return barreFaim.getValeurActuelle();
    }

    public int getFaimMax() {
        return barreFaim.getValeurMax();
    }

    // Méthodes utilitaires
    public boolean estVivant() {
        return getVieActuelle() > 0;
    }

    public boolean aFaim() {
        return getFaimActuelle() < getFaimMax() * 0.3; // Moins de 30%
    }

    public boolean estAffame() {
        return getFaimActuelle() == 0;
    }
}