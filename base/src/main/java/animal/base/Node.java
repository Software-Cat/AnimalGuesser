package animal.base;

import com.fasterxml.jackson.annotation.*;
import com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Iterator;
import java.util.Optional;

@JsonAutoDetect(fieldVisibility = Visibility.ANY)
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonTypeInfo(
        use = JsonTypeInfo.Id.NAME,
        property = "type")
@JsonSubTypes({
        @JsonSubTypes.Type(value = DecisionTree.BranchNode.class),
        @JsonSubTypes.Type(value = DecisionTree.LeafNode.class)
})
public abstract class Node<T> implements Iterable<Node<T>> {

    @Nullable
    @JsonTypeInfo(
            use = JsonTypeInfo.Id.NAME,
            property = "type")
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

    public abstract Optional<DecisionTree.BranchNode<T>> tryGetAsBranch();

    public abstract Optional<DecisionTree.LeafNode<T>> tryGetAsLeaf();

    @JsonIgnore
    public boolean isLeaf() {
        return tryGetAsLeaf().isPresent();
    }

    @JsonIgnore
    public boolean isBranch() {
        return tryGetAsBranch().isPresent();
    }

    @NotNull
    @Override
    public Iterator<Node<T>> iterator() {
        return new InOrderIterator<>(this);
    }
}
