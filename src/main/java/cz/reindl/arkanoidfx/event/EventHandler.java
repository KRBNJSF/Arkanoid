package cz.reindl.arkanoidfx.event;

import cz.reindl.arkanoidfx.entity.*;
import cz.reindl.arkanoidfx.event.boost.Boosts;
import cz.reindl.arkanoidfx.settings.Level;
import cz.reindl.arkanoidfx.settings.Settings;
import cz.reindl.arkanoidfx.sound.Music;
import cz.reindl.arkanoidfx.sound.Sound;
import cz.reindl.arkanoidfx.utils.Interval;
import cz.reindl.arkanoidfx.utils.Utils;
import cz.reindl.arkanoidfx.view.GameView;
import javafx.scene.effect.ColorAdjust;
import javafx.scene.robot.Robot;

import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Random;

public class EventHandler {

    GameView view;
    Boosts boosts = new Boosts(this);
    public Sound sound = new Sound(this);
    public Interval interval;

    public Player player;
    public Ball ball;
    public PowerUp powerUp;
    public ArrayList<Block> blocks;
    public ArrayList<Ball> balls;

    public int blockLives = Settings.NUMBER_OF_BLOCKS;
    public int level = 1;
    public int ballCount = 0;
    public double blockPosX, blockPosY;

    public boolean reset;
    public boolean isStaticLevel;
    public boolean isAi;
    public boolean isGenerated;
    public boolean isSide;
    public boolean isAnim;
    public boolean isOK;
    public boolean isFrozen;

    private int allWidth = 0;
    private int allHeight = 0;

    private double randomX = new Random().nextDouble(7) + 1;

    ColorAdjust colorAdjust;
    DecimalFormat df = new DecimalFormat("#.###");

    public EventHandler(GameView view) {
        this.view = view;
        initGameObjects();
        sound.currentMusic = Music.backgroundTheme2;
        sound.playMusic(sound.currentMusic, true);
    }

    //INITIALIZATION
    private void initGameObjects() {
        colorAdjust = new ColorAdjust();
        colorAdjust.setHue(-.5);
        df.setRoundingMode(RoundingMode.CEILING);

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

        isStaticLevel = false;
        if (isStaticLevel) {
            printStaticLevel(0);
        } else {
            initArray();
            printArray(0);
        }

        blockPosY = blocks.get(Settings.NUMBER_OF_BLOCKS - 1).getY() + blocks.get(Settings.NUMBER_OF_BLOCKS - 1).getHeight();

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
        Settings.DEFAULT_PLAYER_Y = Settings.SCREEN_HEIGHT - player.getWidth() / 2; //player.getY();
    }

