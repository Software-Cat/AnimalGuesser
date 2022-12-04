package animal.state;

import animal.state.base.AppState;
import animal.state.base.ApplicationBase;
import animal.state.base.SelectableState;
import org.jetbrains.annotations.NotNull;

import java.util.List;

import static animal.state.AskerRegistry.nextStateAsker;

public class SelectorState extends AppState {

    private static final List<SelectableState> SELECTABLE_STATES = List.of(
            new ExitState(),
            new PlayState(),
            new ListState(),
            new SearchState(),
            new StatisticsState(),
            new PrintTreeState()
    );

    private SelectableState nextState;

    @Override
    protected @NotNull AppState nextState(@NotNull ApplicationBase app) {
        return nextState;
    }

    @Override
    public void execute(@NotNull ApplicationBase app) {
        nextStateAsker.setContext(SELECTABLE_STATES);
        nextState = nextStateAsker.get();
        System.out.println();
    }
}
