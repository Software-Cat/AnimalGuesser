package animal.linguistics.partofspeech;

import animal.linguistics.prop.Gender;
import animal.linguistics.prop.Number;
import animal.linguistics.prop.Person;

public interface Verb {

    String conjugate(Person person, Number number, Gender gender);

    Verb negative();
}
