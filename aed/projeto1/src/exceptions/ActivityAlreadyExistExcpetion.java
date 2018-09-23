package exceptions;

import java.io.Serializable;

public class ActivityAlreadyExistExcpetion extends Exception implements Serializable{

	
	private static final long serialVersionUID = 0L;

	public ActivityAlreadyExistExcpetion() {
		super("Atividade existente.");
	}

	public ActivityAlreadyExistExcpetion(String message) {
		super(message);
	}
}
