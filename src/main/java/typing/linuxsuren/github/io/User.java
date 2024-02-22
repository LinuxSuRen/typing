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

import typing.linuxsuren.github.io.util.ArrayLists;

import java.util.ArrayList;
import java.util.List;

/**
 * Users could learn vocabulary in the following path:
 * learned -> familiar -> mine
 */
public class User {
    private String name;
    private String password;
    private boolean isParent;
    private List<String> learnedWords;
    private List<String> familiarWords;
    private List<String> mineWords;
    private int score;
    private long learnedTime; // in minutes
    public User() {}

    public User(String name, String password) {
        this.name = name;
        this.password = password;
    }

    public boolean addLearnedWord(String word) {
        if (learnedWords == null) {
            learnedWords = new ArrayList<>();
        }
        return ArrayLists.addUniqueItem(learnedWords, word);
    }

    public boolean addFamiliarWord(String word) {
        if (familiarWords == null) {
            familiarWords = new ArrayList<>();
        }
        return ArrayLists.addUniqueItem(familiarWords, word);
    }

    public boolean addMineWord(String word) {
        if (mineWords == null) {
            mineWords = new ArrayList<>();
        }
        return ArrayLists.addUniqueItem(mineWords, word);
    }

    /** below are the getter and setter methods **/

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

    public void setLearnedWords(List<String> learnedWords) {
        this.learnedWords = learnedWords;
    }

    public List<String> getFamiliarWords() {
        return familiarWords;
    }

    public void setFamiliarWords(List<String> familiarWords) {
        this.familiarWords = familiarWords;
    }

    public List<String> getMineWords() {
        return mineWords;
    }

    public void setMineWords(List<String> mineWords) {
        this.mineWords = mineWords;
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
