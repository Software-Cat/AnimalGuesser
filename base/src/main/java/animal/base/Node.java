package animal.base;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import org.jetbrains.annotations.Nullable;

import java.util.Optional;

public abstract class Node<T> {

    @Nullable
    private final T value;

    @Getter(AccessLevel.PUBLIC)
    @Setter(AccessLevel.PUBLIC)
    @Nullable
    private Node<T> noBranch, yesBranch;

    public Node(@Nullable T value) {
        this.value = value;
        noBranch = yesBranch = null;
    }

    public Optional<T> tryGetValue() {
        return Optional.ofNullable(value);
    }

    public abstract Optional<DecisionTree<T>.BranchNode> tryGetAsBranch();

    public abstract Optional<DecisionTree<T>.LeafNode> tryGetAsLeaf();

    public boolean isLeaf() {
        return tryGetAsLeaf().isPresent();
    }

    public boolean isBranch() {
        return tryGetAsBranch().isPresent();
    }
}
