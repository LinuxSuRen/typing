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
import java.util.Date;

public class TypingStatUI extends JPanel implements KeyFire<Boolean> {
    private JLabel speed = new JLabel();
    private JLabel accuracy = new JLabel();
    private Date begin;
    private Date end;
    private int correctCount;
    private int incorrectCount;
    private Thread updateThread;

    public TypingStatUI() {
        this.setLayout(new GridLayout(4,1));

        this.add(new Label("Speed"));
        this.add(speed);
        this.add(new Label("Accuracy"));
        this.add(accuracy);
    }

    public void start() {
        System.out.println("started");
        this.correctCount = 0;
        this.incorrectCount = 0;
        this.begin = new Date();
        this.end = null;
        this.updateThread = new Thread(() -> {
            while(end == null) {
                Date now = new Date();
                long duration = ((now.getTime() - begin.getTime()) / 1000 / 60);
                if (duration == 0) {
                    duration = 1;
                }

                if (duration > 0) {
                    long rate = correctCount / duration;
                    speed.setText(rate + " WPM");
                }

                int acc = 0;
                if (correctCount > 0 || incorrectCount > 0) {
                    acc = correctCount * 100 / (correctCount + incorrectCount);
                }
                accuracy.setText(acc + "%");

                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
            }
        });
        this.updateThread.start();
    }

    public void end() {
        this.end = new Date();
        System.out.println("ended");
    }

    @Override
    public void fire(String key, Boolean data) {
        if (data) {
            correctCount++;
        } else {
            incorrectCount++;
        }
    }
}
