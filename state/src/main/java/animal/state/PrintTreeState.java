package animal.state;

import animal.state.base.AppState;
import animal.state.base.ApplicationBase;
import animal.state.base.SelectableState;
import org.jetbrains.annotations.NotNull;

public class PrintTreeState extends SelectableState {

    public PrintTreeState() {
        super("Print the Knowledge Tree");
    }

    @Override
    protected @NotNull AppState nextState(@NotNull ApplicationBase app) {
        return new SelectorState();
    }

    @Override
    public void execute(@NotNull ApplicationBase app) {
        System.out.println(app.getTree().toString());
        System.out.println();
    }
}
