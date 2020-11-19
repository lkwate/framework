package dataDefinition;

public class Rule {

	public static final String GREATER = ">";
	public static final String GREATEROREQUAL = ">=";
	public static final String EQUAL = "=";
	public static final String LIKE = "LIKE";
	public static final String LESS = "<";
	public static final String LESSOREQUAL = "<=";
	public static final String NOTEQUAL = "<>";


	private String operator = new String();
	private String value = new String();

	public String getOperator() {
		return operator;
	}

	public void setOperator(String operator) {
		this.operator = operator;
	}

	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}

	public String toString() {
		return "Rule : op = "+this.operator + "\t value = "+this.value;
	}
}
