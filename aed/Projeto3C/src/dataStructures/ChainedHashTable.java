package dataStructures;

/**
 * Chained Hash table implementation
 * 
 * @author AED Team
 * @version 1.0
 * @param <K>
 *            Generic Key, must extend comparable
 * @param <V>
 *            Generic Value
 */

public class ChainedHashTable<K extends Comparable<K>, V> extends HashTable<K, V> {
	/**
	 * Serial Version UID of the Class.
	 */
	static final long serialVersionUID = 0L;

	/**
	 * The array of dictionaries.
	 */
	protected Dictionary<K, V>[] table;

	/**
	 * Constructor of an empty chained hash table, with the specified initial
	 * capacity. Each position of the array is initialized to a new ordered list
	 * maxSize is initialized to the capacity.
	 * 
	 * @param capacity
	 *            defines the table capacity.
	 */

	public ChainedHashTable(int capacity) {
		table = initilizeTable(capacity,table);
	}
	
	@SuppressWarnings("unchecked")
	private Dictionary<K, V>[] initilizeTable(int capacity, Dictionary<K, V>[] table) {
		int arraySize = HashTable.nextPrime((int) (1.1 * capacity));
		// Compiler gives a warning.
		table = (Dictionary<K, V>[]) new Dictionary[arraySize];
		for (int i = 0; i < arraySize; i++)
			table[i] = new OrderedDoubleList<K, V>();
		maxSize = capacity;
		return table;
	}

	public ChainedHashTable() {
		this(DEFAULT_CAPACITY);
	}

	/**
	 * Returns the hash value of the specified key.
	 * 
	 * @param key
	 *            to be encoded
	 * @return hash value of the specified key
	 */
	protected int hash(K key) {
		return Math.abs(key.hashCode()) % table.length;
	}

	@Override
	public V find(K key) {
		return table[this.hash(key)].find(key);
	}

	protected void rehash(){
		Dictionary<K, V>[] newTable = null; 
		newTable = initilizeTable(currentSize * 2, newTable);
		
		newTable.hashCode();
		Iterator<Entry<K,V>> it;
		it = this.iterator();
		while (it.hasNext()) {
			Entry<K,V> entry = it.next();
			int key = Math.abs(entry.getKey().hashCode()) % newTable.length;
			newTable[key].insert(entry.getKey(), entry.getValue());
		}
		table = newTable;
	}

	@Override
	public V insert(K key, V value) {
		if (this.isFull()){
			this.rehash();
			return null;
		}
		V result = table[this.hash(key)].insert(key, value);
		if(result == null)
			currentSize++;
		return result;
	}

	@Override
	public V remove(K key) {
		V result = table[this.hash(key)].remove(key);
		if(result != null)
			currentSize--;
		return result;
	}

	@Override
	public Iterator<Entry<K, V>> iterator() {
		return new ChainedhashIterator<K, V>(table);
	}
}