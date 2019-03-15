package templates;

import javax.swing.*;
import java.awt.*;

public class Text extends JLabel {
    public Text(String text, int x, int y) {
        super(text);
        this.setSize(50, 20);
        this.setLocation(x, y);
        this.setFont(new Font("Arial", Font.PLAIN, 16));
    }
    public Text(String text, int x, int y, int width) {
        super(text);
        this.setSize(width, 20);
        this.setLocation(x, y);
        this.setFont(new Font("Arial", Font.PLAIN, 16));
    }
}
