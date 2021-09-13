package scripts.resources.items.Weapons;

public class NetheriteSword extends Weapon {

    public static final String DEFAULT_NAME = "Netherite Sword";
    public static final String DEFAULT_DESCRIPTION
            = "A melee weapon. *Unoriginality Warning*";
    public static final int DEFAULT_DAMAGE = 50;
    public static final int DEFAULT_ATTACK_DELAY = 5;

    public NetheriteSword() {
        super(DEFAULT_NAME, DEFAULT_DESCRIPTION, DEFAULT_DAMAGE, DEFAULT_ATTACK_DELAY);
    }

}
