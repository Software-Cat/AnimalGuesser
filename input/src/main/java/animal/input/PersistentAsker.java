package animal.input;

import lombok.Getter;
import lombok.Setter;
import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.Random;

public class PersistentAsker<T, U> extends ConcreteAsker<T, U> {

    private final Random random = new Random();

    @NotNull
    @Getter
    @Setter
    private List<String> retryPhrases;

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
                response = getTransformer().apply(scanner.nextLine());

                // This line won't be reached if the super.ask() throws an exception.
                responseIsValid = true;
            } catch (Exception e) {
                System.out.println(retryPhrases.get(random.nextInt(retryPhrases.size())));
            }
        }

        return response;
    }
}
