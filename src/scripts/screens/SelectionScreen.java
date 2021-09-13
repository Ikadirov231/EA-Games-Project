package scripts.screens;

import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.effect.DropShadow;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.stage.Stage;
import scripts.resources.Maze;
import scripts.resources.Player;
import scripts.resources.Point;

import java.io.FileInputStream;
import java.io.FileNotFoundException;

public class SelectionScreen extends Screen {
    private final Stage primaryStage;
    private final Player player;
    private ConfigScreen configScreen;
    private GameScreen gameScreen;
    private Pane mainPane;
    private Maze dungeon1maze;
    private Maze dungeon2maze;
    private Maze dungeon3maze;

    private final boolean dungeon2Unlocked = false;
    private final boolean dungeon3Unlocked = false;

    public SelectionScreen(Stage primaryStage, Player player, ConfigScreen configScreen) {
        this.primaryStage = primaryStage;
        this.player = player;
        this.configScreen = configScreen;
    }

    protected Pane getScene() {
        if (mainPane != null) {
            return mainPane;
        }
        StackPane base = null;

        try {
            base = createPane();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

        mainPane = base;
        return base;
    }

    private StackPane createPane() throws FileNotFoundException {
        StackPane base = new StackPane();

        FileInputStream inputStream = new FileInputStream("sprites/SelectionBackground.gif");
        ImageView background = new ImageView(new Image(inputStream));
        background.setFitWidth(1600);
        background.setFitHeight(800);
        base.getChildren().add(background);

        Label label = new Label("Dungeon Hero");
        label.setFont(Font.font("Papyrus", FontWeight.BOLD, 100));
        label.setTextFill(Color.WHITE);
        label.setAlignment(Pos.CENTER);


        Button dungeon1 = createButton("Dungeon 1");
        Button dungeon2 = createButton("Dungeon 2");
        Button dungeon3 = createButton("Dungeon 3");

        dungeon1.addEventHandler(MouseEvent.MOUSE_CLICKED,
            e -> {
                if (dungeon1maze != null) {
                    Screen.openGameScreen(primaryStage, dungeon1maze, this, "Grass");
                } else {
                    Maze maze = new Maze(player, "Grass", new Point(0, 0));
                    dungeon1maze = maze;
                    Screen.openGameScreen(primaryStage, maze, this, "Grass");
                }
            });

        dungeon2.addEventHandler(MouseEvent.MOUSE_CLICKED,
            e -> {
                System.out.println(player.getWins());
                if (player.getWins() >= 1) {
                    if (dungeon2maze != null) {
                        Screen.openGameScreen(primaryStage, dungeon2maze, this, "Ice");
                    } else {
                        Maze maze = new Maze(player, "Ice", new Point(0, 0));
                        dungeon2maze = maze;
                        Screen.openGameScreen(primaryStage, maze, this, "Ice");
                    }
                }
            });

        Button settings = createButton("Settings");
        Button credits = createButton("Credits");
        Button back = createButton(" Back ");
        back.addEventHandler(MouseEvent.MOUSE_CLICKED,
            e -> {
                if (configScreen != null) {
                    primaryStage.getScene().setRoot(configScreen.getScene());
                } else {
                    configScreen = new ConfigScreen(primaryStage);
                    primaryStage.getScene().setRoot(configScreen.getScene());
                }
            });

        HBox hbox = new HBox(20, dungeon1, dungeon2, dungeon3);
        HBox hbox2 = new HBox(20, settings, credits, back);
        hbox.setAlignment(Pos.CENTER);
        hbox2.setAlignment(Pos.CENTER);

        VBox vbox = new VBox(20, hbox, hbox2);
        VBox vbox2 = new VBox(75, label, vbox);
        vbox2.setAlignment(Pos.TOP_CENTER);

        base.getChildren().add(vbox2);
        return base;
    }

    private Button createButton(String dungeon) {
        Button startGameButton = new Button(dungeon);
        startGameButton.setId(dungeon);
        startGameButton.setStyle("-fx-box-shadow:inset -3px -2px 7px 8px #93de7c;\n"
                + "\t-fx-background:linear-gradient(to bottom, #9dd9ad 5%, #f1faeb 100%);\n"
                + "\t-fx-background-color:#9dd9ad,"
                + "linear-gradient(to bottom, #82ba91 5%, #c3f2a9 100%);\n"
                + "\t-fx-background-radius:75px;"
                + "\t-fx-font-family:Verdana;\n"
                + "\t-fx-font-size:22px;\n"
                + "\t-fx-padding:16px 46px;\n");
        startGameButton.setBorder(Border.EMPTY);
        startGameButton.addEventHandler(MouseEvent.MOUSE_ENTERED,
            e -> {
                DropShadow shadow = new DropShadow();
                shadow.setRadius(10);
                shadow.setColor(Color.WHITE);
                startGameButton.setEffect(shadow);
            });
        startGameButton.addEventHandler(MouseEvent.MOUSE_EXITED,
            e -> startGameButton.setEffect(null));

        return startGameButton;
    }

    public void setDungeon1maze(Maze dungeon1maze) {
        this.dungeon1maze = dungeon1maze;
    }
}