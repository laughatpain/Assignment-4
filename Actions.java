/**
 * Created by James on 5/17/2015.
 */
public class Actions
{
    private long time;
    private ActionTimer actionTimer;

    public Actions(ActionTimer actionTimer, long time)
    {
        this.actionTimer = actionTimer;
        this.time = time;
    }
    public long get_Run_time()
    {
        return time;
    }
    public ActionTimer getAction()
    {
        return actionTimer;
    }
}
