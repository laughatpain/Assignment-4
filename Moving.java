import processing.core.PImage;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Created by James on 5/3/2015.
 */
public abstract class Moving
    extends Entity
{
    protected int animation_rate;

    public Moving (String name, Point position, ArrayList<PImage> imgs, int animation_rate)
    {
        super (name, imgs, position);
        this.animation_rate = animation_rate;
    }
    public int get_animation_rate ()
    {
        return animation_rate;
    }
}
