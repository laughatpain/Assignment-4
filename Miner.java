
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import processing.core.PImage;
/**
 * Created by James on 5/3/2015.
 */
public abstract class Miner
    extends MovingRate
{
    protected int resource_limit;
    protected int resource_count;

    public Miner (String name, Point position, ArrayList<PImage> imgs, int animation_rate, int rate,
                  int resource_limit)
    {
        super (name, position, imgs, animation_rate, rate);
        this.resource_limit = resource_limit;
        this.resource_count = 0;
    }
    public void set_resource_count (int n)
    {
        resource_count = n;
    }
    public int get_resource_count ()
    {
        return resource_count;
    }
    public int get_resource_limit()
    {
        return resource_limit;
    }
    public Miner try_transform_miner (WorldModel world, MinerActions createMiner)
    {
        Miner new_entity = createMiner.run();
        if (this != new_entity)
        {
            world.remove_entity_at(get_position());
            world.add_entity(new_entity);
        }
        return new_entity;
    }
    public CompatableEnt miner_to_ore (WorldModel world, Ore ore)
    {
        Point entity_pt = get_position();
        if (ore==null)
        {
            return new CompatableEnt(entity_pt, false);
        }
        Point ore_pt = ore.get_position();
        if (entity_pt.adjacent_to(ore_pt))
        {
            set_resource_count(1 + get_resource_count());
            world.remove_entity(ore);
            return new CompatableEnt(ore_pt, true);
        }
        else
        {
            Point new_pt = next_position (world, entity_pt, ore_pt);
            world.remove_entity(this);
            return new CompatableEnt(world.move_entity(this, new_pt).get(1), false);
        }
    }
    public CompatableEnt miner_to_smith(WorldModel world, Blacksmith smith) {
        Point entity_pt = get_position();
        if (smith == null) {
            return new CompatableEnt (entity_pt, false);
        }
        Point smith_pt = smith.get_position();
        if (entity_pt.adjacent_to(smith_pt))
        {
            smith.set_resource_count(smith.get_resource_count() + get_resource_count());
            set_resource_count(0);
            return new CompatableEnt(null, true);
        }
        else
        {
            Point new_pt = next_position(world, entity_pt, smith_pt);
            world.move_entity(this, new_pt);
            return new CompatableEnt(world.move_entity(this, new_pt).get(1), false);
        }
    }

    @Override
    public void schedule_entity (WorldModel world, Map<String, ArrayList<PImage>> i_store)
    {
        schedule_miner (world, System.currentTimeMillis(), i_store);
    }
    public void schedule_miner (WorldModel world, long ticks, Map<String, ArrayList<PImage>> i_store)
    {
        schedule_action(create_miner_action(world, i_store), ticks = get_rate());
    }
    public abstract  ActionTimer create_miner_action (WorldModel world, Map<String, ArrayList<PImage>> i_store);

    public static Point next_position(WorldModel world, Point entity_pt, Point dest_pt) {
        int horiz = sign(dest_pt.xCoor() - entity_pt.xCoor());
        Point new_pt = new Point(entity_pt.xCoor() + horiz, entity_pt.yCoor());

        if (horiz == 0 || world.is_occupied(new_pt)) {
            int vert = sign(dest_pt.yCoor() - entity_pt.yCoor());
            new_pt = new Point(entity_pt.xCoor(), entity_pt.yCoor() + vert);

            if (vert == 0 || world.is_occupied(new_pt)) {
                new_pt = entity_pt;
            }
        }

        return  new_pt;
    }

}
