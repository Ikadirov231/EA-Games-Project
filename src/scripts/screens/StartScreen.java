package scripts.screens;

import javafx.geometry.Insets;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Border;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.scene.text.TextAlignment;
import javafx.stage.Stage;
import javafx.scene.effect.DropShadow;

import java.io.FileInputStream;
import java.io.FileNotFoundException;


public class StartScreen extends Screen {

    private Stage primaryStage;

    public StartScreen(Stage primaryStage) {
        this.primaryStage = primaryStage;
    }

    protected Pane getScene() {

        StackPane base = null;

        try {
            base = createPane();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

        return base;
    }

    private StackPane createPane() throws FileNotFoundException {
        StackPane base = new StackPane();

        ImageView bg = getImage();
        base.getChildren().add(bg);

        BorderPane mainPane = new BorderPane();
        mainPane.setPadding(new Insets(50));

        StackPane titlePane = new StackPane();
        Rectangle titleBox2 = new Rectangle();
        titleBox2.setHeight(80);
        titleBox2.setWidth(815);
        titleBox2.setFill(Color.BLACK);
        titleBox2.setArcHeight(50);
        titleBox2.setArcWidth(50);
        titlePane.getChildren().add(titleBox2);
        Rectangle titleBox = new Rectangle();
        titleBox.setHeight(70);
        titleBox.setWidth(800);
        titleBox.setFill(Color.WHITE);
        titleBox.setArcHeight(50);
        titleBox.setArcWidth(50);
        titlePane.getChildren().add(titleBox);

        Text title = new Text("Next-Gen Dungeon Escape 2021");
        title.setTextAlignment(TextAlignment.CENTER);
        title.setFill(Color.ORANGERED);
        title.setFont(new Font("Comic Sans MS", 50));
        titlePane.getChildren().add(title);
        mainPane.setTop(titlePane);

        Button startGameButton = createButton();
        mainPane.setCenter(startGameButton);

        base.getChildren().add(mainPane);
        return base;
    }

    private ImageView getImage() throws FileNotFoundException {
        //String fileName = "castle.jpg";
        String fileName = "dungeonStock.jpg";
        FileInputStream inputStream = new FileInputStream("sprites/" + fileName);
        Image bgImage = new Image(inputStream);
        ImageView bg = new ImageView(bgImage);
        bg.setFitHeight(Screen.DEFAULT_HEIGHT);
        bg.setFitWidth(Screen.DEFAULT_WIDTH);
        return bg;
    }

    private Button createButton() {
        Button startGameButton = new Button("Play!");
        startGameButton.setId("start");
        startGameButton.setStyle(
                "-fx-font: 22 Copperplate_Gothic_Bold; -fx-base:FF0000; -fx-border-color:#000000");
        startGameButton.setBorder(Border.EMPTY);
        startGameButton.addEventHandler(MouseEvent.MOUSE_ENTERED,
            e -> {
                startGameButton.setText("Let's go!");
                DropShadow shadow = new DropShadow();
                shadow.setRadius(10);
                shadow.setColor(Color.WHITE);
                startGameButton.setEffect(shadow);
            });
        startGameButton.addEventHandler(MouseEvent.MOUSE_EXITED,
            e -> {
                startGameButton.setEffect(null);
                startGameButton.setText("Play!");
            });

        startGameButton.addEventHandler(MouseEvent.MOUSE_CLICKED,
            e -> {
                Screen.openConfigScreen(primaryStage);
            });

        return startGameButton;
    }
}