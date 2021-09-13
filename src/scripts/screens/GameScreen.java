package scripts.screens;

import javafx.animation.AnimationTimer;
import javafx.scene.Node;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.effect.DropShadow;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.stage.Stage;
import scripts.resources.*;
import scripts.resources.items.AmmoBox;
import scripts.resources.items.Empty;
import scripts.resources.items.Item;
import scripts.resources.items.Weapons.CombatKnife;
import scripts.resources.items.Weapons.Crosshair;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashSet;

public class GameScreen extends Screen {

    private final Stage primaryStage;
    private final Maze maze;
    private GraphicsContext context;
    private final Player player;
    private final Label health;
    private Label healthPot;
    private Label attackPot;
    private Label money;
    private Label pistolAmmo;
    private Label shotgunAmmo;
    private Label rifleAmmo;
    private ImageView attackPotImg;
    private Canvas minimap;
    private final SelectionScreen selectionScreen;
    private HashSet<String> inputs;
    private HashSet<String> pressedInputs;

    private int totalMoney;
    private int damageDealt;
    private int mons;

    private final Crosshair crosshair;

    private ImageView backgroundContainer;
    private Pane mainPane;
    private final String type;

    private boolean invincible;
    private Calendar endInvincible;
    private Calendar now;

    public GameScreen(Stage primaryStage, Maze maze, SelectionScreen selectionScreen, String type) {
        this.primaryStage = primaryStage;
        this.maze = maze;
        this.player = maze.getPlayer();
        this.type = type.toUpperCase();
        this.health = new Label("  Health:" + this.player.getHealth());
        this.health.setFont(Font.font("Papyrus", FontWeight.BOLD, 25));
        this.health.setTextFill(Color.WHITE);
        maze.drawMinimap();
        this.minimap = maze.getMinimap();
        this.selectionScreen = selectionScreen;
        this.crosshair = maze.getCrosshair();

        totalMoney = player.getWallet().getCount();
        damageDealt = 0;
        mons = 0;

        invincible = true;
        endInvincible = Calendar.getInstance();
        endInvincible.add(Calendar.MILLISECOND, 500);
        now = Calendar.getInstance();
    }

    public void update() {
        maze.getCurrentRoom().update(context);
        maze.getPlayer().getInventory().updateHotbarPane();
        this.health.setText("  Health:" + this.player.getHealth());
        this.money.setText("x " + maze.getPlayer().getWallet().getCount());
        this.healthPot.setText("x " + maze.getPlayer().getHealthPot().getCount());
        this.attackPot.setText("x " + maze.getPlayer().getAttackPot().getCount());
        this.pistolAmmo.setText("x " + maze.getPlayer().getInventory().getAmmo()[0]);
        this.shotgunAmmo.setText("x " + maze.getPlayer().getInventory().getAmmo()[1]);
        this.rifleAmmo.setText("x " + maze.getPlayer().getInventory().getAmmo()[2]);

        minimap = maze.getMinimap();

        maze.updateRoom(context, backgroundContainer);
        if (maze.winner()) {
            Screen.dungeonOneWin(primaryStage, this, player, totalMoney, damageDealt, mons);
        }
        if (maze.loser()) {
            Screen.dungeonOneLose(primaryStage, this, player, totalMoney, damageDealt, mons);
        }

        now = Calendar.getInstance();
        invincible = now.after(endInvincible);
    }

    protected Pane getScene() {
        if (mainPane != null) {
            return mainPane;
        }
        BorderPane bp = new BorderPane();
        bp.setRight(getRightBorder());
        bp.setLeft(getLeftBorder());

        StackPane sp = new StackPane();

        Canvas canvas = new Canvas(1024, 800);
        GraphicsContext context = canvas.getGraphicsContext2D();
        canvas.setId("canvas");

        maze.getCurrentRoom().initialRender(context, sp);

        backgroundContainer = (ImageView) sp.getChildren().get(0);

        player.setImage("sprites/Player/PlayerCombatKnife/PlayerCombatKnifeUp.gif");
        player.render(context);
        crosshair.render(context);
        sp.getChildren().add(canvas);

        bp.setCenter(sp);
        this.context = context;

        inputs = new HashSet<>();
        pressedInputs = new HashSet<>();

        buttonHandler(bp, inputs, pressedInputs);

        AnimationTimer gameloop = gameLoopInit(maze, inputs, pressedInputs);
        gameloop.start();

        StackPane mainStack = new StackPane();
        mainStack.getChildren().add(
                new ImageView(
                        new Image(Utility.getSrc("sprites/" + type + "BorderBackground.jpg"))));
        mainStack.getChildren().add(bp);
        if (type.equals("GRASS")) {
            mainStack.getChildren().add(
                    new ImageView(new Image(Utility.getSrc("sprites/TreeBorder.png"))));
        }
        mainPane = mainStack;
        return mainPane;
    }

