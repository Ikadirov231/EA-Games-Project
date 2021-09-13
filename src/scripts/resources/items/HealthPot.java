package scripts.resources.items;

public class HealthPot extends Item {

    public static final String DEFAULT_NAME = "G";
    public static final String DEFAULT_DESCRIPTION
            = "";
    public static final int DEFAULT_STACK_LIMIT = Integer.MAX_VALUE;

    public HealthPot(int count) {
        super(DEFAULT_NAME, DEFAULT_DESCRIPTION, count, DEFAULT_STACK_LIMIT);
    }

    @Override
    public void addCount(int count) {
        super.addCount(count);
        this.setCount(Math.max(0, this.getCount()));
    }
}
