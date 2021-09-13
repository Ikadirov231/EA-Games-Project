package scripts.resources.items.Weapons;

public class Minigun extends Weapon {

    public static final String DEFAULT_NAME = "Minigun";
    public static final String DEFAULT_DESCRIPTION
            = "A melee weapon. Ammunition not included.";
    public static final int DEFAULT_DAMAGE = 160;
    public static final int DEFAULT_ATTACK_DELAY = 200;

    public Minigun() {
        super(DEFAULT_NAME, DEFAULT_DESCRIPTION, DEFAULT_DAMAGE, DEFAULT_ATTACK_DELAY);
    }

}
