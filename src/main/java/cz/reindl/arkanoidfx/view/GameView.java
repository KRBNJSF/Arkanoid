package cz.reindl.arkanoidfx.view;

import cz.reindl.arkanoidfx.entity.Block;
import cz.reindl.arkanoidfx.settings.Settings;
import cz.reindl.arkanoidfx.event.EventHandler;
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
import javafx.scene.paint.Color;
import javafx.scene.robot.Robot;
import javafx.scene.text.Font;
import javafx.scene.text.FontPosture;
import javafx.scene.text.Text;
import javafx.scene.text.TextAlignment;
import javafx.stage.Screen;
import javafx.stage.Stage;
import javafx.util.Duration;

import java.net.URL;
import java.util.ResourceBundle;

public class GameView extends Application implements Initializable {

    public static Stage stage;
    private Rectangle2D screenSize;
    private EventHandler handler;
    public boolean isRunning;
    public boolean isWin;
    GraphicsContext gc;
    Canvas canvas;

    Font font = new Font("Verdana", 40);
    Text text = new Text(20, 20, "Score: ");

    public void start(Stage stage) throws Exception {
        //GETTING MONITOR DIMENSIONS
        screenSize = Screen.getPrimary().getBounds();
        Settings.SCREEN_WIDTH = screenSize.getWidth();
        Settings.SCREEN_HEIGHT = screenSize.getHeight();

        canvas = new Canvas(Settings.SCREEN_WIDTH, Settings.SCREEN_HEIGHT);
        gc = canvas.getGraphicsContext2D();
        handler = new EventHandler(this);

        stage.setTitle("ArkanoidFX");
        stage.setFullScreen(true);

        Timeline timeline = new Timeline(new KeyFrame(Duration.millis(10), l -> invalidateView(gc)));
        timeline.setCycleCount(Timeline.INDEFINITE);

        canvas.setOnMouseMoved(l -> {
            if (l.getX() <= Settings.SCREEN_WIDTH - handler.player.getWidth()) {
                /* Robot robot = new Robot();
                robot.mouseMove(Settings.SCREEN_WIDTH / 2 - 50, Settings.SCREEN_HEIGHT / 2); */
                handler.player.setX(l.getX());
                if (!isRunning) {
                    handler.ball.setX(handler.player.getX() + handler.player.getWidth() / 2 - handler.ball.getWidth() / 2);
                }
            }
        });
        canvas.setOnMouseClicked(l -> {
            if (l.getButton() == MouseButton.PRIMARY) {
                isWin = false;
                isRunning = true;
            }
        });
        canvas.setOnKeyPressed(event -> handler.player.keyPressed(event));
        canvas.setOnKeyReleased(event -> handler.player.keyReleased(event));
        canvas.requestFocus();

        Scene scene = new Scene(new Pane(canvas));
        scene.setCursor(Cursor.NONE);
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
        });

        stage.setScene(scene);
        GameView.stage = stage;
        stage.show();

        timeline.play();
    }

    private void invalidateView(GraphicsContext gc) {
        text.setFont(Font.font("Verdana", FontPosture.ITALIC, 80));

        gc.setFill(Color.BEIGE);
        gc.fillRect(0, 0, Settings.SCREEN_WIDTH, Settings.SCREEN_HEIGHT);

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

        for (int y = 0; y < 6; y++) {
            for (int x = 0; x < 6; x++) {
                gc.drawImage(handler.blocks.get(iterations).getImage(), handler.blocks.get(iterations).getX(), handler.blocks.get(iterations).getY());
                iterations++;
            }
        }

        gc.drawImage(handler.ball.getImage(), handler.ball.getX(), handler.ball.getY(), handler.ball.getWidth(), handler.ball.getHeight());

        if (isRunning) {
            handler.moveBall();
            handler.checkCollision();
            handler.checkBlockState();
            handler.checkBlockCollision();
        } else if (!isWin) {
            gc.setStroke(Color.BLACK);
            gc.setTextAlign(TextAlignment.CENTER);
            gc.strokeText("Click to Start", Settings.SCREEN_WIDTH / 2, Settings.SCREEN_HEIGHT / 2);
        }
        if (isWin) {
            gc.setStroke(Color.BLACK);
            gc.setTextAlign(TextAlignment.CENTER);
            gc.strokeText("You won", Settings.SCREEN_WIDTH / 2, Settings.SCREEN_HEIGHT / 2);
            handler.gameWin();
        }
        gc.drawImage(handler.player.getImage(), handler.player.getX(), handler.player.getY(), handler.player.getWidth(), handler.player.getHeight());
        gc.setStroke(Color.BLACK);
        gc.setTextAlign(TextAlignment.CENTER);
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
