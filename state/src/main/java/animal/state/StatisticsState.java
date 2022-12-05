package animal.state;

import animal.base.Animal;
import animal.base.Node;
import animal.state.base.AppState;
import animal.state.base.ApplicationBase;
import animal.state.base.SelectableState;
import animal.utils.stats.Statistics;
import com.google.common.collect.Multimap;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;

public class StatisticsState extends SelectableState {

    public StatisticsState() {
        super("Calculate statistics");
    }

    @Override
    protected @NotNull AppState nextState(@NotNull ApplicationBase app) {
        return new SelectorState();
    }

    @Override
    public void execute(@NotNull ApplicationBase app) {
        Multimap<Integer, Node<Animal>> strata = app.getTree().stratified();
        List<Integer> nodeDistribution = new ArrayList<>(strata.size());
        List<Integer> animalDistribution = new ArrayList<>(strata.size());

        for (Map.Entry<Integer, Collection<Node<Animal>>> entry : strata.asMap().entrySet()) {
            for (Node<Animal> node : entry.getValue()) {
                nodeDistribution.add(entry.getKey());

                if (node.isLeaf()) {
                    animalDistribution.add(entry.getKey());
                }
            }
        }

        System.out.println("The Knowledge Tree stats:");
        System.out.println("- root node                    " + app.getTree().root);
        System.out.println("- total number of nodes        " + nodeDistribution.size());
        System.out.println("- total number of animals      " + animalDistribution.size());
        System.out.println("- height of the tree           " + Statistics.max(nodeDistribution));
        System.out.println("- minimum animal's depth       " + Statistics.min(animalDistribution));
        System.out.println("- average animal's depth       " + Statistics.mean(animalDistribution));
        System.out.println();
    }
}
