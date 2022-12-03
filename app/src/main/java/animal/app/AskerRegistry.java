package animal.app;

import animal.base.Animal;
import animal.base.DecisionTree;
import animal.input.Asker;
import animal.input.AskerBuilder;
import animal.linguistics.clause.Statement;
import animal.linguistics.phrase.NounPhrase;
import org.apache.commons.lang3.tuple.Pair;

import java.util.InputMismatchException;
import java.util.List;
import java.util.Objects;
import java.util.Random;
import java.util.function.Function;
import java.util.function.Supplier;

public abstract class AskerRegistry {

    /**
     * Accepts arbitrary user input, returns once user presses enter.
     */
    public static final Asker<String, String> confirmationAsker = AskerBuilder.builder(String.class)
            .queryContextTransformer((String s) -> s)
            .addTransformer((String s) -> "")
            .build();

    /**
     * Asks for an animal.
     */
    public static final Asker<Animal, String> animalAsker = AskerBuilder.builder(String.class)
            .queryContextTransformer((String s) -> s)
            .addTransformer(String::trim)
            .addPredicate((String s) -> !s.isBlank())
            .addTransformer(NounPhrase::parsePhrase)
            .addTransformer(Animal::new)
            .persistent(true)
            .build();

    /**
     * Asks for a statement describing an animal.
     */
    public static final Asker<Statement, Pair<Animal, Animal>> statementAsker = AskerBuilder.builder((Class<Pair<Animal, Animal>>) (Class<?>) Pair.class)
            .queryContextTransformer((Pair<Animal, Animal> animals) -> """
                    Specify a fact that distinguishes %s from %s.
                    The sentence should be of the format: 'It can/has/is ...'.
                    """.formatted(animals.getLeft(), animals.getRight()))
            .addTransformer(String::trim)
            .addPredicate((String s) -> !s.isBlank())
            .addTransformer(Statement::parseStatement)
            .addPredicate(Objects::nonNull)
            .persistent(true)
            .retryPhraseSupplier(() ->
                    """
                            The examples of a statement:
                             - It can fly
                             - It has horn
                             - It is a mammal
                            Specify a fact that distinguishes a cat from a shark.
                            The sentence should be of the format: 'It can/has/is ...'."""
            )
            .build();

    private static final Function<String, Boolean> yesNoProcessor = (String s) -> {
        s = s.toLowerCase();
        s = s.replaceFirst("\\p{Punct}", "");
        switch (s) {
            case "y", "yes", "yeah", "yep", "sure", "right", "affirmative", "correct", "indeed", "you bet", "exactly", "you said it" -> {
                return true;
            }
            case "n", "no", "no way", "nah", "nope", "negative", "i don't think so", "yeah no" -> {
                return false;
            }
        }

        throw new InputMismatchException(
                "The input (\"%s\") cannot be converted to a valid boolean response.".formatted(s));
    };

    private static final Supplier<String> retryPhraseSupplier = new Supplier<>() {

        private final Random random = new Random();

        private final List<String> phrases = List.of(
                "I'm not sure if I caught you: was it yes or no?",
                "Funny, I still don't understand, is it yes or no?",
                "Oh, it is too complicated for me: just tell me yes or no.",
                "Could you please simply say yes or no?",
                "Oh, no, don't try to confuse me: say yes or no.");

        @Override
        public String get() {
            return phrases.get(random.nextInt(phrases.size()));
        }
    };

    public static final Asker<Boolean, DecisionTree.BranchNode<Animal>> branchChooser = AskerBuilder.builder((Class<DecisionTree.BranchNode<Animal>>) (Class) DecisionTree.class)
            .queryContextTransformer((DecisionTree.BranchNode<Animal> node) -> node.getStatement().toQuestion().toString())
            .addTransformer(String::strip)
            .addPredicate((String s) -> !s.isBlank())
            .addTransformer(yesNoProcessor)
            .persistent(true)
            .retryPhraseSupplier(retryPhraseSupplier)
            .build();

    public static final Asker<Boolean, String> replayAsker = AskerBuilder.builder(String.class)
            .queryContextTransformer((String s) -> s)
            .addTransformer(String::strip)
            .addPredicate((String s) -> !s.isBlank())
            .addTransformer(yesNoProcessor)
            .persistent(true)
            .retryPhraseSupplier(retryPhraseSupplier)
            .build();

    /**
     * Asks whether a statement is correct for an animal.
     */
    public static final Asker<Boolean, Animal> statementTruthfulnessAsker = AskerBuilder.builder(Animal.class)
            .queryContextTransformer("Is it correct for %s?"::formatted)
            .addTransformer(String::trim)
            .addPredicate((String s) -> !s.isBlank())
            .addTransformer(yesNoProcessor)
            .persistent(true)
            .retryPhraseSupplier(retryPhraseSupplier)
            .build();

    /**
     * Converts statement about animal to question form and asks it as a yes-no question.
     */
    public static final Asker<Boolean, Statement> statementYNAsker = AskerBuilder.builder(Statement.class)
            .queryContextTransformer((Statement s) -> s.toQuestion().toString())
            .addTransformer(String::trim)
            .addPredicate((String s) -> !s.isBlank())
            .addTransformer(yesNoProcessor)
            .persistent(true)
            .retryPhraseSupplier(retryPhraseSupplier)
            .build();
}
