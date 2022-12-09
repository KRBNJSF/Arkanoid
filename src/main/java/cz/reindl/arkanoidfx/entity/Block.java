package cz.reindl.arkanoidfx.entity;

import cz.reindl.arkanoidfx.settings.Settings;

public class Block extends GameObject {

    private int score, lives, rows, columns;

    private boolean isAlive;

    public Block(int columns, int rows) {
        score = 0;
        lives = 3;
        isAlive = true;
        this.columns = columns;
        this.rows = rows;
        Settings.NUMBER_OF_BLOCKS = rows * columns;
        initBlockObjectImage();
    }

    private void initBlockObjectImage() {
        loadSourceImage(BlockState.HEALTHY.getImgSrc());
        getImageSize();
    }

    public int getScore() {
        return score;
    }

    public void setScore(int score) {
        this.score = score;
    }

    public int getLives() {
        return lives;
    }

    public void setLives(int lives) {
        this.lives = lives;
    }

    public boolean isAlive() {
        return isAlive;
    }

    public void setAlive(boolean alive) {
        isAlive = alive;
    }

    public int getRows() {
        return rows;
    }

    public void setRows(int rows) {
        this.rows = rows;
    }

    public int getColumns() {
        return columns;
    }

    public void setColumns(int columns) {
        this.columns = columns;
    }
}
