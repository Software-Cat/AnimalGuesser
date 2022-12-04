package animal.state;

import animal.base.Animal;
import animal.state.base.AppState;
import animal.state.base.ApplicationBase;
import org.jetbrains.annotations.NotNull;

import static animal.state.AskerRegistry.animalAsker;

public class NewUserIntroState extends AppState {

    @Override
    protected @NotNull AppState nextState(@NotNull ApplicationBase app) {
        return new SelectorState();
    }

    @Override
    public void execute(@NotNull ApplicationBase app) {
        // First animal
        System.out.println("I want to learn about animals.");

        animalAsker.setContext("Which animal do you like most?");
        Animal favAnimal = animalAsker.get();
        app.getTree().addYes(null, null, favAnimal);

        System.out.println();
        System.out.println("Welcome to the animal expert system!");
        System.out.println();
    }
}
