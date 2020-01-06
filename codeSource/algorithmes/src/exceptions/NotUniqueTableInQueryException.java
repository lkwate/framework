package exceptions;

public class NotUniqueTableInQueryException extends Exception{

    public NotUniqueTableInQueryException(String message) {
        super(message);
    }
}
