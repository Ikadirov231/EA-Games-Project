package scripts.resources.rooms;

import javafx.application.Platform;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.StackPane;
import scripts.resources.*;
import scripts.resources.items.AmmoBox;

import java.util.ArrayList;

public class Room {

    private int depth;

    private Room top;
    private Room bottom;
    private Room left;
    private Room right;
    private int[][] layout;
    protected String type;
    private final Point position;

    private boolean visited;

    private boolean topLocked;
    private boolean bottomLocked;
    private boolean leftLocked;
    private boolean rightLocked;

    private Image background;

    protected ObjectPools objectPools;

    public Room(Room top, Room bottom, Room left, Room right, Player player, String type,
                Point position) {
        this.top = top;
        this.bottom = bottom;
        this.left = left;
        this.right = right;
        objectPools = new ObjectPools(player);
        this.type = type;
        this.position = position;

        topLocked = false;
        bottomLocked = false;
        leftLocked = false;
        rightLocked = false;
    }

    public Room(Player player, Point position) {
        this(null, null, null, null, player, null, position);
    }

    public Room(Player player, String type, Point position) {
        this(null, null, null, null, player, type, position);
    }

    public Room(Point position) {
        this(null, position);
    }

    public void setTop(Room top) {
        this.top = top;
    }

    public void setBottom(Room bottom) {
        this.bottom = bottom;
    }

    public void setLeft(Room left) {
        this.left = left;
    }

    public void setRight(Room right) {
        this.right = right;
    }

    public void setObjectPools(ObjectPools objectPools) {
        this.objectPools = objectPools;
    }

    public Room getTop() {
        return top;
    }

    public Room getBottom() {
        return bottom;
    }

    public Room getLeft() {
        return left;
    }

    public Room getRight() {
        return right;
    }

    public ObjectPools getObjectPools() {
        return objectPools;
    }

    public Point getPosition() {
        return position;
    }

    public void enter(Player player) {
        this.objectPools.setPlayer(player);
        this.visited = true;
    }

    public void exit() {
        this.objectPools.setPlayer(null);
    }

    public boolean isInside() {
        return this.objectPools.hasPlayer();
    }

    public boolean topLocked() {
        return topLocked;
    }

    public boolean bottomLocked() {
        return bottomLocked;
    }

    public boolean leftLocked() {
        return leftLocked;
    }

    public boolean rightLocked() {
        return rightLocked;
    }

    public void setTopLock(boolean topLocked) {
        this.topLocked = topLocked;
    }

    public void setBottomLock(boolean bottomLocked) {
        this.bottomLocked = bottomLocked;
    }

    public void setLeftLock(boolean leftLocked) {
        this.leftLocked = leftLocked;
    }

    public void setRightLock(boolean rightLocked) {
        this.rightLocked = rightLocked;
    }

    public boolean visited() {
        return visited;
    }

    public void setVisited(boolean visited) {
        this.visited = visited;
    }

    /**
     * Initialize room objects (e.g. walls, monsters, player, etc.).
     * See example in StartingRoom.java.
     */
    protected void initialize() {

    }

    /**
     * Include everything that goes in the root Graphics context.
     * Remember to call drawObjects() in this method.
     * See example in StartingRoom.java.
     *
     * @param context Graphics context
     */
    public void render(GraphicsContext context) {

    }

    /**
     * Calculate new object positions, data, etc..
     * See example in StartingRoom.java.
     */
    protected void updateObjects() {

    }

    /**
     * Requests an update.
     *
     * @param context Graphics context
     */
    public void update(GraphicsContext context) {
        int x = 1;
        Platform.runLater(new Runnable() {
            public void run() {
                for (GameObject mon : objectPools.getMonsterPool()) {
                    mon.render(context);
                    if (mon instanceof AmmoBox) {
                        continue;
                    }
                    Monster monster = (Monster) mon;
                    ArrayList<Double> path = monster.getPathing();
                    if (monster.getIndexPath() >= path.size()) {
                        monster.checkTrue();
                    } else {
                        monster.getPosition().set(
                                path.get(monster.getIndexPath()), monster.getPosition().getY());
                    }

                    if (monster.isCheck()) {
                        monster.decrementIndexPath();
                    } else {
                        monster.iterateIndexPath();
                    }

                    if (monster.getIndexPath() < 0) {
                        monster.checkFalse();
                        monster.iterateIndexPath();
                    }

                }
                updateObjects();
                context.clearRect(0, 0, 1024, 800);
                render(context);
            }
        });
    }

