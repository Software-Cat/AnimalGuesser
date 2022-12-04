package animal.state;

import animal.base.Animal;
import animal.base.Node;
import animal.state.base.AppState;
import animal.state.base.ApplicationBase;
import animal.state.base.SelectableState;
import org.jetbrains.annotations.NotNull;

public class ListState extends SelectableState {

    public ListState() {
        super("List of all animals");
    }

    @Override
    protected @NotNull AppState nextState(@NotNull ApplicationBase app) {
        return new SelectorState();
    }

    @Override
    public void execute(@NotNull ApplicationBase app) {
        System.out.println("Here are the animals I know:");

        for (Node<Animal> node : app.getTree()) {
            if (node.isLeaf()) {
                System.out.println(" - " + node.tryGetValue().get().getSpecies().withArticle(true));
            }
        }

        System.out.println();
    }
}
