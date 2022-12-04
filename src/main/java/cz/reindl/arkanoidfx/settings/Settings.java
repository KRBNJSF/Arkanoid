package cz.reindl.arkanoidfx.settings;

public abstract class Settings {

    //SCREEN DIMENSIONS
    public static double SCREEN_WIDTH;
    public static double SCREEN_HEIGHT;

    //BLOCKS
    public static int NUMBER_OF_BLOCKS = 36;
    public static int DEFAULT_BLOCK_LIVES;
    public static String DEFAULT_BLOCK_IMG = "healthyBlock.png";

    //BALL
    public static double DEFAULT_BALL_X;
    public static double DEFAULT_BALL_Y;
    public static int DEFAULT_BALL_VELOCITY_X = 0;
    public static int DEFAULT_BALL_VELOCITY_Y = -7;

    //PLAYER
    public static double DEFAULT_PLAYER_X;
    public static int DEFAULT_SCORE;
    public static int DEFAULT_PLAYER_LIVES = 3;

}
