package animal.linguistics.clause;

import animal.linguistics.partofspeech.AuxiliaryVerb;
import animal.linguistics.partofspeech.Noun;
import animal.linguistics.partofspeech.Pronoun;
import animal.linguistics.partofspeech.Verb;
import org.apache.commons.lang3.StringUtils;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class Statement implements Clause {

    @NotNull
    private final Noun subject;

    @NotNull
    private final Verb verb;

    // TODO: Better implementation of object
    @NotNull
    private final String object;

    protected Statement(@NotNull Noun subject, @NotNull Verb verbPhrase, @NotNull String object) {
        this.subject = subject;
        this.verb = verbPhrase;
        this.object = object;
    }

    public static @Nullable Statement parseStatement(@Nullable String statement) {
        if (statement == null) {
            return null;
        }

        statement = statement.replaceAll("\\p{Punct}", " ");
        List<String> words = List.of(statement.split("\\s+"));

        Noun subject = Pronoun.parsePronoun(words.get(0));
        Verb verb = AuxiliaryVerb.parseVerb(words.get(1));
        String object = String.join(" ", words.subList(2, words.size()));
        assert subject != null;
        assert verb != null;

        return new Statement(subject, verb, object);
    }

    public @NotNull Question toQuestion() {
        return new Question(subject, verb, object);
    }

    @Override
    public String toString() {
        String sentence = String.join(" ",
                subject.toString(),
                verb.conjugate(subject.getPerson(), subject.getNumber(), subject.getGender()),
                object);
        sentence = StringUtils.capitalize(sentence);
        sentence = sentence + ".";
        return sentence;
    }
}
