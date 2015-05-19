import processing.core.PImage;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by James on 5/4/2015.
 */
public class WorldObject
{
    protected String name;
    protected ArrayList<PImage> imgs;
    protected int current_img;

    public WorldObject (String name, ArrayList<PImage> imgs)
    {
        this.name = name;
        this.imgs = imgs;
        this.current_img = 0;
    }
    public PImage get_current_image() {
        return imgs.get(current_img);
    }
    public String get_name()
    {
        return name;
    }
    public PImage get_image()
    {
        return imgs.get(current_img);
    }
    public ArrayList<PImage> get_images ()
    {
        return this.imgs;
    }
    public void next_image ()
    {
        this.current_img = (this.current_img+1)% this.imgs.size();
    }

}

