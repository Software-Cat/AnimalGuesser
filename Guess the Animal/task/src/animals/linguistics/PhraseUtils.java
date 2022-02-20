package animals.linguistics;

import org.apache.commons.lang3.RegExUtils;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Arrays;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

public class PhraseUtils {

    // Match nouns starting with a vowel.
    @NotNull
    private static final Pattern STARTS_WITH_VOWEL = Pattern.compile("^[aeiou].*$", Pattern.CASE_INSENSITIVE);

    // Match phrases with an article and another word. Phrases containing only the article is not matched.
    @NotNull
    private static final Pattern HAS_ARTICLE = Pattern.compile("(an?|the)\\s+\\S+.*", Pattern.CASE_INSENSITIVE);

    // Matches any article, even if it is the only word in the phrase.
    @NotNull
    private static final Pattern ONLY_ARTICLE = Pattern.compile("\\b(an?|the)\\b", Pattern.CASE_INSENSITIVE);

    /**
     * Chooses the appropriate indefinite article based on whether the noun starts with a vowel.
     *
     * @param noun the noun to generate an article for
     * @return the generated article
     */
    public static @NotNull Article generateAppropriateIndefiniteArticle(@NotNull String noun) {
        Matcher matcher = STARTS_WITH_VOWEL.matcher(noun);

        if (matcher.matches()) {
            return Article.AN;
        } else {
            return Article.A;
        }
    }

    public static boolean hasArticle(@NotNull String phrase) {
        return HAS_ARTICLE.matcher(phrase).matches();
    }

    public static @Nullable Article getArticle(@NotNull String phrase) {
        if (hasArticle(phrase)) {
            Matcher matcher = ONLY_ARTICLE.matcher(phrase);

            // We already know the string contains an article, so there's no need
            // to use the result from the matcher.
            boolean ignored = matcher.find();

            String article = matcher.group();
            return Article.fromString(article);
        }

        return null;
    }

    public static @NotNull String stripInBetweenSpaces(@NotNull String phrase) {
        return Arrays.stream(phrase.split("\\s"))
                .filter((String s) -> !s.isBlank())
                .collect(Collectors.joining(" "));
    }

    public static @NotNull String getNoun(@NotNull String phrase) {
        if (hasArticle(phrase)) {
            // Remove the first occurrence of the article, if there's a second occurrence
            // it would be a part of the noun itself and should not be removed.
            phrase = RegExUtils.removeFirst(phrase, ONLY_ARTICLE);

            // Remove extra spaces produced after removing the article.
            phrase = phrase.strip();

            // If the noun is a compound word and contains extra spaces between, remove them.
            phrase = stripInBetweenSpaces(phrase);

            return phrase;
        }

        // If the phrase has no article then return it directly.
        return stripInBetweenSpaces(phrase);
    }
}
