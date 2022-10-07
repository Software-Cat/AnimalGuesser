package animal.app;

import animal.base.Animal;
import animal.linguistics.clause.Statement;
import animal.utils.greet.Greeter;
import animal.utils.greet.TimedGreeter;
import org.apache.commons.lang3.tuple.Pair;

import static animal.app.AskerRegistry.*;

public class Application implements Runnable {

    private final Greeter greeter = new TimedGreeter();
    
    @Override
    public void run() {
        // Greeting
        greeter.hello();
        System.out.println();

        // Ask animals
        animalAsker.setContext("first");
        Animal animal1 = animalAsker.get();
        animalAsker.setContext("second");
        Animal animal2 = animalAsker.get();

        // Ask statement
        statementAsker.setContext(Pair.of(animal1, animal2));
        Statement statement = statementAsker.get();
        animalYNAsker.setContext(animal2);
        boolean trueForTwo = animalYNAsker.get();

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

        // Goodbye
        System.out.println();
        greeter.goodbye();
    }
}
