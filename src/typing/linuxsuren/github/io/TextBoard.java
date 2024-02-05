package typing.linuxsuren.github.io;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class TextBoard extends JComponent implements KeyFire {
    private List<JLabel> labels = new ArrayList<JLabel>();

    public TextBoard(String text) {
        this.setLayout(new FlowLayout());
        for (String item : text.split("")) {
            JLabel label = new JLabel(item);
            label.setBackground(Color.PINK);
            this.add(label);
            labels.add(label);
        }
    }

    public void fire(String key) {
        System.out.println(key);
        for (JLabel label: labels) {
            if (label.getText().equals(key)) {
                label.setBackground(Color.GREEN);
            }
        }
    }
}
