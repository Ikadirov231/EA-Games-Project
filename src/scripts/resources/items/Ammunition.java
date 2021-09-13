package scripts.resources.items;

public class Ammunition extends Item {

    public static final String DEFAULT_NAME = "All-purpose Ammunition";
    public static final String DEFAULT_DESCRIPTION
            = "Magically reshapes itself to fit inside any firearm.";
    public static final int DEFAULT_STACK_LIMIT = Integer.MAX_VALUE;

    public Ammunition(int count) {
        super(DEFAULT_NAME, DEFAULT_DESCRIPTION, count, DEFAULT_STACK_LIMIT);
    }

}
