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

package typing.linuxsuren.github.io.util;

import java.util.List;

public class ArrayLists {
    /**
     * Add the unique item into a list
     * @param list
     * @param item
     * @return true if the item does not exist
     */
    public static boolean addUniqueItem(List<String> list, String item) {
        boolean nonExist = true;
        for (String w : list) {
            if (w.equals(item)) {
                nonExist = false;
                break;
            }
        }
        if (nonExist) {
            list.add(item);
        }
        return nonExist;
    }
}
