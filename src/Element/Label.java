package Element;

import javax.swing.JLabel;

public class Label extends JLabel {

    public Label(String title, int size, int x, int y, int w, int h) {
        super(title);
        setFont(Element.getFont(size));
        setBounds(x, y, w, h);
    }
}
