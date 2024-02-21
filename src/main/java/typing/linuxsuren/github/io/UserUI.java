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
import javax.swing.border.CompoundBorder;
import javax.swing.border.EmptyBorder;
import java.awt.event.ActionEvent;
import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

public class UserUI extends JPanel {
    private JTextField username = new JTextField(20);
    private JTextField password = new JPasswordField(20);
    private UserService userSvc = UserService.getInstance();
    private JButton loginBut = new JButton("Login");
    private JButton createBut = new JButton("Create");
    private List<KeyFire> keyFires = new ArrayList<>();
    private boolean toCreate;
    private Timer timer = null;

    public UserUI() {
        refresh();

        loginBut.addActionListener(new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                login();
            }
        });

        createBut.addActionListener(new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                create();
            }
        });

        CompoundBorder border = new CompoundBorder(this.getBorder(), new EmptyBorder(100, 0, 100, 0));

        this.setBorder(border);
        this.setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
        this.add(createPanel("Username:", username));
        this.add(createPanel("Password:", password));
        this.add(components(loginBut, createBut));
    }

    public void refresh() {
        boolean hasAdmin = userSvc.getUserCount() > 0;
        loginBut.setVisible(hasAdmin && !toCreate);
        createBut.setVisible(!hasAdmin || toCreate);

        username.setText("");
        password.setText("");
    }

    private JPanel createPanel(String label, JComponent com) {
        return components(new JLabel(label), com);
    }

    private JPanel components(JComponent ...components) {
        JPanel panel = new JPanel();
        for (JComponent com : components) {
            panel.add(com);
        }
        return panel;
    }

    public void login() {
        try {
            userSvc.login(new User(username.getText(), password.getText()));

            keyFires.forEach((k) -> {
                k.fire("", Code.Login);
            });

            timer = new Timer(true);
            timer.schedule(new TimerTask() {
                @Override
                public void run() {
                    timeout();
                }
            }, 1000 * 60 * 25);
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, e.getMessage());
        } finally {
            refresh();
        }
    }

    public void logout() {
        if (timer != null) {
            timer.cancel();
            timer = null;
        }
        userSvc.logout();

        keyFires.forEach((k) -> {
            k.fire("", Code.Logout);
        });
    }

    public void timeout() {
        logout();
        refresh();

        JOptionPane.showMessageDialog(this, "Please take a break!");
    }

    public void create() {
        try {
            userSvc.createUser(new User(username.getText(), password.getText()));

            JOptionPane.showMessageDialog(this, "User created!");
        } catch (Exception ee) {
            JOptionPane.showMessageDialog(this, ee.getMessage());
        } finally {
            toCreate = false;
            refresh();
        }
    }

    public void addListener(KeyFire key) {
        keyFires.add(key);
    }

    public void setToCreate(boolean toCreate) {
        this.toCreate = toCreate;
    }

    enum Code {
        Login, Logout
    }
}