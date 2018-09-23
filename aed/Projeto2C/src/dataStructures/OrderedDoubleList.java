package dataStructures;

public class OrderedDoubleList<K extends Comparable<K>, V> implements OrderedDictionary<K, V> {

	private static final long serialVersionUID = 0L;

	private DListNode<Entry<K, V>> head;
	private DListNode<Entry<K, V>> tail;

	private int currentSize;

	public OrderedDoubleList() {
		head = null;
		tail = null;
		currentSize = 0;
	}

	@Override
	public boolean isEmpty() {
		return currentSize == 0;
	}

	@Override
	public int size() {
		return currentSize;
	}

	@Override
	public V find(K key) {
		Entry<K, V> search = new EntryClass<K, V>(key, null);
		DListNode<Entry<K, V>> node = head;
		while (node != null && !node.getElement().equals(search)) {
			node = node.getNext();
		}
		if (node == null)
			return null;
		else
			return node.getElement().getValue();

	}

	private void addFirst(DListNode<Entry<K, V>> newNode) {
		if (this.isEmpty())
			tail = newNode;
		else {
			newNode.setNext(head);
			head.setPrevious(newNode);
		}
		head = newNode;
		currentSize++;
	}

	private void addLast(DListNode<Entry<K, V>> newNode) {
		if (this.isEmpty())
			head = newNode;
		else {
			newNode.setPrevious(tail);
			tail.setNext(newNode);
		}
		tail = newNode;
		currentSize++;
	}

	private void addMiddle(DListNode<Entry<K, V>> newNode, DListNode<Entry<K, V>> prevNode) {
		DListNode<Entry<K, V>> nextNode = prevNode.getNext();
		newNode.setNext(nextNode);
		newNode.setPrevious(prevNode);
		prevNode.setNext(newNode);
		nextNode.setPrevious(newNode);
		currentSize++;
	}

	@Override
	public V insert(K key, V value) {
		Entry<K, V> element = new EntryClass<K, V>(key, value);
		DListNode<Entry<K, V>> newNode = new DListNode<Entry<K, V>>(element, null, null);
		DListNode<Entry<K, V>> searchNode = head;

		while (searchNode != null) {
			K KSearch = searchNode.getElement().getKey();
			if (key.compareTo(KSearch) == 0) {
				//replace(newNode, searchNode);
				return searchNode.getElement().setValue(value);
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

	protected void removeFirstNode() {
		head = head.getNext();
		if (head == null)
			tail = null;
		else
			head.setPrevious(null);
		currentSize--;
	}

	private V removeFirst() throws EmptyListException {
		if (this.isEmpty())
			throw new EmptyListException();

		Entry<K, V> element = head.getElement();
		this.removeFirstNode();
		return element.getValue();
	}

	protected void removeLastNode() {
		tail = tail.getPrevious();
		if (tail == null)
			head = null;
		else
			tail.setNext(null);
		currentSize--;
	}

	private V removeLast() throws EmptyListException {
		if (this.isEmpty())
			throw new EmptyListException();

		Entry<K, V> element = tail.getElement();
		this.removeLastNode();
		return element.getValue();
	}

	protected V removeMiddleNode(DListNode<Entry<K, V>> node) {
		DListNode<Entry<K, V>> prevNode = node.getPrevious();
		DListNode<Entry<K, V>> nextNode = node.getNext();
		prevNode.setNext(nextNode);
		nextNode.setPrevious(prevNode);
		currentSize--;
		return node.getElement().getValue();
	}

	@Override
	public V remove(K key) {
		if (this.isEmpty())
			return null;
		DListNode<Entry<K, V>> searchNode = head;
		while (searchNode != tail) {
			K KSearch = searchNode.getElement().getKey();
			if (key.compareTo(KSearch) == 0) {
				if (searchNode == head)
					return removeFirst();
				else
					return removeMiddleNode(searchNode);
			}
			searchNode = searchNode.getNext();
		}
		if (key.compareTo(tail.getElement().getKey()) == 0)
			return removeLast();
		return null;
	}

	// Ordena-os por mais recentes
	@Override
	public Iterator<Entry<K, V>> iterator() {
		return new DoublyLLIterator<Entry<K, V>>(head, tail);
	}

	@Override
	public Entry<K, V> minEntry() throws EmptyDictionaryException {
		if(isEmpty())
			throw new EmptyDictionaryException("Esta vazio.");
		if (head != null)
			return head.getElement();
		return null;
	}

	@Override
	public Entry<K, V> maxEntry() throws EmptyDictionaryException {
		if(isEmpty())
			throw new EmptyDictionaryException("Esta vazio.");
		if (tail != null)
			return tail.getElement();
		return null;
	}

	@Override
	public DListNode<Entry<K, V>> getFirst() {
		return head;
	}

}
