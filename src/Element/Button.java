package Element;

import javax.swing.JButton;
import java.awt.Color;

public class Button extends JButton {

    public Button(String title, int size, int x, int y, int width, int height) {
        super(title);
        setBackground(new Color(2, 117, 216));
        setForeground(Color.white);
        setFont(Element.getFont(size));
        setBounds(x, y, width, height);
    }
}
