package scripts.resources;

import javafx.scene.canvas.GraphicsContext;
import scripts.resources.items.*;
import scripts.resources.items.Weapons.*;

public class Player extends Sprite {

    public static final int DIFFICULTY_EASY = 1001;
    public static final int DIFFICULTY_NORMAL = 1002;
    public static final int DIFFICULTY_HARD = 1003;

    private static final int MAX_HEALTH = 100;

    private static final boolean INVINCIBLE = false;

    private String name;
    private final int difficulty;
    private final Gold wallet;
    private final HealthPot healthPot;
    private final AttackPot attackPot;
    private final Inventory inventory;
    private int health;
    private GraphicsContext context;
    private int wins;


    public Player(String name, int difficulty) {
        this.wins = 0;
        this.name = name;
        this.difficulty = difficulty;
        this.setImage("sprites/Player/PlayerCombatKnife/PlayerCombatKnifeUp.gif");
        wallet = new Gold(getInitGold());
        healthPot = new HealthPot(1);
        attackPot = new AttackPot(5);
        inventory = new Inventory(this);
        inventory.giveItem(new CombatKnife());
        health = 100;
    }


    private int getInitGold() {
        switch (difficulty) {
        case DIFFICULTY_EASY:
            return 500;
        case DIFFICULTY_NORMAL:
            return 300;
        case DIFFICULTY_HARD:
            return 100;
        default:
            return 300; // This should never be reached
        }
    }

    public boolean setName(String name) {
        this.name = name;
        return Utility.checkTextValidity(name);
    }

    public void setHealth(int x) {
        this.health = (INVINCIBLE) ? MAX_HEALTH : x;
    }

    public Inventory getInventory() {
        return inventory;
    }

    public String getName() {
        return name;
    }

    public int getDifficulty() {
        return difficulty;
    }

    public Gold getWallet() {
        return wallet;
    }

    public int getHealth() {
        return (INVINCIBLE) ? MAX_HEALTH : this.health;
    }

    public int getNumRooms() {
        switch (difficulty) {
        case DIFFICULTY_EASY:
            return 10;
        case DIFFICULTY_NORMAL:
            return 25;
        case DIFFICULTY_HARD:
            return 40;
        default:
            return 25; // This should never be reached
        }
    }

    public HealthPot getHealthPot() {
        return healthPot;
    }

    public AttackPot getAttackPot() {
        return attackPot;
    }

    public void useHealthPot() {
        HealthPot hp = this.getHealthPot();

        if (this.getHealth() != 100 && hp.getCount() > 0) {
            this.setHealth(100);
            hp.addCount(-1);
        }
    }

    public void displace(GameObject obj) {
        int x = 0;
        double distance = this.getPosition().getX()
                + (this.getImage().getWidth()) - obj.getPosition().getX();
        double length = this.getPosition().getY()
                + (this.getImage().getHeight()) - obj.getPosition().getY();
        if (length >= 0 && length < distance) {
            distance = length;
            x = 1;
        }

        length = obj.getPosition().getY()
                + (obj.getImage().getHeight()) - this.getPosition().getY();
        if (length >= 0 && length < distance) {
            distance = length;
            x = 2;
        }

        length = obj.getPosition().getX() + (obj.getImage().getWidth()) - this.getPosition().getX();
        if (length >= 0 && length < distance) {
            distance = length;
            x = 3;
        }

        if (x == 0) {
            if (this.getPosition().getX() - distance > 0) {
                this.setPosition(this.getPosition().getX() - distance, this.getPosition().getY());
            }
        } else if (x == 1) {
            if (this.getPosition().getY() - distance > 0) {
                this.setPosition(this.getPosition().getX(), this.getPosition().getY() - distance);
            }
        } else if (x == 2) {
            if (this.getPosition().getY() + distance < 800 - this.getImage().getHeight()) {
                this.setPosition(this.getPosition().getX(), this.getPosition().getY() + distance);
            }
        } else if (x == 3) {
            if (this.getPosition().getX() + distance < 1024 - this.getImage().getWidth()) {
                this.setPosition(this.getPosition().getX() + distance, this.getPosition().getY());
            }
        }
    }

    public void attack(
            scripts.resources.items.Weapons.Crosshair crosshair, GraphicsContext context) {
        if (inventory.getSelectedItem() instanceof Weapon) {
            ((Weapon) inventory.getSelectedItem()).attack(crosshair, context);
        }
    }

    public boolean canAttack() {

        if (!(inventory.getSelectedItem() instanceof Weapon)) {
            return false;
        }

        if (!((Weapon) inventory.getSelectedItem()).inDelay()) {
            return true;
        }

        return false;
    }

    public int getDamage() {
        if (inventory.getSelectedItem() instanceof Weapon) {
            double mult = inventory.gemCount() * 0.5 + 1.0;
            return (int) (((Weapon) inventory.getSelectedItem()).getDamage() * mult);
        }
        return 0;
    }

    public boolean hasAmmo() {
        Item weapon = inventory.getSelectedItem();
        if (weapon instanceof MeleeWeapon) {
            return true;
        } else if (weapon instanceof Pistol) {
            if (this.inventory.getAmmo()[0] > 0) {
                this.inventory.setAmmo(0, this.inventory.getAmmo()[0] - 1);
                return true;
            }
        } else if (weapon instanceof Shotgun) {
            if (this.inventory.getAmmo()[1] > 0) {
                this.inventory.setAmmo(1, this.inventory.getAmmo()[1] - 1);
                return true;
            }
        } else if (weapon instanceof Rifle) {
            if (this.inventory.getAmmo()[2] > 0) {
                this.inventory.setAmmo(2, this.inventory.getAmmo()[2] - 1);
                return true;
            }
        }
        return false;
    }

    public int getWins() {
        return this.wins;
    }

    public void setWins(int i) {
        this.wins = i;
    }

    public void setHealthPot(int i) {
        healthPot.setCount(i);
    }

    public void setAttackPot(int i) {
        attackPot.setCount(i);
    }

    public void resetWallet() {
        wallet.setCount(getInitGold());
    }

    public void resetAmmo() {
        inventory.getAmmo()[0] = 15;
        inventory.getAmmo()[1] = 5;
        inventory.getAmmo()[2] = 10;

        resetGems();
    }

    public void resetGems() {
        for (Item item: inventory.getItems()) {
            if (item instanceof Gem) {
                item = null;
            }
        }
    }
}