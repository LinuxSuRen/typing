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
import java.awt.event.AWTEventListener;
import java.awt.event.KeyEvent;
import java.util.List;
import java.util.ArrayList;

public class Keyboard extends JComponent implements AWTEventListener {
    private List<JButton> row1 = new ArrayList<JButton>();
    private List<KeyFire> keyFires = new ArrayList<>();
    private String[] keys = new String[]{
        "~,1,2,3,4,5,6,7,8,9,0,-,=",
        "q,w,e,r,t,y,u,i,o,p",
        "a,s,d,f,g,h,j,k,l",
        "z,x,c,v,b,n,m",
        " "};

    public Keyboard() {
        this.setLayout(new GridLayout(keys.length,10));

        for (String row : keys) {
            String[] items = row.split(",");

            JPanel panel = new JPanel();
            panel.setLayout(new GridLayout(1,items.length));

            for (String item : items) {
                JButton but = new JButton(item);
                panel.add(but);
                row1.add(but);
            }
            this.add(panel);
        }
    }

    @Override
    public void eventDispatched(AWTEvent event) {
        if (event.getClass() == KeyEvent.class) {
            KeyEvent ke = (KeyEvent) event;

            if (ke.getID() == KeyEvent.KEY_RELEASED) {
                for (JButton but : row1) {
                    if (but.getText().equals(ke.getKeyChar() + "")) {
                        but.doClick();

                        for (KeyFire fire : keyFires) {
                            fire.fire(ke.getKeyChar() + "", "");
                        }
                    }
                }
            }
        }
    }

    public void addKeyListener(KeyFire e) {
        keyFires.add(e);
    }
}
