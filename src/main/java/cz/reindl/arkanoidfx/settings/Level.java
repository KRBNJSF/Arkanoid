package cz.reindl.arkanoidfx.settings;

public enum Level {

    LEVEL1(Settings.LEVEL_1);
    //LEVEL2(Settings.LEVEL_2),
    //LEVEL3(Settings.LEVEL_3);

    private char[][] level;

    Level(char[][] level) {
        this.level = level;
    }

    public char[][] getLevel() {
        return level;
    }

    public void setLevel(char[][] level) {
        this.level = level;
    }
}
