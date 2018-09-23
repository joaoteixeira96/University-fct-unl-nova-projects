package dataStructures;

public class EmptyDictionaryException extends Exception {


	static final long serialVersionUID = 0L;


	public EmptyDictionaryException( )
	{
		super();
	}

	public EmptyDictionaryException( String message )
	{
		super(message);
	}

}