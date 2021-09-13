package scripts.resources.rooms;

import scripts.resources.Monster;
import scripts.resources.Player;
import scripts.resources.Point;
import scripts.resources.items.AmmoBox;

import java.util.*;

public class MazeGenerator {

    private static final int ROOM_DEFAULT = 1001;
    private static final int ROOM_EXIT = 1002;
    private static final int ROOM_START = 1003;
    private static final int ROOM_SHOP = 1004;
    private static final int ROOM_CHALLENGE = 1005;

    public static void formatStartingRoom(Room start, String type, int rooms, int difficulty) {

        start.getPosition().setMark(ROOM_START);
        Set<Point> posSet = new HashSet<Point>();
        posSet.add(start.getPosition());

        for (int i = 1; i < rooms; i++) {
            addLayout(start.getPosition(), posSet);
        }

        addSpecial(start, posSet, ROOM_SHOP, 1);

        int challengeRoomCount = 0;
        if (difficulty == Player.DIFFICULTY_EASY) {
            challengeRoomCount = 2;
        } else if (difficulty == Player.DIFFICULTY_NORMAL) {
            challengeRoomCount = 3;
        } else if (difficulty == Player.DIFFICULTY_HARD) {
            challengeRoomCount = 3;
        }
        for (int i = 0; i < challengeRoomCount; i++) {
            addSpecial(start, posSet, ROOM_CHALLENGE, 1);
        }
        addSpecial(start, posSet, ROOM_EXIT, 5);

        //Japes
        linkRooms(start, posSet, type, difficulty);

    }

    // Recursively traverses the existing maze to randomly add a room position.
    private static void addLayout(Point curr, Set<Point> posSet) {
        curr.setMark(ROOM_DEFAULT);
        addLayoutRec(curr, posSet, 1);
    }

    private static void addLayoutRec(Point curr, Set<Point> posSet, int depth) {
        final int recursionThreshold = 20;
        Point p = curr.getClone();
        int r = (int) (Math.random() * 4);

        do {
            switch (r) {
            case 0:
                p.addY(1);
                break;
            case 1:
                p.addY(-1);
                break;
            case 2:
                p.addX(1);
                break;
            default:
                p.addX(-1);
                break;
            }
        } while (depth > recursionThreshold && posSet.contains(p));

        if (depth > recursionThreshold || !posSet.contains(p)) {
            posSet.add(p);
        } else {
            addLayoutRec(p, posSet, ++depth);
        }
    }

    // Creates a semi-randomized line
    private static void addSpecial(Room start, Set<Point> posSet, int spec, int end) {
        String dir1 = "";
        String dir2 = "";

        dir1 = randomDir();

        while (dir2.equals("") || dir2.equals(dir1)) {
            dir2 = randomDir();
        }

        boolean flag = true;
        int i = 0;

        Point p = start.getPosition().getClone();

        while (i <= end || flag) {
            String curr = dir1;
            if (0 == (int) (Math.random() * 2)) {
                curr = dir2;
            }

            switch (curr) {
            case "up":
                p.addY(1);
                break;
            case "down":
                p.addY(-1);
                break;
            case "left":
                p.addX(-1);
                break;
            case "right":
                p.addX(1);
                break;
            default:
                break;
            }


            if (!posSet.contains(p)) {
                Point adding = p.getClone();
                if (i > end) {
                    adding.setMark(spec);
                    flag = false;
                } else {
                    adding.setMark(ROOM_DEFAULT);
                }
                posSet.add(adding);
            } else {
                end++;
            }
            i++;
        }
    }

    /**
     * Returns a random direction
     *
     * @return A random direction
     */
    private static String randomDir() {
        switch ((int) (Math.random() * 4)) {
        case 0:
            return "up";
        case 1:
            return "down";
        case 2:
            return "right";
        default:
            return "left";
        }
    }

    // Creates and links rooms based on the layout.
    private static void linkRooms(Room start, Set<Point> posSet, String type, int difficulty) {

        // Creating a hashmap of new rooms
        posSet.remove(start.getPosition());
        Map<Point, Room> map = new HashMap<Point, Room>();
        for (Point p : posSet) {
            map.put(p, getRoom(p, type, difficulty));
        }

        // Linking all the new rooms
        for (Point p : posSet) {
            int x = p.getX();
            int y = p.getY();
            Room r = map.get(p);

            Point temp = new Point(x, y + 1);
            if (posSet.contains(temp)) {
                r.setTop(map.get(temp));
            }

            temp = new Point(x, y - 1);
            if (posSet.contains(temp)) {
                r.setBottom(map.get(temp));
            }

            temp = new Point(x + 1, y);
            if (posSet.contains(temp)) {
                r.setRight(map.get(temp));
            }

            temp = new Point(x - 1, y);
            if (posSet.contains(temp)) {
                r.setLeft(map.get(temp));
            }
        }

        // Linking the new rooms to start room
        int x = start.getPosition().getX();
        int y = start.getPosition().getY();

        Point temp = new Point(x, y + 1);
        if (posSet.contains(temp)) {
            start.setTop(map.get(temp));
            map.get(temp).setBottom(start);
        }

        temp = new Point(x, y - 1);
        if (posSet.contains(temp)) {
            start.setBottom(map.get(temp));
            map.get(temp).setTop(start);
        }

        temp = new Point(x + 1, y);
        if (posSet.contains(temp)) {
            start.setRight(map.get(temp));
            map.get(temp).setLeft(start);
        }

        temp = new Point(x - 1, y);
        if (posSet.contains(temp)) {
            start.setLeft(map.get(temp));
            map.get(temp).setRight(start);
        }
    }

    private static Room getRoom(Point pos, String type, int difficulty) {
        Room r;
        switch (pos.getMark()) {
        case ROOM_DEFAULT:
            r = new DefaultRoom(null, type, pos);
            int depth = (int) Math.sqrt((pos.getX() * pos.getX()) + (pos.getY() * pos.getY()));

            for (int c = 0; c < depth; c++) {
                int monType = (int) (Math.random() * 4);
                Monster mon = new Monster(monType);
                double x = 32 + Math.random() * 650;
                double y = 32 + Math.random() * 500;
                mon.setPosition(x, y);
                mon.setPathing("HorizontalLine");
                r.getObjectPools().getMonsterPool().add(mon);
            }

            double spawnRate = difficulty - 999;
            spawnRate = 1 / spawnRate;
            spawnRate = Math.pow(spawnRate, 1.5);
            if (Math.random() < spawnRate) {
                AmmoBox ammobox = new AmmoBox();
                ammobox.setPosition(32 + Math.random() * 900, 32 + Math.random() * 700);
                r.getObjectPools().getMonsterPool().add(ammobox);
            }

            return r;
        case ROOM_EXIT:
            r = new ExitRoom(null, type, pos);

            Monster mon = new Monster(4);
            double x = 32 + Math.random() * 650;
            double y = 32 + Math.random() * 500;
            mon.setPosition(x, y);
            mon.setPathing("HorizontalLine");
            r.getObjectPools().getMonsterPool().add(mon);
            return r;
        case ROOM_SHOP:
            return new Shop(null, type, pos);
        case ROOM_CHALLENGE:
            return new ChallengeRoom(null, type, pos);
        default:
            return null; // should never be reached
        }
    }
}