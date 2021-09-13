package scripts.resources.items;

public class AttackPot extends Item {
    public static final String DEFAULT_NAME = "G";
    public static final String DEFAULT_DESCRIPTION
            = "";
    public static final int DEFAULT_STACK_LIMIT = Integer.MAX_VALUE;

    public AttackPot(int count) {
        super(DEFAULT_NAME, DEFAULT_DESCRIPTION, count, DEFAULT_STACK_LIMIT);
    }
}
