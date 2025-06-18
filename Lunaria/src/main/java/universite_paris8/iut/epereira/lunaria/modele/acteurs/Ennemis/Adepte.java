package universite_paris8.iut.epereira.lunaria.modele.acteurs.Ennemis;

import universite_paris8.iut.epereira.lunaria.modele.Environement;
import universite_paris8.iut.epereira.lunaria.modele.acteurs.Hero;

import java.util.*;

import static universite_paris8.iut.epereira.lunaria.modele.ConfigurationJeu.rdm;

public class Adepte extends EnnemiTerrestre{
    private static long dernierSpawn = 0;

    private Queue<Point> chemin;

    public Adepte(int pv, int v, int degat, int range, Environement env, Hero hero, double x, double y, int cooldown) {
        super(pv, v, degat, range, env, hero, x, y,cooldown);
        ecu.set(2);
    }

    @Override
    public void loot() {
        if (getPv() <= 0) {

        }
    }

        @Override
        public void spawner(){
            if (!conditionApparation())
                return;

            double x = rdm.nextDouble() * getEnv().getWidth();
            double y = trouverHauteurSol(x) - 20;
            int pv = 15 + rdm.nextInt(10);
            int vitesse = 1 + rdm.nextInt(2);
            int degats = 8 + rdm.nextInt(5);

            Adepte nouvelAdepte = new Adepte(pv, vitesse, degats, range, getEnv(), hero, x, y,getCooldown());
            getEnv().ajouter(nouvelAdepte);
            dernierSpawn = System.currentTimeMillis();
        }

        @Override
        public boolean conditionApparation(){
            if (!this.getEnv().getEtatJour().getValue()) {   //faire spawn la nuit
                long maintenant = System.currentTimeMillis();
                return (maintenant - dernierSpawn) > 90000 &&
                        rdm.nextInt(100) < 30; // 30% de chance
            }
            else return false;
        }


    }
