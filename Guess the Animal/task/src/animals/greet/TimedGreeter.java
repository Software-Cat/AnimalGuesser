/*
 * Copyright © Bowen Wu 2022
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package animals.greet;

import animals.util.Range;

import java.time.LocalTime;
import java.util.List;
import java.util.Random;

public class TimedGreeter implements Greeter {

    private final Range<Integer> MORNING = new Range<>(5, 12);
    private final Range<Integer> AFTERNOON = new Range<>(12, 18);

    private final Random random = new Random();

    @Override
    public void hello() {
        int currentHour = LocalTime.now().getHour();

        if (MORNING.isInRange(currentHour)) {
            System.out.println("Good morning!");
        } else if (AFTERNOON.isInRange(currentHour)) {
            System.out.println("Good afternoon!");
        } else {
            System.out.println("Good evening!");
        }
    }

    @Override
    public void goodbye() {
        System.out.println(List.of("Have a nice day!", "See you soon!", "Bye!").get(random.nextInt(3)));
    }
}
