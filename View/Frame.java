package View;

import javax.swing.JFrame;

public class Frame extends JFrame {

    Panel panel;

    public Frame(Panel panel) {

        add(panel);
        pack();
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setResizable(false);
        setTitle("Sorter");
        setVisible(true);
    }
}