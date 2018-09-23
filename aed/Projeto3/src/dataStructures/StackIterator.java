package dataStructures;

/**
 * @author Pedro Xavier (47525) <p.xavier@campus.fct.unl.pt>
 * @author Joao Teixeira (48047) <ja.teixeira@campus.fct.unl.pt>
 */

public interface StackIterator<E> extends Stack<E>{
	
	/**
	 * @return iterador da stack
	 */
	Iterator<E> iterator( );

}
