package animal.base;

import animal.linguistics.clause.Statement;
import animal.linguistics.phrase.NounPhrase;
import org.junit.jupiter.api.Test;

class DecisionTreeTest {

    @Test
    void addYes() {
        Animal cat = new Animal(NounPhrase.parsePhrase("cat"));
        Animal dog = new Animal(NounPhrase.parsePhrase("dog"));
        Animal fish = new Animal(NounPhrase.parsePhrase("fish"));
        Animal monkey = new Animal(NounPhrase.parsePhrase("monkey"));
        Statement statementCD = Statement.parseStatement("It can climb trees.");
        Statement statementDF = Statement.parseStatement("It can swim.");
        Statement statementCM = Statement.parseStatement("It can't swing across branches.");

        DecisionTree<Animal> tree = new DecisionTree<>();

        DecisionTree<Animal>.LeafNode dogNode = tree.addYes(null, null, dog);
        DecisionTree<Animal>.LeafNode catNode = tree.addYes(dogNode, statementCD, cat);
        tree.addYes(dogNode, statementDF, fish);
        tree.addNo(catNode, statementCM, monkey);
    }
}
