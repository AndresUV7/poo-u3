package custom_exceptions;

public class InvalidStageInputException extends Exception {

	private static final long serialVersionUID = 1L;

	public InvalidStageInputException(String message) {
        super(message);
    }
}