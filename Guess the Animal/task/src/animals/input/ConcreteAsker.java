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
import java.util.Scanner;
import java.util.function.Function;

public class ConcreteAsker<T> implements Asker<T> {

    protected final Scanner scanner = new Scanner(System.in);

    @NotNull
    @Getter
    @Setter
    private String inputPrompt;

    @Getter
    @Setter
    @NotNull
    private String query;

    @Getter
    @Setter
    @NotNull
    private Function<@NotNull String, @NotNull T> transformer;

    protected ConcreteAsker() {
    }

    public @NotNull
    T ask() throws InputMismatchException {
        System.out.println(query);
        System.out.print(inputPrompt);
        return transformer.apply(scanner.nextLine());
    }
}
