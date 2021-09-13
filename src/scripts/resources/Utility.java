package scripts.resources;

import scripts.resources.items.Weapons.Crosshair;

import java.io.FileInputStream;
import java.io.FileNotFoundException;

public class Utility {

    public static FileInputStream getSrc(String src) {
        FileInputStream fis = null;
        try {
            fis = new FileInputStream(src);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        return fis;
    }

    public static boolean checkTextValidity(String text) {
        return text != null && text.trim().length() > 0;
    }

    /*public static boolean checkCollisionRect(double x1, double y1, double w1, double h1,
                                             double x2, double y2, double w2, double h2) {
        return y1 < y2 + h2 - 1 && y1 + h1 - 1 > y2 && x1 < x2 + w2 - 1 && x1 + w1 - 1 > x2;
    }*/

    public static boolean checkCollisionRect(int[] ints) {

        double x1 = ints[0];
        double y1 = ints[1];
        double w1 = ints[2];
        double h1 = ints[3];
        double x2 = ints[4];
        double y2 = ints[5];
        double w2 = ints[6];
        double h2 = ints[7];

        return y1 < y2 + h2 - 1 && y1 + h1 - 1 > y2 && x1 < x2 + w2 - 1 && x1 + w1 - 1 > x2;
    }

    public static boolean checkCollisionRect(Crosshair crosshair, GameObject obj) {
        double w1 = crosshair.getImage().getWidth();
        double h1 = crosshair.getImage().getHeight();
        double x1 = crosshair.getPosition().getX();
        double y1 = crosshair.getPosition().getY();
        double w2 = obj.getImage().getWidth();
        double h2 = obj.getImage().getHeight();
        double x2 = obj.getPosition().getX();
        double y2 = obj.getPosition().getY();
        return y1 < y2 + h2 - 1 && y1 + h1 - 1 > y2 && x1 < x2 + w2 - 1 && x1 + w1 - 1 > x2;
    }
}
