package cz.reindl.arkanoidfx.sound;

import cz.reindl.arkanoidfx.event.EventHandler;
import jaco.mp3.player.MP3Player;

import java.io.File;

public class Sound {

    EventHandler handler;
    private String prefix = "src/main/resources/music/";

    public Sound(EventHandler handler) {
        this.handler = handler;
    }

    MP3Player musicPlayer;
    MP3Player soundEffectPlayer;

    //MUSIC
    public File backgroundMusic = new File(prefix + "shopMusic.mp3");

    //SOUND EFFECTS
    public File blockHit = new File(prefix + "punch.mp3");
    public File blockHit2 = new File(prefix + "ratBite.mp3");
    public File blockHit3 = new File(prefix + "daggerHit.mp3");
    public File powerUpHit = new File(prefix + "healEffect.mp3");
    public File gameWinEffect = new File(prefix + "fightWin.mp3");
    public File scoreEarnEffect = new File(prefix + "moneyEarn.mp3");
    public File gameLoseSound = new File(prefix + "fightLoseSound.mp3");
    public File lifeLoseSound = new File(prefix + "levelUp.mp3");
    public File wallHit = new File(prefix + "swordSlash.mp3");

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
