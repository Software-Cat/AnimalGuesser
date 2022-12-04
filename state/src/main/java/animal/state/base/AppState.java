package animal.state.base;

import org.jetbrains.annotations.NotNull;

public abstract class AppState {

    protected abstract @NotNull AppState nextState(@NotNull ApplicationBase app);

    public abstract void execute(@NotNull ApplicationBase app);
}
