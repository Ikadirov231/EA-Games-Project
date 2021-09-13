package scripts.resources;

import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.ImageView;
import javafx.scene.paint.Color;
import scripts.resources.items.Weapons.Crosshair;
import scripts.resources.rooms.*;
import scripts.screens.Screen;

import java.util.HashSet;

public class Maze {

    private Room currentRoom;
    private final Player player;

    private final Crosshair crosshair;
    private final Canvas minimap;
    private final String type;

    public Maze(Player player, String type, Point position) {
        this.crosshair = new Crosshair(player);
        currentRoom = new StartingRoom(player, type, position);
        this.player = player;
        this.type = type.toUpperCase();
        minimap = new Canvas(180, 120);
        createRoomWalls(currentRoom, new HashSet<Point>());
        player.setPosition(500, 400);
    }

    public Room getCurrentRoom() {
        return currentRoom;
    }

    public void setCurrentRoom(Room room) {
        this.currentRoom = room;
    }

    public Player getPlayer() {
        return this.player;
    }

    // Pretty trashy function that allows for room changing
    /*
    The leftmost door is left
    The 2nd leftmost door is down
    The 2nd rightmost door is up
    The rightmost door is right
    */

    public void updateRoom(GraphicsContext context, ImageView iv) {
        double playerX = player.getPosition().getX();
        double playerY = player.getPosition().getY();

        //System.out.println((int) playerX + ", " + (int) playerY);

        if (playerY < 5 && playerX > 453 && playerX < 510 && !currentRoom.topLocked()) {
            if (currentRoom.getTop() != null) {
                Screen.saveTheChild();
                currentRoom.exit();
                currentRoom.getTop().enter(player);
                currentRoom = currentRoom.getTop();
                currentRoom.updateRender(context, iv);
                player.setPosition(475, 710);
                drawMinimap();
            } else {
                //System.out.println("There's nothing here...");
                player.setPosition(playerX, playerY + 5);
            }
        }

        if (playerX < 5 && playerY < 410 && playerY > 345 && !currentRoom.leftLocked()) {
            if (currentRoom.getLeft() != null) {
                Screen.saveTheChild();
                currentRoom.exit();
                currentRoom.getLeft().enter(player);
                currentRoom = currentRoom.getLeft();
                currentRoom.updateRender(context, iv);
                player.setPosition(940, 370);
                drawMinimap();
            } else {
                //System.out.println("There's nothing here...");
                player.setPosition(playerX + 5, playerY);
            }
        }

        if (playerX > 945 && playerY < 410 && playerY > 345 && !currentRoom.rightLocked()) {
            if (currentRoom.getRight() != null) {
                Screen.saveTheChild();
                currentRoom.exit();
                currentRoom.getRight().enter(player);
                currentRoom = currentRoom.getRight();
                currentRoom.updateRender(context, iv);
                player.setPosition(10, 370);
                drawMinimap();
            } else {
                //System.out.println("There's nothing here...");
                player.setPosition(playerX - 5, playerY);
            }

        }

        if (playerY > 721 && playerX > 453 && playerX < 510 && !currentRoom.bottomLocked()) {
            if (currentRoom.getBottom() != null) {
                Screen.saveTheChild();
                currentRoom.exit();
                currentRoom.getBottom().enter(player);
                currentRoom = currentRoom.getBottom();
                currentRoom.updateRender(context, iv);
                player.setPosition(475, 10);
                drawMinimap();
            } else {
                //System.out.println("There's nothing here...");
                player.setPosition(playerX, 719);
            }
        }
    }

    public boolean winner() {
        if (currentRoom instanceof ExitRoom) {
            return ((ExitRoom) currentRoom).win();
        }
        return false;
    }

    public boolean loser() {
        return player.getHealth() <= 0;
    }

    public Canvas getMinimap() {
        return minimap;
    }

    public void drawMinimap() {
        GraphicsContext g = minimap.getGraphicsContext2D();

        g.setFill(Color.WHITE);
        g.fillRect(0, 0, minimap.getWidth() - 1, minimap.getHeight() - 1);

        g.setStroke(Color.GRAY);
        g.strokeRect(0, 0, minimap.getWidth() - 1, minimap.getHeight() - 1);

        final int w = 12;
        final int h = 8;
        final int cx = (int) minimap.getWidth() / 2 - 1 - w / 2;
        final int cy = (int) minimap.getHeight() / 2 - 1 - h / 2;

        drawRooms(cx, cy, w, h, g, currentRoom, new HashSet<Point>());
        g.setStroke(Color.RED);
        g.strokeRect(cx + 2, cy + 2, w - 4, h - 4);
    }

