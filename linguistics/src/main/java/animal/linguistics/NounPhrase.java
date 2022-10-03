package animal.linguistics;

import org.jetbrains.annotations.NotNull;

import java.util.List;

public class NounPhrase {

    @NotNull
    private final String noun;

    @NotNull
    private final Article article;

    protected NounPhrase(@NotNull String noun, @NotNull Article article) {
        this.noun = noun;
        this.article = article;
    }

    public static @NotNull NounPhrase parsePhrase(@NotNull String phrase) {
        List<String> words = List.of(phrase.toLowerCase().split("\\s+"));
        String noun;
        Article article;

        if (words.size() == 1) {
            noun = words.get(0);
            article = Article.defaultArticle(noun);
        } else if (Article.isArticle(words.get(0))) {
            noun = String.join(" ", words.subList(1, words.size()));
            article = Article.toArticle(words.get(0));

            assert article != null;
            if (article.isDefinite()) {
                article = Article.defaultArticle(noun);
            }
        } else {
            noun = words.get(0);
            article = Article.defaultArticle(noun);
        }

        return new NounPhrase(noun, article);
    }

    @Override
    public String toString() {
        return String.join(" ", article.toString(), noun);
    }
}
