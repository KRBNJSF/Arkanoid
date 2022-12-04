package cz.reindl.arkanoidfx.entity;

public enum BlockState {

    HEALTHY("healthyBlock.png"),
    DAMAGED("damagedBlock.png"),
    BROKEN("brokenBlock.png");

    private String imgSrc;

    BlockState(String imgSrc) {
        this.imgSrc = imgSrc;
    }

    public String getImgSrc() {
        return imgSrc;
    }

    public void setImgSrc(String imgSrc) {
        this.imgSrc = imgSrc;
    }

}
