import org.junit.Test;
import processing.core.PImage;

import java.io.*;
import java.util.ArrayList;
import java.util.Map;

/**
 * Created by James on 5/3/2015.
 */
public class WorldModel
{
    private static final int PROPERTY_KEY = 0;

    private static final String BGND_KEY = "background";
    private static final int BGND_NUM_PROPERTIES = 4;
    private static final int BGND_NAME = 1;
    private static final int BGND_COL = 2;
    private static final int BGND_ROW = 3;

    private static final String MINER_KEY = "miner";
    private static final int MINER_NUM_PROPERTIES = 7;
    private static final int MINER_NAME = 1;
    private static final int MINER_LIMIT = 4;
    private static final int MINER_COL = 2;
    private static final int MINER_ROW = 3;
    private static final int MINER_RATE = 5;
    private static final int MINER_ANIMATION_RATE = 6;

    private static final String OBSTACLE_KEY = "obstacle";
    private static final int OBSTACLE_NUM_PROPERTIES = 4;
    private static final int OBSTACLE_NAME = 1;
    private static final int OBSTACLE_COL = 2;
    private static final int OBSTACLE_ROW = 3;

    private static final String ORE_KEY = "ore";
    private static final int ORE_NUM_PROPERTIES = 5;
    private static final int ORE_NAME = 1;
    private static final int ORE_COL = 2;
    private static final int  ORE_ROW = 3;

    private static final String SMITH_KEY = "blacksmith";
    private static final int SMITH_NUM_PROPERTIES = 7;
    private static final int SMITH_NAME = 1;
    private static final int SMITH_COL = 2;
    private static final int SMITH_ROW = 3;
    private static final int SMITH_LIMIT = 4;
    private static final int SMITH_RATE = 5;

    private static final String VEIN_KEY = "vein";
    private static final int VEIN_NUM_PROPERTIES = 6;
    private static final int VEIN_NAME = 1;
    private static final int VEIN_RATE = 4;
    private static final int VEIN_COL = 2;
    private static final int VEIN_ROW = 3;

    protected int num_rows;
    protected int num_cols;
    protected Background [][] background;
    protected Grid occupancy;
    protected ArrayList<Entity> entities;
    protected OrderedList action_queue;
    private String src_path;
    private String world_file;

    public WorldModel (int num_rows, int num_cols, Background background, String src_path, String world_file)
    {
        this.num_rows = num_rows;
        this.num_cols = num_cols;
        this.background = new Background[num_rows][num_cols];
        this.occupancy = new Grid(num_cols, num_rows, null);
        OrderedList ordered_list = new OrderedList();
        this.action_queue = ordered_list;
        this.entities = new ArrayList<>();
        this.src_path = src_path;
        this.world_file = world_file;

    }
    public boolean within_bounds (Point pt)
    {
        return ((pt.xCoor() >= 0) && (pt.xCoor() < num_cols) && (pt.yCoor() >= 0) &&
                (pt.yCoor() < num_rows));
    }
    public boolean is_occupied (Point pt)
    {
        return (within_bounds(pt) &&
                occupancy.get_cell(pt) != null);
    }
    public WorldObject nearest_entity (ArrayList<Entity> entities, ArrayList<Double> distance)
    {
        if (entities.size() == 0)
        {
            return null;
        }
        double pair = distance.get(0);
        int idx = 0;
        for (int i = 0; i < entities.size(); i++)
        {
            if (distance.get(i) < pair)
            {
                pair = distance.get (i);
                idx = i;
            }
        }
        return entities.get(idx);
    }

