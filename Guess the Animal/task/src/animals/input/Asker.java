package animals.input;

import org.jetbrains.annotations.NotNull;

import java.util.InputMismatchException;
import java.util.function.Function;
import java.util.function.Supplier;

public interface Asker<T> extends Supplier<T> {

    @Override
    @NotNull T get() throws InputMismatchException;

    void setQuery(@NotNull String query);

    void setInputPrompt(@NotNull String inputPrompt);

    void setTransformer(@NotNull Function<@NotNull String, @NotNull T> transformer);
}
