package cz.reindl.arkanoidfx.event;

import cz.reindl.arkanoidfx.entity.*;
import cz.reindl.arkanoidfx.event.boost.Boosts;
import cz.reindl.arkanoidfx.settings.Settings;
import cz.reindl.arkanoidfx.utils.Utils;
import cz.reindl.arkanoidfx.view.GameView;
import javafx.scene.effect.ColorAdjust;
import javafx.scene.robot.Robot;

import java.util.ArrayList;
import java.util.Random;

public class EventHandler {

    GameView view;
    Boosts boosts = new Boosts(this);
    public Player player;
    public Ball ball;
    public PowerUp powerUp;
    public ArrayList<Block> blocks;

    public boolean reset;
    public int lives = Settings.NUMBER_OF_BLOCKS;
    public int allWidth = 0;
    public int allHeight = 0;
    public int level = 1;
    ColorAdjust colorAdjust;

    public EventHandler(GameView view) {
        this.view = view;
        initGameObjects();
    }

    private void initGameObjects() {
        colorAdjust = new ColorAdjust();
        colorAdjust.setHue(100);

        player = new Player(Settings.DEFAULT_SCORE, Settings.DEFAULT_PLAYER_LIVES, 5);
        player.setX(Settings.SCREEN_WIDTH / 2 - player.getWidth() / 2);
        player.setY(Settings.SCREEN_HEIGHT - player.getWidth() / 2);

        ball = new Ball(0, -7);
        ball.setX(player.getX() + player.getWidth() / 2 - ball.getWidth() / 2);
        ball.setY(Settings.SCREEN_HEIGHT - (2 * ball.getHeight() + player.getHeight() + 15) - ball.getHeight() / 3);

        powerUp = new PowerUp(0, 5);

        blocks = new ArrayList<>(Settings.NUMBER_OF_BLOCKS);
        initArray();
        printArray(0);

        // FIXME: 09.12.2022 Dynamic Blocks
        /*for (int y = 0; y < blocks.get(0).getRows(); y++) {
            for (int x = 0; x < blocks.get(0).getColumns(); x++) {
                allWidth += blocks.get(iterations).getWidth();
                allWidth += blocks.get(iterations).getHeight();
                if (allWidth <= Settings.SCREEN_WIDTH) {
                    blocks.get(iterations).setX((Settings.SCREEN_WIDTH / 4) + (x * blocks.get(iterations).getWidth()));
                }
                if (allHeight <= Settings.SCREEN_HEIGHT / 2) {
                    blocks.get(iterations).setY(y * blocks.get(iterations).getHeight());
                }
                iterations++;
            }
        }*/

        //DEFAULT Settings
        Settings.DEFAULT_BALL_X = player.getX() + player.getWidth() / 2 - ball.getWidth() / 2; //ball.getX();
        Settings.DEFAULT_BALL_Y = Settings.SCREEN_HEIGHT - (2 * ball.getHeight() + player.getHeight() + 15) - ball.getHeight() / 3; //ball.getY();
        Settings.DEFAULT_PLAYER_X = Settings.SCREEN_WIDTH / 2 - player.getWidth() / 2; //player.getX();
    }

    public void moveBall() {
        if (ball.getY() >= Settings.SCREEN_HEIGHT && player.getLives() <= 0) {
            reset = true;
            resetValues();
        } else if (ball.getY() >= Settings.SCREEN_HEIGHT && player.getLives() > 0) {
            player.setLives(player.getLives() - 1);
            resetBall();
        }
        if (ball.getX() + ball.getWidth() >= Settings.SCREEN_WIDTH) {
            ball.setVelocityX(ball.getVelocityX() * -1);
        }
        if (ball.getX() <= 0) {
            ball.setVelocityX(ball.getVelocityX() * -1);
        }
        if (ball.getY() <= 0) {
            ball.setY(10);
            ball.setVelocityY(ball.getVelocityY() * -1);
        } else {
            ball.setVelocityX(ball.getVelocityX());
        }
        ball.setX(ball.getX() + ball.getVelocityX());
        ball.setY(ball.getY() + ball.getVelocityY());
    }

