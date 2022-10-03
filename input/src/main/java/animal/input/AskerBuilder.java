package animal.input;

import org.jetbrains.annotations.NotNull;

import java.util.InputMismatchException;
import java.util.List;
import java.util.function.Function;
import java.util.function.Predicate;

public final class AskerBuilder<T> {

    @NotNull
    private String inputPrompt = "> ";

    @NotNull
    private String query = "";

    private Function<@NotNull String, @NotNull T> transformer;

    private boolean persistent = false;

    private List<String> retryPhrases = List.of("Please enter a valid response.");

    private AskerBuilder() {
    }

    public static AskerBuilder<String> asker() {
        AskerBuilder<String> builder = new AskerBuilder<>();
        return builder.withTransformer(Function.identity());
    }

    public AskerBuilder<T> withInputPrompt(String inputPrompt) {
        this.inputPrompt = inputPrompt;
        return this;
    }

    public AskerBuilder<T> withQuery(String query) {
        this.query = query;
        return this;
    }

    /**
     * Composes the new transformer after already-existing transformers.
     *
     * @param transformer the new transformer
     * @param <R>         the type returned by the new transformer
     * @return builder
     */
    public <R> AskerBuilder<R> addTransformer(Function<T, R> transformer) {
        Function<String, R> newTransformer = this.transformer.andThen(transformer);
        return new AskerBuilder<R>().withQuery(query).withInputPrompt(inputPrompt).withTransformer(newTransformer);
    }

    /**
     * Composes the new predicate after already-existing transformers. An exception is thrown if the predicate fails.
     *
     * @param predicate the new predicate
     * @return builder
     */
    public AskerBuilder<T> addPredicate(Predicate<T> predicate) {
        return addTransformer((T t) -> {
            if (!predicate.test(t)) {
                throw new InputMismatchException("The input (\"%s\") does not satisfy the predicate (%s).".formatted(t, predicate));
            }
            return t;
        });
    }

    public AskerBuilder<T> persistent(boolean persistent) {
        this.persistent = persistent;
        return this;
    }

    public AskerBuilder<T> withRetryPhrases(List<String> retryPhrases) {
        this.retryPhrases = retryPhrases;
        return this;
    }

    /**
     * Completely overwrites the previous transformer with the new one.
     *
     * @param transformer the new transformer
     * @return builder
     */
    private AskerBuilder<T> withTransformer(Function<String, T> transformer) {
        this.transformer = transformer;
        return this;
    }

    public Asker<T> build() {
        Asker<T> asker;

        if (persistent) {
            PersistentAsker<T> persistentAsker = new PersistentAsker<>();
            persistentAsker.setRetryPhrases(retryPhrases);
            asker = persistentAsker;
        } else {
            asker = new ConcreteAsker<>();
        }

        asker.setQuery(query);
        asker.setInputPrompt(inputPrompt);
        asker.setTransformer(transformer);

        return asker;
    }
}
