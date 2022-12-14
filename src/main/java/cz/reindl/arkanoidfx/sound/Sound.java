package cz.reindl.arkanoidfx.sound;

import cz.reindl.arkanoidfx.event.EventHandler;
import jaco.mp3.player.MP3Player;

import java.io.File;

public class Sound {

    EventHandler handler;

    public Sound(EventHandler handler) {
        this.handler = handler;
    }

    private MP3Player musicPlayer;
    private MP3Player soundEffectPlayer;
    public File currentMusic;
    public boolean isPlayable = true;

    //MUSIC UTILS
    public void playMusic(File file, Boolean loop) {
        setFile(file);
        playSound(file);
        loopSound(loop);
    }

    public void stopMusic(File file) {
        stopSound(file);
    }

    public void pauseMusic(File file) {
        pauseSound(file);
    }

    //SOUND EFFECTS UTILS
    public void playSoundEffect(File file, Boolean loop) {
        if (isPlayable) {
            setSoundEffect(file);
            startSoundEffect(file);
            loopSoundEffect(loop);
        }
    }

    public void pauseSoundEffect(File file) {
        stopSoundEffect(file);
    }

    //MUSIC SETTINGS
    public void setFile(File name) {
        try {
            musicPlayer = new MP3Player(name);
        } catch (Exception e) {
            System.out.println(e);
        }
    }

    //SOUND EFFECTS SETTINGS
    public void setSoundEffect(File name) {
        try {
            soundEffectPlayer = new MP3Player(name);
        } catch (Exception e) {
            System.out.println(e);
        }
    }

    public void startSoundEffect(File name) {
        soundEffectPlayer.play();
    }

    public void loopSoundEffect(Boolean loop) {
        soundEffectPlayer.setRepeat(loop);
    }

    public void stopSoundEffect(File name) {
        soundEffectPlayer.stop();
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

    public void pauseSound(File name) {
        musicPlayer.pause();
    }

}
