package exceptions;

import java.io.Serializable;

public class GroupAlreadyExistExecption extends Exception implements Serializable{

	
	private static final long serialVersionUID = 0L;

	public GroupAlreadyExistExecption() {
		super("Grupo existente.");
	}

	public GroupAlreadyExistExecption(String message) {
		super(message);
	}
}
