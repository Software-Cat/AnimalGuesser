package animal.base;

import animal.linguistics.clause.Statement;
import com.google.common.collect.ArrayListMultimap;
import com.google.common.collect.Multimap;
import lombok.Getter;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Optional;
import java.util.function.Function;

public class DecisionTree<T> implements Iterable<Node<T>> {

    public Node<T> root;

    public DecisionTree() {
        this.root = null;
    }

    private LeafNode addYesRecursive(Node<T> current, @NotNull LeafNode target, @NotNull Statement statement, @NotNull T value) {
        if (current.isLeaf()) {
            return null;
        }

        LeafNode newLeaf = new LeafNode(value);
        if (current.getYesBranch() == target) {
            current.setYesBranch(new BranchNode(
                    statement,
                    current.getYesBranch(),
                    newLeaf));
            return newLeaf;
        } else if (current.getNoBranch() == target) {
            current.setNoBranch(new BranchNode(
                    statement,
                    current.getNoBranch(),
                    newLeaf));
            return newLeaf;
        }

        if (current.getYesBranch() != null) {
            LeafNode fromYes = addYesRecursive(current.getYesBranch(), target, statement, value);
            if (fromYes != null) {
                return fromYes;
            }
        }
        if (current.getNoBranch() != null) {
            return addYesRecursive(current.getNoBranch(), target, statement, value);
        }

        return null;
    }

    /**
     * Add new true value to existing leaf with statement as differentiator
     *
     * @param target    the existing leaf
     * @param statement the differentiating factor between existing and new leaves,
     *                  with the new leaf being true for such statement
     * @param value     the actual value of the new leaf
     * @return the newly created leaf
     */
    public LeafNode addYes(LeafNode target, Statement statement, @NotNull T value) {
        LeafNode newLeaf = new LeafNode(value);
        if (root == null) {
            root = newLeaf;
            return newLeaf;
        } else if (root.isLeaf()) {
            root = new BranchNode(
                    statement,
                    root,
                    newLeaf
            );
            return newLeaf;
        }

        return addYesRecursive(root, target, statement, value);
    }

    private LeafNode addNoRecursive(Node<T> current, @NotNull LeafNode target, @NotNull Statement statement, @NotNull T value) {
        LeafNode newLeaf = new LeafNode(value);
        if (current.getYesBranch() == target) {
            current.setYesBranch(new BranchNode(
                    statement,
                    newLeaf,
                    current.getYesBranch()));
            return newLeaf;
        } else if (current.getNoBranch() == target) {
            current.setNoBranch(new BranchNode(
                    statement,
                    newLeaf,
                    current.getNoBranch()));
            return newLeaf;
        }

        if (current.getYesBranch() != null) {
            LeafNode fromYes = addYesRecursive(current.getYesBranch(), target, statement, value);
            if (fromYes != null) {
                return fromYes;
            }
        }
        if (current.getNoBranch() != null) {
            return addYesRecursive(current.getNoBranch(), target, statement, value);
        }

        return null;
    }

    /**
     * Add new false value to existing leaf with statement as differentiator
     *
     * @param target    the existing leaf
     * @param statement the differentiating factor between existing and new leaves,
     *                  with the new leaf being false for such statement
     * @param value     the actual value of the new leaf
     * @return the newly created leaf
     */
    public LeafNode addNo(LeafNode target, Statement statement, @NotNull T value) {
        LeafNode newLeaf = new LeafNode(value);
        if (root == null) {
            root = newLeaf;
            return newLeaf;
        } else if (root.isLeaf()) {
            root = new BranchNode(
                    statement,
                    newLeaf,
                    root
            );
            return newLeaf;
        }

        return addNoRecursive(root, target, statement, value);
    }

    @NotNull
    @Override
    public Iterator<Node<T>> iterator() {
        return new InOrderIterator<>(this);
    }

    public LeafNode traverseWith(Function<BranchNode, Boolean> branchChooser) {
        if (root == null) {
            return null;
        } else if (root.isLeaf()) {
            return root.tryGetAsLeaf().get();
        }

        Node<T> current = root;
        while (current.isBranch()) {
            boolean choice = branchChooser.apply(current.tryGetAsBranch().get());

            // If choice == true, pick yes path
            if (choice) {
                current = current.getYesBranch();
            } else {
                current = current.getNoBranch();
            }
        }

        return current.tryGetAsLeaf().get();
    }

    public Multimap<T, Statement> allKnowledge() {
        Multimap<T, Statement> map = ArrayListMultimap.create();

        TreeIterator<T> iterator = new InOrderIterator<>(this);

        while (iterator.hasNext()) {
            Node<T> current = iterator.next();

            // If current node is leaf node
            if (current.isLeaf()) {

            }
        }

        return null;
    }

    /**
     * Calculates true/false paths taken to reach specified node
     *
     * @param target the node to reach
     * @return list of true/false decisions
     */
    public List<Boolean> pathTo(Node<T> target) {
        return pathToRecursive(target, root, new ArrayList<>());
    }

    private @Nullable List<Boolean> pathToRecursive(Node<T> target, Node<T> current, List<Boolean> previousDecisions) {
        if (current.equals(target)) {
            return previousDecisions;
        }

        List<Boolean> ret;

        if (current.isBranch()) {
            // Explore yes branch
            List<Boolean> decisions = new ArrayList<>(previousDecisions);
            decisions.add(true);
            ret = pathToRecursive(target, current.getYesBranch(), decisions);

            // Early return if found in yes branch
            if (ret != null) {
                return ret;
            }

            // Explore no branch
            decisions = new ArrayList<>(previousDecisions);
            decisions.add(false);
            ret = pathToRecursive(target, current.getNoBranch(), decisions);

            // Return no branch
            return ret;
        }

        return null;
    }

    public class BranchNode extends Node<T> {

        @Getter
        @NotNull
        private final Statement statement;

        public BranchNode(@NotNull Statement statement, @NotNull Node<T> noBranch, @NotNull Node<T> yesBranch) {
            super(null);
            this.statement = statement;
            setNoBranch(noBranch);
            setYesBranch(yesBranch);
        }

        @Override
        public void setNoBranch(Node<T> noBranch) {
            super.setNoBranch(noBranch);
        }

        @Override
        public void setYesBranch(Node<T> yesBranch) {
            super.setYesBranch(yesBranch);
        }

        @Override
        public Optional<BranchNode> tryGetAsBranch() {
            return Optional.of(this);
        }

        @Override
        public Optional<LeafNode> tryGetAsLeaf() {
            return Optional.empty();
        }
    }

    public class LeafNode extends Node<T> {

        public LeafNode(@NotNull T value) {
            super(value);
        }

        @Override
        public Optional<BranchNode> tryGetAsBranch() {
            return Optional.empty();
        }

        @Override
        public Optional<LeafNode> tryGetAsLeaf() {
            return Optional.of(this);
        }

        @Override
        public void setNoBranch(Node<T> noBranch) {
            throw new UnsupportedOperationException();
        }

        @Override
        public void setYesBranch(Node<T> yesBranch) {
            throw new UnsupportedOperationException();
        }
    }
}
