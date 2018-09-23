
package FitnessTracker.exceptions;

import java.io.Serializable;

public class GroupDoesntHaveAthletesException extends Exception implements Serializable{

	
	private static final long serialVersionUID = 0L;

	public GroupDoesntHaveAthletesException() {
		super("Grupo nao tem adesoes.");
	}

	public GroupDoesntHaveAthletesException(String message) {
		super(message);
	}
}
