package animals.linguistics;

import lombok.Getter;
import org.jetbrains.annotations.NotNull;

import java.util.Locale;
import java.util.StringJoiner;

public class NounPhrase {

    @NotNull
    @Getter
    private final Article article;

    @NotNull
    @Getter
    private final String noun;

    private NounPhrase(@NotNull Article article, @NotNull String noun) {
        this.article = article;
        this.noun = noun;
    }

    public static NounPhrase parsePhrase(@NotNull String rawString) {
        // Blank string check.
        if (rawString.isBlank()) {
            throw new IllegalArgumentException("Phrase cannot be blank");
        }

        // Strip the string of extra whitespaces.
        rawString = rawString.strip();

        // Make the string lowercase
        rawString = rawString.toLowerCase(Locale.ROOT);

        // Split the string into article and noun.
        Article article = PhraseUtils.getArticle(rawString);
        String noun = PhraseUtils.getNoun(rawString);

        // If article is null or definite, generate the indefinite article.
        // If the article is already indefinite, don't do anything. This is because
        // certain words may have custom indefinite articles that is different from
        // the generated version. For example, honor does not start with a vowel
        // but still requires “an”. Not touching the article if it is already
        // indefinite allows this custom behavior to be preserved.
        if (article == null || article.isDefinite()) {
            article = PhraseUtils.generateAppropriateIndefiniteArticle(noun);
        }

        return new NounPhrase(article, noun);
    }

    @Override
    public String toString() {
        return new StringJoiner(" ")
                .add(article.getText())
                .add(noun)
                .toString();
    }
}
