package scripts.resources.rooms;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import scripts.resources.GameObject;
import scripts.resources.Player;
import scripts.resources.Point;

import java.util.Optional;

public class Shop extends Room {

    private GameObject healthPack;
    private boolean timer;

    public Shop(Player player, String type, Point position) {
        super(player, type, position);
        initialize();
    }

    public Shop(Point position) {
        super(position);
        initialize();
    }

    protected void initialize() {
        GameObject block1 = new GameObject("sprites/shop.png");
        block1.setPosition(330, 200);
        getObjectPools().getStructurePool().add(block1);

        healthPack = block1;

        timer = true;
    }

    public void render(GraphicsContext context) {
        // TODO Auto-generated method stub
        drawObjects(context);
    }

    protected void updateObjects() {
        Player player = getObjectPools().getPlayer();
        if (player != null && healthPack != null && player.overlaps(healthPack)) {
            if (timer) {
                timer = false;
                Alert buyAlert = new Alert(Alert.AlertType.CONFIRMATION,
                        "This health pack will restore 100 health!\n\n"
                                + "Do you really wish to buy this health pack for 100 gold?",
                        ButtonType.OK,
                        ButtonType.NO);
                buyAlert.setTitle("Confirmation!");
                Optional<ButtonType> out = buyAlert.showAndWait();

                if (out.isPresent() && out.get() == ButtonType.OK) {
                    if (player.getWallet().getCount() >= 100) {
                        player.getWallet().addCount(-100);
                        player.getHealthPot().addCount(1);
                    } else {
                        Alert errorAlert = new Alert(Alert.AlertType.ERROR);
                        errorAlert.setHeaderText("No Money!");
                        errorAlert.setContentText(
                                "You lack the funds to purchase this item!");
                        errorAlert.showAndWait();
                    }
                }
            }
        } else {
            timer = true;
        }
    }

}
