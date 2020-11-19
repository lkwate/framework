package exceptions;

/**
 * @author kwate
 * @goal : Exception throw when you attempt to perform some operation between variable which do not belong to the same domain
 */
public class DifferentDomainVariableException extends Exception {

	public DifferentDomainVariableException(String message) {
		// TODO Auto-generated constructor stub
		super(message);
	}
}
