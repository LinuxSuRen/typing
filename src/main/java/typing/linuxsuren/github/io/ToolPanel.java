/*
Copyright 2024 LinuxSuRen.

Licensed under the Apache License, Version 2.0 (the "License");
you may not use this file except in compliance with the License.
You may obtain a copy of the License at

	http://www.apache.org/licenses/LICENSE-2.0

Unless required by applicable law or agreed to in writing, software
distributed under the License is distributed on an "AS IS" BASIS,
WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
See the License for the specific language governing permissions and
limitations under the License.
*/

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
    private Map<String, JButton> jButMap = new HashMap<>();

    public ToolPanel() {
        this.setLayout(new FlowLayout(FlowLayout.LEFT));

        butMap.put("Home", ToolCode.Home);
        butMap.put("Refresh", ToolCode.Refresh);
        butMap.put("Logout", ToolCode.Logout);
        butMap.put(ToolCode.CreateUser.name(), ToolCode.CreateUser);

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
            jButMap.put(key, button);
            this.add(button);
        }

        refresh();
    }

    public void refresh() {
        User currentUser = UserService.getInstance().getCurrentUser();
        for (String key : butMap.keySet()) {
            if (butMap.get(key) == ToolCode.CreateUser) {
                jButMap.get(key).setVisible(currentUser != null && currentUser.isParent());
            } else {
                jButMap.get(key).setVisible(currentUser != null);
            }
        }
    }

    public void addKeyFire(KeyFire key) {
        keyFires.add(key);
    }
}

enum ToolCode {
    Home, Refresh, Login, Logout, CreateUser
}
