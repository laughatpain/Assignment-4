import processing.core.PApplet;
import processing.core.PConstants;
import processing.core.PImage;

import java.awt.*;
import java.io.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class Image_Store
{
    public static String DEFAULT_IMAGE_NAME = "background_default";
    public static int DEFAULT_IMAGE_COLOR = 0xfffff;
    private String src_path;
    private String image_list_file;
    PApplet original;
    private Map<String, ArrayList<PImage>> images = new HashMap();

    public Image_Store(Main main, String src_path, String image_list_file)
    {
        this.original = main;
        this.src_path = src_path;
        this.image_list_file = image_list_file;
    }
    public String get_default_img_name ()
    {
        return DEFAULT_IMAGE_NAME;
    }
    public ArrayList<PImage> get_images(String key)
    {
        if(images.containsKey(key));
        {
            return images.get(key);
        }
    }
    public static ArrayList<PImage> get_image(Map<String, ArrayList<PImage>> images, String key) {
        if (images.containsKey(key)) {
            return images.get(key);
        } else {
            return images.get(DEFAULT_IMAGE_NAME);
        }
    }
    private PImage create_default_image (int tile_width, int tile_height)
    {
        return original.loadImage(src_path + "none.bmp");
    }
    public void load_images(int tile_width, int tile_height) throws IOException
    {

        File file = new File(src_path + image_list_file);
        BufferedReader br = new BufferedReader(new FileReader(file));

        String line = null;

        while ((line = br.readLine()) != null) {
            process_image_line(line);
        }

        br.close();

        if (!images.containsKey(DEFAULT_IMAGE_NAME))
        {
            PImage default_img = create_default_image(tile_width, tile_height);
            ArrayList<PImage> default_list = new ArrayList<PImage>();
            default_list.add(default_img);
            images.put(DEFAULT_IMAGE_NAME, default_list);
        }
    }
    private ArrayList<PImage> get_images_internal (String key)
    {
        if (images.containsKey(key)){
            return images.get(key);
        }
        else
        {
            return new ArrayList<PImage>();
        }
    }
    private PImage set_Alpha (PImage img, int baseColor, int alpha)
    {
        baseColor &= DEFAULT_IMAGE_COLOR;
        img.format = PConstants.ARGB;
        img.loadPixels();
        for (int i = 0; i < img.pixels.length; i++)
        {
            if ((img.pixels[i] & DEFAULT_IMAGE_COLOR) == baseColor)
            {
                img.pixels[i] = (alpha << 24) | DEFAULT_IMAGE_COLOR;
            }
        }
        img.updatePixels();
        return img;
    }

    private void process_image_line(String line)
    {
        String[] attrs = line.split(" ");

        if (attrs.length >= 2) {
            String key = attrs[0];

            PImage img = set_Alpha(original.loadImage(src_path + attrs[1]), original.color(252, 252, 252), 0);
            img = set_Alpha(img, original.color(201, 26, 26), 0);
            if (key.compareTo("blob") == 0 || key.compareTo("quake") == 0) {
                img = set_Alpha(img, original.color(255, 255, 255), 0);
            }

            if (img != null) {
                ArrayList<PImage> imgs = get_images_internal(key);
                imgs.add(img);
                images.put(key, imgs);

                if (attrs.length == 6) {
                    String r = attrs[2];
                    String g = attrs[3];
                    String b = attrs[4];
                    String a = attrs[5];
                }
            }
        }
    }
}