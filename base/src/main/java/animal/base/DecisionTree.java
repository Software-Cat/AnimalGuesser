package animal.base;

import animal.linguistics.clause.Statement;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.json.JsonMapper;
import com.google.common.collect.ArrayListMultimap;
import com.google.common.collect.HashMultimap;
import com.google.common.collect.Multimap;
import com.google.common.collect.Multimaps;
import lombok.Getter;
import lombok.experimental.Accessors;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.File;
import java.io.IOException;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

public class DecisionTree<T> implements Iterable<Node<T>> {

    @Getter()
    @Accessors(fluent = true)
    private final Serializer serializer = new Serializer();
    public Node<T> root;

    public DecisionTree() {
        this.root = null;
    }

    private LeafNode<T> addYesRecursive(Node<T> current, @NotNull LeafNode<T> target, @NotNull Statement statement, @NotNull T value) {
        if (current.isLeaf()) {
            return null;
        }

        LeafNode<T> newLeaf = new LeafNode<>(value);
        if (current.getYesBranch() == target) {
            current.setYesBranch(new BranchNode<>(
                    statement,
                    current.getYesBranch(),
                    newLeaf));
            return newLeaf;
        } else if (current.getNoBranch() == target) {
            current.setNoBranch(new BranchNode<>(
                    statement,
                    current.getNoBranch(),
                    newLeaf));
            return newLeaf;
        }

        if (current.getYesBranch() != null) {
            LeafNode<T> fromYes = addYesRecursive(current.getYesBranch(), target, statement, value);
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
    public LeafNode<T> addYes(LeafNode<T> target, Statement statement, @NotNull T value) {
        LeafNode<T> newLeaf = new LeafNode<>(value);
        if (root == null) {
            root = newLeaf;
            return newLeaf;
        } else if (root.isLeaf()) {
            root = new BranchNode<>(
                    statement,
                    root,
                    newLeaf
            );
            return newLeaf;
        }

        return addYesRecursive(root, target, statement, value);
    }

    private LeafNode<T> addNoRecursive(Node<T> current, @NotNull LeafNode<T> target, @NotNull Statement statement, @NotNull T value) {
        LeafNode<T> newLeaf = new LeafNode<>(value);
        if (current.getYesBranch() == target) {
            current.setYesBranch(new BranchNode<>(
                    statement,
                    newLeaf,
                    current.getYesBranch()));
            return newLeaf;
        } else if (current.getNoBranch() == target) {
            current.setNoBranch(new BranchNode<>(
                    statement,
                    newLeaf,
                    current.getNoBranch()));
            return newLeaf;
        }

        if (current.getYesBranch() != null) {
            LeafNode<T> fromYes = addYesRecursive(current.getYesBranch(), target, statement, value);
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
    public LeafNode<T> addNo(LeafNode<T> target, Statement statement, @NotNull T value) {
        LeafNode<T> newLeaf = new LeafNode<>(value);
        if (root == null) {
            root = newLeaf;
            return newLeaf;
        } else if (root.isLeaf()) {
            root = new BranchNode<>(
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

    public LeafNode<T> traverseWith(Function<BranchNode<T>, Boolean> branchChooser) {
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

    public @NotNull Multimap<Integer, Node<T>> stratified() {
        Multimap<Integer, Node<T>> strata = HashMultimap.create();

        if (root != null) {
            stratifiedRecursive(root, strata, 1);
        }

        return strata;
    }

    private void stratifiedRecursive(Node<T> current, Multimap<Integer, Node<T>> strata, int depth) {
        strata.put(depth, current);

        if (current.isBranch()) {
            stratifiedRecursive(current.getYesBranch(), strata, depth + 1);
            stratifiedRecursive(current.getNoBranch(), strata, depth + 1);
        }
    }

    public @NotNull List<Statement> statementsAbout(@Nullable T object) {
        return Objects.requireNonNullElseGet(statementsAboutRecursive(object, root), List::of);
    }

    private @Nullable List<Statement> statementsAboutRecursive(@Nullable T object, Node<T> current) {
        if (current.isLeaf() && current.tryGetAsLeaf().get().tryGetValue().get().equals(object)) {
            return new ArrayList<>();
        } else if (current.isBranch()) {
            DecisionTree.BranchNode<T> currentBranch = current.tryGetAsBranch().get();

            List<Statement> fromYes = statementsAboutRecursive(object, current.getYesBranch());
            List<Statement> fromNo = statementsAboutRecursive(object, current.getNoBranch());

            if (fromYes != null) {
                fromYes.add(currentBranch.statement);
                return fromYes;
            } else if (fromNo != null) {
                fromNo.add(currentBranch.statement.negative());
                return fromNo;
            }
        }

        return null;
    }

    public Multimap<T, Statement> allKnowledge() {
        Multimap<Statement, T> map = ArrayListMultimap.create();
        allKnowledgeRecursive(root, map);

        return Multimaps.invertFrom(map, ArrayListMultimap.create());
    }

    private List<Node<T>> allKnowledgeRecursive(Node<T> current, Multimap<Statement, T> map) {
        if (current.isLeaf()) {
            // If is leaf, return self as list
            List<Node<T>> list = new ArrayList<>();
            list.add(current);
            return list;
        } else { // If branch
            // Get leaves from yes and no branch
            List<Node<T>> leavesOfYes = allKnowledgeRecursive(current.getYesBranch(), map);
            List<Node<T>> leavesOfNo = allKnowledgeRecursive(current.getNoBranch(), map);

            // Add current decision to map
            DecisionTree.BranchNode<T> currentBranch = current.tryGetAsBranch().get();
            map.putAll(currentBranch.statement, leavesOfYes.stream()
                    .map((Node<T> n) -> n.tryGetValue().get())
                    .collect(Collectors.toList()));
            map.putAll(currentBranch.statement.negative(), leavesOfNo.stream()
                    .map((Node<T> n) -> n.tryGetValue().get())
                    .collect(Collectors.toList()));

            // Add leaves of branches as leaves of current
            List<Node<T>> leavesOfCurrent = new ArrayList<>();
            leavesOfCurrent.addAll(leavesOfYes);
            leavesOfCurrent.addAll(leavesOfNo);

            return leavesOfCurrent;
        }
    }

    /**
     * Calculates true/false paths taken to reach specified node
     *
     * @param target the node to reach
     * @return list of true/false decisions
     */
    public List<Boolean> pathTo(Node<T> target) {
        return pathToRecursive(target, root);
    }

    private @Nullable List<Boolean> pathToRecursive(Node<T> target, Node<T> current) {
        if (current.equals(target)) {
            return new ArrayList<>();
        }

        List<Boolean> ret;

        if (current.isBranch()) {
            // Explore yes branch
            ret = pathToRecursive(target, current.getYesBranch());

            // Early return if found in yes branch
            if (ret != null) {
                ret.add(0, true);
                return ret;
            }

            // Explore no branch
            ret = pathToRecursive(target, current.getNoBranch());

            // Return no branch
            if (ret != null) {
                ret.add(0, false);
                return ret;
            }
        }

        return null;
    }

    public static class BranchNode<U> extends Node<U> {

        @Getter
        @NotNull
        private final Statement statement;

        /**
         * This constructor should only be used by Jackson
         */
        public BranchNode() {
            super(null);
            statement = null;
        }

        public BranchNode(@NotNull Statement statement, @NotNull Node<U> noBranch, @NotNull Node<U> yesBranch) {
            super(null);
            this.statement = statement;
            setNoBranch(noBranch);
            setYesBranch(yesBranch);
        }

        @Override
        public void setNoBranch(Node<U> noBranch) {
            super.setNoBranch(noBranch);
        }

        @Override
        public void setYesBranch(Node<U> yesBranch) {
            super.setYesBranch(yesBranch);
        }

        @Override
        public Optional<BranchNode<U>> tryGetAsBranch() {
            return Optional.of(this);
        }

        @Override
        public String toString() {
            return statement.toQuestion().toString();
        }

        @Override
        public Optional<LeafNode<U>> tryGetAsLeaf() {
            return Optional.empty();
        }
    }

    public static class LeafNode<U> extends Node<U> {

        /**
         * This constructor should only be used by Jackson
         */
        public LeafNode() {
            super(null);
        }

        public LeafNode(@NotNull U value) {
            super(value);
        }

        @Override
        public Optional<BranchNode<U>> tryGetAsBranch() {
            return Optional.empty();
        }

        @Override
        public Optional<LeafNode<U>> tryGetAsLeaf() {
            return Optional.of(this);
        }

        @Override
        public void setNoBranch(Node<U> noBranch) {
            throw new UnsupportedOperationException();
        }

        @Override
        public String toString() {
            if (tryGetValue().isEmpty()) {
                return "";
            }
            return tryGetValue().get().toString();
        }

        @Override
        public void setYesBranch(Node<U> yesBranch) {
            throw new UnsupportedOperationException();
        }
    }

    public class Serializer {

        private static final ObjectMapper mapper = new JsonMapper();

        static {
            mapper.registerSubtypes(Animal.class);
        }

        public String fileName = "./animals.json";

        public void write() throws IOException {
            writeTo(new File(fileName));
        }

        public void read() throws IOException {
            readFrom(new File(fileName));
        }

        public boolean fileExists() {
            return new File(fileName).exists();
        }

        public void writeTo(File file) throws IOException {
            mapper.writeValue(file, root);
        }

        public void readFrom(File file) throws IOException {
            root = mapper.readValue(file, new TypeReference<>() {
            });
        }
    }
}
