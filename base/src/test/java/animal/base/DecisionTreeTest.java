package animal.base;

import animal.linguistics.clause.Statement;
import animal.linguistics.phrase.NounPhrase;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

class DecisionTreeTest {

    DecisionTree<Animal> tree;

    DecisionTree.LeafNode<Animal> dogNode;
    DecisionTree.LeafNode<Animal> catNode;

    @BeforeEach
    void setUp() {
        Animal cat = new Animal(NounPhrase.parsePhrase("cat"));
        Animal dog = new Animal(NounPhrase.parsePhrase("dog"));
        Animal fish = new Animal(NounPhrase.parsePhrase("fish"));
        Animal monkey = new Animal(NounPhrase.parsePhrase("monkey"));
        Statement statementCD = Statement.parseStatement("It can climb trees.");
        Statement statementDF = Statement.parseStatement("It can swim.");
        Statement statementCM = Statement.parseStatement("It can't swing across branches.");

        tree = new DecisionTree<>();

        dogNode = tree.addYes(null, null, dog);
        catNode = tree.addYes(dogNode, statementCD, cat);
        tree.addYes(dogNode, statementDF, fish);
        tree.addNo(catNode, statementCM, monkey);
    }

    @Test
    void addYes() {
        Assertions.assertEquals("It can climb trees.", tree.root.tryGetAsBranch()
                .get()
                .getStatement()
                .toString());
    }

    @Test
    void pathTo() {
        Assertions.assertEquals(List.of(false, false), tree.pathTo(dogNode));
        Assertions.assertEquals(List.of(true, true), tree.pathTo(catNode));
    }
}
