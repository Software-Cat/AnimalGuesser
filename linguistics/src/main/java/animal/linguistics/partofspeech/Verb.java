package animal.linguistics.partofspeech;

import animal.linguistics.prop.Gender;
import animal.linguistics.prop.Number;
import animal.linguistics.prop.Person;
import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;

@JsonTypeInfo(
        use = JsonTypeInfo.Id.NAME,
        property = "type")
@JsonSubTypes({
        @JsonSubTypes.Type(value = AuxiliaryVerb.class),
})
public interface Verb {

    String conjugate(Person person, Number number, Gender gender);

    Verb negative();
}
