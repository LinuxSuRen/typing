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

import javazoom.jl.decoder.JavaLayerException;
import javazoom.jl.player.Player;
import typing.linuxsuren.github.io.dictionary.FreeDictionaryAPI;
import typing.linuxsuren.github.io.dictionary.Vocabulary;

import javax.swing.*;
import java.awt.*;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class GuessingGameUI extends JPanel implements KeyFire<String> {
    private final JPanel vocabularyPanel = new JPanel();
    private final JPanel meaningPanel = new JPanel();
    private final JPanel examplePanel = new JPanel();
    private final JPanel statusPanel = new JPanel();
    private final List<JLabel> targets = new ArrayList<>();
    private List<Vocabulary> vocabularyList;
    private int hiddenIndex;

    public GuessingGameUI() {
        JPanel centerPanel = new JPanel();

        this.setLayout(new BorderLayout());
        this.add(statusPanel, BorderLayout.NORTH);
        this.add(centerPanel, BorderLayout.CENTER);

        centerPanel.setLayout(new BoxLayout(centerPanel, BoxLayout.Y_AXIS));
        centerPanel.add(vocabularyPanel);
        centerPanel.add(meaningPanel);
        centerPanel.add(examplePanel);
    }

    public void loadVocabularyList(List<Vocabulary> vList) {
        if (vList == null || vList.isEmpty()) {
            return;
        }
        this.vocabularyList = vList;

        User user = UserService.getInstance().getCurrentUser();
        if (user != null && user.getLearnedWords() != null) {
            for (String w : user.getLearnedWords()) {
                for (Vocabulary v : this.vocabularyList) {
                    if (v.getWord().equals(w)) {
                        this.vocabularyList.remove(v);
                        break;
                    }
                }
            }
        }

        new Thread(() -> {
            for (Vocabulary vol : vocabularyList) {
                if (vol.getMeaning() == null || vol.getMeaning().isEmpty()) {
                    Vocabulary volTmpl = new FreeDictionaryAPI().query(vol.getWord());
                    if (volTmpl != null) {
                        vol.setMeaning(volTmpl.getMeaning());
                    }
                }
            }
        }).start();

        loadNextVocabulary();
    }

    private int nextVocabularyIndex;
    private void loadNextVocabulary() {
        if (vocabularyList.isEmpty()) {
            return;
        }

        int total = vocabularyList.size();
        nextVocabularyIndex = new Random().nextInt(total - 1);

        updateStatusPanel();
        Vocabulary vocabulary = vocabularyList.get(nextVocabularyIndex);
        if (vocabulary.getMeaning() == null || vocabulary.getMeaning().isEmpty()) {
//            vocabularyList.remove(nextVocabularyIndex);
            System.out.println(vocabulary.getWord());
            loadNextVocabulary();
            return;
        }

        targets.clear();
        meaningPanel.removeAll();
        for (String c : vocabulary.getMeaning().split("")) {
            JLabel label = newLabel(c);
            label.setName(c);
            label.setOpaque(true);
            meaningPanel.add(label);
            targets.add(label);
        }
        hideRestPart(meaningPanel, 10);

        vocabularyPanel.removeAll();
        vocabularyPanel.setName(vocabulary.getWord());
        for (int i = 0; i < vocabulary.getWord().split("").length; i++) {
            String c = vocabulary.getWord().split("")[i];
            JLabel label = newLabel(c);
            label.setName(c);
            if (i % 2 != 0) {
                label.setText("_");
                targets.add(label);
            }
            vocabularyPanel.add(label);
        }

        examplePanel.removeAll();
        if (vocabulary.getExample() != null) {
            for (String c : vocabulary.getExample().split("")) {
                examplePanel.add(newLabel(c));
            }
        }
        examplePanel.setVisible(false);
        revalidate();
        repaint();
    }

    private JLabel newLabel(String text) {
        JLabel label = new JLabel(text);
        label.setFont(new Font("", Font.PLAIN, 20));
        return label;
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
        if (!this.isVisible()) {
            return;
        }

        if (targets.isEmpty()) {
            if (examplePanel.isVisible()) {
                UserService.getInstance().markAsLearned(vocabularyList.get(nextVocabularyIndex).getWord());
                vocabularyList.remove(nextVocabularyIndex);

                loadNextVocabulary();
            } else {
                examplePanel.setVisible(true);

                // play the audio
                playWord(vocabularyPanel.getName());
            }
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
        if (targets.isEmpty()) {
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

    private void updateStatusPanel() {
        int total = this.vocabularyList.size();
        statusPanel.removeAll();
        statusPanel.setLayout(new FlowLayout(FlowLayout.LEFT));
        statusPanel.add(new JLabel("Remain: " + total));
    }

    private void playWord(String word) {
        // play the audio
        new Thread(() -> {
            try {
                URL api = new URL("https://cdn.yourdictionary.com/audio/en/" + word + ".mp3");

                Player playMP3 = new Player(api.openStream());
                playMP3.play();
            } catch (MalformedURLException e) {
                throw new RuntimeException(e);
            } catch (IOException e) {
                throw new RuntimeException(e);
            } catch (JavaLayerException e) {
                throw new RuntimeException(e);
            }
        }).start();
    }
}
