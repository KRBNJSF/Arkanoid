package cz.reindl.arkanoidfx.utils;

public class Interval extends Thread {

    /*

    if (interval.isReady()) {
        //DO SOMETHING
        interval.done();
     }

     */

    private int interval;
    private volatile boolean canDo;
    private volatile boolean running;

    public Interval(int interval) {
        this.interval = interval;
        this.running = true;
        this.canDo = true;
    }

    public void run() {
        while (running) {
            boolean canDo;
            synchronized (this) {
                canDo = this.canDo;
            }
            if (!canDo) {
                try {
                    Thread.sleep(interval);
                } catch (InterruptedException e) {
                    System.out.println(e);
                }
                synchronized (this) {
                    this.canDo = true;
                }
            }
        }
    }

    public synchronized void done() {
        this.canDo = false;
    }

    public synchronized boolean isReady() {
        return canDo;
    }

    public int getInterval() {
        return interval;
    }

    public void setInterval(int interval) {
        this.interval = interval;
    }
}

