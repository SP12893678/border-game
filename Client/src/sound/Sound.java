package sound;

import java.io.*;
import javafx.embed.swing.JFXPanel;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;

public class Sound {
    private JFXPanel fxPanel;
    private Media sound_Media;
    private MediaPlayer mediaPlayer;
    private String sound_path;
    private boolean loop;
    private double volume;

    public Sound(String sound_path, boolean loop, double volume) {
        this.sound_path = sound_path;
        this.loop = loop;
        this.volume = volume;
        fxPanel = new JFXPanel();
        fxPanel.setVisible(false);
        SetMedia();
    }

    private void SetMedia() {
        sound_Media = new Media(new File(sound_path).toURI().toString());
        mediaPlayer = new MediaPlayer(sound_Media);
        SetVolume(volume);
        SetLoop(loop);
    }

    public void SetVolume(double volume) {
        this.volume = volume;
        mediaPlayer.setVolume(volume);
    }

    public void SetLoop(boolean loop) {
        this.loop = loop;
        if (loop) {
            mediaPlayer.setCycleCount(MediaPlayer.INDEFINITE);
        }
    }

    public void newMedia() {
        SetMedia();
    }

    public void playSound() {
        mediaPlayer.play();
    }

    public void stopSound() {
        mediaPlayer.stop();
    }

    public void pauseSound() {
        mediaPlayer.pause();
    }
}