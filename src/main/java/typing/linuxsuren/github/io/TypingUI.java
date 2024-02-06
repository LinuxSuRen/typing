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

public class TypingUI extends JPanel implements KeyFire<TypingCategory> {
    private TextBoard textBoard = new TextBoard();
    private TypingStatUI typingStatUI = new TypingStatUI();

    public TypingUI() {
        Keyboard keyboard = new Keyboard();
        keyboard.addKeyListener(textBoard);

        textBoard.addKeyFire(typingStatUI);
        textBoard.addKeyFire(new KeyFire<Boolean>() {
            private boolean ended = true;
            private boolean started = false;
            @Override
            public void fire(String key, Boolean data) {
                System.out.println(key + "-"+ data);
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

        Toolkit.getDefaultToolkit().addAWTEventListener(keyboard, AWTEvent.KEY_EVENT_MASK);

        this.setLayout(new BorderLayout());
        this.add(textBoard, BorderLayout.CENTER);
        this.add(keyboard,BorderLayout.SOUTH);
        this.add(typingStatUI,BorderLayout.EAST);
    }

    @Override
    public void fire(String key, TypingCategory data) {
        textBoard.loadText(data.getText());
    }
}
