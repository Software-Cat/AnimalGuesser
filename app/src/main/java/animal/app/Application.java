package animal.app;

import animal.base.Animal;
import animal.base.DecisionTree;
import animal.base.Node;
import animal.linguistics.clause.Statement;
import animal.utils.greet.Greeter;
import animal.utils.greet.TimedGreeter;
import org.apache.commons.lang3.tuple.Pair;

import java.io.IOException;
import java.util.Collection;
import java.util.Map;
import java.util.StringJoiner;

import static animal.app.AskerRegistry.*;

public class Application implements Runnable {

    private final Greeter greeter = new TimedGreeter();

    private final DecisionTree<Animal> tree = new DecisionTree<>();

    @Override
    public void run() {
        if (tree.serializer().fileExists()) {
            startOldUser();
        } else {
            startNewUser();
        }

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

    private void startNewUser() {
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

    private void startOldUser() {
        // Greeting
        greeter.hello();
        System.out.println();

        // Introduce game
        System.out.println("I know a lot about animals.");
        System.out.println("Let's play a game!");
        System.out.println("You think of an animal, and I guess it.");
        confirmationAsker.setContext("Press enter when you're ready.");
        confirmationAsker.get();
        System.out.println();

        // Load data
        try {
            tree.serializer().read();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private void end() {
        greeter.goodbye();

        // Serialize tree
        try {
            tree.serializer().write();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private void guess() {
        DecisionTree.LeafNode<Animal> result = tree.traverseWith((DecisionTree.BranchNode<Animal> node) -> {
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

    private void learnNewAnimal(DecisionTree.LeafNode<Animal> wrongGuess) {
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
}
