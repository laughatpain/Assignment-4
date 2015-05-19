import processing.core.PImage;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by James on 5/3/2015.
 */
public class Blacksmith
    extends Entity
{
    protected int resource_count;
    protected int resource_limit;
    protected int rate;
    protected int resource_distance;
    public Blacksmith (String name, ArrayList<PImage> imgs, Point position, int rate,
                       int resource_distance, int resource_limit)
    {
        super (name, imgs, position);
        this.rate = rate;
        this.resource_distance = resource_distance;
        this.resource_limit = resource_limit;
        this.resource_count = 0;
        this.resource_distance = 1;
    }
    public int get_resource_count() {
        return resource_count;
    }

    public void set_resource_count(int n) {
        this.resource_count = n;
    }

    public int get_resource_limit() {
        return resource_limit;
    }

    public int get_rate() {
        return rate;
    }

    public int get_resource_distance() {
        return resource_distance;
    }
    public String entity_string ()
    {
        return ("blacksmith" + name + position.xCoor() + position.yCoor() + resource_limit + rate
        + resource_distance);
    }
}

