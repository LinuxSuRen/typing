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
import java.util.List;

public class TypingUI extends JPanel implements KeyFire<TypingCategory> {
    private TextBoard textBoard = new TextBoard();
    private TypingStatUI typingStatUI = new TypingStatUI();
    private DroppingGameUI gameUI = new DroppingGameUI();
    private GuessingGameUI guessingGameUI = new GuessingGameUI();
    private CardLayout centerCard = new CardLayout();
    private JPanel centerPanel = createCenterPanel();
    private JPanel leftPanel = createLeftPanel();
    private String practiceText;

    public TypingUI() {
        Keyboard keyboard = new Keyboard();
        keyboard.addKeyListener(textBoard);
        keyboard.addKeyListener(gameUI);
        keyboard.addKeyListener(guessingGameUI);

        Toolkit.getDefaultToolkit().addAWTEventListener(keyboard, AWTEvent.KEY_EVENT_MASK);

        this.setLayout(new BorderLayout());
        this.add(leftPanel, BorderLayout.WEST);
        this.add(centerPanel, BorderLayout.CENTER);
        this.add(keyboard,BorderLayout.SOUTH);
        this.add(typingStatUI,BorderLayout.EAST);
    }

    private JPanel createCenterPanel() {
        JPanel panel = new JPanel();

        textBoard.addKeyFire(typingStatUI);
        textBoard.addKeyFire(new KeyFire<Boolean>() {
            private boolean ended = true;
            private boolean started = false;
            @Override
            public void fire(String key, Boolean data) {
                switch (key) {
                    case "end":
                        typingStatUI.end();
                        ended = true;
                        started = false;
                        break;
                    default:
                        if (!started) {
                            started = true;
                            typingStatUI.start();
                        }
                        typingStatUI.fire(key, data);
                        break;
                }
            }
        });

        panel.setLayout(centerCard);
        panel.add(gameUI, "game");
        panel.add(textBoard, "normal");
        panel.add(guessingGameUI, "guess");
        centerCard.show(panel, "normal");
        return panel;
    }

    private JPanel createLeftPanel() {
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));

        JButton normalBut = new JButton("Normal");
        normalBut.addActionListener(new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                centerCard.show(centerPanel, "normal");
            }
        });
        JButton gameBut = new JButton("Game");
        gameBut.addActionListener(new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                centerCard.show(centerPanel, "game");
                gameUI.load(practiceText);
            }
        });
        JButton guessBut = new JButton("Guess");
        guessBut.addActionListener(new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                centerCard.show(centerPanel, "guess");

                List<Vocabulary> vocabularyList = VocabularyCache.getInstance().getVocabularyList();
                guessingGameUI.loadVocabularyList(vocabularyList);
            }
        });

        panel.add(normalBut);
        panel.add(gameBut);
        panel.add(guessBut);
        return panel;
    }

    @Override
    public void fire(String key, TypingCategory data) {
        practiceText = data.getText();
        textBoard.loadText(data.getText());
    }
}
