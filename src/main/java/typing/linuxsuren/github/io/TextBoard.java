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
import java.util.ArrayList;
import java.util.List;

public class TextBoard extends JComponent implements KeyFire<String> {
    private List<JLabel> labels = new ArrayList<JLabel>();
    private List<KeyFire> keyFires = new ArrayList<>();
    private int index;

    public TextBoard() {
        this.setLayout(new FlowLayout());
    }

    public void loadText(String text) {
        index = 0;
        this.removeAll();
        labels.clear();
        for (String item : text.split("")) {
            JLabel label = new JLabel(item);
            label.setFont(new Font("",Font.PLAIN, 20));
            label.setOpaque(true);

            this.add(label);
            labels.add(label);
        }
    }

    public void fire(String key, String data) {
        if (index >= labels.size()) {
            return;
        }

        String cmd;
        JLabel label = labels.get(index);
        boolean correct = label.getText().equals(key);
        if (correct) {
            label.setBackground(Color.GREEN);
        } else {
            label.setBackground(Color.PINK);
        }

        if (++index >= labels.size()) {
            cmd = "end";
        } else {
            cmd = label.getText();
        }

        for (KeyFire keyFire : this.keyFires) {
            keyFire.fire(cmd, correct);
        }
    }

    public void addKeyFire(KeyFire keyFire) {
        this.keyFires.add(keyFire);
    }
}
