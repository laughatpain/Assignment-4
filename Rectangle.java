/**
 * Created by James on 5/17/2015.
 */

public class Rectangle {

    private int left;
    private int top;
    private int width;
    private int height;

    public Rectangle(int left, int top, int width, int height) {
        this.left = left;
        this.top = top;
        this.width = width;
        this.height = height;
    }

    public int get_left() {
        return left;
    }

    public int get_top() {
        return top;
    }

    public int get_width() {
        return width;
    }

    public int get_height() {
        return height;
    }

    public boolean collision(int x, int y) {
        return (x >= left) && (x < left + width) && (y >= top) && (y < top + height);
    }
}