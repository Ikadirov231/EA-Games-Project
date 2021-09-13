package scripts.resources.items;

import scripts.resources.GameObject;

public abstract class Item extends GameObject {

    private String name;
    private String description;
    private int count;
    private int stackLimit;

    public Item(String name, String description, int count, int stackLimit) {
        this.name = name;
        this.description = description;
        this.count = count;
        this.stackLimit = stackLimit;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public void addCount(int count) {
        this.count += count;
    }

    public boolean checkTransactionAvailability(int price) {
        return count >= price;
    }

    public int getCount() {
        return count;
    }

    public int getStackLimit() {
        return stackLimit;
    }

    public boolean equals(Item item) {
        return name == item.getName()
                && description == item.getDescription()
                && count == item.getCount()
                && stackLimit == item.getStackLimit();
    }
}
