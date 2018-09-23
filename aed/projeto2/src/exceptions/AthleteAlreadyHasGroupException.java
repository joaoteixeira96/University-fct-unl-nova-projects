package exceptions;

import java.io.Serializable;

public class AthleteAlreadyHasGroupException extends Exception implements Serializable{

	
	private static final long serialVersionUID = 0L;

	public AthleteAlreadyHasGroupException() {
		super("Atleta ja tem grupo.");
	}

	public AthleteAlreadyHasGroupException(String message) {
		super(message);
	}
}
