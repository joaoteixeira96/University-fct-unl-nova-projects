package dataStructures;

public class OrderedDoubleList<K extends Comparable<K>, V> implements OrderedDictionary<K,V> {

	private static final long serialVersionUID = 0L;

    private DListNode<Entry<K,V>> head;
    private DListNode<Entry<K,V>> tail;

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
		Entry<K,V> search= new EntryClass<K, V>(key, null);
		DListNode<Entry<K, V>> node = head;
        while ( node != null && !node.getElement().equals(search) )
        {
            node = node.getNext();
        }
        if ( node == null )
            return null;
        else
            return node.getElement().getValue();
		
	}
	
    private void addFirst(DListNode<Entry<K, V>> newNode ){
        //DListNode<Entry<K, V>> newNode = new DListNode<Entry<K, V>>(newNode2, null, head);
        if ( this.isEmpty() )
            tail = newNode;
        else{
        	newNode.setNext(head);
            head.setPrevious(newNode);
        }
        head = newNode;
        currentSize++;
    }


    private void addLast( DListNode<Entry<K,V>> newNode ){
        if ( this.isEmpty() )
            head = newNode;
        else{
        	newNode.setPrevious(tail);
            tail.setNext(newNode);
        }
        tail = newNode;
        currentSize++;
    }
    
    private void addMiddle(DListNode<Entry<K,V>> newNode, DListNode<Entry<K, V>> prevNode){
        DListNode<Entry<K, V>> nextNode = prevNode.getNext();
        newNode.setNext(nextNode);
        newNode.setPrevious(prevNode);
        prevNode.setNext(newNode);            
        nextNode.setPrevious(newNode);            
        currentSize++;
    }
    
    private void replace(DListNode<Entry<K,V>> newNode,DListNode<Entry<K, V>> oldNode){
    	newNode.setNext(oldNode.getNext());
    	newNode.setPrevious(oldNode.getPrevious());
    	oldNode.setNext(null);
    	oldNode.setPrevious(null);
    }

	@Override
	public V insert(K key, V value) {
		Entry<K,V> element = new EntryClass<K, V>(key, value);
		DListNode<Entry<K,V>> newNode = new DListNode<Entry<K,V>>(element, null, null);
		DListNode<Entry<K, V>> searchNode = head;
		while(searchNode != tail){
			K KSeach = searchNode.getElement().getKey();
			K KNextSearch = searchNode.getNext().getElement().getKey();
			if(key.compareTo(KSeach) == 0){
				replace(newNode, searchNode);
				return searchNode.getElement().getValue();
			}
			if(searchNode != tail && key.compareTo(KSeach) < 0 && key.compareTo(KNextSearch) > 0){
				addMiddle(newNode,searchNode);
				return null;
			}
			if(searchNode == tail){
				addLast(newNode);
				return null;
			}
			searchNode.getNext();
		}
		addFirst(newNode);
		return null;
	}
	
	@Override
	public V insert2(K key, V value) {
		Entry<K,V> element = new EntryClass<K, V>(key, value);
		DListNode<Entry<K,V>> newNode = new DListNode<Entry<K,V>>(element, null, null);
		DListNode<Entry<K, V>> searchNode = head;
		V result = null;
		DListNode<Entry<K, V>> NextNode ;
		while(searchNode != tail){
			K KSearch = searchNode.getElement().getKey();
			K KNextSearch = searchNode.getNext().getElement().getKey();
			NextNode = searchNode.getNext();
			
			if(key.compareTo(KSearch) == 0){
				result = searchNode.getElement().getValue();
				replace(newNode, searchNode);
				searchNode.getNext();
				
				return result ;
			}
			if(key.compareTo(KSearch) < 0 && key.compareTo(KNextSearch) > 0){
				result = searchNode.getElement().getValue();
				DListNode<Entry<K,V>> MidNode = new DListNode<Entry<K,V>>(element,NextNode,searchNode);
				searchNode.setNext(MidNode);
				NextNode.setPrevious(MidNode);
				currentSize++;
				//searchNode.getNext() - anda com a cabeça ate a cauda
				searchNode.getNext();
				
				
				return result;
			}
		}
			if(searchNode == tail){
				//result = searchNode.getElement().getValue();
				addLast(newNode);
				return result;
			}
			else
				result = searchNode.getElement().getValue();
				addFirst(newNode);
			return result;
		}
	
	

	@Override
	public V remove(K key) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Iterator<Entry<K, V>> iterator() {
		// TODO Auto-generated method stub
		return new DoublyLLIterator<Entry<K,V>>(head, tail);
	}

	@Override
	public Entry<K, V> minEntry() throws EmptyDictionaryException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Entry<K, V> maxEntry() throws EmptyDictionaryException {
		// TODO Auto-generated method stub
		return null;
	}

	
}
