package cz.reindl.arkanoidfx.sound;

import java.io.File;

public abstract class Music {

    private static final String PATH_PREFIX = "src/main/resources/music/";

    //MUSIC
    public static File backgroundTheme = new File(PATH_PREFIX + "backgroundMain.mp3");
    public static File backgroundTheme2 = new File(PATH_PREFIX + "bgSoundtrack.mp3");

    //SOUND EFFECTS
    public static File blockHitHealthy = new File(PATH_PREFIX + "blockHitHealthy.mp3");
    public static File blockHitDamaged = new File(PATH_PREFIX + "blockHitHealthy.mp3");
    public static File blockHitBroken = new File(PATH_PREFIX + "blockHitHealthy.mp3");
    public static File platformHit = new File(PATH_PREFIX + "platformHit.mp3");
    public static File powerUpHit = new File(PATH_PREFIX + "healEffect.mp3");
    public static File wallHit = new File(PATH_PREFIX + "punch.mp3");

    public static File gameWinSound = new File(PATH_PREFIX + "gameWinSound.mp3");
    public static File gameLoseSound = new File(PATH_PREFIX + "fightLoseSound.mp3");
    public static File lifeLoseSound = new File(PATH_PREFIX + "levelUp.mp3");

}
