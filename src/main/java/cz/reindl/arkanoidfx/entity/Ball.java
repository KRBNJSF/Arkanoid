package cz.reindl.arkanoidfx.entity;

public class Ball extends GameObject {

    private int velocityX, velocityY;

    public Ball(double x, double y, int velocityX, int velocityY) {
        super(x, y);
        this.velocityX = velocityX;
        this.velocityY = velocityY;
        initBallObjectImage();
    }

    private void initBallObjectImage() {
        loadSourceImage("ball.png");
        getImageSize();
        this.width = 20;
        this.height = 20;
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
