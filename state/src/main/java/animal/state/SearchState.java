package animal.state;

import animal.base.Animal;
import animal.linguistics.clause.Statement;
import animal.state.base.AppState;
import animal.state.base.ApplicationBase;
import animal.state.base.SelectableState;
import org.jetbrains.annotations.NotNull;

import java.util.List;

import static animal.state.AskerRegistry.animalAsker;

public class SearchState extends SelectableState {

    public SearchState() {
        super("Search for an animal");
    }

    @Override
    protected @NotNull AppState nextState(@NotNull ApplicationBase app) {
        return new SelectorState();
    }

    @Override
    public void execute(@NotNull ApplicationBase app) {
        animalAsker.setContext("Enter the animal:");
        Animal animal = animalAsker.get();

        List<Statement> facts = app.getTree().statementsAbout(animal);

        if (facts.size() == 0) {
            System.out.println("No facts about " + animal.getSpecies().withArticle(true) + ".");
        } else {
            System.out.println("Facts about " + animal.getSpecies().withArticle(true) + ":");

            for (Statement fact : facts) {
                System.out.println(" - " + fact);
            }
        }

        System.out.println();
    }
}
