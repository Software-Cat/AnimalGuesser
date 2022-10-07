package animal.input;

import lombok.Getter;
import lombok.Setter;
import org.jetbrains.annotations.NotNull;

import java.util.function.Supplier;

public class PersistentAsker<T, U> extends ConcreteAsker<T, U> {

    @NotNull
    @Getter
    @Setter
    private Supplier<String> retryPhraseSupplier;

    protected PersistentAsker() {
    }

    @Override
    public @NotNull T get() {
        System.out.println(getQuery());

        T response = null;
        boolean responseIsValid = false;

        while (!responseIsValid) {
            try {
                System.out.print(getInputPrompt());
                response = transformer.apply(scanner.nextLine(), context);

                // This line won't be reached if the super.ask() throws an exception.
                responseIsValid = true;
            } catch (Exception e) {
                System.out.println(retryPhraseSupplier.get());
            }
        }

        return response;
    }
}
