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

import java.util.ArrayList;
import java.util.List;

public class User {
    private String name;
    private String password;
    private boolean isParent;
    private List<String> learnedWords;
    private int score;
    private long learnedTime; // in minutes
    public User() {}

    public User(String name, String password) {
        this.name = name;
        this.password = password;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public boolean isParent() {
        return isParent;
    }

    public void setParent(boolean parent) {
        isParent = parent;
    }

    public List<String> getLearnedWords() {
        return learnedWords;
    }

    public boolean addLearnedWord(String word) {
        if (learnedWords == null) {
            learnedWords = new ArrayList<>();
        }
        boolean nonExist = true;
        for (String w : learnedWords) {
            if (w.equals(word)) {
                nonExist = false;
                break;
            }
        }
        if (nonExist) {
            learnedWords.add(word);
        }
        return nonExist;
    }

    public void setLearnedWords(List<String> learnedWords) {
        this.learnedWords = learnedWords;
    }

    public int getScore() {
        return score;
    }

    public void setScore(int score) {
        this.score = score;
    }

    public long getLearnedTime() {
        return learnedTime;
    }

    public void setLearnedTime(long learnedTime) {
        this.learnedTime = learnedTime;
    }
}
