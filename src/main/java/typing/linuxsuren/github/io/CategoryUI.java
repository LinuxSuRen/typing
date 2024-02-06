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
        getParent().repaint();

        // load the new data
        Yaml yaml = new Yaml(new Constructor(TypingSource.class, new LoaderOptions()));
        source = yaml.load(input);

        System.out.println("data loaded");
        for (TypingCategory category : source.getItems()) {
            JPanel item = new JPanel();
            item.setName(category.getName());
            item.add(new JLabel(item.getName()));
            item.setBorder(new LineBorder(Color.BLACK));
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
            getParent().repaint();

            try {
                URL url = new URL("https://gitee.com/linuxsuren/test/raw/master/typing.yaml");

                loadData(url.openStream());
            } catch (IOException e) {
                add(new JLabel(e.getMessage()));
                throw new RuntimeException(e);
            } finally {
                getParent().repaint();
            }
        }));
    }

    public void addListener(KeyFire key) {
        keyFires.add(key);
    }
}
