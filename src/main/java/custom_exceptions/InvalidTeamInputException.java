package custom_exceptions;

public class InvalidTeamInputException extends Exception {

	private static final long serialVersionUID = 1L;

	public InvalidTeamInputException(String message) {
        super(message);
    }
}
