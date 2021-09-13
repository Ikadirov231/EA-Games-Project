package scripts.resources.items.Weapons;

import javafx.scene.image.Image;
import scripts.resources.Utility;

public class Pistol extends LongRangeWeapon {
    public static final String DEFAULT_NAME = "Pistol";
    public static final String DEFAULT_DESCRIPTION
            = "Simple pistol to kill someone.";
    public static final int DEFAULT_DAMAGE = 10;
    public static final int DEFAULT_ATTACK_DELAY = 100;

    public Pistol() {
        super(DEFAULT_NAME, DEFAULT_DESCRIPTION, DEFAULT_DAMAGE, DEFAULT_ATTACK_DELAY);
        setImage(new Image(Utility.getSrc("sprites/Items/Weapons/Pistol.png"), 50, 50, true, true));
    }
}
