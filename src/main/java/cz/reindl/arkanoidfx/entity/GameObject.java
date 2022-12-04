package cz.reindl.arkanoidfx.entity;

import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.shape.Rectangle;

public abstract class GameObject {
    protected int width, height;
    protected double x, y;
    protected Image image;
    protected ImageView imageView;
    protected String imgSrc;

    public GameObject(double x, double y) {
        this.x = x;
        this.y = y;
    }

    protected GameObject() {
    }

    public void loadSourceImage(String imgSrc) {
        this.image = new Image(imgSrc);
        this.imgSrc = imgSrc;
        //this.imageView = new ImageView(this.image);
    }

    protected void getImageSize() {
        this.width = (int) image.getWidth();
        this.height = (int) image.getHeight();
    }

    public Rectangle getRect() {
        return new Rectangle(x, y, width, height);
    }

    public double getX() {
        return x;
    }

    public void setX(double x) {
        this.x = x;
    }

    public double getY() {
        return y;
    }

    public Image getImage() {
        return image;
    }

    public void setImage(Image image) {
        this.image = image;
    }

    public void setY(double y) {
        this.y = y;
    }

    public int getWidth() {
        return width;
    }

    public void setWidth(int width) {
        this.width = width;
    }

    public int getHeight() {
        return height;
    }

    public void setHeight(int height) {
        this.height = height;
    }

    public String getImgSrc() {
        return imgSrc;
    }

    public void setImgSrc(String imgSrc) {
        this.imgSrc = imgSrc;
    }

    public ImageView getImageView() {
        return imageView;
    }

    public void setImageView(ImageView imageView) {
        this.imageView = imageView;
    }

}
