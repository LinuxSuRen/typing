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
import org.yaml.snakeyaml.nodes.Tag;

import java.io.*;
import java.util.ArrayList;

public class UserService {
    private User currentUser;
    private static final UserService instance = new UserService();
    private UserService(){}

    public static UserService getInstance() {
        return instance;
    }

    public int getUserCount() {
        try {
            Users users = read();

            if (users != null && users.getItems() != null) {
                return users.getItems().size();
            }

            return 0;
        } catch (Exception e) {
            System.out.println(e.getMessage());
            return 0;
        }
    }

    public User getCurrentUser() {
        return currentUser;
    }

    public void createUser(User user) {
        if (user.getName() == null || "".equals(user.getName()) || user.getPassword() == null || "".equals(user.getPassword())) {
            throw new IllegalArgumentException("username or password is empty");
        }

        Users users;
        try {
            users = read();
        } catch (Exception e) {
            users = new Users();
        }

        if (users.getItems() == null) {
            users.setItems(new ArrayList<>());
            user.setParent(true);
        } else {
            user.setParent(false);
        }

        users.getItems().add(user);

        write(users);
    }

    public void markAsLearned(String word) {
        if (currentUser == null) {
            return;
        }

        Users users = read();
        boolean needToUpdate = false;
        for (User item : users.getItems()) {
            if (item.getName().equals(currentUser.getName())) {
                needToUpdate = item.addLearnedWord(word);
                break;
            }
        }
        if (needToUpdate) {
            write(users);
        }
    }

    public void login(User user) throws Exception {
        Users users = read();

        if (users == null || users.getItems() == null) {
            throw new Exception("no users");
        }

        boolean success = false;
        for (User u : users.getItems()) {
            if (u.getName().equals(user.getName()) ) {
                success = u.getPassword().equals(user.getPassword());
                user = u;
                break;
            }
        }

        if (!success) {
            throw new Exception("login failed");
        }
        currentUser = user;
    }

    public void logout() {
        this.currentUser = null;
    }

    public Users read() {
        InputStream input = null;
        try {
            String configFile = getConfigFile();
            input = new FileInputStream(configFile);

            Yaml yaml = new Yaml(new Constructor(Users.class, new LoaderOptions()));
            return yaml.load(input);
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        } finally {
            if (input != null) {
                try {
                    input.close();
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }
        }
    }

    private void write(Users users) {
        Yaml yaml = new Yaml();
        String data = yaml.dumpAs(users, Tag.MAP, null);

        try {
            String configFile = getConfigFile();

            OutputStream out = new FileOutputStream(configFile);
            out.write(data.getBytes());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private String getConfigFile() throws FileNotFoundException {
        String dir = System.getProperty("user.home") + File.separator + ".config/typing/";
        if (!new File(dir).exists() && !new File(dir).mkdirs()) {
            throw new FileNotFoundException("cannot create directory: " + dir);
        }
        return dir + File.separator + "users.yaml";
    }
}
