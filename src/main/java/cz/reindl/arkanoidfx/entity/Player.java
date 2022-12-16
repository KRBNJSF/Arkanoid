package cz.reindl.arkanoidfx.entity;

import javafx.scene.input.KeyEvent;

import java.util.Random;

public class Player extends GameObject {

    private int score, lives, velocityX;

    public Player(int score, int lives, int velocityX) {
        super();
        this.score = score;
        this.lives = lives;
        this.velocityX = velocityX;
        initPlatformObjectImage();
    }

    public String getSkin(int bound) {
        skins[0] = "platform.png";
        skins[1] = "platformBetter.png";
        skins[2] = "platformBetterWide.png";
        return skins[new Random().nextInt(bound)];
    }

    private void initPlatformObjectImage() {
        loadSourceImage("platformBetter.png");
        getImageSize();
    }

    public void keyPressed(KeyEvent event) {
        switch (event.getCode()) {
            case A, LEFT -> velocityX = 50;
            case D, RIGHT -> velocityX = -50;
        }

    }

    public void keyReleased(KeyEvent event) {
        switch (event.getCode()) {
            case A, LEFT, D, RIGHT -> velocityX = 0;
        }
    }

    public double getPlayerRect(double range) {
        return getRect().getMinX() + range;
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

    public int getVelocityX() {
        return velocityX;
    }

    public void setVelocityX(int velocityX) {
        this.velocityX = velocityX;
    }

}
