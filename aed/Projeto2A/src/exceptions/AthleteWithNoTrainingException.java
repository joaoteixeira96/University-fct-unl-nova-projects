package exceptions;

import java.io.Serializable;

public class AthleteWithNoTrainingException extends Exception implements Serializable {

	
	private static final long serialVersionUID = 0L;

	public AthleteWithNoTrainingException() {
		super("Atleta sem treinos.");
	}

	public AthleteWithNoTrainingException(String message) {
		super(message);
	}
}
