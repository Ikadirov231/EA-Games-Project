package scripts.resources.rooms;

import javafx.scene.canvas.GraphicsContext;
import scripts.resources.GameObject;
import scripts.resources.Monster;
import scripts.resources.ObjectPools;
import scripts.resources.Player;
import scripts.resources.Point;
import scripts.resources.items.Gem;
import scripts.resources.items.Item;
import scripts.screens.Screen;

public class ChallengeRoom extends Room {

    private GameObject gem;
    private boolean active;

    public ChallengeRoom(Player player, Point position) {
        super(player, position);
        initialize();
    }

    public ChallengeRoom(ObjectPools objectPools, Point position) {
        super(objectPools.getPlayer(), position);
        initialize();
    }

    public ChallengeRoom(Player player, String type, Point position) {
        super(player, type, position);
        initialize();
    }

    public ChallengeRoom(Room top, Room bottom, Room left, Room right, Player player, String type,
                         Point position) {
        super(top, bottom, left, right, player, type, position);
        initialize();
    }

    public ChallengeRoom(Point position) {
        super(position);
        initialize();
    }

    protected void initialize() {
        setVisited(false);

        active = false;
        gem = new GameObject("sprites/Items/gem.png");
        gem.setPosition(450, 250);
        getObjectPools().getStructurePool().add(gem);
    }

    public void render(GraphicsContext context) {
        // TODO Auto-generated method stub
        drawObjects(context);

    }

    @Override
    protected void updateObjects() {

        Player player = this.getObjectPools().getPlayer();
        if (!active && player != null && player.overlaps(gem)) {
            Item gemItem = new Gem();
            player.getInventory().giveItem(gemItem);

            int monsterCount = 0;
            if (player.getDifficulty() == player.DIFFICULTY_EASY) {
                monsterCount = 4;
            } else if (player.getDifficulty() == player.DIFFICULTY_NORMAL) {
                monsterCount = 8;
            } else if (player.getDifficulty() == player.DIFFICULTY_HARD) {
                monsterCount = 16;
            }

            for (int c = 0; c < monsterCount; c++) {
                Monster mon = new Monster(2);
                double x = 82 + Math.random() * 600;
                double y = 200 + (int) (Math.random() * 2) * 200;
                mon.setPosition(x, y);
                mon.setPathing("HorizontalLine");
                objectPools.getMonsterPool().add(mon);
            }

            active = true;
            gem.setImage("sprites/Items/Empty.png");
            Screen.saveTheChild();
        }

        final boolean lock = active && objectPools.getMonsterPool().size() > 0;
        if (getLeft() != null) {
            setLeftLock(lock);
        }
        if (getRight() != null) {
            setRightLock(lock);
        }
        if (getTop() != null) {
            setTopLock(lock);
        }
        if (getBottom() != null) {
            setBottomLock(lock);
        }
    }
}