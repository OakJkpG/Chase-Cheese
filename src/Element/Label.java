package Element;

import javax.swing.JLabel;
import java.awt.Font;

public class Label extends JLabel {
    private static final long serialVersionUID = 1L;

    public Label(String title, int size, int x, int y, int w, int h) {
        super(title);
        setFont(Element.getFont(size));
        setBounds(x, y, w, h);
    }
}
