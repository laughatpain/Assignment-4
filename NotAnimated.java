import processing.core.PImage;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Created by James on 5/3/2015.
 */
public abstract class NotAnimated
    extends Entity
{
    protected int rate;
    public NotAnimated (String name,  ArrayList<PImage> imgs, Point position,int rate)
    {
        super (name, imgs, position);
        this.rate = rate;
    }
    public abstract void schedule_entity (WorldModel world, Map<String, ArrayList<PImage>> i_store);
    public int get_rate ()
    {
        return rate;
    }
}
