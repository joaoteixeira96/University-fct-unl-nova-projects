package dataStructures;

public class BSTBreadthFirstIterator<K, V> implements Iterator<Entry<K, V>> {

	/**
	 * 
	 */
	private static final long serialVersionUID = 0L;
	private BSTNode<K, V> root;
	private Queue<BSTNode<K, V>> queue;

	public BSTBreadthFirstIterator(BSTNode<K, V> root) {
		this.root = root;
		queue = new QueueInList<BSTNode<K, V>>();
		rewind();

	}

	@Override
	public boolean hasNext() {
		return !queue.isEmpty();

	}

	@Override
	public Entry<K, V> next() throws NoSuchElementException {

		BSTNode<K, V> result = queue.dequeue();
		organize(result);
		return result.getEntry();
	}

	@Override
	public void rewind() {
		queue.enqueue(root);

	}

	private void organize(BSTNode<K, V> node) {
		queue.enqueue(node);
		BSTNode<K, V> nodeLeft = node.getLeft();
		BSTNode<K, V> nodeRight = node.getRight();

		if(nodeLeft!= null)
		queue.enqueue(nodeLeft);
		if(nodeRight !=null)
		queue.enqueue(nodeRight);

	}

}
