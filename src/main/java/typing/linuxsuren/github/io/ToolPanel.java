package typing.linuxsuren.github.io;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ToolPanel extends JPanel {
    private List<KeyFire> keyFires = new ArrayList<>();
    private Map<String, ToolCode> butMap = new HashMap<>();

    public ToolPanel() {
        this.setLayout(new FlowLayout(FlowLayout.LEFT));

        butMap.put("Home", ToolCode.Home);
        butMap.put("Refresh", ToolCode.Refresh);

        for (String key : butMap.keySet()) {
            JButton button = new JButton(key);
            button.setFocusable(false);
            button.addActionListener(new AbstractAction() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    if (e.getSource() instanceof JButton) {
                        JButton but = (JButton)e.getSource();

                        for (KeyFire key : keyFires) {
                            key.fire(but.getText(), butMap.get(but.getText()));
                        }
                    }
                }
            });
            this.add(button);
        }
    }

    public void addKeyFire(KeyFire key) {
        keyFires.add(key);
    }
}

enum ToolCode {
    Home, Refresh
}
