package animal.input;

import lombok.Getter;
import lombok.Setter;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Scanner;
import java.util.function.BiFunction;
import java.util.function.Function;

public class ConcreteAsker<T, U> implements Asker<T, U> {

    protected final Scanner scanner = new Scanner(System.in);

    @Getter
    @Setter
    @Nullable
    protected U context;

    @Getter
    @Setter
    @NotNull
    protected BiFunction<@NotNull String, @Nullable U, @NotNull T> transformer;

    @Getter
    @Setter
    @NotNull
    private Function<@Nullable U, @NotNull String> queryContextTransformer;

    @Getter
    @Setter
    @NotNull
    private String inputPrompt;

    protected ConcreteAsker() {
    }

    public @NotNull T get() {
        System.out.println(getQuery());
        System.out.print(inputPrompt);
        return transformer.apply(scanner.nextLine(), context);
    }

    @Override
    public @NotNull String getQuery() {
        return queryContextTransformer.apply(context);
    }
}
