package Element;

import java.awt.Font;
import java.io.File;

public class Element {

    public static Font getFont(int size) {
        try {
            Font font = Font.createFont(Font.TRUETYPE_FONT, new File("resources/font/Mali-Bold.ttf"));
            return font.deriveFont((float) size);
        } catch (Exception e) {
            e.printStackTrace();
            return new Font("SansSerif", Font.PLAIN, size);
        }
    }
}
