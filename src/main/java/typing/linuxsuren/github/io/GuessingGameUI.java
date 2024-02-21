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
import typing.linuxsuren.github.io.stream.RandomSort;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.*;
import java.util.List;
import java.util.function.Predicate;

public class GuessingGameUI extends JPanel implements KeyFire<String> {
    private final JPanel vocabularyPanel = new JPanel();
    private final JPanel meaningPanel = new JPanel();
    private final JPanel examplePanel = new JPanel();
    private final JPanel statusPanel = new JPanel();
    private final JLabel statusLabel = new JLabel();
    private final JComboBox<String> scopeList = new JComboBox<>();
    private final List<JLabel> targets = new ArrayList<>();
    private List<Vocabulary> vocabularyList;
    private int hiddenIndex;

    public GuessingGameUI() {
        setupStatusPanel();
        JPanel centerPanel = new JPanel();

        this.setLayout(new BorderLayout());
        this.add(statusPanel, BorderLayout.NORTH);
        this.add(centerPanel, BorderLayout.CENTER);

        centerPanel.setLayout(new BoxLayout(centerPanel, BoxLayout.Y_AXIS));
        centerPanel.add(vocabularyPanel);
        centerPanel.add(meaningPanel);
        centerPanel.add(examplePanel);
    }

    private void setupStatusPanel() {
        statusPanel.setLayout(new FlowLayout(FlowLayout.LEFT));
        statusPanel.add(new JLabel("Choose scope:"));
        statusPanel.add(scopeList);
        statusPanel.add(statusLabel);

        scopeList.addItem("All");
        scopeList.addItem("ket");
        scopeList.addItem("pet");
        scopeList.addItem("ielts");
        scopeList.addItem("toefl");
        scopeList.addItemListener((e) -> {
            if (e.getSource() == scopeList && e.getStateChange() == ItemEvent.SELECTED) {
                loadNextVocabulary(scopeList.getSelectedItem().toString());
                scopeList.transferFocus();
            }
        });
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

        loadNextVocabulary(scopeList.getSelectedItem().toString());
    }

    private Vocabulary nextVocabulary;
    private void loadNextVocabulary(String scope) {
        long b = vocabularyList.stream().filter((a) -> {
            if (a.getScope() == null || a.getScope().length == 0) {
                return true;
            }

            for (String s : a.getScope()) {
                if (s.equals(scope)) {
                    return true;
                }
            }
            return false;
        }).count();

        if (vocabularyList.isEmpty()) {
            return;
        }

        Optional<Vocabulary> potentialVol = vocabularyList.stream().filter(new VocabularyFilter(scope))
                .sorted(new RandomSort())
                .findAny();
        if (potentialVol.isPresent()) {
            nextVocabulary = potentialVol.get();
        } else {
            return;
        }

        updateStatusPanel();
        if (nextVocabulary.getMeaning() == null || nextVocabulary.getMeaning().isEmpty()) {
//            vocabularyList.remove(nextVocabularyIndex);
            loadNextVocabulary(scope);
            return;
        }

        Users users = UserService.getInstance().read();

        targets.clear();
        meaningPanel.removeAll();
        for (String c : nextVocabulary.getMeaning().split("")) {
            JLabel label = newLabel(c);
            label.setName(c);
            label.setFont(new Font("",Font.PLAIN, users.getFont()));
            label.setOpaque(true);
            meaningPanel.add(label);
            targets.add(label);
        }
        hideRestPart(meaningPanel, 10);

        vocabularyPanel.removeAll();
        vocabularyPanel.setName(nextVocabulary.getWord());
        for (int i = 0; i < nextVocabulary.getWord().split("").length; i++) {
            String c = nextVocabulary.getWord().split("")[i];
            JLabel label = newLabel(c);
            label.setName(c);
            label.setFont(new Font("",Font.PLAIN, users.getFont()));
            if (i % 2 != 0) {
                label.setText("_");
                targets.add(label);
            }
            vocabularyPanel.add(label);
        }

        examplePanel.removeAll();
        if (nextVocabulary.getExample() != null) {
            for (String c : nextVocabulary.getExample().split("")) {
                JLabel label = newLabel(c);
                label.setFont(new Font("",Font.PLAIN, users.getFont()));
                examplePanel.add(label);
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
                UserService.getInstance().markAsLearned(nextVocabulary.getWord());
                vocabularyList.remove(nextVocabulary);

                loadNextVocabulary(scopeList.getSelectedItem().toString());
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
        long total = this.vocabularyList.stream().filter(new VocabularyFilter(scopeList.getSelectedItem().toString())).count();
        statusLabel.setText("Remain: " + total);
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

class VocabularyFilter implements Predicate<Vocabulary> {
    private String scope;
    VocabularyFilter(String scope) {
        this.scope = scope;
    }

    @Override
    public boolean test(Vocabulary vocabulary) {
        if (scope.equalsIgnoreCase("all")) {
            return true;
        }
        if (vocabulary.getScope() == null) {
            return true;
        }
        for (String s : vocabulary.getScope()) {
            if (s.equals(this.scope)) {
                return true;
            }
        }
        return false;
    }
}