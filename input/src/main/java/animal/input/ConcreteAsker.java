package animal.input;

import lombok.Getter;
import lombok.Setter;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Scanner;
import java.util.function.Function;

public class ConcreteAsker<T, U> implements Asker<T, U> {

    protected final Scanner scanner = new Scanner(System.in);

    @Getter
    @Setter
    @NotNull
    private String inputPrompt;

    @Getter
    @Setter
    @NotNull
    private Function<@NotNull String, @NotNull T> transformer;

    @Getter
    @Setter
    @Nullable
    private U context;

    @Getter
    @Setter
    @NotNull
    private Function<@Nullable U, @NotNull String> queryContextTransformer;

    protected ConcreteAsker() {
    }

    public @NotNull T get() {
        System.out.println(getQuery());
        System.out.print(inputPrompt);
        return transformer.apply(scanner.nextLine());
    }

    @Override
    public @NotNull String getQuery() {
        return queryContextTransformer.apply(context);
    }
}
