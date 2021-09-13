package scripts.resources;

import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.effect.DropShadow;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Border;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import scripts.resources.items.Empty;
import scripts.resources.items.Gem;
import scripts.resources.items.Item;
import scripts.resources.items.Weapons.CombatKnife;

public class Inventory {
    public static final int DEFAULT_INVENTORY_CAPACITY = 6;
    private int currSlot;
    private final Item[] items;
    private Button[] hotbarButtons;
    //ammo[0] = pistol ammo, ammo[1] = shotgun ammo, ammo[2] = AR ammo
    private int[] ammo = new int[3];
    private final Player player;
    private Pane hotbarPane;

    public Inventory(Player player) {
        this.player = player;
        items = new Item[DEFAULT_INVENTORY_CAPACITY];
        currSlot = 0;

        for (int i = 0; i < items.length; i++) {
            items[i] = new Empty();
        }

        ammo[0] = 15;
        ammo[1] = 5;
        ammo[2] = 10;

        initHotbarPane();
    }

    public int[] getAmmo() {
        return ammo;
    }

    public void setAmmo(int[] ammo) {
        this.ammo = ammo;
    }

    public void setAmmo(int index, int ammo) {
        this.ammo[index] = ammo;
    }

    public boolean giveItem(int slot, Item item) {
        updateHotbarPane();
        if (items[slot] instanceof Empty) {
            items[slot] = item;
            updateHotbarPane();
            player.setImage(player.getFullFilename().replace("CombatKnife", item.getName()));
            return true;
        }
        return false;
    }

    public boolean giveItem(Item item) {
        updateHotbarPane();
        for (int i = 0; i < items.length; i++) {
            if (items[i] instanceof Empty) {
                items[i] = item;
                updateHotbarPane();
                player.setImage(player.getFullFilename().replace("CombatKnife", item.getName()));
                return true;
            }
        }
        return false;
    }

    public void replaceItem(int slot, Item item) {
        if (slot == 0) {
            return;
        }
        String oldItem = items[slot].getName();
        if (oldItem.equals("Empty Slot")) {
            oldItem = "CombatKnife";
        }
        items[slot] = item;
        player.setImage(player.getFullFilename().replace(oldItem, item.getName()));
    }

    public void removeItem(int slot) {
        if (slot == 0) {
            return;
        }
        String oldItem = items[slot].getName();
        items[slot] = new Empty();
        player.setImage(player.getFullFilename().replace(oldItem, "CombatKnife"));
        updateHotbarPane();
    }

    public void removeItem(Item item) {
        if (item instanceof CombatKnife) {
            return;
        }
        for (int i = 0; i < items.length; i++) {
            if (items[i].equals(item)) {
                items[i] = new Empty();
            }
        }
        player.setImage(player.getFullFilename().replace(item.getName(), "CombatKnife"));
        updateHotbarPane();
    }

    public void selectItem(int slot) {
        hotbarButtons[currSlot].setStyle("\t-fx-background-color:#ffffff;\n"
                + "\t-fx-background-radius:16px;"
                + "\t-fx-border-radius:16px;\n"
                + "\t-fx-border-width:4px;"
                + "\t-fx-border-color:#3dcc4b;\n"
                + "\t-fx-display:inline-block;\n"
                + "\t-fx-font-color:#000000;\n"
                + "\t-fx-font-family:Arial;\n"
                + "\t-fx-font-size:17px;\n"
                + "\t-fx-padding:25px 30px;\n");
        int oldSlot = currSlot;
        if (!(items[slot] instanceof Gem)) {
            currSlot = slot;
        }
        hotbarButtons[currSlot].setStyle("\t-fx-background-color:#93fa9d;\n"
                + "\t-fx-background-radius:16px;"
                + "\t-fx-border-radius:16px;\n"
                + "\t-fx-border-width:4px;"
                + "\t-fx-border-color:#3dcc4b;\n"
                + "\t-fx-display:inline-block;\n"
                + "\t-fx-font-color:#000000;\n"
                + "\t-fx-font-family:Arial;\n"
                + "\t-fx-font-size:17px;\n"
                + "\t-fx-padding:25px 30px;\n");
        String newItem = items[currSlot].getName();
        if (newItem.equals("Empty Slot")) {
            newItem = "CombatKnife";
        }
        String oldItem = items[oldSlot].getName();
        if (oldItem.equals("Empty Slot")) {
            oldItem = "CombatKnife";
        }
        player.setImage(player.getFullFilename().replace(oldItem, newItem));
    }

