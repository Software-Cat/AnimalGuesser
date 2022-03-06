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

package animals.base;

import animals.greet.Greeter;
import animals.greet.TimedGreeter;
import animals.input.Asker;
import animals.input.AskerBuilder;

import java.util.InputMismatchException;
import java.util.List;

public class Application implements Runnable {

    private final Greeter greeter = new TimedGreeter();

    private final Asker<Animal> animalAsker = AskerBuilder.asker()
            .withQuery("Enter an animal:")
            .addTransformer(String::trim)
            .addPredicate((String s) -> !s.isBlank())
            .addTransformer(Animal::new)
            .build();

    private final Asker<Boolean> yesNoAsker = AskerBuilder.asker()
            .withQuery("Is it a ...?")
            .addTransformer(String::trim)
            .addPredicate((String s) -> !s.isBlank())
            .addTransformer((String s) -> {
                s = s.toLowerCase();
                s = s.replaceFirst("\\p{Punct}", "");
                switch (s) {
                    case "y", "yes", "yeah", "yep", "sure", "right", "affirmative",
                            "correct", "indeed", "you bet", "exactly", "you said it":
                        return true;
                    case "n", "no", "no way", "nah", "nope", "negative",
                            "i don't think so", "yeah no":
                        return false;
                }
                throw new InputMismatchException(
                        "The input (\"%s\") cannot be converted to a valid boolean response.".formatted(s));
            })
            .persistent(true)
            .withRetryPhrases(List.of(
                    "I'm not sure if I caught you: was it yes or no?",
                    "Funny, I still don't understand, is it yes or no?",
                    "Oh, it is too complicated for me: just tell me yes or no.",
                    "Could you please simply say yes or no?",
                    "Oh, no, don't try to confuse me: say yes or no."))
            .build();

    @Override
    public void run() {
        greeter.hello();
        System.out.println();

        Animal animal = animalAsker.ask();

        yesNoAsker.setQuery("Is it %s?".formatted(animal));
        if (yesNoAsker.ask()) {
            System.out.println("You answered: Yes");
        } else {
            System.out.println("You answered: No");
        }

        System.out.println();
        greeter.goodbye();
    }
}
