package FitnessTracker.exceptions;

import java.io.Serializable;

public class InvalidNumberStepsException extends Exception implements Serializable{

	
	private static final long serialVersionUID = 0L;

	public InvalidNumberStepsException() {
		super("Numero de passos invalido.");
	}

	public InvalidNumberStepsException(String message) {
		super(message);
	}
}
