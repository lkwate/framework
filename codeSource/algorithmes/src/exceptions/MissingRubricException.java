package exceptions;

/**
 * @author kwate
 * goals : Exception throws when you attemps to perform (Create, update) operation on a table which doesn't exist
 */
public class MissingRubricException extends Exception{

    public MissingRubricException(String message) {
        super(message);
    }
}
