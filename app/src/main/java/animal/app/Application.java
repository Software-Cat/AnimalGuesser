package animal.app;

import animal.base.Animal;
import animal.base.DecisionTree;
import animal.base.Node;
import animal.linguistics.clause.Statement;
import animal.utils.greet.Greeter;
import animal.utils.greet.TimedGreeter;
import org.apache.commons.lang3.tuple.Pair;

import java.util.Collection;
import java.util.Map;
import java.util.StringJoiner;

import static animal.app.AskerRegistry.*;

public class Application implements Runnable {

    private final Greeter greeter = new TimedGreeter();

    private final DecisionTree<Animal> tree = new DecisionTree<>();

    @Override
    public void run() {
        start();

        boolean running = true;
        replayAsker.setContext("Would you like to play again?");
        while (running) {
            guess();

            allKnowledge();

            running = replayAsker.get();
            System.out.println();
        }

        end();
    }

    private void start() {
        // Greeting
        greeter.hello();
        System.out.println();

        // First animal
        System.out.println("I want to learn about animals.");
        animalAsker.setContext("Which animal do you like most?");
        Animal favAnimal = animalAsker.get();
        tree.addYes(null, null, favAnimal);
        System.out.println("Wonderful! I've learned so much about animals!");

        // Introduce game
        System.out.println("Let's play a game!");
        System.out.println("You think of an animal, and I guess it.");
        confirmationAsker.setContext("Press enter when you're ready.");
        confirmationAsker.get();
        System.out.println();
    }

    private void end() {
        greeter.goodbye();
    }

    private void guess() {
        DecisionTree<Animal>.LeafNode result = tree.traverseWith((DecisionTree<Animal>.BranchNode node) -> {
            branchChooser.setContext(node);
            return branchChooser.get();
        });
        Animal resultAnimal = result.tryGetValue().get();

        statementYNAsker.setContext(Statement.parseStatement("It is " + resultAnimal));
        boolean guessedRight = statementYNAsker.get();

        if (!guessedRight) {
            learnNewAnimal(result);
        }
    }

    private void allKnowledge() {
        // Animal facts
        System.out.println("I have learned the following facts about animals:");

        for (Map.Entry<Animal, Collection<Statement>> entry : tree.allKnowledge().asMap().entrySet()) {
            StringJoiner joiner = new StringJoiner(" ");

            // Add animal noun with definite article to joiner
            for (Statement statement : entry.getValue()) {
                joiner.add(statement.withSubject(
                        entry.getKey().getSpecies().withArticle(true)
                ).toString());
            }

            System.out.println("  - " + joiner);
        }

        // All questions
        System.out.println("I can distinguish these animals by asking the questions:");

        for (Node<Animal> node : tree) {
            if (node.isBranch()) {
                System.out.println("  - " + node.tryGetAsBranch().get().getStatement().toQuestion());
            }
        }

        // Conclusion
        System.out.println("Nice! I've learned so much about animals!");
        System.out.println();
    }

    private void learnNewAnimal(DecisionTree<Animal>.LeafNode wrongGuess) {
        // Get animal actually thought of
        animalAsker.setContext("I give up. What animal do you have in mind?");
        Animal newAnimal = animalAsker.get();

        // Get fact differentiating new animal
        statementAsker.setContext(Pair.of(wrongGuess.tryGetValue().get(), newAnimal));
        Statement newFact = statementAsker.get();

        // Get whether fact is true for new animal
        statementTruthfulnessAsker.setContext(newAnimal);
        boolean newFactTrueForNewAnimal = statementTruthfulnessAsker.get();

        // Update tree
        if (newFactTrueForNewAnimal) {
            tree.addYes(wrongGuess, newFact, newAnimal);
        } else {
            tree.addNo(wrongGuess, newFact, newAnimal);
        }
    }

    @Deprecated
    private void deprecated() {
        // Ask animals
        animalAsker.setContext("Enter the first animal:");
        Animal animal1 = animalAsker.get();
        animalAsker.setContext("Enter the second animal:");
        Animal animal2 = animalAsker.get();

        // Ask statement
        statementAsker.setContext(Pair.of(animal1, animal2));
        Statement statement = statementAsker.get();
        statementTruthfulnessAsker.setContext(animal2);
        boolean trueForTwo = statementTruthfulnessAsker.get();

        // Statistics
        System.out.println("I learned the following facts about animals:");
        if (trueForTwo) {
            System.out.println(" - " + statement.withSubject(
                    animal1.getSpecies().withArticle(true)).negative());
            System.out.println(" - " + statement.withSubject(
                    animal2.getSpecies().withArticle(true)));
        } else {
            System.out.println(" - " + statement.withSubject(
                    animal1.getSpecies().withArticle(true)));
            System.out.println(" - " + statement.withSubject(
                    animal2.getSpecies().withArticle(true)).negative());
        }

        System.out.println("I can distinguish these animals by asking the question:");
        System.out.println(" - " + statement.toQuestion());

        // Tree generation
        DecisionTree<Animal>.LeafNode animal1Node = tree.addYes(null, null, animal1);
        if (trueForTwo) {
            tree.addYes(animal1Node, statement, animal2);
        } else {
            tree.addNo(animal1Node, statement, animal2);
        }

        // Tree traversal
        DecisionTree<Animal>.LeafNode result = tree.traverseWith((DecisionTree<Animal>.BranchNode node) -> {
            branchChooser.setContext(node);
            return branchChooser.get();
        });

        System.out.println(result.tryGetValue().get());

        // Goodbye
        System.out.println();
        greeter.goodbye();
    }
}
