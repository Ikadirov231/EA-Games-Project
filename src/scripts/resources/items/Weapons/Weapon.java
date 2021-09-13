package scripts.resources.items.Weapons;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.util.Duration;
import scripts.resources.GameObject;
import scripts.resources.Player;
import scripts.resources.Vector;
import scripts.resources.items.Item;

import java.io.File;
import java.util.Date;

public abstract class Weapon extends Item {

    public static final int DEFAULT_COUNT = 1;
    public static final int DEFAULT_STACK_LIMIT = 1;

    private final int damage;
    private final int attackDelay;
    private MediaPlayer attackSound;
    private long attackTime;

    public Weapon(String name, String description, int damage, int attackDelay) {
        super(name, description, DEFAULT_COUNT, DEFAULT_STACK_LIMIT);
        this.damage = damage;
        this.attackDelay = attackDelay;
        attackTime = 0;
    }

    public int getDamage() {
        return damage;
    }

    public int getAttackDelay() {
        return attackDelay;
    }

    public boolean inDelay() {
        Date time = new Date();
        return attackTime + attackDelay >= time.getTime();
    }

    public void attack(Crosshair crosshair, GraphicsContext context) {
        Date time = new Date();
        attackTime = time.getTime();
        Player player = crosshair.getPlayer();
        String orientation = crosshair.getOrientation();
        Vector initialPos = player.getPosition();

        Media media = new Media(new File("audio/" + this.getName() + ".mp3").toURI().toString());
        attackSound = new MediaPlayer(media);
        attackSound.play();

        renderAttack(crosshair, this, context, initialPos);

        if (this instanceof MeleeWeapon) {
            return;
        }

        GameObject bullet = new GameObject("sprites/Items/Weapons/Bullet.png");
        bullet.setPosition(crosshair.getPosition().getX(), crosshair.getPosition().getY());

        int speed = 50;

        Timeline timeline = new Timeline(new KeyFrame(Duration.millis(6), ev -> {
            if (orientation.equals("UP")) {
                bullet.setPosition(
                        bullet.getPosition().getX(), bullet.getPosition().getY() - speed);
                bullet.render(context);
            } else if (orientation.equals("DOWN")) {
                bullet.setPosition(
                        bullet.getPosition().getX(), bullet.getPosition().getY() + speed);
                bullet.render(context);
            } else if (orientation.equals("LEFT")) {
                bullet.setPosition(
                        bullet.getPosition().getX() - speed, bullet.getPosition().getY());
                bullet.render(context);
            } else {
                bullet.setPosition(
                        bullet.getPosition().getX() + speed, bullet.getPosition().getY());
                bullet.render(context);
            }

        }));
        timeline.setCycleCount(35);
        timeline.play();
    }

    private void renderAttack(
            Crosshair crosshair, Weapon weapon, GraphicsContext context, Vector initialPos) {
        String orientation = crosshair.getOrientation();
        Player player = crosshair.getPlayer();
        String name = weapon.getName();
        int offset = 0;

        if (this instanceof MeleeWeapon) {
            offset = 1;
        }

        if (orientation.equals("UP")) {
            player.setPosition(
                    player.getPosition().getX(), player.getPosition().getY() - (10 * offset));
            player.setImage(
                    "sprites/Player/Player" + name + "/Attack/Player" + name + "AttackUp.gif");
            player.render(context);
        } else if (orientation.equals("DOWN")) {
            player.setPosition(
                    player.getPosition().getX() - (20 * offset), player.getPosition().getY());
            player.setImage(
                    "sprites/Player/Player" + name + "/Attack/Player" + name + "AttackDown.gif");
            player.render(context);
        } else if (orientation.equals("LEFT")) {
            player.setPosition(player.getPosition().getX()
                    - (10 * offset), player.getPosition().getY() - (20 * offset));
            player.setImage(
                    "sprites/Player/Player" + name + "/Attack/Player" + name + "AttackLeft.gif");
            player.render(context);
        } else {
            player.setImage(
                    "sprites/Player/Player" + name + "/Attack/Player" + name + "AttackRight.gif");
            player.render(context);
        }

        Timeline timeline =
                new Timeline(new KeyFrame(Duration.millis(weapon.getAttackDelay()), ev -> {
                }));
        timeline.setCycleCount(1);
        timeline.play();
        timeline.setOnFinished(event -> resetImage(crosshair, this, context, initialPos));
    }

    private void resetImage(
            Crosshair crosshair, Weapon weapon, GraphicsContext context, Vector initialPos) {
        String orientation = crosshair.getOrientation();
        Player player = crosshair.getPlayer();
        String name = weapon.getName();


        int offset = 0;
        if (this instanceof MeleeWeapon) {
            offset = 1;
        }

        if (orientation.equals("UP")) {
            player.setImage("sprites/Player/Player" + name + "/Player" + name + "Up.gif");
            player.setPosition(initialPos.getX(), initialPos.getY() + (10 * offset));
            player.render(context);
        } else if (orientation.equals("DOWN")) {
            player.setImage("sprites/Player/Player" + name + "/Player" + name + "Down.gif");
            player.setPosition(initialPos.getX() + (20 * offset), initialPos.getY());
            player.render(context);
        } else if (orientation.equals("LEFT")) {
            player.setImage("sprites/Player/Player" + name + "/Player" + name + "Left.gif");
            player.setPosition(
                    initialPos.getX() + (10 * offset), initialPos.getY() + (20 * offset));
            player.render(context);
        } else {
            player.setImage("sprites/Player/Player" + name + "/Player" + name + "Right.gif");
            player.setPosition(initialPos.getX(), initialPos.getY());
            player.render(context);
        }
    }
}
