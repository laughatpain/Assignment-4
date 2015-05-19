import processing.core.PImage;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;


/**
 * Created by James on 5/3/2015.
 */
public abstract class Entity
    extends WorldObject
{
    protected Point position;
    protected ArrayList<Actions> pending_actions;

    public Entity (String name, ArrayList<PImage> imgs, Point position)
    {
        super(name, imgs);
        this.position = position;
        this.pending_actions = new ArrayList<Actions>();
    }

    public Point get_position ()
    {
        return position;
    }
    public Point set_position (Point point)
    {
        return position = point;
    }
    public void remove_pending_actions (ActionTimer actionList)
    {
        for (Actions action : pending_actions)
        {
            if (action.getAction() == actionList)
            {
                pending_actions.remove(action);
                break;
            }
        }
    }
    public abstract void schedule_entity (WorldModel world, Map<String, ArrayList<PImage>> i_store);
    public void schedule_action(ActionTimer actions, long time) {
        Actions action = new Actions (actions, time);
        add_pending_actions(action);
    }
    public void add_pending_actions ( Actions action)
    {
        pending_actions.add(action);
    }
    public ArrayList<Actions> get_pending_actions ()
    {
        return pending_actions;
    }
    public void clear_pending_actions (WorldModel world)
    {
       pending_actions = new ArrayList<>();
    }
    public boolean isTime (long current_time)
    {
        return (get_pending_actions().size() !=0 && (current_time >= get_pending_actions().get(0).get_Run_time()));
    }
    public static int sign(int x)
    {
        if (x < 0) {
            return -1;
        } else if (x > 0) {
            return 1;
        } else {
            return 0;
        }
    }
}

