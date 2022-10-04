package animal.linguistics.clause;

import animal.linguistics.partofspeech.Noun;
import animal.linguistics.partofspeech.Verb;
import org.apache.commons.lang3.StringUtils;
import org.jetbrains.annotations.NotNull;

public class Question implements Clause {

    @NotNull
    private final Noun subject;

    @NotNull
    private final Verb verb;

    @NotNull
    private final String object;

    protected Question(@NotNull Noun subject, @NotNull Verb verbPhrase, @NotNull String object) {
        this.subject = subject;
        this.verb = verbPhrase;
        this.object = object;
    }

    @Override
    public String toString() {
        String sentence = String.join(" ",
                verb.conjugate(subject.getPerson(), subject.getNumber(), subject.getGender()),
                subject.toString(),
                object);
        sentence = StringUtils.capitalize(sentence);
        sentence = sentence + "?";
        return sentence;
    }
}
