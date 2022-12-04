package animal.state;

import animal.state.base.AppState;
import animal.state.base.ApplicationBase;
import org.jetbrains.annotations.NotNull;

import java.io.IOException;

public class OldUserIntroState extends AppState {

    @Override
    protected @NotNull AppState nextState(@NotNull ApplicationBase app) {
        return new SelectorState();
    }

    @Override
    public void execute(@NotNull ApplicationBase app) {
        // Deserialize tree
        try {
            app.getTree().serializer().read();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        System.out.println("Welcome to the animal expert system!");
        System.out.println();
    }
}
