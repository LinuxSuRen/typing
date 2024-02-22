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

import typing.linuxsuren.github.io.component.SentencePanel;
import typing.linuxsuren.github.io.component.WordPanel;
import typing.linuxsuren.github.io.dictionary.FreeDictionaryAPI;
import typing.linuxsuren.github.io.dictionary.Vocabulary;
import typing.linuxsuren.github.io.service.DefaultAudioService;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ItemEvent;
import java.util.*;
import java.util.List;
import java.util.function.Predicate;

public class GuessingGameUI extends JPanel implements KeyFire<String> {
    private final WordPanel vocabularyPanel = new WordPanel(WordPanel.ShowMode.Half);
    private final SentencePanel meaningPanel = new SentencePanel();
    private final SentencePanel examplePanel = new SentencePanel();
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
        if (vocabularyList.isEmpty()) {
            return;
        }

        Collections.shuffle(vocabularyList);
        Optional<Vocabulary> potentialVol = vocabularyList.stream().filter(new VocabularyFilter(scope))
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
        meaningPanel.setFont(users.getFont());
        meaningPanel.loadSentence(nextVocabulary.getMeaning(), 3);
        this.hiddenIndex = meaningPanel.getVisibleLettersCount();
        System.out.println("hiddenIndex:" + hiddenIndex);
        targets.addAll(meaningPanel.getLetters());

        vocabularyPanel.setWord(nextVocabulary.getWord());
        vocabularyPanel.setFont(users.getFont());
        targets.addAll(vocabularyPanel.getHiddenLetters());

        examplePanel.setFont(users.getFont());
        examplePanel.loadSentence(nextVocabulary.getExample(), -1);
        examplePanel.setVisible(false);
        revalidate();
        repaint();
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
                new DefaultAudioService().play(vocabularyPanel.getName());
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
            JLabel label = targets.get(hiddenIndex);
            label.setVisible(true);
            if (label.getText().isEmpty()) {
                label.setText(label.getName());
            }
        }
    }

    private void updateStatusPanel() {
        long total = this.vocabularyList.stream().filter(new VocabularyFilter(scopeList.getSelectedItem().toString())).count();
        statusLabel.setText("Remain: " + total);
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