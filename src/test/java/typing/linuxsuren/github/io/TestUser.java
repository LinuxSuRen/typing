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

import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class TestUser {
    @Test
    public void testAddLearnedWord() {
        User user = new User("fake", "pass");
        user.addFamiliarWord("good");
        user.addLearnedWord("good");
        user.addMineWord("good");

        assertEquals(user.getFamiliarWords().size(), 1);
        assertEquals(user.getLearnedWords().size(), 1);
        assertEquals(user.getMineWords().size(), 1);

        assertEquals(user.getName(), "fake");
        assertEquals(user.getPassword(), "pass");
    }
}
