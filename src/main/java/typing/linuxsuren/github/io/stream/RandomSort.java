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

import java.util.Comparator;
import java.util.Random;

public class RandomSort implements Comparator<Object> {
    @Override
    public int compare(Object o1, Object o2) {
        int a = new Random().nextInt(4) - 2;
        if (a < -1) {
            a = -1;
        }
        return a;
    }
}
