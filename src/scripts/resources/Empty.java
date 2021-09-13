package scripts.resources;

import javafx.scene.image.Image;
import scripts.resources.items.Item;

public class Empty extends Item {
    public static final String DEFAULT_NAME = "Empty Slot";
    public static final String DEFAULT_DESCRIPTION = "";
    public static final int DEFAULT_COUNT = 1;
    public static final int DEFAULT_STACK_LIMIT = 1;

    public Empty() {
        super(DEFAULT_NAME, DEFAULT_DESCRIPTION, DEFAULT_COUNT, DEFAULT_STACK_LIMIT);
        setImage(new Image(Utility.getSrc("sprites/Items/Empty.png"), 50, 50, true, true));
    }
}
