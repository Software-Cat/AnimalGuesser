package animal.state.base;

import animal.state.StartState;
import lombok.Getter;
import lombok.Setter;

public class StateMachine implements Runnable {

    @Getter
    private final ApplicationBase app;

    @Getter
    @Setter
    private AppState state;

    public StateMachine(ApplicationBase app) {
        this(app, new StartState());
    }

    public StateMachine(ApplicationBase app, AppState initialState) {
        this.app = app;
        this.state = initialState;
    }

    @Override
    public void run() {
        while (state != null) {
            state.execute(app);
            setState(state.nextState(app));
        }
    }
}
