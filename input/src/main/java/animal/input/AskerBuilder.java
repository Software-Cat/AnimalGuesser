package animal.input;

import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;
import org.apache.commons.lang3.tuple.Pair;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.InputMismatchException;
import java.util.function.*;

@Accessors(chain = true, fluent = true)
public final class AskerBuilder<T, U> {

    @NotNull
    @Getter
    @Setter
    private Function<@Nullable U, @NotNull String> queryContextTransformer = (U ignored) -> "Enter the response:";

    @NotNull
    @Getter
    @Setter
    private String inputPrompt = "> ";

    @Getter
    @Setter
    private BiFunction<@NotNull String, @Nullable U, @NotNull Pair<T, U>> transformer;

    @Getter
    @Setter
    private boolean persistent = false;

    @Getter
    @Setter
    private Supplier<String> retryPhraseSupplier = () -> "Please enter a valid response.";

    private AskerBuilder() {
    }

    public static <V> AskerBuilder<String, V> builder(Class<V> contextClass) {
        AskerBuilder<String, V> builder = new AskerBuilder<>();
        return builder.transformer(Pair::of);
    }

    /**
     * Composes the new transformer after already-existing transformers.
     *
     * @param newTransformer the new transformer
     * @param <R>            the type returned by the new transformer
     * @return builder
     */
    public <R> AskerBuilder<R, U> addTransformer(@NotNull BiFunction<@NotNull T, @Nullable U, @NotNull R> newTransformer) {
        BiFunction<@NotNull String, @Nullable U, @NotNull Pair<R, U>> composite = this.transformer.andThen(
                (Pair<T, U> pair) -> Pair.of(newTransformer.apply(pair.getLeft(), pair.getRight()), pair.getRight()));
        return new AskerBuilder<R, U>()
                .queryContextTransformer(queryContextTransformer)
                .inputPrompt(inputPrompt)
                .transformer(composite)
                .retryPhraseSupplier(retryPhraseSupplier)
                .persistent(persistent);
    }

    public <R> AskerBuilder<R, U> addTransformer(@NotNull Function<@NotNull T, @NotNull R> newTransformer) {
        return addTransformer((T value, U context) -> newTransformer.apply(value));
    }

    /**
     * Composes the new predicate after already-existing transformers. An exception is thrown if the predicate fails.
     *
     * @param predicate the new predicate
     * @return builder
     */
    public AskerBuilder<T, U> addPredicate(BiPredicate<T, U> predicate) {
        return addTransformer((T value, U context) -> {
            if (!predicate.test(value, context)) {
                throw new InputMismatchException("The input (\"%s\") does not satisfy the predicate (%s).".formatted(value, predicate));
            }
            return value;
        });
    }

    public AskerBuilder<T, U> addPredicate(Predicate<T> predicate) {
        return addTransformer((T value) -> {
            if (!predicate.test(value)) {
                throw new InputMismatchException("The input (\"%s\") does not satisfy the predicate (%s).".formatted(value, predicate));
            }
            return value;
        });
    }

    public Asker<T, U> build() {
        Asker<T, U> asker;

        if (persistent) {
            PersistentAsker<T, U> persistentAsker = new PersistentAsker<>();
            persistentAsker.setRetryPhraseSupplier(retryPhraseSupplier);
            asker = persistentAsker;
        } else {
            asker = new ConcreteAsker<>();
        }

        asker.setQueryContextTransformer(queryContextTransformer);
        asker.setInputPrompt(inputPrompt);
        asker.setTransformer((String s, U context) -> transformer.apply(s, context).getLeft());

        return asker;
    }
}