    public void scrollItemRight() {
        currSlot++;
        if (currSlot >= DEFAULT_INVENTORY_CAPACITY) {
            currSlot = 0;
        }
    }

    public void scrollItemLeft() {
        currSlot--;
        if (currSlot < 0) {
            currSlot = DEFAULT_INVENTORY_CAPACITY - 1;
        }
    }

    public Item getSelectedItem() {
        return items[currSlot];
    }

    public int getSelectedSlot() {
        return currSlot;
    }

    public Pane getHotbarPane() {
        return hotbarPane;
    }

    private void initHotbarPane() {
        VBox hotbar = new VBox();
        hotbarButtons = new Button[DEFAULT_INVENTORY_CAPACITY - 1];
        for (int i = 0; i < DEFAULT_INVENTORY_CAPACITY - 1; i++) {

            final int findex = i;
            Button btn = createInventoryButton("");
            btn.setGraphic(new ImageView(items[findex].getImage()));
            btn.setOnAction(e -> selectItem(findex));

            hotbar.getChildren().add(btn);
            hotbarButtons[i] = btn;
        }
        selectItem(0);

        Label label1 = new Label(" Inventory");
        label1.setFont(Font.font("Papyrus", FontWeight.BOLD, 25));
        label1.setTextFill(Color.WHITE);

        VBox vb1 = new VBox(label1, hotbar);
        HBox inventoryHb = new HBox(new Label("\t\t"), vb1);
        inventoryHb.setId("inventory");

        hotbarPane = inventoryHb;
    }

    public void updateHotbarPane() {

        for (int c = 0; c < hotbarButtons.length; c++) {
            hotbarButtons[c].setGraphic((new ImageView(items[c].getImage())));
        }
    }

    private Button createInventoryButton(String text) {
        Button inventoryButton = new Button(text);
        inventoryButton.setId(text);
        inventoryButton.setStyle("\t-fx-background-color:#ffffff;\n"
                + "\t-fx-background-radius:16px;"
                + "\t-fx-border-radius:16px;\n"
                + "\t-fx-border-width:4px;"
                + "\t-fx-border-color:#3dcc4b;\n"
                + "\t-fx-display:inline-block;\n"
                + "\t-fx-font-color:#000000;\n"
                + "\t-fx-font-family:Arial;\n"
                + "\t-fx-font-size:17px;\n"
                + "\t-fx-padding:25px 30px;\n");
        inventoryButton.setBorder(Border.EMPTY);
        inventoryButton.addEventHandler(MouseEvent.MOUSE_ENTERED,
            e -> {
                DropShadow shadow = new DropShadow();
                shadow.setRadius(10);
                shadow.setColor(Color.WHITE);
                inventoryButton.setEffect(shadow);
            });
        inventoryButton.addEventHandler(MouseEvent.MOUSE_EXITED,
            e -> inventoryButton.setEffect(null));

        inventoryButton.setMaxSize(75, 75);
        inventoryButton.setMinSize(75, 75);
        return inventoryButton;
    }

    public Item[] getItems() {
        return items;
    }

    public int getCurrSlot() {
        return currSlot;
    }

    public void addAmmo() {
        ammo[0] += 30;
        ammo[1] += 10;
        ammo[2] += 20;
    }

    public int gemCount() {
        int count = 0;
        for (Item item : items) {
            if (item instanceof Gem) {
                count++;
            }
        }
        return count;
    }
}