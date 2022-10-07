package animal.app;

import animal.base.Animal;
import animal.base.DecisionTree;
import animal.base.Node;
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

    public static final Asker<Animal, String> animalAsker = AskerBuilder.builder(String.class)
            .queryContextTransformer("Enter the %s animal"::formatted)
            .addTransformer(String::trim)
            .addPredicate((String s) -> !s.isBlank())
            .addTransformer(NounPhrase::parsePhrase)
            .addTransformer(Animal::new)
            .persistent(true)
            .build();

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

    public static final Asker<Node<Animal>, DecisionTree<Animal>.BranchNode> branchChooser = AskerBuilder.builder((Class<DecisionTree<Animal>.BranchNode>) (Class) DecisionTree.class)
            .addTransformer(String::strip)
            .addPredicate((String s) -> !s.isBlank())
            .addTransformer((String s, DecisionTree<Animal>.BranchNode current) -> {
                statementAsker.get();
                return current.getYesBranch();
            })
            .build();

    private static final Function<String, Boolean> yesNoProcessor = (String s) -> {
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

    public static final Asker<Boolean, Animal> animalYNAsker = AskerBuilder.builder(Animal.class)
            .queryContextTransformer("Is it correct for %s?"::formatted)
            .addTransformer(String::trim)
            .addPredicate((String s) -> !s.isBlank())
            .addTransformer(yesNoProcessor)
            .persistent(true)
            .retryPhraseSupplier(retryPhraseSupplier)
            .build();

    public static final Asker<Boolean, Statement> statementYNAsker = AskerBuilder.builder(Statement.class)
            .queryContextTransformer((Statement s) -> s.toQuestion().toString())
            .addTransformer(String::trim)
            .addPredicate((String s) -> !s.isBlank())
            .addTransformer(yesNoProcessor)
            .persistent(true)
            .retryPhraseSupplier(retryPhraseSupplier)
            .build();
}
