package scripts.resources.items;

public class Gold extends Item {

    public static final String DEFAULT_NAME = "Gold";
    public static final String DEFAULT_DESCRIPTION
            = "Used as currency. Highly deflated in value, yet magically lightweight.";
    public static final int DEFAULT_STACK_LIMIT = Integer.MAX_VALUE;

    public Gold(int count) {
        super(DEFAULT_NAME, DEFAULT_DESCRIPTION, count, DEFAULT_STACK_LIMIT);
    }

}
