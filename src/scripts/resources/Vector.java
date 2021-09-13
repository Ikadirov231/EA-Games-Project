package scripts.resources;

public class Vector {

    private double x;
    private double y;

    public Vector(double x, double y) {
        this.set(x, y);
    }

    public double getX() {
        return x;
    }
    public double getY() {
        return y;
    }

    public void set(double x, double y) {
        this.x = x;
        this.y = y;
    }

    public void add(double x, double y) {
        this.x += x;
        this.y += y;
    }

    public void add(Vector other) {
        this.x += other.getX();
        this.y += other.getY();
    }

    public void multiply(double v) {
        this.x *= v;
        this.y *= v;
    }
}