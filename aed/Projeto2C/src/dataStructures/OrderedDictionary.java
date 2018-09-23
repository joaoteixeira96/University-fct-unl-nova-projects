package dataStructures; 

/**
 * @author Pedro Xavier (47525) <p.xavier@campus.fct.unl.pt>
 * @author Joao Teixeira (48047) <ja.teixeira@campus.fct.unl.pt>
 */
public interface OrderedDictionary<K extends Comparable<K>, V> extends Dictionary<K,V>{                                                                   


    /**
     * @return the entry with the smallest key in the dictionary.
     * @throws EmptyDictionaryException Se o dicionario estiver vazio
     */
    Entry<K,V> minEntry( ) throws EmptyDictionaryException;

  
    /**
     * @return the entry with the largest key in the dictionary.
     * @throws EmptyDictionaryException Se o dicionario estiver vazio
     */
    Entry<K,V> maxEntry( ) throws EmptyDictionaryException;

	/**
	 * @return an iterator of the entries in the dictionary which preserves the key order relation.
	 */
	DListNode<Entry<K, V>> getFirst();
  

} 
