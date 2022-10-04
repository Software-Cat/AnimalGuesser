package animal.linguistics.partofspeech;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public enum Article {
    A(false),
    AN(false),
    THE(true);

    private final boolean DEFINITE;

    Article(boolean definite) {
        this.DEFINITE = definite;
    }

    public static Article defaultIndefiniteArticle(@NotNull String noun) {
        if (noun.matches("(?i)[aeiou].*")) {
            return AN;
        } else {
            return A;
        }
    }

    public static boolean isArticle(@NotNull String word) {
        return word.matches("(?i)(a|an|the)");
    }

    public static @Nullable Article parseArticle(@Nullable String word) {
        if (word == null || !isArticle(word)) {
            return null;
        }

        for (Article article : Article.values()) {
            if (article.name().equalsIgnoreCase(word)) {
                return article;
            }
        }

        return null;
    }

    public boolean isDefinite() {
        return DEFINITE;
    }

    @Override
    public String toString() {
        return name().toLowerCase();
    }
}
