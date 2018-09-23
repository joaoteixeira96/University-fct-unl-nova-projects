package FitnessTracker.exceptions;

import java.io.Serializable;

public class InvalidOptionException extends Exception implements Serializable{

	
	private static final long serialVersionUID = 0L;

	public InvalidOptionException() {
		super("Opcao invalida.");
	}

	public InvalidOptionException(String message) {
		super(message);
	}
}
