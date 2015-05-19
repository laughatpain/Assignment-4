import org.w3c.dom.css.Rect;
import processing.core.PApplet;
import processing.core.PGraphics;
import processing.core.PImage;

import java.util.ArrayList;


/**
 * Created by James on 5/17/2015.
 */
public class WorldView
{
    private WorldModel world;
    private Rectangle viewport;
    private int tile_width;
    private int tile_height;
    private int num_rows;
    private int num_cols;
    private PApplet origin;

    public WorldView (PApplet origin, WorldModel world, int view_cols, int view_rows, int tile_width, int tile_height )
    {
        this.origin = origin;
        this.viewport = new Rectangle(0, 0, view_cols, view_rows);
        this.world = world;
        this.tile_height = tile_height;
        this.tile_width = tile_width;
        this.num_cols = world.get_Num_cols();
        this.num_rows = world.get_Num_rows();
    }

    private void draw_background ()
    {
        for (int y = 0; y<this.viewport.get_height(); y++)
        {
            for (int x = 0; x < this.viewport.get_width(); x++)
            {
                Point pt = viewport_to_world(viewport, new Point(x, y));
                PImage img = world.get_background_image(pt);
                origin.image(img, x * this.tile_width, y * tile_height);
            }
        }
    }
    private void draw_entities ()
    {
        for (Entity entity : world.get_entities())
        {
            if (viewport.collision(entity.get_position().xCoor(), entity.get_position().yCoor()))
            {
                Point v_pt = world_to_viewport(viewport, entity.get_position());
                origin.image(entity.get_current_image(), v_pt.xCoor() * tile_width, v_pt.yCoor() * tile_height);
            }
        }
    }
    private void draw_viewport ()
    {
        draw_background();
        draw_entities ();
    }
    public void update_view(int dx, int dy)
    {
        this.viewport = create_shifted_viewport(viewport, dx, dy, num_rows, num_cols);
    }
    public void update_view_tiles(ArrayList<Point> tiles) {

        for (Point tile : tiles) {
            if (this.viewport.collision(tile.xCoor(), tile.yCoor())) {
                Point v_pt = world_to_viewport(this.viewport, tile);
                PImage img = get_tile_image(v_pt);
                this.update_tile(v_pt, img);
            }
        }
    }
    private Rectangle update_tile(Point view_tile_pt, PImage surface) {
        int abs_x = view_tile_pt.xCoor() * this.tile_width;
        int abs_y = view_tile_pt.yCoor() * this.tile_height;

        origin.image(surface, abs_x, abs_y);

        return new Rectangle(abs_x, abs_y, tile_width, tile_height);
    }

    private PImage get_tile_image(Point view_tile_pt) {
        Point pt = viewport_to_world(this.viewport, view_tile_pt);
        PImage bgnd = world.get_background_image(pt);
        WorldObject occupant =world.get_tile_occupant(pt);
        if (occupant != null) {
            PGraphics temp = origin.createGraphics(this.tile_width, this.tile_height);
            temp.image(bgnd, 0, 0);
            temp.image(occupant.get_current_image(), 0, 0);
            return temp.get();
        } else {
            return bgnd;
        }
    }
    public static Point viewport_to_world (Rectangle viewport, Point pt)
    {
        return new Point(pt.xCoor() + viewport.get_left(), pt.yCoor() - viewport.get_top());
    }
    public static Point world_to_viewport (Rectangle viewport, Point pt)
    {
        return new Point (pt.xCoor() - viewport.get_left(), pt.yCoor() - viewport.get_top());
    }
    public static int clamp (int v, int low, int high)
    {
        return Math.min(high, Math.max(v, low));
    }
    public static Rectangle create_shifted_viewport(Rectangle viewport, int dx, int dy, int num_rows, int num_cols)
    {
        int new_x = clamp(viewport.get_left() + dx, 0, num_cols - viewport.get_width());
        int new_y = clamp(viewport.get_top() + dy, 0 , num_rows - viewport.get_height());

        return new Rectangle (new_x, new_y, viewport.get_width(), viewport.get_height());
    }
}