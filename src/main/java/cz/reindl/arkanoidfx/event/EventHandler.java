package cz.reindl.arkanoidfx.event;

import cz.reindl.arkanoidfx.entity.Ball;
import cz.reindl.arkanoidfx.entity.Block;
import cz.reindl.arkanoidfx.entity.Player;
import cz.reindl.arkanoidfx.settings.Settings;
import cz.reindl.arkanoidfx.view.GameView;
import javafx.scene.input.KeyEvent;
import javafx.scene.robot.Robot;

import java.util.ArrayList;

public class EventHandler {

    GameView view;
    public Player player;
    public Ball ball;
    public ArrayList<Block> blocks;
    public boolean reset;

    public EventHandler(GameView view) {
        this.view = view;
        initGameObjects();
    }

    private void initGameObjects() {
        player = new Player(0, 0, 0, 2, 5);
        player.setX(Settings.SCREEN_WIDTH / 2 - player.getWidth() / 2);
        player.setY(Settings.SCREEN_HEIGHT - player.getWidth() / 2);

        ball = new Ball(0, 0, 0, -5);
        ball.setX(player.getX() + player.getWidth() / 2 - ball.getWidth() / 2);
        ball.setY(Settings.SCREEN_HEIGHT - (2 * ball.getHeight() + player.getHeight() + 5) - ball.getHeight() / 3);

        blocks = new ArrayList<>(Settings.NUMBER_OF_BLOCKS);

        int iterations = 0;

        for (int i = 0; i < Settings.NUMBER_OF_BLOCKS; i++) {
            blocks.add(new Block(0, 0));
        }

        for (int y = 0; y < 5; y++) {
            for (int x = 0; x < 5; x++) {
                blocks.get(iterations).setX(x + blocks.get(iterations).getWidth());
                blocks.get(iterations).setY(y + blocks.get(iterations).getHeight());
                iterations++;
            }
        }

        //DEFAULT Settings
        Settings.DEFAULT_BALL_X = player.getX() + player.getWidth() / 2 - ball.getWidth() / 2; //ball.getX();
        Settings.DEFAULT_BALL_Y = Settings.SCREEN_HEIGHT - (2 * ball.getHeight() + player.getHeight()) - ball.getHeight() / 3; //ball.getY();
        Settings.DEFAULT_PLAYER_X = Settings.SCREEN_WIDTH / 2 - player.getWidth() / 2; //player.getX();
    }

    public void moveBall() {
        if (ball.getY() >= Settings.SCREEN_HEIGHT) {
            reset = true;
            resetValues();
        }
        if (ball.getX() + ball.getWidth() >= Settings.SCREEN_WIDTH) {
            ball.setVelocityX(ball.getVelocityX() * -1);
        }
        if (ball.getX() <= 0) {
            ball.setVelocityX(ball.getVelocityX() * -1);
        }
        if (ball.getY() <= 0) {
            ball.setVelocityY(ball.getVelocityY() * -1);
        } else {
            ball.setVelocityX(ball.getVelocityX());
        }
        ball.setX(ball.getX() + ball.getVelocityX());
        ball.setY(ball.getY() + ball.getVelocityY());
    }

    public void resetValues() {
        if (reset) {
            view.isRunning = false;
            ball.setVelocityX(Settings.DEFAULT_BALL_VELOCITY_X);
            ball.setVelocityY(Settings.DEFAULT_BALL_VELOCITY_Y);
            ball.setX(Settings.DEFAULT_BALL_X);
            ball.setY(Settings.DEFAULT_BALL_Y);

            player.setX(Settings.DEFAULT_PLAYER_X);
            Robot robot = new Robot();
            robot.mouseMove(Settings.SCREEN_WIDTH / 2 - 50, Settings.SCREEN_HEIGHT / 2);
            player.setScore(0);
            player.setLives(Settings.DEFAULT_PLAYER_LIVES);
            for (int i = 0; i < Settings.NUMBER_OF_BLOCKS; i++) {
                blocks.get(i).setLives(Settings.DEFAULT_PLAYER_LIVES);
                blocks.get(i).loadSourceImage(Settings.DEFAULT_BLOCK_IMG);
            }
            reset = false;
        }
    }

    public void checkCollision() {
        if (ball.getRect().intersects(player.getRect().getBoundsInParent())) {
            if (ball.getRect().getX() + ball.getWidth() <= player.getPlayerRect(player.getWidth() / 3)) {
                ball.setVelocityY(ball.getVelocityY() * -1);
                ball.setVelocityX(-2);
                if (ball.getVelocityX() >= 0) {
                    ball.setVelocityX(ball.getVelocityX() * -1);
                }
            }
            if (ball.getBallRect() <= player.getPlayerRect(player.getWidth() / 1.5) && ball.getBallRect() > player.getPlayerRect(player.getWidth() / 3)) {
                ball.setVelocityY(ball.getVelocityY() * -1);
                //ball.setVelocityX(-5);
                ball.setVelocityX(0);
            }
            if (ball.getBallRect() <= player.getPlayerRect(player.getWidth()) && ball.getBallRect() > player.getPlayerRect(player.getWidth() / 1.5)) {
                ball.setVelocityY(ball.getVelocityY() * -1);
                //ball.setVelocityX(5);
                if (ball.getVelocityX() <= 0) {
                    ball.setVelocityX(-2);
                    ball.setVelocityX(ball.getVelocityX() * -1);
                }
            }
        }
    }

    public void checkBlockCollision() {
        for (Block block : blocks) {
            if (ball.getRect().intersects(block.getRect().getBoundsInParent()) && block.getLives() > 0) {
                addToScore();
                System.out.println(player.getScore() + ": " + block.getX());
                if (block.getLives() == 3) {
                    block.setLives(2);
                    block.loadSourceImage("damagedBlock.png");
                } else if (block.getLives() == 2) {
                    block.setLives(1);
                    block.loadSourceImage("brokenBlock.png");
                } else {
                    block.setLives(0);
                    block.loadSourceImage("invisBlock.png");
                }
                ball.setVelocityY(ball.getVelocityY() * -1);
                //ball.setVelocityX(-5);
                if (ball.getVelocityX() > 0) {
                    ball.setVelocityX(ball.getVelocityX() * -1);
                }
            }
        }

    }

    private void addToScore() {
        player.setScore(player.getScore() + 100);
    }

}
