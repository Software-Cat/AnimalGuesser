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

package animals.input;

import lombok.Getter;
import lombok.Setter;
import org.jetbrains.annotations.NotNull;

import java.util.InputMismatchException;
import java.util.List;
import java.util.Random;

public class PersistentAsker<T> extends ConcreteAsker<T> {

    private final Random random = new Random();

    @NotNull
    @Getter
    @Setter
    private List<String> retryPhrases;

    protected PersistentAsker() {
    }

    @Override
    public @NotNull
    T ask() throws InputMismatchException {
        System.out.println(getQuery());

        T response = null;
        boolean responseIsValid = false;

        while (!responseIsValid) {
            try {
                System.out.print(getInputPrompt());
                response = getTransformer().apply(scanner.nextLine());

                // This line won't be reached if the super.ask() throws an exception.
                responseIsValid = true;
            } catch (InputMismatchException e) {
                System.out.println(retryPhrases.get(random.nextInt(retryPhrases.size())));
            }
        }

        return response;
    }
}
