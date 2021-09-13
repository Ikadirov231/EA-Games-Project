package scripts.screens;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.effect.DropShadow;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Border;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.stage.Stage;
import scripts.resources.Player;
import scripts.resources.Utility;
import scripts.resources.items.Weapons.CombatKnife;
import scripts.resources.items.Weapons.Pistol;
import scripts.resources.items.Weapons.Rifle;
import scripts.resources.items.Weapons.Shotgun;
import scripts.resources.items.Weapons.Weapon;

public class ConfigScreen extends Screen {

    private final Stage primaryStage;
    private Pane mainPane;

    public ConfigScreen(Stage primaryStage) {
        this.primaryStage = primaryStage;
    }

    protected Pane getScene() {
        if (mainPane != null) {
            return mainPane;
        }
        GridPane pane = new GridPane();

        pane.setVgap(20);
        pane.setHgap(20);

        // TextField for entering name
        Label lbl1 = new Label("Name:");
        lbl1.setFont(Font.font("Papyrus", FontWeight.BOLD, 25));
        lbl1.setTextFill(Color.WHITE);
        TextField txf1 = createTextField();
        pane.add(lbl1, 0, 0);
        pane.add(txf1, 1, 0);

        // ComboBox for choosing difficulty
        Label lbl2 = new Label("Difficulty:");
        lbl2.setFont(Font.font("Papyrus", FontWeight.BOLD, 25));
        lbl2.setTextFill(Color.WHITE);
        ObservableList<String> cbx1opt =
                FXCollections.observableArrayList("Easy", "Normal", "Hard");
        ComboBox<String> cbx1 = createComboBox(cbx1opt);
        cbx1.setValue("Normal");
        pane.add(lbl2, 0, 1);
        pane.add(cbx1, 1, 1);

        // ComboBox for choosing weapon
        Label lbl3 = new Label("Weapon:");
        lbl3.setFont(Font.font("Papyrus", FontWeight.BOLD, 25));
        lbl3.setTextFill(Color.WHITE);
        lbl3.setAlignment(Pos.TOP_LEFT);
        ObservableList<String> cbx2opt =
                FXCollections.observableArrayList(
                        Pistol.DEFAULT_NAME,
                        Shotgun.DEFAULT_NAME,
                        Rifle.DEFAULT_NAME);
        ComboBox<String> cbx2 = createComboBox(cbx2opt);
        cbx2.setValue(Pistol.DEFAULT_NAME);
        pane.add(lbl3, 0, 2);
        pane.add(cbx2, 1, 2);


        // Play button
        Button btn1 = createButton("PLAY");
        pane.add(btn1, 1, 4);
        pane.setAlignment(Pos.CENTER);
        btn1.addEventHandler(MouseEvent.MOUSE_CLICKED,
            e -> {

                // Grab UI data
                String name = txf1.getText();

                int difficulty;
                switch (cbx1.getValue()) {
                case "Easy":
                    difficulty = Player.DIFFICULTY_EASY;
                    break;
                case "Normal":
                    difficulty = Player.DIFFICULTY_NORMAL;
                    break;
                case "Hard":
                    difficulty = Player.DIFFICULTY_HARD;
                    break;
                default:
                    difficulty = Player.DIFFICULTY_NORMAL;
                }

                Weapon weapon;
                switch (cbx2.getValue()) {
                case Pistol.DEFAULT_NAME:
                    weapon = new Pistol();
                    break;
                case Shotgun.DEFAULT_NAME:
                    weapon = new Shotgun();
                    break;
                case Rifle.DEFAULT_NAME:
                    weapon = new Rifle();
                    break;
                default:
                    weapon = new Pistol();
                    System.out.println(cbx1.getValue());
                }

                // Check name validity
                if (!Utility.checkTextValidity(name)) {
                    Alert errorAlert = new Alert(AlertType.ERROR);
                    errorAlert.setHeaderText("Invalid Name");
                    errorAlert.setContentText(
                            "Null, empty, or whitespace-only names are not allowed.");
                    errorAlert.showAndWait();
                    return;
                }
                if (name.length() > 12) {
                    name = name.substring(0, 12);
                }

                // If name is valid, launch game
                Player player = new Player(name, difficulty);
                player.getInventory().giveItem(1, weapon);
                player.getInventory().giveItem(0, new CombatKnife());
                Screen.openSelectionScreen(primaryStage, player, this);
            });


        StackPane sp = new StackPane();
        ImageView background = new ImageView(
                new Image(Utility.getSrc("sprites/SelectionBackground.gif")));
        background.setFitWidth(1600);
        background.setFitHeight(800);
        sp.getChildren().add(background);
        sp.getChildren().add(pane);

        mainPane = sp;
        return sp;
    }

    private TextField createTextField() {
        TextField tf = new TextField("Dio Brando");
        tf.setStyle("-fx-padding: 5px;\n"
                + "     -fx-background-radius:16px;"
                + "     -fx-font-size: 16px;\n"
                + "     -fx-border-width: 1px;\n"
                + "     -fx-border-color: #CCCCCC;\n"
                + "     -fx-background-color: #ffffff;\n"
                + "     -fx-color: #000000;\n"
                + "     -fx-border-style: solid;\n"
                + "     -fx-border-radius: 16px;\n");
        return tf;
    }

    private ComboBox<String> createComboBox(ObservableList<String> opts) {
        ComboBox<String> cb = new ComboBox<String>(opts);
        cb.setStyle("-fx-font-size: 16px;\n"
                + "        -fx-display: inline-block;\n"
                + "        -fx-width: 100%;\n"
                + "        -fx-cursor: pointer;\n"
                + "        -fx-padding: 1px 10px;\n"
                + "        -fx-outline: 0;\n"
                + "        -fx-border-radius: 16px;\n"
                + "        -fx-background: #e6e6e6;\n"
                + "        -fx-color: #7b7b7b;");
        return cb;
    }

    private Button createButton(String str) {
        Button startGameButton = new Button(str);
        startGameButton.setId(str);
        startGameButton.setStyle("-fx-box-shadow:inset -3px -2px 7px 8px #93de7c;\n"
                + "\t-fx-background:linear-gradient(to bottom, #9dd9ad 5%, #f1faeb 100%);\n"
                + "\t-fx-background-color:#9dd9ad,"
                + "linear-gradient(to bottom, #82ba91 5%, #c3f2a9 100%);\n"
                + "\t-fx-background-radius:75px;"
                + "\t-fx-font-family:Verdana;\n"
                + "\t-fx-font-size:16px;\n"
                + "\t-fx-padding:10px 30px;\n");
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
































