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

package typing.linuxsuren.github.io.component;

import javax.swing.*;
import java.util.ArrayList;
import java.util.List;

/**
 * This panel built on {@link WordPanel}
 */
public class SentencePanel extends JPanel {
    private List<WordPanel> wordPanels = new ArrayList<>();
    private int font;
    private int initialShownWords;

    public void loadSentence(String sentence, int initialShownWords) {
        this.removeAll();
        this.wordPanels.clear();
        if (sentence == null || sentence.isEmpty()) {
            return;
        }
        this.initialShownWords = initialShownWords;

        String[] words = sentence.split(" ");
        for (int i = 0; i < words.length; i++) {
            String word = words[i];
            WordPanel.ShowMode mode = WordPanel.ShowMode.Full;
            if (initialShownWords >= 0) {
                mode = i < initialShownWords ? WordPanel.ShowMode.Full : WordPanel.ShowMode.None;
            }
            WordPanel wordPanel = new WordPanel(mode);
            wordPanel.setFont(font);
            wordPanel.setHiddenPlaceHolder("");
            wordPanel.setWord(word);

            this.add(wordPanel);
            this.wordPanels.add(wordPanel);
        }
    }

    public List<JLabel> getLetters() {
        List<JLabel> letters = new ArrayList<>();
        for (WordPanel panel : wordPanels) {
            letters.addAll(panel.getLetters());
        }
        return letters;
    }

    public int getVisibleLettersCount() {
        int index = 0;
        for (int i = 0; i < initialShownWords; i++) {
            index += wordPanels.get(i).getLetters().size();
        }
        return index;
    }

    public void setFont(int font) {
        this.font = font;
    }
}