    /**
     * Reads data from objectPools and adds their images to the root pane
     * to each of their corresponding positions.
     *
     * @param context Context
     */
    protected void drawObjects(GraphicsContext context) {

        for (GameObject obj : getObjectPools().getStructurePool()) {
            obj.render(context);
        }

        for (GameObject obj : getObjectPools().getMonsterPool()) {
            obj.render(context);
        }

        for (GameObject obj : getObjectPools().getEntityPool()) {
            obj.render(context);
        }

        for (GameObject obj : getObjectPools().getEffectsPool()) {
            obj.render(context);
        }

        if (isInside()) {
            Player plr = getObjectPools().getPlayer();
            plr.render(context);
        }
    }

    /**
     * Draws the walls/doors on the game pane's border
     *
     * @param type Type of background
     */
    public void createWalls(String type) {

        int[][] lay = new int[32][25];

        for (int c = 0; c < lay.length; c++) {
            if (c < lay[0].length) {
                lay[0][c] = 1;
                lay[lay.length - 1][c] = 1;
            }
            lay[c][0] = 1;
            lay[c][lay[0].length - 1] = 1;
        }

        lay[15][0] = (top == null) ? 1 : -1;
        lay[16][0] = (top == null) ? 1 : -2;
        lay[0][12] = (left == null) ? 1 : -3;
        lay[0][13] = (left == null) ? 1 : -4;
        lay[31][12] = (right == null) ? 1 : -3;
        lay[31][13] = (right == null) ? 1 : -4;
        lay[15][24] = (bottom == null) ? 1 : -1;
        lay[16][24] = (bottom == null) ? 1 : -2;

        Image temp = new Image(Utility.getSrc("sprites/" + type.toUpperCase() + "Wall.png"));
        final int tile = (int) temp.getWidth();

        for (int c = 0; c < lay.length; c++) {
            for (int d = 0; d < lay[0].length; d++) {

                switch (lay[c][d]) {
                case 1:
                    GameObject wall = new GameObject();
                    wall.setImage("sprites/" + type.toUpperCase() + "Wall.png");
                    wall.setPosition((int) (c * wall.getImage().getWidth()),
                            (int) (d * wall.getImage().getHeight()));
                    objectPools.getStructurePool().add(wall);
                    break;

                case -1:
                    GameObject ld = new GameObject();
                    ld.setImage("sprites/LeftDoor.png");
                    ld.setPosition(c * tile,
                            d * tile);
                    objectPools.getStructurePool().add(ld);
                    break;

                case -2:
                    GameObject rd = new GameObject();
                    rd.setImage("sprites/RightDoor.png");
                    rd.setPosition(c * tile,
                            d * tile);
                    objectPools.getStructurePool().add(rd);
                    break;

                case -3:
                    GameObject rds = new GameObject();
                    rds.setImage("sprites/RightDoorSide.png");
                    rds.setPosition(c * tile,
                            d * tile);
                    objectPools.getStructurePool().add(rds);
                    break;

                case -4:
                    GameObject lds = new GameObject();
                    lds.setImage("sprites/LeftDoorSide.png");
                    lds.setPosition(c * tile,
                            d * tile);
                    objectPools.getStructurePool().add(lds);
                    break;

                default:
                    break;
                }
            }
        }
    }

    public void initialRender(GraphicsContext context, StackPane sp) {

        int random = (int) (Math.random() * 4);
        background = new Image(Utility.getSrc(
                "Sprites/Backgrounds/" + type + "Background" + random + ".png"));
        ImageView iv = new ImageView(background);
        iv.setFitWidth(1024);
        iv.setFitHeight(800);
        sp.getChildren().add(iv);
    }

    public void updateRender(GraphicsContext context, ImageView iv) {
        if (background == null) {
            int random = (int) (Math.random() * 4);
            background = new Image(
                    Utility.getSrc("Sprites/Backgrounds/" + type + "Background" + random + ".png"));
        }
        iv.setImage(background);
        drawObjects(context);
    }

    public int getDepth() {
        return depth;
    }

    public void setDepth(int depth) {
        this.depth = depth;
    }
}