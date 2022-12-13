package cz.reindl.arkanoidfx.sound;

import cz.reindl.arkanoidfx.event.EventHandler;
import jaco.mp3.player.MP3Player;

import java.io.File;

public class Sound {

    EventHandler handler;

    public Sound(EventHandler handler) {
        this.handler = handler;
    }

    MP3Player musicPlayer;
    MP3Player soundEffectPlayer;

    //MUSIC
    public File backgroundMusic = new File("src/main/resources/forge.mp3");

    //SOUND EFFECTS
    //public File soundEffect = new File("");

    //MUSIC SETTINGS
    public void setFile(File name) {
        try {
            musicPlayer = new MP3Player(name);
        } catch (Exception e) {
            System.out.println(e);
        }
    }

    public void playSound(File name) {
        musicPlayer.play();
    }

    public void loopSound(Boolean loop) {
        musicPlayer.setRepeat(loop);
    }

    public void stopSound(File name) {
        musicPlayer.stop();
    }

    //SOUND EFFECTS SETTINGS
    public void setSoundEffect(File name) {
        try {
            soundEffectPlayer = new MP3Player(name);
        } catch (Exception e) {
            System.out.println(e);
        }
    }

    public void playSoundEffect(File name) {
        soundEffectPlayer.play();
    }

    public void loopSoundEffect(Boolean loop) {
        soundEffectPlayer.setRepeat(loop);
    }

    public void stopSoundEffect(File name) {
        soundEffectPlayer.stop();
    }

}
