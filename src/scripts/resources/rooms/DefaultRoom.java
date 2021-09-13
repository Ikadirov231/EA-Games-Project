package scripts.resources.rooms;

import javafx.scene.canvas.GraphicsContext;
import scripts.resources.ObjectPools;
import scripts.resources.Player;
import scripts.resources.Point;

public class DefaultRoom extends Room {

    public DefaultRoom(Player player, Point position) {
        super(player, position);
        initialize();
    }

    public DefaultRoom(ObjectPools objectPools, Point position) {
        super(objectPools.getPlayer(), position);
        initialize();
    }

    public DefaultRoom(Player player, String type, Point position) {
        super(player, type, position);
        initialize();
    }

    public DefaultRoom(Room top, Room bottom, Room left, Room right, Player player, String type,
                       Point position) {
        super(top, bottom, left, right, player, type, position);
        initialize();
    }

    public DefaultRoom(Point position) {
        super(position);
        initialize();
    }

    protected void initialize() {
        setVisited(false);
    }

    public void render(GraphicsContext context) {
        // TODO Auto-generated method stub
        drawObjects(context);

    }

    @Override
    protected void updateObjects() {
        final boolean hasMonsters = objectPools.getMonsterPool().size() > 0;
        if (getLeft() != null) {
            setLeftLock(hasMonsters && !getLeft().visited());
        }
        if (getRight() != null) {
            setRightLock(hasMonsters && !getRight().visited());
        }
        if (getTop() != null) {
            setTopLock(hasMonsters && !getTop().visited());
        }
        if (getBottom() != null) {
            setBottomLock(hasMonsters && !getBottom().visited());
        }
    }
}
