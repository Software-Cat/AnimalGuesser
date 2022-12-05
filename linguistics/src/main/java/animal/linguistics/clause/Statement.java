package animal.linguistics.clause;

import animal.linguistics.partofspeech.AuxiliaryVerb;
import animal.linguistics.partofspeech.Noun;
import animal.linguistics.partofspeech.Pronoun;
import animal.linguistics.partofspeech.Verb;
import lombok.EqualsAndHashCode;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;

@EqualsAndHashCode(callSuper = true)
public class Statement extends MainClause {

    /**
     * This constructor should only be used by Jackson
     */
    public Statement() {
        this(null, null, null);
    }

    protected Statement(@NotNull Noun subject, @NotNull Verb verb, @NotNull String object) {
        super(subject, verb, object);
    }

    public static @Nullable Statement parseStatement(@Nullable String statement) {
        if (statement == null) {
            return null;
        }

        statement = statement.replaceAll("([.,?!\"'])", " ");
        List<String> words = List.of(statement.split("\\s+"));

        Noun subject = Pronoun.parsePronoun(words.get(0));
        Verb verb = AuxiliaryVerb.parseVerb(words.get(1));
        String object = String.join(" ", words.subList(2, words.size()));

        if (subject == null || verb == null) {
            return null;
        }

        return new Statement(subject, verb, object);
    }

    @Override
    public Statement withSubject(Noun newSubject) {
        return new Statement(newSubject, verb, object);
    }

    public @NotNull Question toQuestion() {
        return new Question(subject, verb, object);
    }

    public @NotNull Statement negative() {
        return new Statement(subject, verb.negative(), object);
    }
}
