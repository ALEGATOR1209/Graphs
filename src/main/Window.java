package main;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

class Window extends JFrame {
    private int width = 500, height = 500;
    private int x = 0, y = 0;

    Window(int x, int y, int width, int height, String title) {
        super(title);
        this.width = width;
        this.height = height;
        this.x = x;
        this.y = y;
        this.init();
    }
    Window(int width, int height, String title) {
        super(title);
        this.width = width;
        this.height = height;
        this.init();
    }
    Window(String title) {
        super(title);
        this.init();
    }

    private void init() {
        this.setBounds(this.x, this.y, this.width, this.height);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    }
}
