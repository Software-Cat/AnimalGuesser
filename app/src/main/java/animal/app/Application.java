package animal.app;

import animal.base.Animal;
import animal.input.Asker;
import animal.input.AskerBuilder;
import animal.linguistics.clause.Statement;
import animal.linguistics.phrase.NounPhrase;
import animal.utils.greet.Greeter;
import animal.utils.greet.TimedGreeter;

import java.util.InputMismatchException;
import java.util.List;

public class Application implements Runnable {

    private final Greeter greeter = new TimedGreeter();

    private final Asker<Animal> animalAsker = AskerBuilder.asker()
            .withQuery("Enter an animal:")
            .addTransformer(String::trim)
            .addPredicate((String s) -> !s.isBlank())
            .addTransformer(NounPhrase::parsePhrase)
            .addTransformer(Animal::new)
            .persistent(true)
            .build();

    private final Asker<Boolean> yesNoAsker = AskerBuilder.asker()
            .withQuery("Is it a ...?")
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

    private final Asker<Statement> statementAsker = AskerBuilder.asker()
            .withQuery("Specify a fact that distinguishes a ... from a ...")
            .addTransformer(String::trim)
            .addPredicate((String s) -> !s.isBlank())
            .addTransformer(Statement::parseStatement)
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
        greeter.hello();
        System.out.println();

        animalAsker.setQuery("Enter the first animal:");
        Animal animal1 = animalAsker.get();
        animalAsker.setQuery("Enter the second animal:");
        Animal animal2 = animalAsker.get();

        statementAsker.setQuery("""
                Specify a fact that distinguishes %s from %s.
                The sentence should be of the format: 'It can/has/is ...'.
                """.formatted(animal1, animal2));
        Statement statement = statementAsker.get();
        System.out.println(statement);
        System.out.println(statement.toQuestion());

        System.out.println();
        greeter.goodbye();
    }
}
