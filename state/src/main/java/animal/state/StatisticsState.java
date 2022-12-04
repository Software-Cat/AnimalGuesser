package animal.state;

import animal.state.base.AppState;
import animal.state.base.ApplicationBase;
import animal.state.base.SelectableState;
import org.jetbrains.annotations.NotNull;

public class StatisticsState extends SelectableState {

    public StatisticsState() {
        super("Calculate statistics");
    }

    @Override
    protected @NotNull AppState nextState(@NotNull ApplicationBase app) {
        return null;
    }

    @Override
    public void execute(@NotNull ApplicationBase app) {

    }
}
