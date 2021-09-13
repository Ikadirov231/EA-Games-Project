package driver;

import javafx.application.Application;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.stage.Stage;
import javafx.util.Duration;
import scripts.screens.Screen;

import java.io.File;

public class Main extends Application {

    private static MediaPlayer backgroundMusic;
    @Override
    public void start(Stage primaryStage) throws Exception {
        Screen.openStartScreen(primaryStage);
        Media media = new Media(new File("audio/BackgroundMusic.mp3").toURI().toString());
        backgroundMusic = new MediaPlayer(media);
        backgroundMusic.setOnEndOfMedia(new Runnable() {
            public void run() {
                backgroundMusic.seek(Duration.ZERO);
            }
        });
        backgroundMusic.play();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
