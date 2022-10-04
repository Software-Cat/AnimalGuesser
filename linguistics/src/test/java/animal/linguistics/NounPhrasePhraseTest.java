package animal.linguistics;

import animal.linguistics.phrase.NounPhrase;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class NounPhrasePhraseTest {

    @Test
    void parsePhrase() {
        assertEquals("a cat", NounPhrase.parsePhrase("cat").toString());
        assertEquals("a cat", NounPhrase.parsePhrase("a cat").toString());
        assertEquals("a cat", NounPhrase.parsePhrase("the cat").toString());
        assertEquals("an ape", NounPhrase.parsePhrase("ape").toString());
        assertEquals("an ape", NounPhrase.parsePhrase("an ape").toString());
        assertEquals("an ape", NounPhrase.parsePhrase("the ape").toString());
        assertEquals("an elephant", NounPhrase.parsePhrase("an elephant").toString());
        assertEquals("a unicorn", NounPhrase.parsePhrase("a unicorn").toString());
        assertEquals("a xantic sargo", NounPhrase.parsePhrase("a xantic sargo").toString());
        assertEquals("an xeme", NounPhrase.parsePhrase("an xeme").toString());
    }
}
