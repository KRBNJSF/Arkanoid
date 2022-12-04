package cz.reindl.arkanoidfx.entity;

public class Block extends GameObject {

    private int score, lives;

    public Block() {
        score = 0;
        lives = 3;
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
}
