package scripts.resources;

public class Point extends Object {
    private int x;
    private int y;
    private int mark;

    public Point(int x, int y, int mark) {
        this.x = x;
        this.y = y;
        this.mark = mark;
    }

    public Point(int x, int y) {
        this(x, y, 0);
    }

    public void setX(int x) {
        this.x = x;
    }

    public void setY(int y) {
        this.y = y;
    }

    public void addX(int x) {
        this.x += x;
    }

    public void addY(int y) {
        this.y += y;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    @Override
    public boolean equals(Object obj) {
        return obj instanceof Point && ((Point) obj).getX() == x && ((Point) obj).getY() == y;
    }

    public Point getClone() {
        return new Point(x, y, mark);
    }

    public void setMark(int mark) {
        this.mark = mark;
    }

    public int getMark() {
        return mark;
    }

    public String toString(boolean showMark) {
        if (showMark) {
            return "(" + x + ", " + y + " ; " + mark + ")";
        } else {
            return "(" + x + ", " + y + ")";
        }
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + x;
        result = prime * result + y;
        return result;
    }
}
