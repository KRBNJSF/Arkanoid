package cz.reindl.arkanoidfx.settings;

public abstract class Settings {

    //SCREEN DIMENSIONS
    public static double SCREEN_WIDTH;
    public static double SCREEN_HEIGHT;
    public static final int BOUND_WIDTH = 10;

    //BLOCKS
    public static int NUMBER_OF_BLOCKS = 49;
    public static int DEFAULT_BLOCK_LIVES = 3;
    public static String DEFAULT_BLOCK_IMG = "healthyBlock.png";

    //BALL
    public static double DEFAULT_BALL_X;
    public static double DEFAULT_BALL_Y;
    public static int DEFAULT_BALL_VELOCITY_X = 0;
    public static int DEFAULT_BALL_VELOCITY_Y = -7;

    //PLAYER
    public static double DEFAULT_PLAYER_X;
    public static double DEFAULT_PLAYER_Y;
    public static int DEFAULT_SCORE = 0;
    public static int DEFAULT_PLAYER_LIVES = 2;

    //LEVELS
    public static char[][] LEVEL_1 = {
            {'x', 'x', 'x', 'x', 'x', 'x'},
            {'x', 'x', '_', '_', 'x', 'x'},
            {'x', 'x', '_', '_', 'x', 'x'},
            {'x', 'x', '_', '_', 'x', 'x'},
            {'_', '_', '_', '_', '_', '_'},
            {'_', '_', '_', '_', '_', '_'}
    };

    public static char[][] LEVEL_2 = {
            {'x', 'x', 'x', 'x', 'x', 'x'},
            {'x', 'x', 'x', 'x', 'x', 'x'},
            {'x', 'x', 'x', 'x', 'x', 'x'},
            {'x', 'x', '_', '_', 'x', 'x'},
            {'x', 'x', '_', '_', 'x', 'x'},
            {'x', 'x', '_', '_', 'x', 'x'},
    };

    public static String[][] LEVEL_3 = {
            {"x", "x", "x", "x", "x", "x"},
            {"x", "x", "x", "x", "x", "x"},
            {"x", "x", "x", "x", "x", "x"},
            {"x", "x", "x", "x", "x", "x"},
            {"x", "_", "x", "x", "_", "x"},
            {"x", "_", "x", "x", "_", "x"},
    };

}
