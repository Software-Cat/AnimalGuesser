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

import animals.linguistics.NounPhrase;
import org.jetbrains.annotations.NotNull;


public class Animal {

    @NotNull
    private final NounPhrase species;

    public Animal(@NotNull String species) {
        this.species = NounPhrase.parsePhrase(species);
    }

    @Override
    public String toString() {
        return species.toString();
    }
}