    public void resetValues() {
        if (reset) {
            view.lastScore = player.getScore();
            view.isRunning = false;
            player.loadSourceImage(player.getSkin(3));
            player.setWidth((int) player.getImage().getWidth());
            player.setHeight((int) player.getImage().getHeight());
            player.setX(Settings.DEFAULT_PLAYER_X);

            ball.loadSourceImage(ball.getSkin(3));
            resetBall();

            player.setScore(Settings.DEFAULT_SCORE);
            player.setLives(Settings.DEFAULT_PLAYER_LIVES);

            blocks.clear();
            int ranX = new Random().nextInt(5) + 3;
            int ranY = new Random().nextInt(5) + 3;
            Settings.NUMBER_OF_BLOCKS = ranX * ranY;
            lives = Settings.NUMBER_OF_BLOCKS;

            initArray();
            blocks.get(0).setColumns(ranY);
            blocks.get(0).setRows(ranX);

            for (int i = 0; i < Settings.NUMBER_OF_BLOCKS; i++) {
                blocks.get(i).setLives(Settings.DEFAULT_BLOCK_LIVES);
                blocks.get(i).setAlive(true);
                blocks.get(i).loadSourceImage(Settings.DEFAULT_BLOCK_IMG);
            }

            printArray(0);

            reset = false;
        }
    }

    private void resetBall() {
        view.isRunning = false;
        ball.setVelocityX(Settings.DEFAULT_BALL_VELOCITY_X);
        ball.setVelocityY(Settings.DEFAULT_BALL_VELOCITY_Y);
        ball.setX(Settings.DEFAULT_BALL_X);
        ball.setY(Settings.DEFAULT_BALL_Y);

        powerUp.setVisible(false);

        Robot robot = new Robot();
        robot.mouseMove(Settings.SCREEN_WIDTH / 2 - 50, Settings.SCREEN_HEIGHT / 2);
    }

    public void checkBallCollision() {
        if (ball.getRect().intersects(player.getRect().getBoundsInParent())) {

            if (ball.getBallRect() <= player.getPlayerRect(player.getWidth() / 5)) {
                ball.setVelocityY(ball.getVelocityY() * -1);
                ball.setVelocityX(-6);
                if (ball.getVelocityX() >= 0) {
                    ball.setVelocityX(ball.getVelocityX() * -1);
                }
            }

            if (ball.getBallRect() <= player.getPlayerRect(player.getWidth() / 2.5) && ball.getBallRect() > player.getPlayerRect(player.getWidth() / 5)) {
                ball.setVelocityY(ball.getVelocityY() * -1);
                ball.setVelocityX(-3);
                if (ball.getVelocityX() >= 0) {
                    ball.setVelocityX(ball.getVelocityX() * -1);
                }
            }

            if (ball.getBallRect() <= player.getPlayerRect(player.getWidth() / 1.66) && ball.getBallRect() > player.getPlayerRect(player.getWidth() / 2.5)) {
                ball.setVelocityY(ball.getVelocityY() * -1);
                ball.setVelocityX(new Random().nextInt(3) - 1);
            }

            if (ball.getBallRect() <= player.getPlayerRect(player.getWidth() / 1.25) && ball.getBallRect() > player.getPlayerRect(player.getWidth() / 1.66)) {
                ball.setVelocityY(ball.getVelocityY() * -1);
                ball.setVelocityX(4);
                if (ball.getVelocityX() <= 0) {
                    ball.setVelocityX(-4);
                    ball.setVelocityX(ball.getVelocityX() * -1);
                }
            }

            if (ball.getBallRect() <= player.getPlayerRect(player.getWidth()) && ball.getBallRect() > player.getPlayerRect(player.getWidth() / 1.25)) {
                ball.setVelocityY(ball.getVelocityY() * -1);
                ball.setVelocityX(6);
                if (ball.getVelocityX() <= 0) {
                    ball.setVelocityX(-6);
                    ball.setVelocityX(ball.getVelocityX() * -1);
                }
            }

        }
    }

