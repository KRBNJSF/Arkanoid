package cz.reindl.arkanoidfx.event;

import cz.reindl.arkanoidfx.entity.*;
import cz.reindl.arkanoidfx.event.boost.Boosts;
import cz.reindl.arkanoidfx.settings.Settings;
import cz.reindl.arkanoidfx.sound.Music;
import cz.reindl.arkanoidfx.sound.Sound;
import cz.reindl.arkanoidfx.utils.Utils;
import cz.reindl.arkanoidfx.view.GameView;
import javafx.scene.effect.ColorAdjust;
import javafx.scene.robot.Robot;

import java.util.ArrayList;
import java.util.Random;

public class EventHandler {

    GameView view;
    Boosts boosts = new Boosts(this);
    public Sound sound = new Sound(this);

    public Player player;
    public Ball ball;
    public PowerUp powerUp;
    public ArrayList<Block> blocks;
    public ArrayList<Ball> balls;

    public boolean reset;
    public int lives = Settings.NUMBER_OF_BLOCKS;
    public double blockPosX, blockPosY;
    public int level = 1;
    public int ballCount = 0;
    public int allWidth = 0;
    public int allHeight = 0;

    ColorAdjust colorAdjust;

    public EventHandler(GameView view) {
        this.view = view;
        initGameObjects();
        sound.currentMusic = Music.backgroundTheme;
        sound.playMusic(sound.currentMusic, true);
    }

    //INITIALIZATION
    private void initGameObjects() {
        colorAdjust = new ColorAdjust();
        colorAdjust.setHue(100);

        player = new Player(Settings.DEFAULT_SCORE, Settings.DEFAULT_PLAYER_LIVES, 5);
        player.setX(Settings.SCREEN_WIDTH / 2 - player.getWidth() / 2);
        player.setY(Settings.SCREEN_HEIGHT - player.getWidth() / 2);

        ball = new Ball(0, -7);
        ball.setX(player.getX() + player.getWidth() / 2 - ball.getWidth() / 2);
        ball.setY(Settings.SCREEN_HEIGHT - (2 * ball.getHeight() + player.getHeight() + 15) - ball.getHeight() / 3);

        balls = new ArrayList<Ball>();

        balls.add(ball);

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

        //DEFAULT SETTINGS
        Settings.DEFAULT_BALL_X = player.getX() + player.getWidth() / 2 - ball.getWidth() / 2; //ball.getX();
        Settings.DEFAULT_BALL_Y = Settings.SCREEN_HEIGHT - (2 * ball.getHeight() + player.getHeight() + 15) - ball.getHeight() / 3; //ball.getY();
        Settings.DEFAULT_PLAYER_X = Settings.SCREEN_WIDTH / 2 - player.getWidth() / 2; //player.getX();
    }

    //BALL MOVEMENT + BALL UTILITIES
    public void moveBall() {
        for (int i = 0; i < balls.size(); i++) {
            if (balls.get(i).getY() >= Settings.SCREEN_HEIGHT && player.getLives() <= 0) {
                sound.playSoundEffect(Music.gameLoseSound, false);
                reset = true;
                level = 1;
                resetValues();
            } else if (balls.get(i).getY() >= Settings.SCREEN_HEIGHT && player.getLives() > 0) {
                balls.remove(balls.get(i));
                ballCount--;
                if (ballCount < 0) {
                    player.setLives(player.getLives() - 1);
                    sound.playSoundEffect(Music.lifeLoseSound, false);
                    resetBall();
                }
                return;
            }
            if (balls.get(i).getX() + balls.get(i).getWidth() >= Settings.SCREEN_WIDTH) {
                balls.get(i).setVelocityX(balls.get(i).getVelocityX() * -1);
                sound.playSoundEffect(Music.wallHit, false);
            }
            if (balls.get(i).getX() <= 0) {
                balls.get(i).setVelocityX(balls.get(i).getVelocityX() * -1);
                sound.playSoundEffect(Music.wallHit, false);
            }
            if (balls.get(i).getY() <= 0) {
                balls.get(i).setY(10);
                balls.get(i).setVelocityY(balls.get(i).getVelocityY() * -1);
                sound.playSoundEffect(Music.wallHit, false);
            } else {
                balls.get(i).setVelocityX(balls.get(i).getVelocityX());
            }
            balls.get(i).setX(balls.get(i).getX() + balls.get(i).getVelocityX());
            balls.get(i).setY(balls.get(i).getY() + balls.get(i).getVelocityY());
        }
    }

