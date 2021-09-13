package scripts.resources.items;

import javafx.scene.image.Image;
import scripts.resources.GameObject;
import scripts.resources.Utility;

public class AmmoBox extends GameObject {

    public static final int PISTOLAMMO = 30;
    public static final int SHOTGUNAMMO = 10;
    public static final int RIFLEAMMO = 20;

    public AmmoBox() {
        this.setImage(new Image(
                Utility.getSrc("sprites/Items/Weapons/AmmoBox.png")));
    }
}
