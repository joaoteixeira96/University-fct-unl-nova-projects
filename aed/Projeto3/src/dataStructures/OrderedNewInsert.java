package dataStructures;

public class OrderedNewInsert <K extends Comparable<K>, V> extends OrderedDoubleList<K,V> implements OrderedList<K, V>{
	
	private static final long serialVersionUID = 0L;

	public OrderedNewInsert (){
		super();
	}

	public V insert(K key, V value) {
		Entry<K, V> element = new EntryClass<K, V>(key, value);
		DListNode<Entry<K, V>> newNode = new DListNode<Entry<K, V>>(element, null, null);
		DListNode<Entry<K, V>> searchNode = head;

		while (searchNode != null) {
			K KSearch = searchNode.getElement().getKey();
			if (key.compareTo(KSearch) == 0){
				if(searchNode.getNext() != null){
					DListNode<Entry<K, V>> searchEqualsNode = searchNode;
					DListNode<Entry<K, V>> KNextSearch = searchEqualsNode.getNext();
					while(KNextSearch != null && key.compareTo(KNextSearch.getElement().getKey()) == 0){
							KNextSearch = KNextSearch.getNext();
					}
					if(KNextSearch == null)
						addLast(newNode);
					else
						addMiddle(newNode, KNextSearch.getPrevious());
				}
				else
					addLast(newNode);
				return null;
			}
			if (searchNode.getNext() != null) {
				K KNextSearch = searchNode.getNext().getElement().getKey();
				if (key.compareTo(KSearch) > 0 && key.compareTo(KNextSearch) < 0) {
					addMiddle(newNode, searchNode);
					return null;
				}
			}
			searchNode = searchNode.getNext();
		}
		if (tail != null && key.compareTo(tail.getElement().getKey()) > 0) //&& )
			addLast(newNode);
		else
			addFirst(newNode);
		return null;
	}
	
	@Override
	public List<V> getValues(){
		List<V> values = new DoublyLinkedList<V>();
		DListNode<Entry<K, V>> searchNode = head;
		for(int i = 0; i < currentSize; i++, searchNode.getNext()){
			values.addLast(searchNode.getElement().getValue());
		}
		return values;
	}
	
}
