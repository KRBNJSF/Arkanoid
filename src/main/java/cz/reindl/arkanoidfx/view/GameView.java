package cz.reindl.arkanoidfx.view;

import cz.reindl.arkanoidfx.settings.Level;
import cz.reindl.arkanoidfx.settings.Settings;
import cz.reindl.arkanoidfx.event.EventHandler;
import cz.reindl.arkanoidfx.utils.Interval;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Application;
import javafx.fxml.Initializable;
import javafx.geometry.Rectangle2D;
import javafx.scene.Cursor;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseButton;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.robot.Robot;
import javafx.scene.text.*;
import javafx.stage.Screen;
import javafx.stage.Stage;
import javafx.util.Duration;

import java.net.URL;
import java.util.ResourceBundle;

public class GameView extends Application implements Initializable {

    public Timeline timeline;
    public boolean isRunning, isPaused;
    public boolean isWin;
    public int lastScore = 0;
    private Rectangle2D screenSize;
    private EventHandler handler;
    GraphicsContext gc;
    Canvas canvas;
    StackPane root;
    Stage currentStage;
    Scene currentScene;

    Font font = new Font("verdana", 40);
    Text scoreValueText = new Text("Score: ");

    public void start(Stage stage) throws Exception {

        //GETTING MONITOR DIMENSIONS
        screenSize = Screen.getPrimary().getBounds();
        Settings.SCREEN_WIDTH = screenSize.getWidth();
        Settings.SCREEN_HEIGHT = screenSize.getHeight();

        handler = new EventHandler(this);
        handler.interval = new Interval(1000);
        handler.interval.start();

        canvas = new Canvas(Settings.SCREEN_WIDTH, Settings.SCREEN_HEIGHT);
        gc = canvas.getGraphicsContext2D();

        timeline = new Timeline(new KeyFrame(Duration.millis(10), l -> invalidateView(gc)));
        timeline.setCycleCount(Timeline.INDEFINITE);
        timeline.play();

        root = new StackPane(canvas);
        randomBlockColor();

        Scene scene = new Scene(new Pane(canvas)); //When everything is fixed, add root as a parameter
        scene.setCursor(Cursor.NONE);
        currentScene = scene;

        stage.setTitle("ArkanoidFX");
        stage.setFullScreen(true);
        stage.setScene(scene);
        stage.show();
        currentStage = stage;

        canvas.setOnMouseMoved(mouseEvent -> {
            if (mouseEvent.getX() <= Settings.SCREEN_WIDTH - handler.player.getWidth()) {
                if (isPaused) {
                    Robot robot = new Robot();
                    robot.mouseMove(handler.player.getX(), Settings.SCREEN_HEIGHT / 2);
                } else if (!handler.isAi) {
                    handler.player.setX(mouseEvent.getX());
                    if (!isRunning) {
                        handler.ball.setX(handler.player.getX() + handler.player.getWidth() / 2 - handler.ball.getWidth() / 2);
                    }
                }
            }
        });
        canvas.setOnMouseClicked(mouseEvent -> {
            if (mouseEvent.getButton() == MouseButton.PRIMARY) {
                isWin = false;
                isRunning = true;
                isPaused = false;
                lastScore = 0;
            }
        });
        canvas.requestFocus();
        canvas.setOnKeyPressed(event -> handler.player.keyPressed(event));
        canvas.setOnKeyReleased(event -> handler.player.keyReleased(event));

        scene.addEventHandler(KeyEvent.KEY_PRESSED, keyEvent -> {
            if (keyEvent.getCode() == KeyCode.ESCAPE) {
                stage.close();
                isRunning = false;
                System.exit(0);
            }
            if (keyEvent.getCode() == KeyCode.R) {
                isRunning = false;
                handler.reset = true;
                handler.resetValues();
            }
            if (keyEvent.getCode() == KeyCode.O) {
                handler.player.setScore(handler.player.getScore() + 100);
                isWin = true;
            }
            if (keyEvent.getCode() == KeyCode.P) {
                isRunning = false;
                isPaused = true;
            }
            if (keyEvent.getCode() == KeyCode.ENTER) {
                if (handler.sound.isPlayable) {
                    handler.sound.pauseMusic(handler.sound.currentMusic);
                    handler.sound.isPlayable = false;
                } else {
                    handler.sound.playSound(handler.sound.currentMusic);
                    handler.sound.isPlayable = true;
                }
            }
            if (keyEvent.getCode() == KeyCode.M) {
                handler.sound.isPlayable = !handler.sound.isPlayable;
            }
            if (keyEvent.getCode() == KeyCode.U) {
                handler.isAi = !handler.isAi;
            }

            //TEST
            if (keyEvent.getCode() == KeyCode.SPACE) {
                isWin = false;
                isRunning = true;
                isPaused = false;
                lastScore = 0;
            }
            if (keyEvent.getCode() == KeyCode.A && handler.player.getX() > handler.player.getWidth() / 2) {
                handler.player.setX(handler.player.getX() - 50);
            }
            if (keyEvent.getCode() == KeyCode.D && handler.player.getX() + handler.player.getWidth() < Settings.SCREEN_WIDTH) {
                handler.player.setX(handler.player.getX() + 50);
            }
            if (!isRunning && !isPaused) {
                handler.ball.setX(handler.player.getX() + handler.player.getWidth() / 2 - handler.ball.getWidth() / 2);
            }
        });

    }

