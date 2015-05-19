import processing.core.PImage;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Created by James on 5/3/2015.
 */
public class MinerNotFull
    extends Miner
{
    public MinerNotFull(String name, Point position, ArrayList<PImage> imgs, int animation_rate, int rate,
                        int resource_limit)
    {
        super(name, position, imgs, animation_rate, rate, resource_limit);
    }
    public String entity_string ()
    {
        return ("miner" + name + position.xCoor() + position.yCoor() + resource_limit + rate
        + animation_rate);
    }

    public MinerActions try_transform_miner_not_full (WorldModel world)
    {
        MinerActions createMiner = () ->
        {
            if (resource_count < resource_limit)
            {
                return this;
            }
            else
            {
                MinerFull new_entity = new MinerFull(get_name(), get_position(), get_images(),
                        get_animation_rate(), get_rate(), get_resource_limit());
                return new_entity;
            }
        };
        return createMiner;
    }
    public ActionTimer create_miner_action (WorldModel world, Map<String, ArrayList<PImage>> i_store)
    {
        return  create_miner_not_full_action(world, i_store);
    }
    private ActionTimer create_miner_not_full_action (WorldModel world, Map<String, ArrayList<PImage>> i_store)
    {
        ActionTimer[] action = {null};

        action[0] = (long current_ticks) ->
        {
            remove_pending_actions(action[0]);
            Point entity_pt = get_position();
            Ore ore = (Ore) world.find_nearest(entity_pt, Ore.class);
            CompatableEnt search = this.miner_to_ore(world, ore);
            ArrayList<Point> tiles = new ArrayList<>();
            tiles.add(search.getPoint());
            Miner new_entity = this;

            if (search.isFound()) {
                new_entity = this.try_transform_miner(world, try_transform_miner_not_full(world));
            }

            new_entity.schedule_action(new_entity.create_miner_action(world, i_store), current_ticks + new_entity.get_rate());

            return tiles;
        };

        return action[0];
    }
}


