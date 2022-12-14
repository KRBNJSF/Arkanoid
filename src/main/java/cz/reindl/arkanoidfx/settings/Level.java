package cz.reindl.arkanoidfx.settings;

public enum Level {

    LEVEL1(Settings.LEVEL_1),
    LEVEL2(Settings.LEVEL_2),
    LEVEL3(Settings.LEVEL_3);

    private String[][] level;

    Level(String[][] level) {
        this.level = level;
    }

    public String[][] getLevel() {
        return level;
    }

    public void setLevel(String[][] level) {
        this.level = level;
    }
}
