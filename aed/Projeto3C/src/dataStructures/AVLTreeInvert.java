package dataStructures;

public class AVLTreeInvert<K extends Comparable<K>, V> extends AVLTree<K, V>{

	private static final long serialVersionUID = 0L;
	
	@Override
	public Iterator<Entry<K, V>> iterator() {
		return new BSTKeyInvertOrderIterator<K,V>(root);
	}

}
