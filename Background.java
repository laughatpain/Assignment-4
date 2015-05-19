import processing.core.PImage;


import java.util.ArrayList;
import java.util.List;

/**
 * Created by James on 5/3/2015.
 */
public class Background
    extends WorldObject
{
    ArrayList<PImage> imgs;
    public Background (String name, ArrayList<PImage> imgs)
    {
        super (name, imgs);
        this.imgs = imgs;
    }
    public ArrayList<PImage> get_images ()
    {
        return imgs;
    }
}

