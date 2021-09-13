package scripts.screens;

import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.scene.Scene;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import javafx.util.Duration;
import scripts.resources.Maze;
import scripts.resources.Player;

public abstract class Screen {

    public static final int DEFAULT_WIDTH = 1400;
    public static final int DEFAULT_HEIGHT = 800;
    public static final int DEFAULT_FRAME_DELAY = 100; // in milliseconds
    private static Stage stage;
    private static Scene scene;
    private static GameScreen gameScreen;
    private static Timeline timeline;

    public static void openStartScreen(Stage primaryStage) {
        stage = primaryStage;
        StartScreen ss = new StartScreen(primaryStage);
        scene = new Scene(ss.getScene(), DEFAULT_WIDTH, DEFAULT_HEIGHT);
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    public static void openConfigScreen(Stage primaryStage) {
        ConfigScreen cs = new ConfigScreen(primaryStage);
        gameScreen = null;
        scene.setRoot(cs.getScene());
    }

    public static void
        openSelectionScreen(Stage primaryStage, Player player, ConfigScreen configScreen) {
        SelectionScreen sels = new SelectionScreen(primaryStage, player, configScreen);
        scene.setRoot(sels.getScene());
    }

    public static void
        openGameScreen(
            Stage primaryStage, Maze maze, SelectionScreen selectionScreen, String type) {
        if (gameScreen != null) {
            scene.setRoot(gameScreen.getScene());
            gameScreen.resetInputs();
            return;
        }
        GameScreen gs = new GameScreen(primaryStage, maze, selectionScreen, type);
        gameScreen = gs;
        scene.setRoot(gs.getScene());

        timeline = new Timeline(new KeyFrame(Duration.millis(10), ev -> {
            gs.update();
        }));
        timeline.setCycleCount(Animation.INDEFINITE);
        timeline.play();

        gameScreen.resetInputs();
    }

    public static void dungeonOneWin(Stage primaryStage, GameScreen gameScreen, Player player) {
        DungeonOneWin temp = new DungeonOneWin(primaryStage, gameScreen, player);
        timeline.stop();
        scene.setRoot(temp.getScene());
    }

    public static void dungeonOneWin(
            Stage primaryStage, GameScreen gameScreen, Player player, int money, int dmg, int x) {
        DungeonOneWin temp = new DungeonOneWin(primaryStage, gameScreen, player, money, dmg, x);
        timeline.stop();
        scene.setRoot(temp.getScene());
    }

    public static void dungeonOneLose(Stage primaryStage, GameScreen gameScreen, Player player) {
        LoseScreen temp = new LoseScreen(primaryStage, gameScreen, player);
        timeline.stop();
        scene.setRoot(temp.getScene());
    }

    public static void dungeonOneLose(
            Stage primaryStage, GameScreen gameScreen, Player player, int money, int dmg, int x) {
        LoseScreen temp = new LoseScreen(primaryStage, gameScreen, player, money, dmg, x);
        timeline.stop();
        scene.setRoot(temp.getScene());
    }

    public static void deleteGameScreen() {
        gameScreen = null;
        timeline.stop();
    }

    public static GameScreen getGameScreen() {
        return gameScreen;
    }

    public static Timeline getTimeline() {
        return timeline;
    }

    protected abstract Pane getScene();

    public static void saveTheChild() {
        gameScreen.updateInvincible();
    }

}
