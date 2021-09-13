package scripts.resources.items.Weapons;

import javafx.scene.image.Image;
import scripts.resources.Utility;

public class CombatKnife extends MeleeWeapon {

    public static final String DEFAULT_NAME = "CombatKnife";
    public static final String DEFAULT_DESCRIPTION
            = "Simple CombatKnife to kill someone.";
    public static final int DEFAULT_DAMAGE = 25;
    public static final int DEFAULT_ATTACK_DELAY = 500;

    public CombatKnife() {
        super(DEFAULT_NAME, DEFAULT_DESCRIPTION, DEFAULT_DAMAGE, DEFAULT_ATTACK_DELAY);
        setImage(new Image(
                Utility.getSrc("sprites/Items/Weapons/CombatKnife.png"), 50, 50, true, true));
    }

}
