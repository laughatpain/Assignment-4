import processing.core.PImage;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Created by James on 5/3/2015.
 */
public class Ore
    extends NotAnimated
{
    private static final int BLOB_RATE_SCALE = 4;
    public Ore (String name, Point position, ArrayList<PImage> imgs, int rate)
    {
        super (name,  imgs, position, rate = 5000);
    }
    public String entity_string ()
    {
        return ("ore" + name + position.xCoor() + position.yCoor() + rate);
    }
    @Override
    public void schedule_entity (WorldModel world, Map<String, ArrayList <PImage>> i_store)
    {
        schedule_ore(world, System.currentTimeMillis(), i_store);
    }
    public void schedule_ore (WorldModel world, long ticks, Map<String, ArrayList<PImage>> i_store)
    {
        schedule_action(create_ore_transformation_action(world, i_store), ticks + get_rate());
    }
    private ActionTimer create_ore_transformation_action (WorldModel world, Map<String, ArrayList<PImage>> i_store)
    {
        ActionTimer [] action = {null};
        action[0] = (long current_ticks) ->
        {
            remove_pending_actions(action[0]);

            ArrayList<Point> tiles = new ArrayList<>();
            OreBlob blob = new OreBlob(get_name() + " -- blob", get_position(), i_store.get("blob"), get_rate() / BLOB_RATE_SCALE, 1);
            blob.schedule_blob(world, current_ticks, i_store);
            world.remove_entity(this);
            world.add_entity(blob);
            tiles.add(blob.get_position());
            return tiles;
        };
    return action[0];
    }
}
