package animal.linguistics.partofspeech;

import animal.linguistics.prop.Gender;
import animal.linguistics.prop.Number;
import animal.linguistics.prop.Person;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public enum AuxiliaryVerb implements Verb {
    TO_BE("am", "are", "is", "are", "are", "are"),
    TO_HAVE("have", "have", "has", "have", "have", "have"),
    TO_BE_ABLE_TO("can", "can", "can"),
    TO_NOT_BE("am not", "aren't", "isn't", "aren't", "aren't", "aren't"),
    TO_NOT_HAVE("don't have", "don't have", "doesn't have", "don't have", "don't have", "don't have"),
    TO_BE_UNABLE_TO("can't", "can't", "can't", "can't", "can't", "can't");

    private final List<String> CONJUGATIONS;

    AuxiliaryVerb(String... conjugations) {
        this.CONJUGATIONS = List.of(conjugations);
    }

    public static @Nullable AuxiliaryVerb parseVerb(@Nullable String word) {
        if (word == null) {
            return null;
        }

        for (AuxiliaryVerb verb : AuxiliaryVerb.values()) {
            for (String conj : verb.CONJUGATIONS) {
                if (word.equalsIgnoreCase(conj)) {
                    return verb;
                }
            }
        }

        return null;
    }

    @Override
    public String conjugate(Person person, Number number, Gender gender) {
        return CONJUGATIONS.get((number.intValue() - 1) * 3 + person.intValue() - 1);
    }

    @Override
    public Verb negative() {
        switch (this) {
            case TO_BE -> {
                return TO_NOT_BE;
            }
            case TO_HAVE -> {
                return TO_NOT_HAVE;
            }
            case TO_BE_ABLE_TO -> {
                return TO_BE_UNABLE_TO;
            }
            case TO_NOT_BE -> {
                return TO_BE;
            }
            case TO_NOT_HAVE -> {
                return TO_HAVE;
            }
            case TO_BE_UNABLE_TO -> {
                return TO_BE_ABLE_TO;
            }
            default -> throw new IllegalStateException("Unexpected value: " + this);
        }
    }
}
