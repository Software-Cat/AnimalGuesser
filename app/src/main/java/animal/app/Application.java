package animal.app;

import animal.base.Animal;
import animal.input.Asker;
import animal.input.AskerBuilder;
import animal.linguistics.clause.Statement;
import animal.linguistics.phrase.NounPhrase;
import animal.utils.greet.Greeter;
import animal.utils.greet.TimedGreeter;
import org.apache.commons.lang3.tuple.Pair;

import java.util.InputMismatchException;
import java.util.List;
import java.util.Objects;

public class Application implements Runnable {

    private final Greeter greeter = new TimedGreeter();

    private final Asker<Animal, String> animalAsker = AskerBuilder.asker(String.class)
            .withQueryContextTransformer("Enter the %s animal"::formatted)
            .addTransformer(String::trim)
            .addPredicate((String s) -> !s.isBlank())
            .addTransformer(NounPhrase::parsePhrase)
            .addTransformer(Animal::new)
            .persistent(true)
            .build();

    private final Asker<Boolean, Animal> yesNoAsker = AskerBuilder.asker(Animal.class)
            .withQueryContextTransformer("Is it correct for %s?"::formatted)
            .addTransformer(String::trim)
            .addPredicate((String s) -> !s.isBlank())
            .addTransformer((String s) -> {
                s = s.toLowerCase();
                s = s.replaceFirst("\\p{Punct}", "");
                switch (s) {
                    case "y", "yes", "yeah", "yep", "sure", "right", "affirmative",
                            "correct", "indeed", "you bet", "exactly", "you said it":
                        return true;
                    case "n", "no", "no way", "nah", "nope", "negative",
                            "i don't think so", "yeah no":
                        return false;
                }
                throw new InputMismatchException(
                        "The input (\"%s\") cannot be converted to a valid boolean response.".formatted(s));
            })
            .persistent(true)
            .withRetryPhrases(List.of(
                    "I'm not sure if I caught you: was it yes or no?",
                    "Funny, I still don't understand, is it yes or no?",
                    "Oh, it is too complicated for me: just tell me yes or no.",
                    "Could you please simply say yes or no?",
                    "Oh, no, don't try to confuse me: say yes or no."))
            .build();

    private final Asker<Statement, Pair<Animal, Animal>> statementAsker = AskerBuilder.asker((Class<Pair<Animal, Animal>>) (Class<?>) Pair.class)
            .withQueryContextTransformer((Pair<Animal, Animal> animals) -> """
                    Specify a fact that distinguishes %s from %s.
                    The sentence should be of the format: 'It can/has/is ...'.
                    """.formatted(animals.getLeft(), animals.getRight()))
            .addTransformer(String::trim)
            .addPredicate((String s) -> !s.isBlank())
            .addTransformer(Statement::parseStatement)
            .addPredicate(Objects::nonNull)
            .persistent(true)
            .withRetryPhrases(List.of(
                    """
                            The examples of a statement:
                             - It can fly
                             - It has horn
                             - It is a mammal
                            Specify a fact that distinguishes a cat from a shark.
                            The sentence should be of the format: 'It can/has/is ...'."""
            ))
            .build();

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
        yesNoAsker.setContext(animal2);
        boolean trueForTwo = yesNoAsker.get();

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
        System.out.println(" - " + statement.negative().toQuestion());

        // Goodbye
        System.out.println();
        greeter.goodbye();
    }
}
