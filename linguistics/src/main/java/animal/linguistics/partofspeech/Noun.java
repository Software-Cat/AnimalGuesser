package animal.linguistics.partofspeech;

import animal.linguistics.prop.Gender;
import animal.linguistics.prop.Number;
import animal.linguistics.prop.Person;

public interface Noun {

    Person getPerson();

    Number getNumber();

    Gender getGender();
}