    private AnimationTimer gameLoopInit(
            Maze maze, HashSet<String> inputs, HashSet<String> inputs2) {
        return new AnimationTimer() {
            private long x = 0;
            @Override
            public void handle(long l) {
                //PLAYER
                player.setVelocity(0, 0);
                if (primaryStage.isFocused()) {
                    movement(inputs);
                    ArrayList<GameObject> monList =
                            maze.getCurrentRoom().getObjectPools().getMonsterPool();
                    for (int i = 0; i < monList.size(); i++) {
                        GameObject obj = monList.get(i);
                        if (player.overlaps(obj)) {
                            if (obj instanceof AmmoBox) {
                                player.getInventory().addAmmo();
                                monList.remove(i);
                            } else {
                                Monster mon = (Monster) obj;
                                if (l - x > 1000000000 && invincible) {
                                    player.setHealth((int) (player.getHealth() - mon.getAD()));
                                    x = l;
                                }
                                player.displace(mon);
                            }
                        }
                        if (crosshair.overlaps(obj)) {
                            if (obj instanceof Monster) {
                                Monster mon = (Monster) obj;
                                if (inputs2.contains("P") && player.canAttack()) {
                                    if (player.hasAmmo()) {
                                        player.attack(crosshair, context);
                                        if (inputs2.contains("GO")) {
                                            mon.setHP(mon.getHP() - 5 * player.getDamage());
                                            player.getAttackPot().addCount(-1);
                                            attackPotImg.setImage(new Image(
                                                    Utility.getSrc("sprites/Items/mana.png")));
                                            inputs2.remove("GO");
                                            damageDealt += player.getDamage() * 5;
                                        } else {
                                            mon.setHP(mon.getHP() - player.getDamage());
                                            damageDealt += player.getDamage();
                                        }
                                    }
                                }
                                if (mon.getHP() <= 0) {
                                    player.getWallet().addCount(mon.getCoinValue());
                                    totalMoney += mon.getCoinValue();
                                    mons++;
                                    monList.remove(i);
                                    i--;
                                }
                                inputs2.remove("P");
                            }
                        }
                    }
                    if (inputs2.contains("P")) {
                        if (player.hasAmmo() && player.canAttack()) {
                            player.attack(crosshair, context);
                        }
                        inputs2.remove("P");
                    }
                    if (inputs2.contains("O")) {
                        player.useHealthPot();
                        inputs2.remove("P");
                    }
                    if (inputs2.contains("I")) {
                        if (player.getAttackPot().getCount() > 0) {
                            if (inputs2.contains("GO")) {
                                inputs2.remove("GO");
                                attackPotImg.setImage(
                                        new Image(Utility.getSrc("sprites/Items/mana.png")));
                                attackPotImg.setId("ow");
                            } else {
                                inputs2.add("GO");
                                attackPotImg.setImage(
                                        new Image(Utility.getSrc("sprites/Items/manaGlow.jpg")));
                                attackPotImg.setId("pow");
                            }
                        } else {
                            attackPotImg.setImage(
                                    new Image(Utility.getSrc("sprites/Items/mana.png")));
                            attackPotImg.setId("ow");
                        }
                        inputs2.remove("I");
                    }
                    // Item drop/pickup control
                    // N for drop
                    double cw = crosshair.getImage().getWidth();
                    double ch = crosshair.getImage().getHeight();
                    double cx = crosshair.getPosition().getX();
                    double cy = crosshair.getPosition().getY();
                    Item selectedItem = player.getInventory().getSelectedItem();
                    if (inputs2.contains("N")) {
                        if (!(selectedItem instanceof Empty)
                                && !(selectedItem instanceof CombatKnife)) {
                            player.getInventory().removeItem(selectedItem);
                            selectedItem.setPosition(
                                    cx + cw / 2 - selectedItem.getImage().getWidth() / 2,
                                    cy + ch / 2 - selectedItem.getImage().getHeight() / 2);
                            maze.getCurrentRoom()
                                    .getObjectPools().getEntityPool().add(selectedItem);
                        }
                        inputs2.remove("N");
                    // M for pickup
                    } else if (inputs2.contains("M")) {

                        if (selectedItem instanceof Empty) {
                            int slot = player.getInventory().getSelectedSlot();
                            Item pickup = null;
                            int index = 0;

                            for (GameObject obj : maze.getCurrentRoom()
                                    .getObjectPools().getEntityPool()) {

                                if (!(obj instanceof Item)) {
                                    index++;
                                    continue;
                                }
                                double w = obj.getImage().getWidth();
                                double h = obj.getImage().getHeight();
                                double x = obj.getPosition().getX();
                                double y = obj.getPosition().getY();
                                //if (Utility.checkCollisionRect(cx, cy, cw, ch, x, y, w, h)) {
                                if (Utility.checkCollisionRect(crosshair, obj)) {
                                    pickup = (Item) obj;
                                    player.getInventory().giveItem(slot, pickup);
                                    maze.getCurrentRoom()
                                            .getObjectPools().getEntityPool().remove(index);
                                    break;
                                }
                                index++;
                            }
                        }
                        inputs2.remove("M");
                    }
                    player.render(context);
                    crosshair.render(context);
                } else {
                    inputs.clear();
                }
            }
        };
    }

