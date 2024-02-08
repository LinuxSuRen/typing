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
import java.util.List;

public class GuessingGameUI extends JPanel implements KeyFire<String> {
    private JPanel vocabularyPanel = new JPanel();
    private JPanel meaningPanel = new JPanel();
    private JPanel examplePanel = new JPanel();
    private List<Vocabulary> vocabularyList;

    public GuessingGameUI() {
        this.setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));

        vocabularyPanel.setVisible(false);
        examplePanel.setVisible(false);
    }

    public void loadVocabularyList(List<Vocabulary> vocabularyList) {
        this.vocabularyList = vocabularyList;
        if (vocabularyList == null || vocabularyList.isEmpty()) {
            return;
        }

        Vocabulary vocabulary = vocabularyList.get(0);
        loadVocabulary(vocabulary);
    }

    public void loadVocabulary(Vocabulary vocabulary) {
        vocabularyPanel.removeAll();
        for (String c : vocabulary.getWord().split("")) {
            vocabularyPanel.add(new JLabel(c));
        }

        meaningPanel.removeAll();
        for (String c : vocabulary.getMeaning().split("")) {
            meaningPanel.add(new JLabel(c));
        }

        examplePanel.removeAll();
        for (String c : vocabulary.getExample().split("")) {
            examplePanel.add(new JLabel(c));
        }
    }

    @Override
    public void fire(String key, String data) {

    }
}
