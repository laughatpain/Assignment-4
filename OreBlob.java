import processing.core.PImage;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Created by James on 5/3/2015.
 */
public  class OreBlob
    extends MovingRate
{
    public OreBlob (String name, Point position, ArrayList<PImage> imgs, int rate, int animation_rate)
    {
        super (name, position, imgs, rate, animation_rate);
    }
    public String entity_string()
    {
        return ("obstacle" + name + position.xCoor() + position.yCoor());
    }
    @Override
    public void schedule_entity (WorldModel world, Map<String, ArrayList <PImage>> i_store)
    {
        schedule_blob(world, System.currentTimeMillis(), i_store);
    }
    public void schedule_blob (WorldModel world, long ticks, Map<String, ArrayList<PImage>> i_store)
    {
        schedule_action(create_ore_blob_action(world, i_store), ticks + get_rate());
    }
    private ActionTimer create_ore_blob_action (WorldModel world, Map<String, ArrayList<PImage>> i_store)
    {
        ActionTimer[] action = {null};

        action[0] = (long current_ticks) -> {

            remove_pending_actions(action[0]);
            ArrayList<Point> tiles = new ArrayList<>();
            Point entity_pt = get_position();
            Vein vein = (Vein) world.find_nearest(entity_pt, Vein.class);
            CompatableEnt search = blob_to_vein(world, vein);

            long next_time = current_ticks + this.get_rate();
            if (search.isFound()) {
                Quake quake = new Quake("quake", search.getPoint(), i_store.get("quake"), 100);
                quake.schedule_entity(world, i_store);
                world.add_entity(quake);
                next_time = current_ticks + this.get_rate() * 2;
            }
            this.schedule_action(this.create_ore_blob_action(world, i_store), next_time);
            return tiles;
        };
        return action[0];
    }
    private CompatableEnt blob_to_vein (WorldModel world, Vein vein)
    {
        Point entity_pt = get_position();
        if (vein == null)
        {
            return new CompatableEnt(entity_pt, false);
        }
        Point vein_pt = vein.get_position();
        if (entity_pt.adjacent_to(vein_pt))
        {
            world.remove_entity(vein);
            return new CompatableEnt(vein_pt, true);
        }
        else
        {
            Point new_pt = blob_next_position (world, entity_pt, vein_pt);
            WorldObject old_entity = world.get_tile_occupant(new_pt);
            if (old_entity != null && old_entity.getClass() == Ore.class)
            {
                world.remove_entity((Entity) old_entity);
            }
            return new CompatableEnt(world.move_entity(this, new_pt).get(1), false);
        }
    }
    public static Point blob_next_position (WorldModel world, Point entity_pt, Point dest_pt)
    {
        int horiz = sign (dest_pt.xCoor() - entity_pt.xCoor());
        Point new_pt = new Point(entity_pt.xCoor() + horiz, entity_pt.yCoor());
        if(horiz == 0 || (world.is_occupied(new_pt) && world.get_tile_occupant(new_pt).getClass() != Ore.class))
        {
            int vert = sign (dest_pt.yCoor() - entity_pt.yCoor());
            new_pt = new Point(entity_pt.xCoor(), entity_pt.yCoor() + vert);
            if (vert == 0 || (world.is_occupied(new_pt) && world.get_tile_occupant(new_pt).getClass() != Ore.class))
            {
                new_pt = entity_pt;
            }
        }
        return new_pt;
    }
}
