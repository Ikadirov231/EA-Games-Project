package scripts.resources.rooms;

import javafx.scene.canvas.GraphicsContext;
import scripts.resources.Player;
import scripts.resources.Point;

public class StartingRoom extends Room {

    private Player player;

    public StartingRoom(Player player, String type, Point position) {
        super(player, type, position);
        this.player = player;
        initialize();
    }

    public StartingRoom(Room top, Room bottom, Room left, Room right, Player player, String type,
                        Point position) {
        super(top, bottom, left, right, player, type, position);
        initialize();
    }

    protected void initialize() {
        MazeGenerator.formatStartingRoom(
                this, this.type, player.getNumRooms(), player.getDifficulty());
        setVisited(true);
    }

    public void render(GraphicsContext context) {

        // Draw background, game objects, and UI (in order).
        drawObjects(context);
    }

    protected void updateObjects() {

    }
}
