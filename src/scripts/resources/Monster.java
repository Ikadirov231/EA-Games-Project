package scripts.resources;


import java.util.ArrayList;

public class Monster extends GameObject {
    private double hp = 0;
    private double ad = 0;
    private int coinValue;
    private int i;
    private boolean check;
    private ArrayList<Double> pathing;
    private static ArrayList<String> monsters = new ArrayList<String>();

    //Loads upon startup, initializes default images
    static {
        monsters.add(0, "frog.png");
        monsters.add(1, "bacteria.png");
        monsters.add(2, "frankenstein.png");
        monsters.add(3, "spider.gif");
        monsters.add(4, "FIREdragon.gif");
    }

    //Enables flexibility to add monster images in other classes, may never be used
    public void setMonsterImage(int number, String fileName) {
        monsters.add(number, fileName);
    }

    //Main constructor which takes in a type and makes a monster with
    // a specific HP and AD (attack damage) and sprite based on the type passed in (1 - 3).
    public Monster(int type) {
        switch (type) {
        case 1:
            this.hp = 10;
            this.ad = 20;
            this.coinValue = 15;
            this.setImage("sprites/Monster/" + monsters.get(1));
            break;
        case 2:
            this.hp = 50;
            this.ad = 40;
            this.coinValue = 25;
            this.setImage("sprites/Monster/" + monsters.get(2));
            break;
        case 3:
            this.hp = 50;
            this.ad = 20;
            this.coinValue = 20;
            this.setImage("sprites/Monster/" + monsters.get(3));
            break;
        case 4:
            this.hp = 250;
            this.ad = 50;
            this.coinValue = 100;
            this.setImage("sprites/Monster/" + monsters.get(4));
            break;
        default:
            this.hp = 20;
            this.ad = 10;
            this.coinValue = 10;
            this.setImage("sprites/Monster/" + monsters.get(0));
            break;
        }
    }

    public double getHP() {
        return this.hp;
    }

    public void setHP(double hp) {
        this.hp = hp;
    }

    public double getAD() {
        return this.ad;
    }

    public void setAD(double ad) {
        this.ad = ad;
    }

    public ArrayList<Double> setPathing(String type) {
        switch (type) {
        case "HorizontalLine":
            ArrayList<Double> path = new ArrayList<Double>();
            for (double i = this.getPosition().getX();
                 i < this.getPosition().getX() + 200; i = i + 2) {
                path.add(i);
            }
            this.pathing = path;
            return path;
        default:
            return null;
        }
    }

    public ArrayList<Double> getPathing() {
        return this.pathing;
    }


    public int getIndexPath() {
        return i;
    }

    public void iterateIndexPath() {
        this.i++;
    }

    public void decrementIndexPath() {
        this.i--;
    }

    public void checkTrue() {
        this.check = true;
    }

    public void checkFalse() {
        this.check = false;
    }

    public ArrayList<String> getMonsters() {
        return monsters;
    }

    public boolean isCheck() {
        return check;
    }

    public void setCheck(boolean check) {
        this.check = check;
    }

    public int getCoinValue() {
        return this.coinValue;
    }
}