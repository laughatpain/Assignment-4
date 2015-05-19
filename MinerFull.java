
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import processing.core.PImage;
/**
 * Created by James on 5/3/2015.
 */
public class MinerFull
    extends Miner
{
    public MinerFull (String name, Point position, ArrayList<PImage> imgs, int animation_rate, int rate,
                      int resource_limit)
    {
        super (name, position, imgs, animation_rate, rate, resource_limit);
    }
    @Override
    public ActionTimer create_miner_action (WorldModel world, Map<String, ArrayList<PImage>> i_store)
    {
        return create_miner_full_action(world, i_store);
    }
    private ActionTimer create_miner_full_action (WorldModel world,Map<String, ArrayList<PImage>> i_store)
    {
        ActionTimer[] action = {null};
        action [0] = (long current_ticks) ->
        {
            remove_pending_actions(action[0]);
            Point entity_pt = get_position();
            Blacksmith smith = (Blacksmith) world.find_nearest(entity_pt, Blacksmith.class);
            CompatableEnt compatable = miner_to_smith(world, smith);
            ArrayList<Point> tiles = new ArrayList<>();
            tiles.add(compatable.getPoint());
            Miner new_entity = this;
            if (compatable.isFound())
            {
                new_entity = try_transform_miner(world, try_transform_miner_full(world));
            }
            new_entity.schedule_action(new_entity.create_miner_action(world, i_store), current_ticks + new_entity.get_rate());
            return tiles;
        };
        return action [0];
    }
    public MinerActions try_transform_miner_full(WorldModel world) {

        MinerActions newMiner = () -> {
            MinerNotFull new_entity = new MinerNotFull(get_name(), get_position(), get_images(),
                    get_rate(), this.get_resource_limit(), 2);

            return new_entity;
        };

        return newMiner;
    }
}
