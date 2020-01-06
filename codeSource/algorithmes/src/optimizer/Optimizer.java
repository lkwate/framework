package optimizer;

import java.util.LinkedHashMap;

import exceptions.DifferentDomainVariableException;
import exceptions.MissingValueofVariableException;
import systemVariable.Formular;
import systemVariable.Variable;

public class Optimizer {

	private LinkedHashMap<String, String> optimizerOneVariable(double beta1, double beta2, double epsilon, int numepoch, Formular f0,
			String varname, double h, double alpha) throws MissingValueofVariableException {

		double df, v = 0, s, vc, sc, var = 0, valf1 = 0, valf0;
		LinkedHashMap<String, String> varMap = new LinkedHashMap<String, String>();
		LinkedHashMap<String, Double> infos = new LinkedHashMap<>();
		varMap.put(varname, "0");
		for (int t = 1; t <= numepoch; t++) {

			valf0 = f0.evaluate(varMap);
			varMap.put(varname, Double.toString(var + h));
			valf1 = f0.evaluate(varMap);
			df = (valf1 - valf0) / h;
			//v = beta1 * v + (1 - beta1) * df;
			//s = beta2 * v + (1 - beta2) * df * df;
			//vc = v / (1 - Math.pow(beta1, t));
			//sc = s / (1 - Math.pow(beta2, t));
			var = var - alpha * df;// / Math.sqrt(sc + epsilon);
			varMap.put(varname, Double.toString(var));

		}
		return varMap;
	}
	
	public static void main(String[] args) throws DifferentDomainVariableException, MissingValueofVariableException {
		Optimizer optimizer = new Optimizer();
		Variable var = new Variable(); 
		var.setIdDomain(15000);
		var.setName("var");
		Formular f = new Formular("( ( var - 3 ) ^ 2 )");
		f.addVariable(var);
		System.out.println(optimizer.optimizerOneVariable(0.9, 0.99, 0.01, 1000, f, var.getName(), 0.01, 0.01));
	}
}
