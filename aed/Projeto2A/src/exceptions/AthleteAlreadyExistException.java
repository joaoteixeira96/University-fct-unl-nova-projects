package exceptions;

import java.io.Serializable;

public class AthleteAlreadyExistException extends Exception implements Serializable {

	
	private static final long serialVersionUID = 0L;

	public AthleteAlreadyExistException() {
		super("Atleta existente.");
	}

	public AthleteAlreadyExistException(String message) {
		super(message);
	}

}
