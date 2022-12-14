package cz.reindl.arkanoidfx.event.boost;

import cz.reindl.arkanoidfx.entity.Ball;
import cz.reindl.arkanoidfx.event.EventHandler;

import java.util.Random;

public class Boosts {

    EventHandler handler;
    public int ballCount = 0;

    public Boosts(EventHandler handler) {
        this.handler = handler;
    }

    public void boost1() {
        handler.player.loadSourceImage("boostedPlatform.png");
        handler.player.setWidth((int) handler.player.getImage().getWidth());
        handler.player.setHeight((int) handler.player.getImage().getHeight());
    }

    public void boost2(double x, double y) {
        for (int i = 0; i < handler.balls.size(); i++) {
            ballCount++;
            handler.balls.add(new Ball(new Random().nextInt(3) - 1, -7));
            handler.balls.get(ballCount).setX(x);
            handler.balls.get(ballCount).setY(y);
            System.out.println("Ball count: " + handler.balls.size());
            return;
        }
    }

}
