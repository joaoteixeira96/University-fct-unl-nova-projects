package exceptions;

import java.io.Serializable;

public class AthleteDontBelongInGroupException extends Exception implements Serializable{


	private static final long serialVersionUID = 0L;

	public AthleteDontBelongInGroupException() {
		super("Atleta nao pertence ao grupo.");
	}

	public AthleteDontBelongInGroupException(String message) {
		super(message);
	}
}
