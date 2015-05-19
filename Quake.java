
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import processing.core.PImage;
/**
 * Created by James on 5/3/2015.
 */
public class Quake
    extends Moving
{
    private static final long QUAKE_DURATION = 1100;
    public Quake (String name, Point position, ArrayList<PImage> imgs, int animation_rate)
    {
        super(name, position, imgs, animation_rate);
    }
    @Override
    public void schedule_entity (WorldModel world, Map<String, ArrayList <PImage>> i_store)
    {
        schedule_quake(world, System.currentTimeMillis());
    }
    private void schedule_quake(WorldModel world, long ticks) {
        schedule_action(create_entity_death_action(world), ticks + QUAKE_DURATION);
    }
    private ActionTimer create_entity_death_action(WorldModel world) {

        ActionTimer[] action = {null};

        action[0] = (long current_ticks) -> {

            remove_pending_actions(action[0]);
            ArrayList<Point> tiles = new ArrayList<>();
            Point pt = get_position();
            world.remove_entity(this);
            tiles.add(pt);
            return tiles;
        };

        return action[0];
    }
}
