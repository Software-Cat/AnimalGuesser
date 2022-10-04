package animal.input;

import lombok.Getter;
import lombok.Setter;
import org.jetbrains.annotations.NotNull;

import java.util.Scanner;
import java.util.function.Function;

public class ConcreteAsker<T> implements Asker<T> {

    protected final Scanner scanner = new Scanner(System.in);

    @Getter
    @Setter
    @NotNull
    private String inputPrompt;

    @Getter
    @Setter
    @NotNull
    private String query;

    @Getter
    @Setter
    @NotNull
    private Function<@NotNull String, @NotNull T> transformer;

    protected ConcreteAsker() {
    }

    public @NotNull T get() {
        System.out.println(query);
        System.out.print(inputPrompt);
        return transformer.apply(scanner.nextLine());
    }
}
