package scripts.resources.items;

import javafx.scene.image.Image;
import scripts.resources.Utility;

public class Gem extends Item {
    public static final String DEFAULT_NAME = "Gem";
    public static final String DEFAULT_DESCRIPTION
            = "+50% Damage";
    public static final int DEFAULT_DAMAGE = 0;
    public static final int DEFAULT_ATTACK_DELAY = 0;

    public Gem() {
        super(DEFAULT_NAME, DEFAULT_DESCRIPTION, DEFAULT_DAMAGE, DEFAULT_ATTACK_DELAY);
        setImage(new Image(Utility.getSrc("sprites/Items/gem.png"), 50, 50, true, true));
    }
}