package scripts.resources.items.Weapons;

public class TacticalShovel extends Weapon {

    public static final String DEFAULT_NAME = "Tactical Shovel";
    public static final String DEFAULT_DESCRIPTION
            = "A melee weapon. Too tactical to dig with.";
    public static final int DEFAULT_DAMAGE = 80;
    public static final int DEFAULT_ATTACK_DELAY = 100;

    public TacticalShovel() {
        super(DEFAULT_NAME, DEFAULT_DESCRIPTION, DEFAULT_DAMAGE, DEFAULT_ATTACK_DELAY);
    }

}
