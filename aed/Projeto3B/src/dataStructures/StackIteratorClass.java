package dataStructures;

public class StackIteratorClass<E> extends StackInList<E> implements StackIterator<E>{
	
	private static final long serialVersionUID = 0L;

	@Override
	public Iterator<E> iterator() {
		return list.iterator();
	}

}
