package universite_paris8.iut.epereira.lunaria.controleur;

import javax.sound.sampled.*;
import java.io.File;
import java.io.IOException;

public class LecteurAudio {

    private Clip clip;

    public void jouerSon(String cheminFichier) {
        try {
            AudioInputStream audioInput = AudioSystem.getAudioInputStream(new File(cheminFichier));
            clip = AudioSystem.getClip();
            clip.open(audioInput);
            clip.loop(Clip.LOOP_CONTINUOUSLY); // Son en boucle
            clip.start();
        } catch (UnsupportedAudioFileException | IOException | LineUnavailableException e) {
            e.printStackTrace();
        }
    }

    public void arreter() {
        if (clip != null) {
            clip.stop();
            clip.close();
        }
    }
}