    private void movement(HashSet<String> inputs) {
        int speed = 400;
        if (inputs.contains("SHIFT")) {
            speed *= 2;
        }

        if (inputs.contains("W")) {
            player.getVelocity().add(0, -speed);
            crosshair.setPosition(
                    player.getPosition().getX() + 25, player.getPosition().getY() - 40);
            crosshair.setOrientation("UP");
        }
        if (inputs.contains("A")) {
            player.getVelocity().add(-speed, 0);
            crosshair.setPosition(
                    player.getPosition().getX() - 40, player.getPosition().getY());
            crosshair.setOrientation("LEFT");
        }
        if (inputs.contains("S")) {
            player.getVelocity().add(0, speed);
            crosshair.setPosition(
                    player.getPosition().getX(), player.getPosition().getY() + 70);
            crosshair.setOrientation("DOWN");
        }
        if (inputs.contains("D")) {
            player.getVelocity().add(speed, 0);
            crosshair.setPosition(
                    player.getPosition().getX() + 70, player.getPosition().getY() + 25);
            crosshair.setOrientation("RIGHT");
        }
        player.getVelocity().multiply(1 / 60.0);
        player.getPosition().add(player.getVelocity());
        crosshair.getPosition().add(player.getVelocity());
        if (player.getPosition().getX() < 0) {
            player.setPosition(0, player.getPosition().getY());
        } else if (player.getPosition().getX() > 1024 - player.getImage().getWidth()) {
            player.setPosition(
                    1024 - player.getImage().getWidth(), player.getPosition().getY());
        }
        if (player.getPosition().getY() < 0) {
            player.setPosition(player.getPosition().getX(), 0);
        } else if (player.getPosition().getY() > 800 - player.getImage().getHeight()) {
            player.setPosition(
                    player.getPosition().getX(), 800 - player.getImage().getHeight());
        }
    }

    private void buttonHandler(BorderPane bp, HashSet<String> inputs, HashSet<String> inputs2) {
        bp.setOnKeyPressed(
            (KeyEvent event) -> {
                String keyName = event.getCode().toString();
                if (!inputs.contains(keyName)) {
                    inputs.add(keyName);
                    if (keyName == "A") {
                        player.setImage(player.getFilename() + "Left.gif");
                    } else if (keyName == "D") {
                        player.setImage(player.getFilename() + "Right.gif");
                    } else if (keyName == "S") {
                        player.setImage(player.getFilename() + "Down.gif");
                    } else if (keyName == "W") {
                        player.setImage(player.getFilename() + "Up.gif");
                    }
                }
            }
        );
        bp.setOnKeyReleased(
            (KeyEvent event) -> {
                String keyName = event.getCode().toString();
                inputs.remove(event.getCode().toString());
                inputs2.add(keyName);
            }
        );
    }

