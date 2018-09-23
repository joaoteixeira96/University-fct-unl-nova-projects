package exceptions;

import java.io.Serializable;

public class InvalidTimeException extends Exception implements Serializable{

	
	private static final long serialVersionUID = 0L;

	public InvalidTimeException() {
		super("Tempo invalido.");
	}

	public InvalidTimeException(String message) {
		super(message);
	}
}
