package cn.longhaiyan.attachment.utils;

import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by chenxb on 17-4-27.
 */
public class ExifUtil {

    public static Map<String, Integer> getPicSize(File file) {
        Image image = null;
        Map<String, Integer> map = new HashMap<>();
        try {
            image = javax.imageio.ImageIO.read(file);
            map.put("width", image.getWidth(null));
            map.put("height", image.getHeight(null));
        } catch (IOException e) {
            e.printStackTrace();
        }
        return map;
    }
}
