package animal.base;

import java.util.ArrayDeque;
import java.util.Deque;
import java.util.Iterator;

public abstract class TreeIterator<T> implements Iterator<Node<T>> {

    protected final Deque<Node<T>> traversal;

    public TreeIterator(DecisionTree<T> tree) {
        traversal = new ArrayDeque<>();
    }

    @Override
    public boolean hasNext() {
        return !traversal.isEmpty();
    }
}
