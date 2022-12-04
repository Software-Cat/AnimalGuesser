package animal.state.base;

import animal.base.Animal;
import animal.base.DecisionTree;

public interface ApplicationBase extends Runnable {

    DecisionTree<Animal> getTree();
}
