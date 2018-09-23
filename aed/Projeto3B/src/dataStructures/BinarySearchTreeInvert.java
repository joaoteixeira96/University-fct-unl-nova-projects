package dataStructures;

public class BinarySearchTreeInvert<K extends Comparable<K>, V> extends BinarySearchTree<K, V> 
	implements OrderedDictionary<K,V> {


	private static final long serialVersionUID = 0L;
	
	@Override
	public Iterator<Entry<K, V>> iterator() {
		return new BSTKeyInvertOrderIterator<K,V>(root);
	}

}
