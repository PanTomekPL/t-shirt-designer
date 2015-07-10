package pl.net.kabala.confitura2015;

import java.awt.*;
import java.text.SimpleDateFormat;
import java.util.Date;

public class Utils {
    public static String makeImageFilename() {
        SimpleDateFormat df = new SimpleDateFormat("yyyyMMddHHmmssSSS");
        String timestamp = df.format(new Date());
        return "koszulka-" + timestamp + ".png";
    }

    public static Font getDefaultFont() {
        GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
        Font[] fonts = ge.getAllFonts();
        if (fonts.length > 0) {
            return fonts[0];
        }
        return null;
    }
}