    private void drawRooms(int cx, int cy, int w, int h, GraphicsContext g, Room curr,
                           HashSet<Point> visited) {

        int rx = curr.getPosition().getX();
        int ry = curr.getPosition().getY();

        visited.add(curr.getPosition());

        g.setStroke(Color.BLUE);
        g.strokeRect(cx, cy, w, h);

        if (!curr.visited()) {
            g.setFill(Color.DARKGRAY);

        } else if (!(curr instanceof DefaultRoom)) {
            if (curr instanceof StartingRoom) {
                g.setFill(Color.GREEN);
            } else if (curr instanceof ExitRoom) {
                g.setFill(Color.ORANGE);
            } else if (curr instanceof Shop) {
                g.setFill(Color.YELLOW);
            }

        } else {
            g.setFill(Color.LIGHTGRAY);
        }

        g.fillRect(cx + 1, cy + 1, w - 2, h - 2);

        if (curr.getTop() != null && !visited.contains(new Point(rx, ry + 1))) {
            drawRooms(cx, cy - h, w, h, g, curr.getTop(), visited);

        }
        if (curr.getBottom() != null && !visited.contains(new Point(rx, ry - 1))) {
            drawRooms(cx, cy + h, w, h, g, curr.getBottom(), visited);

        }
        if (curr.getRight() != null && !visited.contains(new Point(rx + 1, ry))) {
            drawRooms(cx + w, cy, w, h, g, curr.getRight(), visited);

        }
        if (curr.getLeft() != null && !visited.contains(new Point(rx - 1, ry))) {
            drawRooms(cx - w, cy, w, h, g, curr.getLeft(), visited);
        }
    }

    private void createRoomWalls(Room curr, HashSet<Point> visited) {

        int rx = curr.getPosition().getX();
        int ry = curr.getPosition().getY();

        visited.add(curr.getPosition());

        curr.createWalls(type);

        if (curr.getTop() != null && !visited.contains(new Point(rx, ry + 1))) {
            createRoomWalls(curr.getTop(), visited);

        }
        if (curr.getBottom() != null && !visited.contains(new Point(rx, ry - 1))) {
            createRoomWalls(curr.getBottom(), visited);

        }
        if (curr.getRight() != null && !visited.contains(new Point(rx + 1, ry))) {
            createRoomWalls(curr.getRight(), visited);

        }
        if (curr.getLeft() != null && !visited.contains(new Point(rx - 1, ry))) {
            createRoomWalls(curr.getLeft(), visited);
        }
    }

    public Crosshair getCrosshair() {
        return crosshair;
    }

    // TODO implement the rest of this class to function as the entire maze of rooms

    /* [SOME HELPFUL INFO]
     * Yeah, its heccing long and you might not have to read these.
     * Just read them if you feel like reading it might be quicker to do stuff.
     *
     *
     * ==> IMPORTANT METHODS IN ROOM OBJECTS
     * First of all, Room is the abstract superclass for these subclasses:
     * StartingRoom, Shop, DefaultRoom, and ExitRoom.
     *
     * There are three abstract methods in Room.java:
     *
     * Use the initialize() method for loading up the room's contents that will
     * exist upon first entering the room. Things like walls, monsters, shopkeepers,
     * etc. gets created here.
     *
     * The contents of the pane is created via the drawPane() method. Things like
     * background image, UI, and object drawing goes here. In most cases, you can
     * just copy the contents from StartingRoom.java's drawPane().
     *
     * Updating data of objects are done in updateObjects(). Things like movement,
     * status update, and adding/removing objects are done here. This method will be
     * called regularly in an automatic fashion.
     *
     *
     * ==> ROOM TRAVERSAL
     * From one room, its four adjascent rooms may be accessed with getTop(),
     * setTop(), getLeft(), and so on (you get the idea). It's kinda like
     * doubly linked lists (or undirected graphs, to be more precise).
     *
     *
     * ==> ACCESSING OBJECTS IN A ROOM
     * All objects in a room are stored in the objectPools variable. It contains
     * the player, structures, monsters, etc.; see ObjectPools.java for details.
     *
     * Simply call the getObjectPools() and chain up some methods to access whatever
     * game object you want in that room.
     *
     *
     * ==> ADDITIONAL NOTES
     * In case you are still confused about what methods to use, I did some
     * commenting in Room.java. You can also check out StartingRoom.java for
     * example usage of some stuff described above.
     *
     * Also, the update loop that I currently set up is kinda terrible.
     * Not only is it unoptimized, but also, if you haven't noticed it yet,
     * closing the window after starting the game does not actually terminate
     * the program. This update loop is the one to blame.
     * The loop is in Screen.java's openGameScreen(). If you know a better way
     * of doing this, it'd be really nice if you take care of it.
     *     ^ Delete this paragraph if you fixed it.
     *
     * Try to keep this comment for the duration of this milestone for others to see.
     */

}