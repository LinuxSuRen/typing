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
import javax.swing.border.CompoundBorder;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class GuessingGameUI extends JPanel implements KeyFire<String> {
    private JPanel vocabularyPanel = new JPanel();
    private JPanel meaningPanel = new JPanel();
    private JPanel examplePanel = new JPanel();
    private List<JLabel> targets = new ArrayList<>();
    private List<Vocabulary> vocabularyList;
    private int hiddenIndex;

    public GuessingGameUI() {
        this.setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));

        this.add(vocabularyPanel);
        this.add(meaningPanel);
        this.add(examplePanel);
    }

    public void loadVocabularyList(List<Vocabulary> vocabularyList) {
        this.vocabularyList = vocabularyList;
        if (vocabularyList == null || vocabularyList.isEmpty()) {
            return;
        }

        Vocabulary vocabulary = vocabularyList.get(0);
        loadVocabulary(vocabulary);
        revalidate();
        repaint();
    }

    public void loadVocabulary(Vocabulary vocabulary) {
        targets.clear();
        meaningPanel.removeAll();
        for (String c : vocabulary.getMeaning().split("")) {
            JLabel label = new JLabel(c);
            label.setName(c);
            label.setOpaque(true);
            meaningPanel.add(label);
            targets.add(label);
        }
        hideRestPart(meaningPanel, 10);

        vocabularyPanel.removeAll();
        for (int i = 0; i < vocabulary.getWord().split("").length; i++) {
            String c = vocabulary.getWord().split("")[i];
            JLabel label = new JLabel(c);
            label.setName(c);
            if (i % 2 != 0) {
                label.setText("_");
                targets.add(label);
            }
            vocabularyPanel.add(label);
        }

        examplePanel.removeAll();
        for (String c : vocabulary.getExample().split("")) {
            examplePanel.add(new JLabel(c));
        }
    }

    private void hideRestPart(JComponent com, int count) {
        Component[] children = com.getComponents();
        if (children.length > count) {
            hiddenIndex = count;
            for (int i = count; i < children.length; i++) {
                children[i].setVisible(false);
            }
        }
    }

    @Override
    public void fire(String key, String data) {
        if (targets.size() == 0) {
            return;
        }

        JLabel label = targets.get(0);
        boolean match = label.getName().toLowerCase().equals(key);
        if (match) {
            label.setBackground(Color.GREEN);
            label.setText(key);
            showNextLetter();
            targets.remove(0);
        }

        trimNonLetter();
    }

    private void trimNonLetter() {
        if (targets.size() == 0) {
            return;
        }
        JLabel label = targets.get(0);
        char c = label.getName().toLowerCase().charAt(0);
        if (c < 'a' || c > 'z') {
            showNextLetter();
            targets.remove(0);

            trimNonLetter();
        };
    }

    private void showNextLetter() {
        if (targets.size() > hiddenIndex) {
            targets.get(hiddenIndex).setVisible(true);
        }
    }
}
