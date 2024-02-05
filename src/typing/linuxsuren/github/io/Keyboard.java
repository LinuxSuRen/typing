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
         "z,x,c,v,b,n,m"};

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
                            fire.fire(ke.getKeyChar() + "");
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
