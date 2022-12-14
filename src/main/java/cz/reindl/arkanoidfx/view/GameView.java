package cz.reindl.arkanoidfx.view;

import cz.reindl.arkanoidfx.settings.Settings;
import cz.reindl.arkanoidfx.event.EventHandler;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Application;
import javafx.event.ActionEvent;
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
import javafx.scene.paint.Color;
import javafx.scene.robot.Robot;
import javafx.scene.text.*;
import javafx.stage.Screen;
import javafx.stage.Stage;
import javafx.util.Duration;

import java.net.URL;
import java.util.ResourceBundle;

public class GameView extends Application implements Initializable {

    public static Stage stage;
    public Timeline timeline;
    public boolean isRunning, isPaused;
    public boolean isWin;
    public int lastScore = 0;
    private Rectangle2D screenSize;
    private EventHandler handler;
    GraphicsContext gc;
    Canvas canvas;

    //Font font = new Font("verdana", 40);
    Text text = new Text("Score: ");

    public void start(Stage stage) throws Exception {

        //GETTING MONITOR DIMENSIONS
        screenSize = Screen.getPrimary().getBounds();
        Settings.SCREEN_WIDTH = screenSize.getWidth();
        Settings.SCREEN_HEIGHT = screenSize.getHeight();

        handler = new EventHandler(this);

        canvas = new Canvas(Settings.SCREEN_WIDTH, Settings.SCREEN_HEIGHT);
        gc = canvas.getGraphicsContext2D();

        timeline = new Timeline(new KeyFrame(Duration.millis(10), l -> invalidateView(gc)));
        timeline.setCycleCount(Timeline.INDEFINITE);
        timeline.play();

        Scene scene = new Scene(new Pane(canvas));
        scene.setCursor(Cursor.NONE);

        stage.setTitle("ArkanoidFX");
        stage.setFullScreen(true);
        stage.setScene(scene);
        GameView.stage = stage;
        stage.show();

        canvas.setOnMouseMoved(mouseEvent -> {
            if (mouseEvent.getX() <= Settings.SCREEN_WIDTH - handler.player.getWidth()) {
                if (isPaused) {
                    Robot robot = new Robot();
                    robot.mouseMove(handler.player.getX(), Settings.SCREEN_HEIGHT / 2);
                } else {
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

    private void invalidateView(GraphicsContext gc) {
        text.setFont(Font.font("verdana", FontWeight.BOLD, FontPosture.REGULAR, 40));

        gc.setFill(Color.BEIGE);
        gc.fillRect(0, 0, Settings.SCREEN_WIDTH, Settings.SCREEN_HEIGHT);
        gc.setStroke(Color.BLACK);
        gc.setTextAlign(TextAlignment.CENTER);

        //gc.setFill(Color.BLACK);
        //gc.setFont(Font.font(40));

        int iterations = 0;
        /*for (int i = 0; i < Settings.NUMBER_OF_BLOCKS; i++) {
            gc.drawImage(handler.blocks.get(i).getImage(), handler.blocks.get(i).getX() + i * handler.blocks.get(i).getWidth() - 100, handler.blocks.get(i).getHeight());
            if (i * handler.blocks.get(i).getWidth() <= Settings.SCREEN_WIDTH / 2) {
                gc.drawImage(handler.blocks.get(i).getImage(), handler.blocks.get(i).getX() + i * handler.blocks.get(i).getWidth() - 100, handler.blocks.get(i).getHeight() * i);
            } else if (i * handler.blocks.get(i).getWidth() >= Settings.SCREEN_WIDTH / 2) {
                iterations++;
            } else {
                handler.blocks.get(i).setX(50);
                gc.drawImage(handler.blocks.get(i).getImage(), handler.blocks.get(i).getX() + iterations * handler.blocks.get(i).getWidth(), iterations * handler.blocks.get(i).getHeight() + 10 * i);
            }
        }*/

        if (!handler.reset) {
            for (int y = 0; y < handler.blocks.get(0).getRows(); y++) {
                for (int x = 0; x < handler.blocks.get(0).getColumns(); x++) {
                    gc.drawImage(handler.blocks.get(iterations).getImage(), handler.blocks.get(iterations).getX(), handler.blocks.get(iterations).getY());
                    iterations++;
                }
            }
        }

        for (int i = 0; i < handler.balls.size(); i++) {
            gc.drawImage(handler.balls.get(i).getImage(), handler.balls.get(i).getX(), handler.balls.get(i).getY(), handler.ball.getWidth(), handler.ball.getHeight());
        }

        if (isRunning) {
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
        if (handler.player.getLives() >= 0) {
            gc.strokeText(String.valueOf("Lives: " + handler.player.getLives()) + "\n Level: " + handler.level, Settings.SCREEN_WIDTH - 100, 60);
        }
        if (isWin) {
            gc.strokeText("You won \n Score: " + lastScore + "\n Level: " + handler.level, Settings.SCREEN_WIDTH / 2, Settings.SCREEN_HEIGHT / 2);
            handler.gameWin();
        }
        if (handler.powerUp.isVisible()) {
            gc.drawImage(handler.powerUp.getImage(), handler.powerUp.getX(), handler.powerUp.getY(), handler.powerUp.getWidth(), handler.powerUp.getHeight());
        }
        gc.drawImage(handler.player.getImage(), handler.player.getX(), handler.player.getY(), handler.player.getWidth(), handler.player.getHeight());
        //gc.setFont(Font.font(text.getText(), 40));
        gc.strokeText(text.getText() + String.valueOf(handler.player.getScore()), 70, 60);
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
    }

    public static void main(String[] args) {
        launch();
    }

}
