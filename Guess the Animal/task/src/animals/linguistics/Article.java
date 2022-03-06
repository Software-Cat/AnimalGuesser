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

package animals.linguistics;

import lombok.Getter;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Locale;

public enum Article {
    A("a", false),
    AN("an", false),
    THE("the", true);

    @Getter
    @NotNull
    private final String text;

    @Getter
    private final boolean definite;

    Article(@NotNull String text, boolean definite) {
        this.text = text;
        this.definite = definite;
    }

    public static @Nullable
    Article fromString(@NotNull String text) {
        for (Article article : Article.values()) {
            if (article.text.equals(text)) {
                return Article.valueOf(text.toUpperCase(Locale.ROOT));
            }
        }

        return null;
    }
}
