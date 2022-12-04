package cz.reindl.arkanoidfx.entity;

import javafx.event.EventHandler;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;

public class Player extends GameObject {

    private int score, lives, velocityX;

    public Player(double x, double y, int score, int lives, int velocityX) {
        super(x, y);
        this.score = score;
        this.lives = lives;
        this.velocityX = velocityX;
        initPlatformObjectImage();
    }

    private void initPlatformObjectImage() {
        loadSourceImage("platformBetter.png");
        getImageSize();
        this.height = 20;
        this.width = 100;
    }

    public void keyPressed(KeyEvent event) {
        switch (event.getCode()) {
            case A, LEFT -> velocityX = 5;
            case D, RIGHT -> velocityX = -5;
        }

    }

    public void keyReleased(KeyEvent event) {
        switch (event.getCode()) {
            case A, LEFT, D, RIGHT -> velocityX = 0;
        }
    }

    public double getPlayerRect(double range) {
        return getRect().getX() + range;
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