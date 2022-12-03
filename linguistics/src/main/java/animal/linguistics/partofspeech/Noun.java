package animal.linguistics.partofspeech;

import animal.linguistics.phrase.NounPhrase;
import animal.linguistics.prop.Gender;
import animal.linguistics.prop.Number;
import animal.linguistics.prop.Person;
import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;

@JsonTypeInfo(
        use = JsonTypeInfo.Id.NAME,
        property = "type")
@JsonSubTypes({
        @JsonSubTypes.Type(value = NounPhrase.class),
        @JsonSubTypes.Type(value = Pronoun.class),
})
public interface Noun {

    Person getPerson();

    Number getNumber();

    Gender getGender();
}
