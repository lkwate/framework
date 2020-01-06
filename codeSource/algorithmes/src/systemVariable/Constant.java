package systemVariable;

import java.util.LinkedHashMap;

import exceptions.IncorrectValueofConstantException;

/**
 * Description of constant
 * 
 * @author kwate
 *
 */
public class Constant extends AtomicValue {

	private final String value;

	public Constant(String name, String value) throws IncorrectValueofConstantException {
		super();
		this.setName(name);
		Constant.checkConstantValue(value);
		this.value = value;
	}

	
	public String getValue() {
		return value;
	}

	public String toString() {
		return this.getName()+" = "+this.value;
	}
	
	private static void checkConstantValue(String value) throws IncorrectValueofConstantException {
		if (value == null) throw new NullPointerException();
		LinkedHashMap<Character, Integer> mapChecker = new LinkedHashMap<Character, Integer>();
		int temp = 0;
		for (int i = 0; i < value.length(); i++) {
			if (!mapChecker.containsKey(value.charAt(i))) 
				mapChecker.put(value.charAt(i), 1);
			else {
				temp = mapChecker.get(value);
				mapChecker.put(value.charAt(i), temp + 1);
			}
		}
		temp = 0;
		for (int i = 0;i < 10; i++) {
			temp += mapChecker.get((char)i);
		}
		if (temp < value.length() - 1) throw new IncorrectValueofConstantException("incorrect value of constante");
	}
}
