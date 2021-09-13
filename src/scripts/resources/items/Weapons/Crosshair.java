package scripts.resources.items.Weapons;

import scripts.resources.GameObject;
import scripts.resources.Player;
import scripts.resources.Sprite;
import scripts.resources.Vector;

public class Crosshair extends Sprite {
    private Player player;
    private String orientation;

    public Crosshair(Player player) {
        this.player = player;
        this.setImage("sprites/CrossHair.png");
        this.orientation = "UP";
    }

    @Override
    public void setPosition(double x, double y) {
        super.setPosition(x, y);
    }

    @Override
    public Vector getPosition() {
        return super.getPosition();
    }

    public void setOrientation(String orientation) {
        this.orientation = orientation;
    }

    public String getOrientation() {
        return this.orientation;
    }

    public Player getPlayer() {
        return this.player;
    }

    @Override
    public boolean overlaps(GameObject other) {
        boolean noOverlap = true;
        if (!(player.getInventory().getSelectedItem() instanceof Weapon)) {
            return false;
        }
        if ((Weapon) player.getInventory().getSelectedItem() instanceof MeleeWeapon) {
            noOverlap =
                    this.getPosition().getX()
                            + (this.getImage().getWidth() * 0.8) < other.getPosition().getX()
                            || this.getPosition().getY()
                            + (this.getImage().getHeight() * 0.8) < other.getPosition().getY()
                            || other.getPosition().getY()
                            + (other.getImage().getHeight() * 0.8) < this.getPosition().getY()
                            || other.getPosition().getX()
                            + (other.getImage().getWidth() * 0.8) < this.getPosition().getX();
        } else {
            if (orientation.equals("UP")) {
                noOverlap =
                        this.getPosition().getX()
                                + (this.getImage().getWidth() * 0.8) < other.getPosition().getX()
                                || this.getPosition().getY()
                                + (this.getImage().getHeight() * 0.8) < other.getPosition().getY()
                                || other.getPosition().getX()
                                + (other.getImage().getWidth() * 0.8) < this.getPosition().getX();
            } else if (orientation.equals("DOWN")) {
                noOverlap =
                        this.getPosition().getX()
                                + (this.getImage().getWidth() * 0.8) < other.getPosition().getX()
                                || other.getPosition().getY()
                                + (other.getImage().getHeight() * 0.8) < this.getPosition().getY()
                                || other.getPosition().getX()
                                + (other.getImage().getWidth() * 0.8) < this.getPosition().getX();

            } else if (orientation.equals("LEFT")) {
                noOverlap =
                        this.getPosition().getX()
                                + (this.getImage().getWidth() * 0.8) < other.getPosition().getX()
                                || this.getPosition().getY()
                                + (this.getImage().getHeight() * 0.8) < other.getPosition().getY()
                                || other.getPosition().getY()
                                + (other.getImage().getHeight() * 0.8) < this.getPosition().getY();
            } else {
                noOverlap =
                        this.getPosition().getY()
                                + (this.getImage().getHeight() * 0.8) < other.getPosition().getY()
                                || other.getPosition().getY()
                                + (other.getImage().getHeight() * 0.8) < this.getPosition().getY()
                                || other.getPosition().getX()
                                + (other.getImage().getWidth() * 0.8) < this.getPosition().getX();
            }
        }
        return !noOverlap;
    }
}