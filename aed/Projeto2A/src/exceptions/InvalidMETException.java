package exceptions;

import java.io.Serializable;

public class InvalidMETException extends Exception implements Serializable{

	
	private static final long serialVersionUID = 0L;

	public InvalidMETException() {
		super("MET invalido.");
	}

	public InvalidMETException(String message) {
		super(message);
	}
}
