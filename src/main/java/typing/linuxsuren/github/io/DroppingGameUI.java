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
import java.util.Random;

public class DroppingGameUI extends JPanel implements KeyFire<String> {
    public DroppingGameUI() {
    }

    private List<Word> targets = new ArrayList<>();

    public void load(String text) {
        String[] items = text.replace(" ", "").split("");
        JPanel panel = this;
        new Thread(() -> {
            for (String c : items) {
                if (panel.isVisible()) {
                    Word word = new Word(c);

                    Random r = new Random();
                    int margin = 30;
                    int result = r.nextInt(panel.getSize().width + margin) - margin;

                    word.setLocation(result, 0);
                    targets.add(word);
                }

                try {
                    Thread.sleep(1600);
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
            }
        }).start();

        // move the targets
        new Thread(() -> {
            while(true) {
                if (panel.isVisible()) {
                    for (int i = 0; i < targets.size(); i++) {
                        Word target = targets.get(i);

                        Point loc = target.getLocation();
                        target.setLocation(loc.x, loc.y + 8);
                        panel.revalidate();
                        panel.repaint();
                    }
                }

                try {
                    Thread.sleep(500);
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
            }
        }).start();
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        g.setFont(new Font("", Font.PLAIN, 30));
        for (int i = 0; i < targets.size(); i++) {
            Word target = targets.get(i);
            g.drawString(target.getText(), target.x, target.y);
        }
    }

    public void pause() {}

    @Override
    public void fire(String key, String data) {
        if (!this.isVisible() || targets.isEmpty()) {
            return;
        }

        if (targets.get(0).getText().equals(key)) {
            targets.remove(0);
        }
    }
}

class Word extends Point {
    private final String text;
    public Word(String text) {
        this.text = text;
    }

    public String getText() {
        return text;
    }
}
