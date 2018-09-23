package dataStructures;

public class ChainedhashIterator<K, V>  implements Iterator<Entry<K,V>>{

	private static final long serialVersionUID = 0L;

	private Dictionary<K, V>[] table;
	private DListNode<Entry<K,V>> nextToReturn;
	private DListNode<Entry<K,V>> firstNode;
	private int currentSize ;
	private int currentPosition;

	public ChainedhashIterator(Dictionary<K, V>[] table) {
		this.table = table;
		firstNode = null;
		currentSize = table.length;
		currentPosition = 0;
		rewind();
	}

	@Override
	public boolean hasNext() {
		return nextToReturn != null;
	}

	@Override
	public Entry<K,V> next() throws NoSuchElementException {
		if(!hasNext()){
			throw new NoSuchElementException();
		}
		Entry<K, V> element = nextToReturn.getElement();
		nextToReturn = nextToReturn.getNext();
		if(nextToReturn == null)
			findNext();
		return element;
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	private void findNext(){
		for(int i = currentPosition; i < currentSize; i++)
			if(table[i] != null){
				firstNode = ((OrderedDictionary)table[i]).getFirst();
				nextToReturn = firstNode;
				currentPosition++;
				if(nextToReturn != null)
					return;
			}
	}

	@Override
	public void rewind() {
		findNext();
	}
}
