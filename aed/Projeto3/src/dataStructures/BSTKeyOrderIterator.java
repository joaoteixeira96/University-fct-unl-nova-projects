package dataStructures;

public class BSTKeyOrderIterator<K, V> implements Iterator<Entry<K,V>> {
	
	static final long serialVersionUID = 0L;

	private BSTNode<K, V> root;
	private Stack<BSTNode<K,V>> stack;
	
	public BSTKeyOrderIterator(BSTNode<K,V> root) {
		this.root = root;
		stack = new StackInList<BSTNode<K,V>>();
		rewind();
	}
	@Override
	public boolean hasNext() {
		return !stack.isEmpty();
	}

	@Override
	public Entry<K, V> next() throws NoSuchElementException {
		BSTNode<K, V> value = stack.pop();
		BSTNode<K, V> rightValue = value.getRight();
		if(rightValue != null){	
			organize(rightValue);
		}
		return value.getEntry();
	}

	@Override
	public void rewind() {
		organize(root);
		
	}
	private void organize(BSTNode<K, V> node){
		if(node != null){
		stack.push(node);
			BSTNode<K, V> nodeLeft = node.getLeft();
			while(nodeLeft != null){
				stack.push(nodeLeft);
				nodeLeft = nodeLeft.getLeft();
			}
		}
	}

}
