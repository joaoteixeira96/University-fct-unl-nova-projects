package exceptions;

import java.io.Serializable;

public class ActivityNotExistException extends Exception implements Serializable {

	
	private static final long serialVersionUID = 0L;

	public ActivityNotExistException() {
		super("Atividade inexistente.");
	}

	public ActivityNotExistException(String message) {
		super(message);
	}
}
