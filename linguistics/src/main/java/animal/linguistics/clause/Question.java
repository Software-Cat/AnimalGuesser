package animal.linguistics.clause;

import animal.linguistics.partofspeech.Noun;
import animal.linguistics.partofspeech.Verb;
import lombok.EqualsAndHashCode;
import org.apache.commons.lang3.StringUtils;
import org.jetbrains.annotations.NotNull;

@EqualsAndHashCode(callSuper = true)
public class Question extends MainClause {

    protected Question(@NotNull Noun subject, @NotNull Verb verb, @NotNull String object) {
        super(subject, verb, object);
    }

    @Override
    public String toString() {
        String sentence = String.join(" ",
                verb.conjugate(subject.getPerson(), subject.getNumber(), subject.getGender()),
                subject.toString(),
                object);
        sentence = StringUtils.capitalize(sentence);
        return sentence + "?";
    }

    @Override
    public Question withSubject(Noun newSubject) {
        return new Question(newSubject, verb, object);
    }

    public @NotNull Statement toStatement() {
        return new Statement(subject, verb, object);
    }
}
