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

public class Startup {
    public static void main(String[] args) throws ClassNotFoundException, UnsupportedLookAndFeelException, InstantiationException, IllegalAccessException {
        JFrame frame = new JFrame("Typing");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        TypingUI typingUI = new TypingUI();
        CategoryUI categoryUI = new CategoryUI();
        categoryUI.loadSync();
        categoryUI.addListener(typingUI);

        CardLayout cardLayout = new CardLayout();
        JPanel centerPanel = new JPanel();
        centerPanel.setLayout(cardLayout);
        centerPanel.add("category", new JScrollPane(categoryUI));
        centerPanel.add("typing", typingUI);
        cardLayout.show(centerPanel,"category");
        categoryUI.addListener((key, data) -> {
            cardLayout.show(centerPanel,"typing");
        });

        ToolPanel toolPanel = new ToolPanel();
        toolPanel.addKeyFire((key, data) -> {
            if (data == ToolCode.Home) {
                cardLayout.show(centerPanel,"category");
            } else if (data == ToolCode.Refresh) {
                categoryUI.loadSync();
            }
        });

        JPanel rootPanel = new JPanel();
        rootPanel.setLayout(new BorderLayout());
        rootPanel.add(centerPanel, BorderLayout.CENTER);
        rootPanel.add(toolPanel,BorderLayout.NORTH);

        frame.getContentPane().add(rootPanel);

        UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());

        frame.setLocationRelativeTo(null);
        frame.setSize(1300, 600);
        frame.setVisible(true);
    }
}
