package cz.reindl.arkanoidfx.event.boost;

import cz.reindl.arkanoidfx.entity.Ball;
import cz.reindl.arkanoidfx.event.EventHandler;

import java.util.Random;

public record Boosts(EventHandler handler) {

    public void boost1() {
        handler.player.loadSourceImage("boostedPlatform.png");
        handler.player.setWidth((int) handler.player.getImage().getWidth());
        handler.player.setHeight((int) handler.player.getImage().getHeight());
    }

    public void boost2(double x, double y) {
        handler.ballCount++;
        handler.balls.add(new Ball(new Random().nextInt(3) - 1, 7));
        handler.balls.get(handler.ballCount).setX(x);
        handler.balls.get(handler.ballCount).setY(y);
        handler.balls.get(handler.ballCount).loadSourceImage(handler.balls.get(handler.ballCount).getSkin(3));
        System.out.println("Ball count: " + handler.balls.size());
    }

}
