package animal.input;

import org.jetbrains.annotations.NotNull;

import java.util.function.Function;
import java.util.function.Supplier;

public interface Asker<T, U> extends Supplier<T> {

    @Override
    @NotNull
    T get();

    String getQuery();

    void setContext(@NotNull U context);

    void setQueryContextTransformer(@NotNull Function<U, String> contextToQuery);

    void setInputPrompt(@NotNull String inputPrompt);

    void setTransformer(@NotNull Function<@NotNull String, @NotNull T> transformer);
}
