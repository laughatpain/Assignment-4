import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import processing.core.PImage;

/**
 * Created by James on 5/3/2015.
 */
public abstract class MovingRate
    extends Moving
{
    protected int rate;

    public MovingRate (String name, Point position, ArrayList<PImage> imgs, int animation_rate, int rate)
    {
        super (name, position, imgs, animation_rate);
        this.rate = rate;
    }
    public int get_rate ()
    {
        return rate;
    }
    public abstract void schedule_entity (WorldModel world, Map<String, ArrayList<PImage>> i_store);
}