    public void checkBlockCollision() {
        for (Block block : blocks) {
            if (ball.getRect().intersects(block.getRect().getBoundsInParent()) && block.getLives() > 0 && block.isAlive()) {
                addToScore();
                if (new Random().nextInt(2) == 1 && !powerUp.isVisible()) {
                    powerUp.setX(block.getX() + block.getWidth() / 2);
                    powerUp.setY(block.getY());
                    powerUp.setVisible(true);
                }
                System.out.println(player.getScore() + ": " + block.getX());
                if (block.getLives() == 3) {
                    block.setLives(2);
                    block.loadSourceImage(BlockState.DAMAGED.getImgSrc());
                    ball.setVelocityY(ball.getVelocityY() * -1);
                    changeBall();
                    return;
                }
                if (block.getLives() == 2) {
                    block.setLives(1);
                    block.loadSourceImage(BlockState.BROKEN.getImgSrc());
                    ball.setVelocityY(ball.getVelocityY() * -1);
                    changeBall();
                    return;
                }
                if (block.getLives() == 1) {
                    block.setLives(0);
                    block.loadSourceImage(BlockState.INVISIBLE.getImgSrc());
                    ball.setVelocityY(ball.getVelocityY() * -1);
                    changeBall();
                    return;
                }
                //ball.setVelocityY(ball.getVelocityY() * -1);
                // FIXME: 04.12.2022 Percentage collision change
                //ball.setVelocityX(-5);
            }
        }

    }

    public void checkBlockState() {
        if (lives <= 0) {
            view.isWin = true;
        }
        for (int i = 0; i < blocks.size(); i++) {
            if (blocks.get(i).getLives() == 0 && blocks.get(i).isAlive()) {
                blocks.get(i).setAlive(false);
                lives--;
            }
        }
    }

    public void checkPowerUpCollision() {
        if (player.getRect().getBoundsInParent().intersects(powerUp.getRect().getBoundsInParent()) && powerUp.isVisible()) {
            switch (Utils.getRandomNumber(2, 0)) { // FIXME: 13.12.2022 Add more power ups and change percentage
                case 0 -> boosts.boost1();
                case 1 -> {
                    System.out.println("nic");
                }
            }
            powerUp.setVisible(false);
        }
    }

    public void gameWin() {
        view.isRunning = false;
        if (player.getScore() > 0) {
            reset = true;
            level++;
        }
        resetValues();
    }

    private void printArray(int iterations) {
        for (int y = 0; y < blocks.get(0).getRows(); y++) {
            for (int x = 0; x < blocks.get(0).getColumns(); x++) {
                blocks.get(iterations).setX((Settings.SCREEN_WIDTH / 4) + (x * blocks.get(iterations).getWidth()));
                blocks.get(iterations).setY(y * blocks.get(iterations).getHeight());
                if (lives >= 6 && level > 1) {
                    blocks.get(iterations).setAlive(Utils.getRandomBoolean());
                }
                if (!blocks.get(iterations).isAlive()) {
                    blocks.get(iterations).loadSourceImage(BlockState.INVISIBLE.getImgSrc());
                    blocks.get(iterations).setLives(0);
                    lives--;
                }
                iterations++;
            }
        }
    }

    private void initArray() {
        for (int i = 0; i < Settings.NUMBER_OF_BLOCKS; i++) {
            blocks.add(new Block(7, 7));
        }
    }

    private void addToScore() {
        player.setScore(player.getScore() + 100);
    }

    private void changeBall() {
        String oldImg = ball.getImgSrc();
        while (ball.getImgSrc().equals(oldImg)) {
            ball.loadSourceImage(ball.getSkin(3));
        }
    }

    public void checkPowerUpPosition() {
        checkPowerUpCollision();
        if (powerUp.isVisible()) {
            powerUp.setY(powerUp.getY() + powerUp.getVelocityY());
        }
        if (powerUp.getY() >= Settings.SCREEN_HEIGHT) {
            powerUp.setVisible(false);
        }
    }

}
