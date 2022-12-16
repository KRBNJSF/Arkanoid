package cz.reindl.arkanoidfx.entity;

import java.util.Random;

public class PowerUp extends GameObject {

    private int velocityX, velocityY;
    private boolean isVisible;

    public PowerUp(int velocityX, int velocityY) {
        super();
        this.velocityX = velocityX;
        this.velocityY = velocityY;
        initPowerUpObjectImage();
    }

    private void initPowerUpObjectImage() {
        loadSourceImage("powerUp.png");
        getImageSize();
    }

    public double getPowerUpRect() {
        return getRect().getMaxX();
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

    public boolean isVisible() {
        return isVisible;
    }

    public void setVisible(boolean visible) {
        isVisible = visible;
    }
}