    public WorldObject find_nearest (Point pt, Class type)
    {
        ArrayList<Entity> oftype = new ArrayList<Entity>();
        ArrayList<Double> distance = new ArrayList<Double>();
        WorldObject nearest = entities.get(0);
        for (Entity ent : entities)
        {
            if (ent instanceof Entity)
            {
                oftype.add(ent);
                distance.add(distance_sq(pt, ent.get_position()));
            }
        }
        return nearest_entity(oftype, distance);
    }
    public void add_entity(Entity entity)
    {
        Point pt = entity.get_position();
        if (within_bounds(pt));
        {
            Entity old_entity = (Entity) occupancy.get_cell(pt);
            if (old_entity != null)
            {//fix needed
                old_entity.clear_pending_actions(this);
            }
            occupancy.set_cell(pt, entity);
            entities.add(entity);
        }
    }
    public ArrayList<Point> move_entity (Entity entity, Point pt)
    {
        ArrayList<Point> tiles= new ArrayList<Point>();
        if (within_bounds(pt));
        {
            Point old_pt = entity.get_position ();
            occupancy.set_cell(old_pt, null);
            tiles.add(old_pt);
            occupancy.set_cell (pt, entity);
            tiles.add(pt);
            entity.set_position(pt);
        }
        return tiles;
    }
    public void remove_entity (Entity entity)
    {
        remove_entity_at(entity.get_position());
    }
    public void remove_entity_at (Point pt)
    {
        if ((within_bounds(pt) && (occupancy.get_cell(pt)) != null))
        {
            Entity entity = (Entity) occupancy.get_cell(pt);
            entity.set_position(new Point(-1, -1));
            entities.remove(entity);
            occupancy.set_cell(pt, null);
        }
    }
    public Background get_background (Point pt)
    {
        if  (within_bounds(pt))
        {
            return background[pt.yCoor()][pt.xCoor()];
        }
        else
        {
            return null;
        }
    }
    public PImage get_background_image (Point pt)
    {
        if (within_bounds(pt))
        {
            return background[pt.yCoor()][pt.xCoor()].get_images().get(0);
        }
        else
        {return null;}
    }
    public void set_background (Point pt, Background bgnd)
    {
        if (within_bounds(pt))
        {
          background[pt.yCoor()][pt.xCoor()] = bgnd;
        }
    }
    public WorldObject get_tile_occupant (Point pt)
    {
        if (this.within_bounds(pt)) {
            return this.occupancy.get_cell(pt);
        } else {
            return null;
        }
    }
    public int get_Num_cols ()
    {
        return num_cols;
    }
    public int get_Num_rows ()
    {
        return num_rows;
    }

    public ArrayList<Entity> get_entities()
    {
        return entities;
    }

    public double distance_sq (Point p1, Point p2)
    {
        return ((p1.xCoor() - p2.xCoor()*(p1.xCoor() - p2.xCoor())) +
                ((p1.yCoor() - p2.yCoor())*(p1.yCoor() - p2.yCoor())));
    }
    public void load_world(Map<String, ArrayList<PImage>> i_store, boolean run) throws IOException {
        File file = new File(this.src_path + this.world_file);
        BufferedReader br = new BufferedReader(new FileReader(file));

        String line = null;

        while ((line = br.readLine()) != null) {
            String[] properties = line.split(" ");
            if (properties.length != 0) {
                if (properties[PROPERTY_KEY].compareTo(BGND_KEY) == 0) {
                    add_background_from_file(properties, i_store);
                } else {
                    add_entity_from_file(properties, i_store, run);
                }
            }
        }

        br.close();
    }

    public void add_entity_from_file (String[] properties, Map<String, ArrayList<PImage>> i_store, boolean run)
    {
        WorldObject new_entity = create_from_properties(properties, i_store);
        if (new_entity !=null)
        {
            this.add_entity((Moving) new_entity);
            if(run)
            {
                try
                {
                    Moving entity = (Moving) new_entity;
                    entity.schedule_entity(this, i_store);
                }
                catch (ClassCastException e)
                {
                }
            }
        }
    }

