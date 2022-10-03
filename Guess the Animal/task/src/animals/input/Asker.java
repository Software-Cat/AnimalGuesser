package animals.input;

import org.jetbrains.annotations.NotNull;

import java.util.InputMismatchException;
import java.util.function.Function;
import java.util.function.Supplier;

public interface Asker<T> extends Supplier<T> {

    @Override
    default @NotNull T get() {
        return ask();
    }

    @NotNull T ask() throws InputMismatchException;

    void setQuery(@NotNull String query);

    void setInputPrompt(@NotNull String inputPrompt);

    void setTransformer(@NotNull Function<@NotNull String, @NotNull T> transformer);
}
