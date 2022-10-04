package animal.linguistics.partofspeech;

import animal.linguistics.prop.Gender;
import animal.linguistics.prop.Number;
import animal.linguistics.prop.Person;
import org.jetbrains.annotations.Nullable;

public enum Pronoun implements Noun {
    I(Person.FIRST, Number.SINGULAR, Gender.NEUTRAL),
    YOU(Person.SECOND, Number.SINGULAR, Gender.NEUTRAL),
    HE(Person.THIRD, Number.SINGULAR, Gender.MASCULINE),
    SHE(Person.THIRD, Number.SINGULAR, Gender.FEMININE),
    IT(Person.THIRD, Number.SINGULAR, Gender.NEUTRAL),
    WE(Person.FIRST, Number.PLURAL, Gender.NEUTRAL),
    THEY(Person.FIRST, Number.PLURAL, Gender.NEUTRAL);

    private final Person PERSON;
    private final Number NUMBER;
    private final Gender GENDER;

    Pronoun(Person person, Number number, Gender gender) {
        this.PERSON = person;
        this.NUMBER = number;
        this.GENDER = gender;
    }

    public static @Nullable Pronoun parsePronoun(@Nullable String word) {
        if (word == null) {
            return null;
        }

        for (Pronoun pronoun : Pronoun.values()) {
            if (pronoun.name().equalsIgnoreCase(word)) {
                return pronoun;
            }
        }

        return null;
    }

    public Person getPerson() {
        return PERSON;
    }

    public Number getNumber() {
        return NUMBER;
    }

    public Gender getGender() {
        return GENDER;
    }

    @Override
    public String toString() {
        return name().toLowerCase();
    }
}
