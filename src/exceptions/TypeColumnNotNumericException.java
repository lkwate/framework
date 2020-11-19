package exceptions;

public class TypeColumnNotNumericException extends Exception{

    public TypeColumnNotNumericException() {
        super();
    }

    public TypeColumnNotNumericException(String message) {
        super(message);
    }
}
