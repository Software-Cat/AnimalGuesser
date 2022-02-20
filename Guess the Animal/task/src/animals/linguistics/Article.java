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

    public static @Nullable Article fromString(@NotNull String text) {
        for (Article article : Article.values()) {
            if (article.text.equals(text)) {
                return Article.valueOf(text.toUpperCase(Locale.ROOT));
            }
        }

        return null;
    }
}
