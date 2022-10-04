package animal.linguistics.clause;

import animal.linguistics.partofspeech.AuxiliaryVerb;
import animal.linguistics.partofspeech.Pronoun;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

class StatementTest {

    @Test
    void parseStatement() {
        Statement s = Statement.parseStatement("It can climb trees.");
        assertEquals(Pronoun.IT, s.subject);
        assertEquals(AuxiliaryVerb.TO_BE_ABLE_TO, s.verb);
        assertEquals("climb trees", s.object);
        assertEquals("It can climb trees.", s.toString());

        s = Statement.parseStatement("It has a horn");
        assertEquals(Pronoun.IT, s.subject);
        assertEquals(AuxiliaryVerb.TO_HAVE, s.verb);
        assertEquals("a horn", s.object);
        assertEquals("It has a horn.", s.toString());

        s = Statement.parseStatement("it is a shy animal");
        assertEquals(Pronoun.IT, s.subject);
        assertEquals(AuxiliaryVerb.TO_BE, s.verb);
        assertEquals("a shy animal", s.object);
        assertEquals("It is a shy animal.", s.toString());

        s = Statement.parseStatement("Is it a mammal?");
        assertNull(s);

        s = Statement.parseStatement("It is a mammal?");
        assertEquals(Pronoun.IT, s.subject);
        assertEquals(AuxiliaryVerb.TO_BE, s.verb);
        assertEquals("a mammal", s.object);
        assertEquals("It is a mammal.", s.toString());
    }

    @Test
    void toQuestion() {
        Question q = Statement.parseStatement("It can   climb trees.").toQuestion();
        assertEquals(Pronoun.IT, q.subject);
        assertEquals(AuxiliaryVerb.TO_BE_ABLE_TO, q.verb);
        assertEquals("climb trees", q.object);
        assertEquals("Can it climb trees?", q.toString());

        q = Statement.parseStatement("It has a horn").toQuestion();
        assertEquals(Pronoun.IT, q.subject);
        assertEquals(AuxiliaryVerb.TO_HAVE, q.verb);
        assertEquals("a horn", q.object);
        assertEquals("Has it a horn?", q.toString());

        q = Statement.parseStatement("it is a shy animal").toQuestion();
        assertEquals(Pronoun.IT, q.subject);
        assertEquals(AuxiliaryVerb.TO_BE, q.verb);
        assertEquals("a shy animal", q.object);
        assertEquals("Is it a shy animal?", q.toString());

        q = Statement.parseStatement("It is a mammal?").toQuestion();
        assertEquals(Pronoun.IT, q.subject);
        assertEquals(AuxiliaryVerb.TO_BE, q.verb);
        assertEquals("a mammal", q.object);
        assertEquals("Is it a mammal?", q.toString());
    }

    @Test
    void negative() {
        Statement s = Statement.parseStatement("It can climb trees.").negative();
        assertEquals("It can't climb trees.", s.toString());

        s = Statement.parseStatement("It has a horn").negative();
        assertEquals("It doesn't have a horn.", s.toString());

        s = Statement.parseStatement("it is a shy animal").negative();
        assertEquals("It isn't a shy animal.", s.toString());

        s = Statement.parseStatement("It is a mammal?").negative();
        assertEquals("It isn't a mammal.", s.toString());
    }
}
