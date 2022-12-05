package animal.app;

import animal.base.Animal;
import animal.base.DecisionTree;
import animal.state.base.ApplicationBase;
import animal.state.base.StateMachine;
import lombok.Getter;

public class Application implements ApplicationBase {
    
    @Getter
    private final DecisionTree<Animal> tree = new DecisionTree<>();

    private final StateMachine stateMachine = new StateMachine(this);

    @Override
    public void run() {
        stateMachine.run();
    }
}