    public void add_background_from_file(String[] properties, Map<String, ArrayList<PImage>> i_store)
    {
        if (properties.length >= BGND_NUM_PROPERTIES)
        {
            Point pt = new Point(Integer.parseInt(properties[BGND_COL]),
                    Integer.parseInt(properties[BGND_ROW]));
            String name = properties [BGND_NAME];
            set_background(pt, new Background(name, Image_Store.get_image(i_store, name)));
        }
    }
    private WorldObject create_from_properties(String[] properties, Map<String, ArrayList<PImage>> i_store) {
        String key = properties[PROPERTY_KEY];
        if (properties.length != 0) {
            if (key.compareTo(MINER_KEY) == 0) {
                return create_miner(properties, i_store);
            } else if (key.compareTo(VEIN_KEY) == 0) {
                return create_vein(properties, i_store);
            } else if (key.compareTo(ORE_KEY) == 0) {
                return create_ore(properties, i_store);
            } else if (key.compareTo(SMITH_KEY) == 0) {
                return create_smith(properties, i_store);
            } else if (key.compareTo(OBSTACLE_KEY) == 0) {
                return create_obstacle(properties, i_store);
            } else {
                return null;
            }
        } else {
            return null;
        }
    }
    private WorldObject create_miner(String[] properties, Map<String, ArrayList<PImage>> i_store) {
        if (properties.length == MINER_NUM_PROPERTIES) {
            Miner miner = new MinerNotFull(properties[MINER_NAME],
                    new Point(Integer.parseInt(properties[MINER_COL]), Integer.parseInt(properties[MINER_ROW])),
                    Image_Store.get_image(i_store, MINER_KEY),
                    Integer.parseInt(properties[MINER_ANIMATION_RATE]),
                    Integer.parseInt(properties[MINER_RATE]),
                    Integer.parseInt(properties[MINER_LIMIT]));
            return miner;
        } else {
            return null;
        }
    }
    private WorldObject create_vein(String[] properties, Map<String, ArrayList<PImage>> i_store) {
        if (properties.length == VEIN_NUM_PROPERTIES) {
            Vein vein = new Vein(properties[VEIN_NAME],
                    new Point(Integer.parseInt(properties[VEIN_COL]),
                    Integer.parseInt(properties[VEIN_ROW])),
                    Image_Store.get_image(i_store, VEIN_KEY),
                    Integer.parseInt(properties[VEIN_RATE]),4);
            return vein;
        } else {
            return null;
        }
    }

    private WorldObject create_ore(String[] properties, Map<String,ArrayList<PImage>> i_store) {
        if (properties.length == ORE_NUM_PROPERTIES) {
            Ore ore = new Ore(properties[ORE_NAME],
                    new Point(Integer.parseInt(properties[ORE_COL]), Integer.parseInt(properties[ORE_ROW])),
                    Image_Store.get_image(i_store, ORE_KEY),5000);
            return ore;
        } else {
            return null;
        }
    }

    private WorldObject create_smith(String[] properties, Map<String,ArrayList<PImage>> i_store) {
        if (properties.length == SMITH_NUM_PROPERTIES) {
            Blacksmith smith= new Blacksmith(properties[SMITH_NAME],
                    Image_Store.get_image(i_store, SMITH_KEY),
                    new Point(Integer.parseInt(properties[SMITH_COL]), Integer.parseInt(properties[SMITH_ROW])),
                    Integer.parseInt(properties[SMITH_LIMIT]),
                    Integer.parseInt(properties[SMITH_RATE]),4);
            return smith;
        } else {
            return null;
        }
    }

    private WorldObject create_obstacle(String[] properties, Map<String,ArrayList<PImage>> i_store) {
        if (properties.length == OBSTACLE_NUM_PROPERTIES) {
            Obstacle obstacle= new Obstacle(properties[OBSTACLE_NAME],
                    Image_Store.get_image(i_store, OBSTACLE_KEY),
                    new Point(Integer.parseInt(properties[OBSTACLE_COL]),
                            Integer.parseInt(properties[OBSTACLE_ROW]))) ;
            return obstacle;
        } else {
            return null;
        }
    }
    public static Point find_open_around(WorldModel world, Point pt, int distance) {
        for (int dy = -distance; dy < distance + 1; dy++) {
            for (int dx = -distance; dx < distance + 1; dx++) {
                Point new_pt = new Point(pt.xCoor() + dx, pt.yCoor() + dy);

                if (world.within_bounds(new_pt) && !(world.is_occupied(new_pt))) {
                    return new_pt;
                }
            }
        }
        return null;
    }
}
