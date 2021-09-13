package scripts.resources.rooms;

import javafx.scene.canvas.GraphicsContext;
import scripts.resources.GameObject;
import scripts.resources.Player;
import scripts.resources.Point;

public class ExitRoom extends Room {

    private boolean exitLocked;

    public ExitRoom(Player player, Point position) {
        super(player, position);
        initialize();
    }

    public ExitRoom(Player player, String type, Point position) {
        super(player, type, position);
        initialize();
    }

    public ExitRoom(Point position) {
        super(position);
        initialize();
    }

    protected void initialize() {
        exitLocked = true;
        setVisited(false);
        GameObject block1 = new GameObject("sprites/exit.png");
        block1.setPosition(300, 300);
        getObjectPools().getStructurePool().add(block1);

    }

    public void render(GraphicsContext context) {
        // Draw background, game objects, and UI (in order).
        drawObjects(context);

    }

    protected void updateObjects() {
        exitLocked = objectPools.getMonsterPool().size() > 0;
    }

    public boolean win() {
        double objx = getObjectPools().getStructurePool().get(0).getPosition().getX();
        double objy = getObjectPools().getStructurePool().get(0).getPosition().getY();

        double playx = getObjectPools().getPlayer().getPosition().getX();
        double playy = getObjectPools().getPlayer().getPosition().getY();

        if (objx + 30 > playx && objx - 30 < playx) {
            if (objy + 30 > playy && objy - 30 < playy) {
                return !exitLocked;
            }
        }
        return false;
    }

    public boolean exitLocked() {
        return exitLocked;
    }

    public void setExitLock(boolean exitLocked) {
        this.exitLocked = exitLocked;
    }

}
