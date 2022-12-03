package animal.base;

import java.util.Iterator;
import java.util.LinkedList;

public abstract class TreeIterator<T> implements Iterator<Node<T>> {

    protected final LinkedList<Node<T>> traversal;

    public TreeIterator(Node<T> node) {
        traversal = new LinkedList<>();
    }

    @Override
    public boolean hasNext() {
        return !traversal.isEmpty();
    }
}
