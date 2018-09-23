package dataStructures;

public class BSTKeyInvertOrderIterator<K, V> extends BSTKeyOrderIterator<K, V> implements Iterator<Entry<K,V>> {

	private static final long serialVersionUID = 0L;

	public BSTKeyInvertOrderIterator(BSTNode<K, V> root) {
		super(root);
	}
	
	@Override
	public Entry<K, V> next() throws NoSuchElementException {
		BSTNode<K, V> value = stack.pop();
		BSTNode<K, V> leftValue = value.getLeft();
		if(leftValue != null){	
			organize(leftValue);
		}
		return value.getEntry();//.getValue
	}
	
	@Override
	public void rewind() {
		organize(root);
	}
	
	private void organize(BSTNode<K, V> node){
		if(node != null){
		stack.push(node);
			BSTNode<K, V> nodeRight = node.getRight();
			while(nodeRight != null){
				stack.push(nodeRight);
				nodeRight = nodeRight.getRight();
			}
		}
	}

}
