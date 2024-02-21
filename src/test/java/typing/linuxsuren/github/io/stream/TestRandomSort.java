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

package typing.linuxsuren.github.io.stream;
import static org.junit.Assert.assertNotEquals;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class TestRandomSort {
    @Test
    public void testCompare() {
        List<String> array = new ArrayList<>();
        for (int i = 0; i < 10; i++) {
            array.add("" + i);
        }

        assertNotEquals(getRandomItem(array), getRandomItem(array));
    }

    private String getRandomItem(List<String> array) {
        Optional<String> item = array.stream().sorted(new RandomSort()).findAny();
        return item.get();
    }
}
