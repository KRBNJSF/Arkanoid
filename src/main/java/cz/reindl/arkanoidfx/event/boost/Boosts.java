package cz.reindl.arkanoidfx.event.boost;

import cz.reindl.arkanoidfx.event.EventHandler;

public class Boosts {

    EventHandler handler;

    public Boosts(EventHandler handler) {
        this.handler = handler;
    }

    public void boost1() {
        handler.player.loadSourceImage("boostedPlatform.png");
        handler.player.setWidth((int) handler.player.getImage().getWidth());
        handler.player.setHeight((int) handler.player.getImage().getHeight());
    }

}