    public void moveAIPlayer() {
        if (isAi) {
            if (!isGenerated) {
                randomX = new Random().nextDouble(7) + 1;
                System.out.println("Random AI X: " + df.format(randomX));
                isGenerated = true;
            }
            player.setX(balls.get(0).getX() - (player.getWidth() / randomX));
        }
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
            //if (ballCount >= 0) {
            if (balls.get(i).getX() + balls.get(i).getWidth() >= Settings.SCREEN_WIDTH) {
                balls.get(i).setVelocityX(balls.get(i).getVelocityX() * -1);
                sound.playSoundEffect(Music.wallHit, false);
            }
            if (balls.get(i).getX() <= 0) {   //Settings.BOUND_WIDTH for bg image
                balls.get(i).setVelocityX(balls.get(i).getVelocityX() * -1);
                sound.playSoundEffect(Music.wallHit, false);
            }
            if (balls.get(i).getY() <= 0) {   //Settings.BOUND_WIDTH for bg image
                balls.get(i).setY(10);        //(Settings.BOUND_WIDTH + 5) for bg image
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

                if (ballValue.getVelocityY() > 0) {
                    ballValue.setVelocityY(ballValue.getVelocityY() * -1);
                }

                ballValue.setY(Settings.DEFAULT_BALL_Y);
                isGenerated = false;

                sound.playSoundEffect(Music.platformHit, false);
                isAnim = true;

                if (ballValue.getBallRect() <= player.getPlayerRect(player.getWidth() / 5)) {
                    ballValue.setVelocityX(-6);
                    if (ballValue.getVelocityX() >= 0) {
                        ballValue.setVelocityX(ballValue.getVelocityX() * -1);
                    }
                }

                if (ballValue.getBallRect() <= player.getPlayerRect(player.getWidth() / 2.5) && ballValue.getBallRect() > player.getPlayerRect(player.getWidth() / 5)) {
                    ballValue.setVelocityX(-3);
                    if (ballValue.getVelocityX() >= 0) {
                        ballValue.setVelocityX(ballValue.getVelocityX() * -1);
                    }
                }

                if (ballValue.getBallRect() <= player.getPlayerRect(player.getWidth() / 1.66) && ballValue.getBallRect() > player.getPlayerRect(player.getWidth() / 2.5)) {
                    ballValue.setVelocityX(new Random().nextInt(3) - 1);
                }

                if (ballValue.getBallRect() <= player.getPlayerRect(player.getWidth() / 1.25) && ballValue.getBallRect() > player.getPlayerRect(player.getWidth() / 1.66)) {
                    ballValue.setVelocityX(4);
                    if (ballValue.getVelocityX() <= 0) {
                        ballValue.setVelocityX(-4);
                        ballValue.setVelocityX(ballValue.getVelocityX() * -1);
                    }
                }

                if (ballValue.getBallRect() <= player.getPlayerRect(player.getWidth() + 5) && ballValue.getBallRect() > player.getPlayerRect(player.getWidth() / 1.25)) {
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
                    }

                    //RIGHT SIDE
                    //  if (ballValue.getX() <= block.getX() + block.getWidth() && ballValue.getVelocityX() < 0 && ballValue.getY() + ballValue.getHeight() < (block.getY() + block.getHeight()/* + ballValue.getHeight()*/)) {
                    //     System.out.println("Right Collision - Ball: " + ballValue.getX() + " " + block.getX() + block.getWidth());
                    //    ballValue.setX(block.getX() + block.getWidth() + 1);
                    //  /*if (ballValue.getY() >= block.getHeight() / 2) {
                    //    ballValue.setVelocityY(ballValue.getVelocityY() * -1);
                    // }*/
                    // ballValue.setVelocityX(ballValue.getVelocityX() * -1);
                    // isSide = true;
                    //LEFT SIDE
                    // } else if (ballValue.getX() + ballValue.getWidth() >= block.getX() && ballValue.getX() + ballValue.getWidth() < 2 * block.getX() && ballValue.getVelocityX() > 0 && ballValue.getY() + ballValue.getHeight() <= (block.getY() + block.getHeight()/* + ballValue.getHeight()*/)) {
                    //   System.out.println("Left Collision - Ball: " + ballValue.getX() + ballValue.getWidth() + " Block: " + block.getX());
                    // ballValue.setX(block.getX() - 1);
                    //  /*if (ballValue.getY() >= block.getHeight() / 2 && ballValue.getVelocityY() < 0) {
                    //     ballValue.setVelocityY(ballValue.getVelocityY() * -1);
                    //  } else if (ballValue.getY() >= block.getHeight() / 2 && ballValue.getVelocityY() > 0) {
                    //      ballValue.setVelocityY(ballValue.getVelocityY());
                    //  }*/
                    // ballValue.setVelocityX(ballValue.getVelocityX() * -1);
                    //isSide = true;
                    // }

                    System.out.println("Score: " + player.getScore() + "\nBlock X: " + block.getX() + "\n--------------");
                    if (block.getLives() == 3) {
                        block.setLives(2);
                        block.loadSourceImage(BlockState.DAMAGED.getImgSrc());
                        if (ballValue.getY() <= block.getY() + block.getHeight() && ballValue.getY() > block.getY() + block.getHeight() / 1.1) {
                            ballValue.setVelocityY(ballValue.getVelocityY() * -1);
                        } else {
                            ballValue.setVelocityY(ballValue.getVelocityY() * Utils.getRandomArrayValue());
                            ballValue.setVelocityX(ballValue.getVelocityX() * -1);
                        }
                        sound.playSoundEffect(Music.blockHitHealthy, false);
                        changeBall();
                        return;
                    }
                    if (block.getLives() == 2) {
                        block.setLives(1);
                        block.loadSourceImage(BlockState.BROKEN.getImgSrc());
                        if (ballValue.getY() <= block.getY() + block.getHeight() && ballValue.getY() > block.getY() + block.getHeight() / 1.1) {
                            ballValue.setVelocityY(ballValue.getVelocityY() * -1);
                        } else {
                            ballValue.setVelocityY(ballValue.getVelocityY() * Utils.getRandomArrayValue());
                            ballValue.setVelocityX(ballValue.getVelocityX() * -1);
                        }
                        sound.playSoundEffect(Music.blockHitDamaged, false);
                        changeBall();
                        return;
                    }
                    if (block.getLives() == 1) {
                        block.setLives(0);
                        block.loadSourceImage(BlockState.INVISIBLE.getImgSrc());
                        if (ballValue.getY() <= block.getY() + block.getHeight() && ballValue.getY() > block.getY() + block.getHeight() / 1.1) {
                            ballValue.setVelocityY(ballValue.getVelocityY() * -1);
                        } else {
                            ballValue.setVelocityY(ballValue.getVelocityY() * Utils.getRandomArrayValue());
                            ballValue.setVelocityX(ballValue.getVelocityX() * -1);
                        }
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
        if (blockLives <= 0) {
            view.isWin = true;
            sound.playSoundEffect(Music.gameWinSound, false);
        }
        for (Block block : blocks) {
            if (block.getLives() == 0 && block.isAlive()) {
                block.setAlive(false);
                blockLives--;
            }
        }
    }

    //POWER UP UTILITIES
    private void checkPowerUpCollision() {
        if (player.getRect().getBoundsInParent().intersects(powerUp.getRect().getBoundsInParent()) && powerUp.isVisible()) {
            sound.playSoundEffect(Music.powerUpHit, false);
            switch (Utils.getRandomIntegerNumber(0, 4)) { // FIXME: 13.12.2022 Add more power ups and change percentage
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
                if (blockLives >= 6 && level > 1) {
                    blocks.get(iterations).setAlive(Utils.getRandomBoolean());
                }
                if (!blocks.get(iterations).isAlive()) {
                    blocks.get(iterations).loadSourceImage(BlockState.INVISIBLE.getImgSrc());
                    blocks.get(iterations).setLives(0);
                    blockLives--;
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

    private void printStaticLevel(int iterations) {

        initStaticLevel();

        for (int y = 0; y < Level.LEVEL1.getLevel().length; y++) {
            for (int x = 0; x < Level.LEVEL1.getLevel().length; x++) {
                colorAdjust.setHue(Utils.getRandomDoubleNumber(-1, 1));

                blocks.get(iterations).getImageView().setEffect(colorAdjust);
                blocks.get(iterations).getImageView().setX((Settings.SCREEN_WIDTH / 4) + (x * blocks.get(iterations).getWidth()));
                blocks.get(iterations).getImageView().setY(y * blocks.get(iterations).getHeight());
                blocks.get(iterations).getImageView().setImage(blocks.get(iterations).getImage());

                blocks.get(iterations).setX((Settings.SCREEN_WIDTH / 4) + (x * blocks.get(x).getWidth()));
                blocks.get(iterations).setY(y * blocks.get(y).getHeight());
                iterations++;
            }
        }
        setStaticLevelBlockState();
    }

    private void initStaticLevel() {
        for (int i = 0; i < Settings.NUMBER_OF_BLOCKS; i++) {
            blocks.add(new Block(Level.LEVEL1.getLevel().length, Level.LEVEL1.getLevel().length));
        }
    }

    private void setStaticLevelBlockState() {
        for (int i = 0; i < blocks.get(0).getRows(); i++) {
            for (int j = 0; j < blocks.get(0).getColumns(); j++) {
                if (Level.LEVEL1.getLevel()[i][j] == '_') {
                    blocks.get(i).setAlive(false);
                    blocks.get(i).loadSourceImage(BlockState.INVISIBLE.getImgSrc());
                    blocks.get(i).setLives(0);
                    blockLives--;
                }
            }
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

    private void addToScore() {
        player.setScore(player.getScore() + 100);
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
            if (!isStaticLevel) {
                int ranX = Utils.getRandomIntegerNumber(3, 8);
                int ranY = Utils.getRandomIntegerNumber(3, 8);
                Settings.NUMBER_OF_BLOCKS = ranX * ranY;
                blockLives = Settings.NUMBER_OF_BLOCKS;

                initArray();
                blocks.get(0).setColumns(ranY);
                blocks.get(0).setRows(ranX);

                for (int i = 0; i < Settings.NUMBER_OF_BLOCKS; i++) {
                    blocks.get(i).setLives(Settings.DEFAULT_BLOCK_LIVES);
                    blocks.get(i).setAlive(true);
                    blocks.get(i).loadSourceImage(Settings.DEFAULT_BLOCK_IMG);
                }

                printArray(0);
            } else {
                printStaticLevel(0);
            }

            reset = false;
        }
    }

    private void resetBall() {
        Settings.DEFAULT_BALL_Y = Settings.SCREEN_HEIGHT - (2 * ball.getHeight() + player.getHeight() + 15) - ball.getHeight() / 3;
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
