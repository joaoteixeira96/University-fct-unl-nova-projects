package exceptions;

import java.io.Serializable;

public class InvalidValueException extends Exception implements Serializable{
	
	
	private static final long serialVersionUID = 0L;

	public InvalidValueException() {
		super("Valores invalidos.");
	}
	
	public InvalidValueException(String message) {
		super(message);
	}

}
