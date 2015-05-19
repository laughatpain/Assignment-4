import java.io.IOException;
import java.util.ArrayList;
import processing.core.PApplet;
import processing.core.PImage;
import java.util.Map;

public class Main
        extends PApplet {
    private final boolean RUN_AFTER_LOAD = true;
    private final String SRC_PATH = "C:/Users/James/IdeaProjects/CPE 102/src";
    private final String IMAGE_LIST_FILE = "imagelist";
    private final String WORLD_FILE = "gaia.sav";
    private long next_time;
    private static int ANIMATION_TIME = 100;
    private int WORLD_WIDTH_SCALE = 2;
    private int WORLD_HEIGHT_SCALE = 2;
    private int SCREEN_WIDTH = 640;
    private int SCREEN_HEIGHT = 480;
    private int TILE_WIDTH = 32;
    private int TILE_HEIGHT = 32;
    private WorldModel world;
    private WorldView view;
    private Image_Store i_store = new Image_Store(this, SRC_PATH,IMAGE_LIST_FILE);
    private Map<String, ArrayList<PImage>> map;

    public void setup() {
        size(SCREEN_WIDTH, SCREEN_HEIGHT);

        try {
            i_store.load_images(32, 32);
        } catch (IOException e) {
            e.printStackTrace();
        }

        int num_cols = SCREEN_WIDTH / TILE_WIDTH * WORLD_WIDTH_SCALE;
        int num_rows = SCREEN_HEIGHT / TILE_HEIGHT * WORLD_HEIGHT_SCALE;

        Background default_background = create_default_background((ArrayList<PImage>) i_store.get_images(i_store.get_default_img_name()));

        world = new WorldModel(num_rows, num_cols, default_background, SRC_PATH, WORLD_FILE);
        view = new WorldView(this, world, SCREEN_WIDTH / TILE_WIDTH, SCREEN_HEIGHT / TILE_HEIGHT, TILE_WIDTH, TILE_HEIGHT);

        try {
            world.load_world(map, RUN_AFTER_LOAD);
        } catch (IOException e) {
            e.printStackTrace();
        }

        view.update_view(0, 0);

        next_time = System.currentTimeMillis() + ANIMATION_TIME;
    }
    private Background create_default_background (ArrayList<PImage> bgnd)
    {
        return new Background(i_store.get_default_img_name(), bgnd);
    }

    public void draw()
    {
        long time = System.currentTimeMillis();
        if( time >= next_time)
        {
            next_images();
            next_time = time + ANIMATION_TIME;
            make_moves();
        }
    }
    private void make_moves ()
    {
        for (int x = 0; x < world.get_Num_cols(); x++)
        {
            for (int y = 0; y < world.get_Num_rows(); y++)
            {
                Point location = new Point(x,y);
                if (world.is_occupied(location))
                {
                    try
                    {
                    Entity entity = (Entity) world.get_tile_occupant(location);
                        if (entity.isTime(System.currentTimeMillis()))
                        {
                            Actions action = entity.get_pending_actions().get(0);
                            action.getAction().run(System.currentTimeMillis());
                            break;
                        }
                    }
                    catch (ClassCastException e)
                    {
                    }
                }
            }
        }
    }
    public void keyPressed()
    {
        int x = 0;
        int y = 0;
        if (key == CODED) {
            switch (keyCode)
            {
                case RIGHT:
                    x = 1;
                    break;
                case LEFT:
                    x = -1;
                    break;
                case UP:
                    y = -1;
                    break;
                case DOWN:
                    y = 1;
                    break;
            }
            view.update_view(x, y);
        }
    }
    private void next_images()
    {
        for (WorldObject entity : world.get_entities())
        {
            entity.get_image();
        }
    }
    public static void main(String args[]) {
        PApplet.main(new String[] { "MyProcessingSketch" });
    }

}