package cz.reindl.arkanoidfx.entity;

import java.util.ArrayList;
import java.util.Random;

public class Ball extends GameObject {

    private int velocityX, velocityY;
    private ArrayList<Ball> balls;

    public Ball(int velocityX, int velocityY) {
        super();
        this.velocityX = velocityX;
        this.velocityY = velocityY;
        initBallObjectImage();
        balls = new ArrayList<Ball>();
    }

    private void initBallObjectImage() {
        loadSourceImage("ball.png");
        getImageSize();
    }

    public String getSkin(int bound) {
        skins[0] = "ball.png";
        skins[1] = "ball2.png";
        skins[2] = "ball3.png";
        return skins[new Random().nextInt(bound)];
    }

    public double getBallRect() {
        return getRect().getX() + getWidth();
    }

    public int getVelocityX() {
        return velocityX;
    }

    public void setVelocityX(int velocityX) {
        this.velocityX = velocityX;
    }

    public int getVelocityY() {
        return velocityY;
    }

    public void setVelocityY(int velocityY) {
        this.velocityY = velocityY;
    }

    public double getDefaultX() {
        return 0;
    }

    public double getDefaultY() {
        return 0;
    }

    public ArrayList<Ball> getBalls() {
        return balls;
    }

    public void setBalls(ArrayList<Ball> balls) {
        this.balls = balls;
    }
}
