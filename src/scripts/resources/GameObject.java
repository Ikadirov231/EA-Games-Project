package scripts.resources;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;

public class GameObject {

    private Vector position;
    private Vector velocity;
    private String filename;
    private Image image;

    public GameObject() {
        position = new Vector(0, 0);
        velocity = new Vector(0, 0);
    }

    public GameObject(String filename) {
        position = new Vector(0, 0);
        velocity = new Vector(0, 0);
        this.filename = filename;
        setImage(filename);
    }

    public boolean overlaps(GameObject other) {
        boolean noOverlap =
                this.position.getX() + (this.image.getWidth() * 0.8) < other.getPosition().getX()
                        || this.position.getY()
                        + (this.image.getHeight() * 0.8) < other.getPosition().getY()
                        || other.getPosition().getY()
                        + (other.getImage().getHeight() * 0.8) < this.position.getY()
                        || other.getPosition().getX()
                        + (other.getImage().getWidth() * 0.8) < this.position.getX();
        return !noOverlap;
    }

    public void render(GraphicsContext context) {
        context.drawImage(image, position.getX(), position.getY());
    }

    public Vector getPosition() {
        return position;
    }

    public void setPosition(double x, double y) {
        position.set(x, y);
    }

    public Vector getVelocity() {
        return velocity;
    }

    public void setVelocity(int x, int y) {
        velocity.set(x, y);
    }

    public Image getImage() {
        return image;
    }

    public String getFilename() {
        int x = filename.indexOf("Up");
        x = Math.max(x, filename.indexOf("Down"));
        x = Math.max(x, filename.indexOf("Left"));
        x = Math.max(x, filename.indexOf("Right"));
        return this.filename.substring(0, x);
    }

    public String getFullFilename() {
        return this.filename;
    }


    public void setImage(String filename) {
        this.filename = filename;
        image = new Image(Utility.getSrc(filename));
    }

    public void setImage(Image image) {
        this.image = image;
    }
}