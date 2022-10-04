package animal.input;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.InputMismatchException;
import java.util.List;
import java.util.function.Function;
import java.util.function.Predicate;

public final class AskerBuilder<T, U> {

    @NotNull
    private Function<@Nullable U, @NotNull String> queryContextTransformer = (U ignored) -> "Enter the response:";

    @NotNull
    private String inputPrompt = "> ";

    private Function<@NotNull String, @NotNull T> transformer;

    private boolean persistent = false;

    private List<String> retryPhrases = List.of("Please enter a valid response.");

    private AskerBuilder() {
    }

    public static <V> AskerBuilder<String, V> asker(Class<V> contextClass) {
        AskerBuilder<String, V> builder = new AskerBuilder<>();
        return builder.withTransformer(Function.identity());
    }

    public AskerBuilder<T, U> withInputPrompt(String inputPrompt) {
        this.inputPrompt = inputPrompt;
        return this;
    }

    public AskerBuilder<T, U> withQueryContextTransformer(Function<@Nullable U, @NotNull String> contextToQuery) {
        this.queryContextTransformer = contextToQuery;
        return this;
    }

    /**
     * Composes the new transformer after already-existing transformers.
     *
     * @param transformer the new transformer
     * @param <R>         the type returned by the new transformer
     * @return builder
     */
    public <R> AskerBuilder<R, U> addTransformer(@NotNull Function<@NotNull T, @NotNull R> transformer) {
        Function<String, R> newTransformer = this.transformer.andThen(transformer);
        return new AskerBuilder<R, U>()
                .withQueryContextTransformer(queryContextTransformer)
                .withInputPrompt(inputPrompt)
                .withTransformer(newTransformer);
    }

    /**
     * Composes the new predicate after already-existing transformers. An exception is thrown if the predicate fails.
     *
     * @param predicate the new predicate
     * @return builder
     */
    public AskerBuilder<T, U> addPredicate(Predicate<T> predicate) {
        return addTransformer((T t) -> {
            if (!predicate.test(t)) {
                throw new InputMismatchException("The input (\"%s\") does not satisfy the predicate (%s).".formatted(t, predicate));
            }
            return t;
        });
    }

    public AskerBuilder<T, U> persistent(boolean persistent) {
        this.persistent = persistent;
        return this;
    }

    public AskerBuilder<T, U> withRetryPhrases(List<String> retryPhrases) {
        this.retryPhrases = retryPhrases;
        return this;
    }

    /**
     * Completely overwrites the previous transformer with the new one.
     *
     * @param transformer the new transformer
     * @return builder
     */
    private AskerBuilder<T, U> withTransformer(Function<String, T> transformer) {
        this.transformer = transformer;
        return this;
    }

    public Asker<T, U> build() {
        Asker<T, U> asker;

        if (persistent) {
            PersistentAsker<T, U> persistentAsker = new PersistentAsker<>();
            persistentAsker.setRetryPhrases(retryPhrases);
            asker = persistentAsker;
        } else {
            asker = new ConcreteAsker<>();
        }

        asker.setQueryContextTransformer(queryContextTransformer);
        asker.setInputPrompt(inputPrompt);
        asker.setTransformer(transformer);

        return asker;
    }
}
