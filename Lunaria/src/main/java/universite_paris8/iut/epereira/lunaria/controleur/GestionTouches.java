package universite_paris8.iut.epereira.lunaria.controleur;

import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import universite_paris8.iut.epereira.lunaria.modele.Environement;
import universite_paris8.iut.epereira.lunaria.modele.acteurs.mobPassif.PNJ;

import java.security.Key;

public class GestionTouches {
    private Environement env;
    private Controleur controleur;

    public GestionTouches(Environement env, Controleur controleur) {
        this.env = env;
        this.controleur = controleur;
    }

    public void gererTouchePressee(KeyEvent event) {
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
                    controleur.getPauseID().setVisible(true);
                    controleur.getGestionBoucle().arreter();
                } else {
                    controleur.getGestionBoucle().demarrer();
                    controleur.getPauseID().setVisible(false);
                }
                break;
            case I: //ouvrire ou fermer l'inventaire
                if (env.getHero().getActions().get(5)) {

                } else {
                    env.getHero().getActions().set(4, !env.getHero().getActions().get(4));
                    if (controleur.getGestionInventaire().getInventaireBooleanOvert()) {
                        controleur.getGestionInventaire().setInvVisible(true);
                        controleur.getGestionInventaire().setInventaireBooleanOvert(!controleur.getGestionInventaire().getInventaireBooleanOvert());
                    } else {
                        controleur.getGestionInventaire().setInvVisible(false);
                        controleur.getGestionInventaire().setInventaireBooleanOvert(!controleur.getGestionInventaire().getInventaireBooleanOvert());
                    }

                }
                break;
            case E:
                for(PNJ pnj : env.getPNJs()) {
                    double distanceX = Math.abs(env.getHero().getPosX() - pnj.getPosX());
                    double distanceY = Math.abs(env.getHero().getPosY() - pnj.getPosY());
                    double distance = Math.sqrt(distanceX * distanceX + distanceY * distanceY);

                    if (distance <= env.getHero().getRange()) {
                        env.setMarchand(pnj.getInv());
                    }
                }
                break;
            case DIGIT1:
                controleur.getGestionInventaire().selectItem(0);
                break;

            case DIGIT2:
                controleur.getGestionInventaire().selectItem(1);
                break;

            case DIGIT3:
                controleur.getGestionInventaire().selectItem(2);
                break;

            case DIGIT4:
                controleur.getGestionInventaire().selectItem(3);
                break;

            case DIGIT5:
                controleur.getGestionInventaire().selectItem(4);
                break;

            case DIGIT6:
                controleur.getGestionInventaire().selectItem(5);
                break;

            case DIGIT7:
                controleur.getGestionInventaire().selectItem(6);
                break;

            case DIGIT8:
                controleur.getGestionInventaire().selectItem(7);
                break;

            case DIGIT9:
                controleur.getGestionInventaire().selectItem(8);
                break;

        }
    }
    //gere quand la touche est relacher
    public void gererToucheRelachee(KeyEvent event) {
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

}
