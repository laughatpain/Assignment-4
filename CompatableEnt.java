/**
 * Created by James on 5/17/2015.
 */
public class CompatableEnt
{
    final Point pt;
    private final boolean found;

    public CompatableEnt(Point pt, boolean found) {
        this.pt = pt;
        this.found = found;
    }

    public Point getPoint() {
        return pt;
    }

    public boolean isFound() {
        return found;
    }
}
