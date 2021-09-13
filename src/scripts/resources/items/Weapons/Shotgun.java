package scripts.resources.items.Weapons;

import javafx.scene.image.Image;
import scripts.resources.Utility;

public class Shotgun extends LongRangeWeapon {
    public static final String DEFAULT_NAME = "Shotgun";
    public static final String DEFAULT_DESCRIPTION
            = " ";
    public static final int DEFAULT_DAMAGE = 30;
    public static final int DEFAULT_ATTACK_DELAY = 100;

    public Shotgun() {
        super(DEFAULT_NAME, DEFAULT_DESCRIPTION, DEFAULT_DAMAGE, DEFAULT_ATTACK_DELAY);
        setImage(new Image(
                Utility.getSrc("sprites/Items/Weapons/Shotgun.png"), 50, 50, true, true));
    }
}

