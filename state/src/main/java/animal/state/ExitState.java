package animal.state;

import animal.state.base.AppState;
import animal.state.base.ApplicationBase;
import animal.state.base.SelectableState;
import animal.utils.greet.Greeter;
import animal.utils.greet.TimedGreeter;
import org.jetbrains.annotations.NotNull;

import java.io.IOException;

public class ExitState extends SelectableState {

    private final Greeter greeter = new TimedGreeter();

    public ExitState() {
        super("Exit");
    }

    /**
     * @param app the app
     * @return always returns null
     */
    @Override
    protected @NotNull AppState nextState(@NotNull ApplicationBase app) {
        return null;
    }

    @Override
    public void execute(@NotNull ApplicationBase app) {
        greeter.goodbye();

        // Serialize tree
        try {
            app.getTree().serializer().write();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
