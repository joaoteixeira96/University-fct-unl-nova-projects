package FitnessTracker.exceptions;

import java.io.Serializable;

public class GroupsDoesntExistException extends Exception implements Serializable{

	
	private static final long serialVersionUID = 0L;

	public GroupsDoesntExistException() {
		super("Nao existem grupos.");
	}

	public GroupsDoesntExistException(String message) {
		super(message);
	}
}
