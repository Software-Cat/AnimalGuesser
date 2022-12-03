package animal.base;

import java.util.NoSuchElementException;

public class InOrderIterator<T> extends TreeIterator<T> {

    public InOrderIterator(DecisionTree<T> tree) {
        this(tree.root);
    }

    public InOrderIterator(Node<T> node) {
        super(node);
        moveLeft(node);
    }

    private void moveLeft(Node<T> current) {
        while (current != null) {
            traversal.push(current);
            current = current.getNoBranch();
        }
    }

    @Override
    public Node<T> next() {
        if (!hasNext())
            throw new NoSuchElementException();

        Node<T> current = traversal.pop();

        if (current.getYesBranch() != null) {
            moveLeft(current.getYesBranch());
        }

        return current;
    }
}