    private void randomBlockColor() {
        // FIXME: 16.12.2022 Block color change
        int index = 0;
        while (index < Settings.NUMBER_OF_BLOCKS) {
            root.getChildren().addAll(handler.blocks.get(index).getImageView());
            index++;
        }
    }

    private void invalidateView(GraphicsContext gc) {
        textInitialization();

        //RENDERING
        backgroundRender();
        randomLevelRender(0);
        staticLevelRender(0);
        ballRender();
        playerRender();
        powerUpRender();

        //ANIMATIONS
        if (isRunning) {
            handler.moveAIPlayer();
            handler.moveBall();
            handler.checkBallCollision();
            handler.checkBlockState();
            handler.checkBlockCollision();
            handler.checkPowerUpPosition();
        } else if (!isWin && !isPaused) {
            gc.strokeText("Click to Start", Settings.SCREEN_WIDTH / 2, Settings.SCREEN_HEIGHT / 2);
        } else if (!isWin) {
            gc.strokeText("PAUSED", Settings.SCREEN_WIDTH / 2, Settings.SCREEN_HEIGHT / 2);
        }

        scoreText();
        winText();
        checkEvent();

    }

    //BACKGROUND
    private void backgroundRender() {
        // FIXME: 16.12.2022 gc.drawImage(backgroundImg, 0, 0);
        gc.setFill(Color.BEIGE);
        gc.fillRect(0, 0, Settings.SCREEN_WIDTH, Settings.SCREEN_HEIGHT);
    }

    //OBJECTS
    private void staticLevelRender(int iterations) {
        if (handler.isStaticLevel) {
            for (int y = 0; y < Level.LEVEL1.getLevel().length; y++) {
                for (int x = 0; x < Level.LEVEL1.getLevel().length; x++) {
                    System.out.println(Level.LEVEL1.getLevel().length);
                    System.out.println(Level.LEVEL1.getLevel()[y][x]);
                    gc.drawImage(handler.blocks.get(iterations).getImage(), handler.blocks.get(iterations).getX(), handler.blocks.get(iterations).getY());
                    iterations++;
                }
            }
        }
    }

    private void randomLevelRender(int iterations) {
        if (!handler.reset && !handler.isStaticLevel) {
            for (int y = 0; y < handler.blocks.get(0).getRows(); y++) {
                for (int x = 0; x < handler.blocks.get(0).getColumns(); x++) {
                    gc.drawImage(handler.blocks.get(iterations).getImage(), handler.blocks.get(iterations).getX(), handler.blocks.get(iterations).getY());
                    iterations++;
                }
            }
        }
    }


    private void playerRender() {
        gc.drawImage(handler.player.getImage(), handler.player.getX(), handler.player.getY(), handler.player.getWidth(), handler.player.getHeight());
    }

    private void ballRender() {
        for (int i = 0; i < handler.balls.size(); i++) {
            gc.drawImage(handler.balls.get(i).getImage(), handler.balls.get(i).getX(), handler.balls.get(i).getY(), handler.ball.getWidth(), handler.ball.getHeight());
        }
    }

    private void powerUpRender() {
        if (handler.powerUp.isVisible()) {
            gc.drawImage(handler.powerUp.getImage(), handler.powerUp.getX(), handler.powerUp.getY(), handler.powerUp.getWidth(), handler.powerUp.getHeight());
        }
    }

    //TEXT
    private void textInitialization() {
        scoreValueText.setFont(Font.font("verdana", FontWeight.BOLD, FontPosture.REGULAR, 40));
        gc.setStroke(Color.BLACK);
        gc.setTextAlign(TextAlignment.CENTER);
    }

    private void winText() {
        if (isWin) {
            gc.strokeText("You won \n Score: " + lastScore + "\n Level: " + handler.level, Settings.SCREEN_WIDTH / 2, Settings.SCREEN_HEIGHT / 2);
            handler.gameWin();
        }
    }

    private void scoreText() {
        if (handler.player.getLives() >= 0) {
            gc.strokeText(String.valueOf("Lives: " + handler.player.getLives()) + "\n Level: " + handler.level, Settings.SCREEN_WIDTH - 100, 60);
        }
        gc.strokeText(scoreValueText.getText() + String.valueOf(handler.player.getScore()), 70, 60);
    }

    //OTHERS
    private void checkEvent() {
        boolean isFreeze = false;
        if (isFreeze) {
            sideCollisionFreeze();
        } else {
            platformAnim();
        }
    }

    private void platformAnim() {
        handler.interval.setInterval(35);
        if (handler.interval.isReady() && handler.isAnim) {
            handler.player.setY(handler.player.getY() + 5);
            //handler.interval.done();
            handler.isAnim = false;
            handler.isOK = true;
            handler.interval.setCanDo(false);
        }

        if (handler.interval.isCanDo() && handler.isOK) {
            handler.player.setY(handler.player.getY() - 5);
            handler.isOK = false;
        }
    }

    private void sideCollisionFreeze() {
        handler.interval.setInterval(1000);
        if (handler.interval.isReady() && handler.isSide) {
            isPaused = true;
            isRunning = false;
            //handler.interval.done();
            handler.isSide = false;
            handler.isFrozen = true;
            handler.interval.setCanDo(false);
        }

        if (handler.interval.isCanDo() && handler.isFrozen) {
            isPaused = false;
            isRunning = true;
            handler.isFrozen = false;
        }

        //isRunning = !isPaused;

    }


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
    }

    public static void main(String[] args) {
        launch();
    }

}
