package exceptions;

import java.io.Serializable;

public class AthleteNotExistException extends Exception implements Serializable{

	
	private static final long serialVersionUID = 0L;

	public AthleteNotExistException() {
		super("Atleta inexistente.");
	}

	public AthleteNotExistException(String message) {
		super(message);
	}
}
