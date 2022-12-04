package animal.state;

import animal.state.base.AppState;
import animal.state.base.ApplicationBase;
import animal.utils.greet.Greeter;
import animal.utils.greet.TimedGreeter;
import org.jetbrains.annotations.NotNull;

public class StartState extends AppState {

    private final Greeter greeter = new TimedGreeter();
    
    @Override
    protected @NotNull AppState nextState(@NotNull ApplicationBase app) {
        if (app.getTree().serializer().fileExists()) {
            return new OldUserIntroState();
        } else {
            return new NewUserIntroState();
        }
    }

    @Override
    public void execute(@NotNull ApplicationBase app) {
        // Greeting
        greeter.hello();
        System.out.println();
    }
}
