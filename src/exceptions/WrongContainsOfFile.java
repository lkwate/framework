package exceptions;

/**
 * exception thrown where the contains of file is wrong
 */
public class WrongContainsOfFile extends Exception {

    public WrongContainsOfFile(String message) {
        super(message);
    }
}