    public void checkBallCollision() {
        for (Ball ballValue : balls) {
            if (ballValue.getRect().intersects(player.getRect().getBoundsInParent())) {

                sound.playSoundEffect(Music.platformHit, false);

                if (ballValue.getBallRect() <= player.getPlayerRect(player.getWidth() / 5)) {
                    ballValue.setVelocityY(ballValue.getVelocityY() * -1);
                    ballValue.setVelocityX(-6);
                    if (ballValue.getVelocityX() >= 0) {
                        ballValue.setVelocityX(ballValue.getVelocityX() * -1);
                    }
                }

                if (ballValue.getBallRect() <= player.getPlayerRect(player.getWidth() / 2.5) && ballValue.getBallRect() > player.getPlayerRect(player.getWidth() / 5)) {
                    ballValue.setVelocityY(ballValue.getVelocityY() * -1);
                    ballValue.setVelocityX(-3);
                    if (ballValue.getVelocityX() >= 0) {
                        ballValue.setVelocityX(ballValue.getVelocityX() * -1);
                    }
                }

                if (ballValue.getBallRect() <= player.getPlayerRect(player.getWidth() / 1.66) && ballValue.getBallRect() > player.getPlayerRect(player.getWidth() / 2.5)) {
                    ballValue.setVelocityY(ball.getVelocityY() * -1);
                    ballValue.setVelocityX(Utils.getRandomNumber(3, -1));
                }

                if (ballValue.getBallRect() <= player.getPlayerRect(player.getWidth() / 1.25) && ballValue.getBallRect() > player.getPlayerRect(player.getWidth() / 1.66)) {
                    ballValue.setVelocityY(ball.getVelocityY() * -1);
                    ballValue.setVelocityX(4);
                    if (ballValue.getVelocityX() <= 0) {
                        ballValue.setVelocityX(-4);
                        ballValue.setVelocityX(ballValue.getVelocityX() * -1);
                    }
                }

                if (ballValue.getBallRect() <= player.getPlayerRect(player.getWidth() + 5) && ballValue.getBallRect() > player.getPlayerRect(player.getWidth() / 1.25)) {
                    ballValue.setVelocityY(ballValue.getVelocityY() * -1);
                    ballValue.setVelocityX(6);
                    if (ballValue.getVelocityX() <= 0) {
                        ballValue.setVelocityX(-6);
                        ballValue.setVelocityX(ballValue.getVelocityX() * -1);
                    }
                }

            }
        }
    }

    private void changeBall() {
        for (int i = 0; i < balls.size(); i++) {
            String oldImg = balls.get(i).getImgSrc();
            while (balls.get(i).getImgSrc().equals(oldImg)) {
                balls.get(i).loadSourceImage(balls.get(i).getSkin(3));
            }
        }
    }

    //BLOCK UTILITIES
    public void checkBlockCollision() {
        for (Block block : blocks) {
            for (Ball ballValue : balls) {
                if (ballValue.getRect().intersects(block.getRect().getBoundsInParent()) && block.getLives() > 0 && block.isAlive() && block.getX() != 0) {
                    addToScore();
                    if (new Random().nextInt(2) == 1 && !powerUp.isVisible()) {
                        powerUp.setX(block.getX() + block.getWidth() / 2);
                        powerUp.setY(block.getY());
                        powerUp.setVisible(true);
                        blockPosX = block.getX() + block.getWidth() / 2;
                        blockPosY = block.getY() + block.getHeight();
                    }
                    System.out.println("Score: " + player.getScore() + "\nBlock X: " + block.getX() + "\n--------------");
                    if (block.getLives() == 3) {
                        block.setLives(2);
                        block.loadSourceImage(BlockState.DAMAGED.getImgSrc());
                        ballValue.setVelocityY(ballValue.getVelocityY() * -1);
                        sound.playSoundEffect(Music.blockHitHealthy, false);
                        changeBall();
                        return;
                    }
                    if (block.getLives() == 2) {
                        block.setLives(1);
                        block.loadSourceImage(BlockState.BROKEN.getImgSrc());
                        ballValue.setVelocityY(ballValue.getVelocityY() * -1);
                        sound.playSoundEffect(Music.blockHitDamaged, false);
                        changeBall();
                        return;
                    }
                    if (block.getLives() == 1) {
                        block.setLives(0);
                        block.loadSourceImage(BlockState.INVISIBLE.getImgSrc());
                        ballValue.setVelocityY(ballValue.getVelocityY() * -1);
                        sound.playSoundEffect(Music.blockHitBroken, false);
                        changeBall();
                        return;
                    }
                    // FIXME: 04.12.2022 Percentage collision change
                }
            }
        }

    }

    public void checkBlockState() {
        if (lives <= 0) {
            view.isWin = true;
            sound.playSoundEffect(Music.gameWinSound, false);
        }
        for (Block block : blocks) {
            if (block.getLives() == 0 && block.isAlive()) {
                block.setAlive(false);
                lives--;
            }
        }
    }

    //POWER UP UTILITIES
    private void checkPowerUpCollision() {
        if (player.getRect().getBoundsInParent().intersects(powerUp.getRect().getBoundsInParent()) && powerUp.isVisible()) {
            sound.playSoundEffect(Music.powerUpHit, false);
            switch (Utils.getRandomNumber(4, 0)) { // FIXME: 13.12.2022 Add more power ups and change percentage
                case 0 -> boosts.boost1();
                case 1 -> boosts.boost2(blockPosX, blockPosY);
                default -> {
                }
            }
            powerUp.setVisible(false);
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

    //OTHERS
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

    public void gameWin() {
        view.isRunning = false;
        if (player.getScore() > 0) {
            reset = true;
            level++;
        }
        resetValues();
    }

    //RESTARTS
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
            int ranX = Utils.getRandomNumber(5, 3);
            int ranY = Utils.getRandomNumber(5, 3);
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
        ballCount = 0;
        balls.clear();
        ball = new Ball(0, -7);
        ball.setVelocityX(Settings.DEFAULT_BALL_VELOCITY_X);
        ball.setVelocityY(Settings.DEFAULT_BALL_VELOCITY_Y);
        ball.setX(Settings.DEFAULT_BALL_X);
        ball.setY(Settings.DEFAULT_BALL_Y);
        balls.add(ball);

        powerUp.setVisible(false);

        Robot robot = new Robot();
        robot.mouseMove(Settings.SCREEN_WIDTH / 2 - 50, Settings.SCREEN_HEIGHT / 2);
    }

}
