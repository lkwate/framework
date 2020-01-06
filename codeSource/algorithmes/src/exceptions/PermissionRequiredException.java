package exceptions;

/**
 * @author kwate
 * goal : Exception throws when a user attempt to perform some operation without permission onto the system.
 */
public class PermissionRequiredException extends Exception {

    public PermissionRequiredException(String message) {
        super(message);
    }
}
