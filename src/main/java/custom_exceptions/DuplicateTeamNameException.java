package custom_exceptions;

public class DuplicateTeamNameException extends Exception {
    private static final long serialVersionUID = 1L;

	public DuplicateTeamNameException(String message) {
        super(message);
    }
}
