package animal.state;

import animal.base.Animal;
import animal.base.DecisionTree;
import animal.linguistics.clause.Statement;
import animal.state.base.AppState;
import animal.state.base.ApplicationBase;
import animal.state.base.SelectableState;
import org.apache.commons.lang3.tuple.Pair;
import org.jetbrains.annotations.NotNull;

import static animal.state.AskerRegistry.*;

public class PlayState extends SelectableState {

    public PlayState() {
        super("Play the guessing game");
    }

    @Override
    protected @NotNull AppState nextState(@NotNull ApplicationBase app) {
        booleanAsker.setContext("Would you like to play again?");
        if (booleanAsker.get()) {
            return new PlayState();
        } else {
            System.out.println();
            return new SelectorState();
        }
    }

    @Override
    public void execute(@NotNull ApplicationBase app) {
        // Introduce game
        System.out.println("You think of an animal, and I guess it.");
        confirmationAsker.setContext("Press enter when you're ready.");
        confirmationAsker.get();

        // Guess
        guess(app);
    }

    private void guess(@NotNull ApplicationBase app) {
        DecisionTree.LeafNode<Animal> result = app.getTree().traverseWith((DecisionTree.BranchNode<Animal> node) -> {
            branchChooser.setContext(node);
            return branchChooser.get();
        });
        Animal resultAnimal = result.tryGetValue().get();

        statementYNAsker.setContext(Statement.parseStatement("It is " + resultAnimal));
        boolean guessedRight = statementYNAsker.get();

        if (!guessedRight) {
            learnNewAnimal(app, result);
        }
    }

    private void learnNewAnimal(@NotNull ApplicationBase app, DecisionTree.LeafNode<Animal> wrongGuess) {
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
            app.getTree().addYes(wrongGuess, newFact, newAnimal);
        } else {
            app.getTree().addNo(wrongGuess, newFact, newAnimal);
        }

        // Feedback
        System.out.println("Wonderful! I've learned so much about animals!");
    }
}
