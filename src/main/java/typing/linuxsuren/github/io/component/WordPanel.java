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

import typing.linuxsuren.github.io.service.DefaultAudioService;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.ArrayList;
import java.util.List;

/**
 * This class represents a word. For example, English word 'good' in this panel.
 * And it will have the relevant features. <br/>
 * * Play the pronunciation when click <br/>
 * * Show the explanation and example sentence when db-click <br/>
 * * Show the letters partially
 */
public class WordPanel extends JPanel implements MouseListener {
    private String word;
    private ShowMode mode;
    private int font;
    private String hiddenPlaceHolder = "_";
    private List<JLabel> hiddenLetters = new ArrayList<>();
    private List<JLabel> allLetters = new ArrayList<>();

    public WordPanel(ShowMode mode) {
        this.mode = mode;
        this.addMouseListener(this);
    }

    public void setWord(String word) {
        this.removeAll();
        this.hiddenLetters.clear();

        String[] items = word.split("");
        for (int i = 0; i < items.length; i++) {
            String s = items[i];

            JLabel label = new JLabel();
            label.setFont(new Font("",Font.PLAIN, this.font));
            if ((i % 2 == 0 || mode == ShowMode.Full) && mode != ShowMode.None) {
                label.setText(s);
            } else {
                label.setText(hiddenPlaceHolder);
                hiddenLetters.add(label);
            }
            label.setOpaque(true);
            label.setName(s);
            this.add(label);
            this.allLetters.add(label);
        }
        new DefaultAudioService().saveCacheAsync(word);

        this.word = word.replace(",", "").replace(".", "");
        this.setName(this.word);
    }

    public List<JLabel> getHiddenLetters() {
        return hiddenLetters;
    }

    public List<JLabel> getLetters() {
        return allLetters;
    }

    public boolean haveHiddenLetters() {
        for (JLabel label : hiddenLetters) {
            if (label.getText().equals(hiddenPlaceHolder)) {
                return true;
            }
        }
        return false;
    }

    public void setFont(int font) {
        this.font = font;
    }

    public void setHiddenPlaceHolder(String hiddenPlaceHolder) {
        this.hiddenPlaceHolder = hiddenPlaceHolder;
    }

    private boolean canPlay() {
        return new DefaultAudioService().hasCache(this.word) && !haveHiddenLetters();
    }

    @Override
    public void mouseClicked(MouseEvent e) {
        if (e.getClickCount() == 1 && canPlay()) {
            new DefaultAudioService().play(word);
        }
    }

    @Override
    public void mousePressed(MouseEvent e) {
        // no need
    }

    @Override
    public void mouseReleased(MouseEvent e) {
        // no need
    }

    @Override
    public void mouseEntered(MouseEvent e) {
        if (canPlay()) {
            this.setCursor(new Cursor(Cursor.HAND_CURSOR));
        }
    }

    @Override
    public void mouseExited(MouseEvent e) {
        // no need
    }

    public enum ShowMode {
        Half, Full, None
    }
}
