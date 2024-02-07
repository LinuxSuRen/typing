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

import org.yaml.snakeyaml.LoaderOptions;
import org.yaml.snakeyaml.Yaml;
import org.yaml.snakeyaml.constructor.Constructor;

import javax.swing.*;
import javax.swing.border.CompoundBorder;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

/**
 * This panel will show the typing category.
 */
public class CategoryUI extends JPanel {
    private TypingSource source;
    private List<KeyFire> keyFires = new ArrayList<>();

    public CategoryUI() {
        this.setLayout(new GridLayout(0,4));
    }

    public void loadData(InputStream input) {
        // clear the existing components
        while(this.getComponentCount() > 0) {
            this.remove(0);
        }

        // load the new data
        Yaml yaml = new Yaml(new Constructor(TypingSource.class, new LoaderOptions()));
        source = yaml.load(input);

        System.out.println("data loaded");
        for (TypingCategory category : source.getItems()) {
            JPanel item = new JPanel();
            item.setLayout(new BoxLayout(item, BoxLayout.Y_AXIS));
            item.setName(category.getName());
            item.add(createLabel(item.getName()));
            item.add(createLabel(category.getDescription()));
            item.setMinimumSize(new Dimension(200, 200));

            CompoundBorder border = new CompoundBorder(
                    new EmptyBorder(6, 6, 6, 6),
                    new LineBorder(Color.BLACK, 1, true));

            item.setBorder(border);
            this.add(item);

            item.addMouseListener(new MouseAdapter() {
                @Override
                public void mouseClicked(MouseEvent e) {
                   for (KeyFire key : keyFires) {
                       TypingCategory target = null;
                       String name = e.getComponent().getName();
                       for (TypingCategory category : source.getItems()) {
                           if (category.getName().equals(name)) {
                               target = category;
                           }
                       }

                       if (target == null) {
                           return;
                       }
                       key.fire(name, target);
                   }
                }
            });
        }
    }

    public void loadSync() {
        SwingUtilities.invokeLater(new Thread(() -> {
            add(new JLabel("loading..."));

            try {
                URL url = new URL("https://gitee.com/linuxsuren/test/raw/master/typing.yaml");

                loadData(url.openStream());
            } catch (IOException e) {
                add(new JLabel(e.getMessage()));
                throw new RuntimeException(e);
            }
        }));
    }

    public void addListener(KeyFire key) {
        keyFires.add(key);
    }

    public JLabel createLabel(String text) {
        JLabel label = new JLabel(text);
        label.setAlignmentX(Component.CENTER_ALIGNMENT);
        return label;
    }
}
