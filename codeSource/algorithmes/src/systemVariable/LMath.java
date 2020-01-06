package systemVariable;

import java.util.LinkedHashMap;

public class LMath {

	/**
	 * names of function
	 */
	public static final String ABS = "abs";
	public static final String ASIN = "asin";
	public static final String ATAN = "atan";
	public static final String COS = "cos";
	public static final String SIN = "sin";
	public static final String TAN = "tan";
	public static final String EXP = "exp";
	public static final String LOG = "log";
	public static final String POW = "pow";
	public static final String SQRT = "sqrt";
	
	private static LinkedHashMap<String, IFunction> singleFunctions = new LinkedHashMap<String, IFunction>();
	static {
		LMath.singleFunctions.put(LMath.ABS, c ->{return Math.abs(c);});
		LMath.singleFunctions.put(LMath.ASIN, c ->{return Math.asin(c);});
		LMath.singleFunctions.put(LMath.ATAN, c ->{return Math.atan(c);});
		LMath.singleFunctions.put(LMath.COS, c ->{return Math.cos(c);});
		LMath.singleFunctions.put(LMath.SIN, c ->{return Math.sin(c);});
		LMath.singleFunctions.put(LMath.TAN, c ->{return Math.tan(c);});
		LMath.singleFunctions.put(LMath.EXP, c ->{return Math.exp(c);});
		LMath.singleFunctions.put(LMath.LOG, c ->{return Math.log(c);});
		LMath.singleFunctions.put(LMath.SQRT, c ->{return Math.sqrt(c);});
	}
	
	public static boolean functionExists(String function) {
		return LMath.singleFunctions.containsKey(function);
	}
	
	public static IFunction getOneParameterFunction(String name) {
		return LMath.singleFunctions.get(name);
	}
}
