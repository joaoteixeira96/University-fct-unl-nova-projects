package FitnessTracker.exceptions;

import java.io.Serializable;

public class GroupNotExistException extends Exception implements Serializable{

	
	private static final long serialVersionUID = 0L;

	public GroupNotExistException() {
		super("Grupo inexistente.");
	}

	public GroupNotExistException(String message) {
		super(message);
	}
}
