package dataStructures;

public interface OrderedList<K extends Comparable<K>, V> extends OrderedDictionary<K, V> {
	
	List<V> getValues();
	
}
