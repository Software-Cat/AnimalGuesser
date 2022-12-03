package animal.linguistics.phrase;

import animal.linguistics.partofspeech.Article;
import animal.linguistics.prop.Gender;
import animal.linguistics.prop.Number;
import animal.linguistics.prop.Person;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Getter;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public class NounPhrase implements animal.linguistics.partofspeech.Noun {

    @NotNull
    @Getter
    private final String noun;

    @NotNull
    @Getter
    private final Article article;

    /**
     * This constructor should only be used by Jackson
     */
    public NounPhrase() {
        noun = null;
        article = null;
    }

    protected NounPhrase(@NotNull String noun, @NotNull Article article) {
        this.noun = noun;
        this.article = article;
    }

    protected NounPhrase(@NotNull String noun) {
        this.noun = noun;
        this.article = Article.defaultIndefiniteArticle(noun);
    }

    /**
     * Parses a noun phrase from a string
     *
     * @param phrase the string
     * @return the noun phrase with indefinite article
     */
    public static @NotNull NounPhrase parsePhrase(@NotNull String phrase) {
        List<String> words = List.of(phrase.toLowerCase().split("\\s+"));
        String noun;
        Article article;

        if (words.size() == 1) {
            return new NounPhrase(words.get(0));
        } else if (Article.isArticle(words.get(0))) {
            noun = String.join(" ", words.subList(1, words.size()));
            article = Article.parseArticle(words.get(0));

            assert article != null;
            if (article.isDefinite()) {
                article = Article.defaultIndefiniteArticle(noun);
            }

            return new NounPhrase(noun, article);
        } else {
            return new NounPhrase(words.get(0));
        }
    }

    /**
     * Returns new noun phrase with specified article
     * <p>
     * If the user wants definite, then always use {@link Article#THE}.
     * If the user wants indefinite, then use default if A/AN unknown.
     * If the user wants indefinite but the default A/AN is overridden,
     * use the override.
     *
     * @param definite is the new noun phrase definite
     * @return changed noun phrase
     */
    public @NotNull NounPhrase withArticle(boolean definite) {
        if (definite) {
            return new NounPhrase(noun, Article.THE);
        } else if (this.article.isDefinite()) {
            return new NounPhrase(noun);
        } else {
            return this;
        }
    }

    @Override
    public String toString() {
        return String.join(" ", article.toString(), noun);
    }

    @JsonIgnore
    @Override
    public Person getPerson() {
        return Person.THIRD;
    }

    @Override
    @JsonIgnore
    public Number getNumber() {
        return Number.SINGULAR;
    }

    @Override
    @JsonIgnore
    public Gender getGender() {
        return Gender.NEUTRAL;
    }
}
