package cz.reindl.arkanoidfx.entity;

import java.util.Random;

public class Ball extends GameObject {

    private int velocityX, velocityY;

    public Ball(int velocityX, int velocityY) {
        super();
        this.velocityX = velocityX;
        this.velocityY = velocityY;
        initBallObjectImage();
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

}
