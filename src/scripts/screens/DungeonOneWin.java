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
import scripts.resources.Player;
import scripts.resources.Utility;

public class DungeonOneWin extends Screen {

    private final Stage primaryStage;
    private final GameScreen gameScreen;
    private final Player player;

    private int money;
    private int damage;
    private int mons;


    public DungeonOneWin(Stage primarystage, GameScreen gameScreen, Player player) {
        this.primaryStage = primarystage;
        this.gameScreen = gameScreen;
        this.player = player;
        damage = 0;
        money = 0;
        mons = 0;
    }

    public DungeonOneWin(
            Stage primarystage, GameScreen gameScreen, Player player,
            int damage, int money, int x) {
        this(primarystage, gameScreen, player);
        this.damage = damage;
        this.money = money;
        mons = x;
    }

    protected Pane getScene() {
        StackPane sp = new StackPane();
        ImageView background = new ImageView(new Image(Utility.getSrc("sprites/winScreen.png")));
        background.setFitWidth(1024);
        background.setFitHeight(800);
        sp.getChildren().add(background);

        Button back = createButton(" Back to Main ");
        Button exit = createExitButton(" Exit Game ");
        VBox vb = new VBox();
        vb.setAlignment(Pos.BOTTOM_CENTER);

        Label stats = new Label("Total Damage Done: " + damage + "\nTotal Gold: " + money
                + "\n" + "Total Monsters Killed: " + mons + "\n");
        stats.setFont(Font.font("Papyrus", FontWeight.BOLD, 18));

        vb.getChildren().add(stats);
        vb.getChildren().add(back);
        vb.getChildren().add(exit);

        back.setOnAction(e -> {
            Screen.deleteGameScreen();
            player.setHealth(100);
            player.setAttackPot(5);
            player.setHealthPot(1);
            player.resetWallet();
            player.resetAmmo();
            player.setWins(1);
            openSelectionScreen(primaryStage, player, null);
        });

        exit.setOnAction(e -> {
            primaryStage.close();
        });
        sp.getChildren().add(vb);

        return sp;
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
                + "\t-fx-font-size:15px;\n"
                + "\t-fx-padding:8px 26px;\n");
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

    private Button createExitButton(String dungeon) {
        Button startGameButton = new Button(dungeon);
        startGameButton.setId(dungeon);
        startGameButton.setStyle("-fx-box-shadow:inset -3px -2px 7px 8px #93de7c;\n"
                + "\t-fx-background:linear-gradient(to bottom, #9dd9ad 5%, #f1faeb 100%);\n"
                + "\t-fx-background-color:#9dd9ad,"
                + "linear-gradient(to bottom, #82ba91 5%, #c3f2a9 100%);\n"
                + "\t-fx-background-radius:75px;"
                + "\t-fx-font-family:Verdana;\n"
                + "\t-fx-font-size:15px;\n"
                + "\t-fx-padding:8px 26px;\n");
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
}
