package dataStructures;

public class EntryClass<K,V> implements Entry<K, V> {
	
	private static final long serialVersionUID = 0L;
	
	private K key;
	private V value;
	
	public EntryClass(K key, V value) {
		this.key = key;
		this.value = value;
	}

	@Override
	public K getKey() {
		return key;
	}

	@Override
	public V getValue() {
		return value;
	}
	
	protected V setValue(V newValue){
		V result = value;
		value = newValue;
		return result;
	}
	
	protected K setKey(K newKey){
		K result = key;
		key = newKey;
		return result;
	}
	
	public boolean equals(Object obj) {
		@SuppressWarnings("unchecked")
		Entry<K, V> entry = (Entry<K, V>) obj;
		return(entry.getKey().equals(this.key));
	}

}