    private Node getRightBorder() {
        ImageView gold =
                new ImageView(new Image(Utility.getSrc("sprites/Items/Gold.gif")));
        ImageView pistolAmmoPic =
                new ImageView(new Image(Utility.getSrc("sprites/Items/Weapons/PistolAmmo.png")));
        ImageView shotgunAmmoPic =
                new ImageView(new Image(Utility.getSrc("sprites/Items/Weapons/ShotgunAmmo.png")));
        ImageView rifleAmmoPic =
                new ImageView(new Image(Utility.getSrc("sprites/Items/Weapons/RifleAmmo.png")));
        gold.setFitWidth(40);
        gold.setFitHeight(40);

        pistolAmmo = new Label("x " + maze.getPlayer().getInventory().getAmmo()[0]);
        pistolAmmo.setFont(Font.font("Papyrus", FontWeight.BOLD, 13));
        pistolAmmo.setTextFill(Color.WHITE);

        shotgunAmmo = new Label("x " + maze.getPlayer().getInventory().getAmmo()[1]);
        shotgunAmmo.setFont(Font.font("Papyrus", FontWeight.BOLD, 13));
        shotgunAmmo.setTextFill(Color.WHITE);

        rifleAmmo = new Label("x " + maze.getPlayer().getInventory().getAmmo()[2]);
        rifleAmmo.setFont(Font.font("Papyrus", FontWeight.BOLD, 13));
        rifleAmmo.setTextFill(Color.WHITE);

        HBox ammoHBox = new HBox(
                pistolAmmoPic, pistolAmmo, new Label("  "),
                rifleAmmoPic, rifleAmmo, new Label("  "),
                shotgunAmmoPic, shotgunAmmo);

        Label label3 = new Label("x " + maze.getPlayer().getWallet().getCount());
        label3.setFont(Font.font("Papyrus", FontWeight.BOLD, 25));
        label3.setTextFill(Color.WHITE);

        money = label3;

        Label label4 = health;

        HBox hb = new HBox(label4);
        HBox hb2 = new HBox(gold, label3);
        HBox hb3 = new HBox(minimap);

        ImageView pots1 = new ImageView(new Image(Utility.getSrc("sprites/Items/health.png")));
        ImageView pots2 = new ImageView(new Image(Utility.getSrc("sprites/Items/mana.png")));

        attackPotImg = pots2;
        attackPotImg.setId("ow");

        pots1.setFitWidth(40);
        pots1.setFitHeight(40);

        pots2.setFitWidth(40);
        pots2.setFitHeight(40);

        healthPot = new Label("x " + maze.getPlayer().getHealthPot().getCount());
        attackPot = new Label("x " + maze.getPlayer().getAttackPot().getCount());

        healthPot.setFont(Font.font("Papyrus", FontWeight.BOLD, 25));
        healthPot.setTextFill(Color.WHITE);

        attackPot.setFont(Font.font("Papyrus", FontWeight.BOLD, 25));
        attackPot.setTextFill(Color.WHITE);

        HBox hb4 = new HBox(pots1, healthPot);
        HBox hb5 = new HBox(pots2, attackPot);

        VBox vb3 = new VBox(new Label("\n"), hb3, new Label("\n\n"), hb, ammoHBox, hb2, hb4, hb5);

        return vb3;
    }

    private Node getLeftBorder() {
        Label label = new Label(" Player:\t\t\t\n " + maze.getPlayer().getName());
        label.setFont(Font.font("Papyrus", FontWeight.BOLD, 25));
        label.setTextFill(Color.WHITE);

        Button back = createButton(" Back ");
        back.setOnAction(e -> {
            primaryStage.getScene().setRoot(selectionScreen.getScene());
            resetButtonClicks();
        });
        Button restart = createButton("Restart");
        restart.setOnAction(e -> {
            Screen.deleteGameScreen();
            player.setHealth(100);
            player.setAttackPot(5);
            player.setHealthPot(1);
            player.resetWallet();
            player.resetAmmo();
            openSelectionScreen(primaryStage, player, null);
        });

        HBox hb = new HBox(back, restart);
        VBox vb2 = new VBox(50, label, maze.getPlayer().getInventory().getHotbarPane(), hb);
        vb2.setId("leftUI");

        return vb2;
    }

    public void setHealth(int health) {
        player.setHealth(health);
        this.health.setText("Health:" + this.player.getHealth());
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

    public void resetButtonClicks() {
        this.inputs.clear();
        this.pressedInputs.clear();
    }

    public SelectionScreen getSelectionScreen() {
        return this.selectionScreen;
    }

    public void updateInvincible() {
        endInvincible = Calendar.getInstance();
        endInvincible.add(Calendar.MILLISECOND, 500);
        invincible = false;
    }

    public boolean isInvincible() {
        return invincible;
    }

    public Calendar getEndInvincible() {
        return endInvincible;
    }

    public void resetInputs() {
        inputs.clear();
        player.setVelocity(0, 0);
    }
}