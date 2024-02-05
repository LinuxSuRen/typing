package typing.linuxsuren.github.io;

import javax.swing.*;
import java.awt.*;

public class Startup {
    public static void main(String[] args) {
        JFrame frame = new JFrame("Typing");
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        TextBoard textBoard = new TextBoard("hello");
        Keyboard keyboard = new Keyboard();
        keyboard.addKeyListener(textBoard);

        JPanel panel = new JPanel();
        panel.setLayout(new BorderLayout());
        panel.add(textBoard, BorderLayout.CENTER);
        panel.add(keyboard,BorderLayout.SOUTH);

        frame.getContentPane().add(panel);
        Toolkit.getDefaultToolkit().addAWTEventListener(keyboard, AWTEvent.KEY_EVENT_MASK);

        frame.setSize(1300, 600);
        frame.setVisible(true);
    }
}
