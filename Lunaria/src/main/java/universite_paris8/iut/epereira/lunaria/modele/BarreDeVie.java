package universite_paris8.iut.epereira.lunaria.modele;

import javafx.animation.FadeTransition;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.scene.effect.Glow;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.util.Duration;

public class BarreDeVie extends Pane {
    private Rectangle barreFond;   // Fond gris
    private Rectangle barreHP;     // Barre verte de PV
    private int pvMax;
    private int pvActuels;

    public BarreDeVie(int pvMax, int largeur, int hauteur) {
        this.pvMax = pvMax;
        this.pvActuels = pvMax;

        // Créer le fond de la barre (gris)
        barreFond = new Rectangle(largeur, hauteur);
        barreFond.setFill(Color.DARKGRAY);
        barreFond.setStroke(Color.BLACK);
        barreFond.setStrokeWidth(1);

        // Créer la barre de vie (verte)
        barreHP = new Rectangle(largeur, hauteur);
        barreHP.setFill(Color.GREEN);

        getChildren().addAll(barreFond, barreHP);
        setPrefSize(largeur, hauteur);
    }

    /**
     * Met à jour la barre de vie avec les PV actuels
     * @param pv Les points de vie actuels
     */
    public void mettreAJour(int pv) {
        // Stocker les anciens PV pour comparer
        int anciensPV = this.pvActuels;

        // Mettre à jour les PV actuels (entre 0 et pvMax)
        this.pvActuels = Math.max(0, Math.min(pv, pvMax));

        // Calculer la largeur proportionnelle aux PV
        double ratio = (double) pvActuels / pvMax;
        double nouvelleWidth = barreFond.getWidth() * ratio;

        // Mettre à jour la largeur de la barre de vie
        barreHP.setWidth(nouvelleWidth);

        // Changer la couleur selon le niveau de vie
        if (ratio < 0.25) {
            barreHP.setFill(Color.RED);
        } else if (ratio < 0.5) {
            barreHP.setFill(Color.ORANGE);
        } else {
            barreHP.setFill(Color.GREEN);
        }

        // Option: ajouter un effet visuel si les PV changent
        if (pvActuels < anciensPV) {
            // Effet quand on perd des PV - petit flash rouge
            FadeTransition flash = new FadeTransition(Duration.millis(100), barreHP);
            flash.setFromValue(0.5);
            flash.setToValue(1.0);
            flash.play();
        } else if (pvActuels > anciensPV) {
            // Effet quand on gagne des PV - petit flash vert brillant
            barreHP.setEffect(new Glow(0.8));

            Timeline removeGlow = new Timeline(
                    new KeyFrame(Duration.millis(500), e -> barreHP.setEffect(null))
            );
            removeGlow.play();
        }
    }
}