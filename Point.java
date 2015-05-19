/**
 * Created by James on 5/3/2015.
 */
public class Point
{
    private int x;
    private int y;

    public Point (int x, int y)
    {
        this.x = x;
        this.y = y;
    }
    public int xCoor ()
    {
        return this.x;
    }
    public int yCoor ()
    {
        return this.y;
    }

    public boolean adjacent_to (Point other)
    {
        return (Math.abs(xCoor() - other.xCoor()) == 1 && yCoor() == other.yCoor()) ||
                (Math.abs(yCoor() - other.yCoor()) == 1 && xCoor() == other.xCoor());
    }
}
