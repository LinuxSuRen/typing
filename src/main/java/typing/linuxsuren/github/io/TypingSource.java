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

import typing.linuxsuren.github.io.dictionary.Vocabulary;

import java.util.List;

public class TypingSource {
    private String author;
    private List<TypingCategory> items;
    private List<Vocabulary> dictionary;

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public List<TypingCategory> getItems() {
        return items;
    }

    public void setItems(List<TypingCategory> items) {
        this.items = items;
    }

    public List<Vocabulary> getDictionary() {
        return dictionary;
    }

    public void setDictionary(List<Vocabulary> dictionary) {
        this.dictionary = dictionary;
    }
}
