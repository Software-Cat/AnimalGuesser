package animal.linguistics.clause;

import animal.linguistics.partofspeech.Noun;
import animal.linguistics.partofspeech.Verb;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import org.apache.commons.lang3.StringUtils;
import org.jetbrains.annotations.NotNull;

@EqualsAndHashCode
public abstract class MainClause implements Clause {

    @NotNull
    @Getter
    protected final Noun subject;

    @NotNull
    @Getter
    protected final Verb verb;

    // TODO: Find better implementation of object
    @NotNull
    @Getter
    protected final String object;

    protected MainClause(@NotNull Noun subject, @NotNull Verb verb, @NotNull String object) {
        this.subject = subject;
        this.verb = verb;
        this.object = object;
    }

    @Override
    public String toString() {
        String sentence = String.join(" ",
                subject.toString(),
                verb.conjugate(subject.getPerson(), subject.getNumber(), subject.getGender()),
                object);
        sentence = StringUtils.capitalize(sentence);
        return sentence + ".";
    }

    public abstract MainClause withSubject(Noun subject);
}
