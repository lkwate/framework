package systemVariable;

import java.math.BigDecimal;
import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.HashSet;
import java.util.Stack;

import exceptions.DifferentDomainVariableException;

public interface IFormular {

	static void checkSameDomain(Formular f1, Formular f2) throws DifferentDomainVariableException {
		if (f1 == null || f2 == null)
			throw new IllegalArgumentException();
		if (f1 == f2)
			return;
		HashSet<Long> verificatorId = new HashSet<>();
		for (Variable v : f1.getVariablesList())
			verificatorId.add(v.getIdDomain());
		for (Variable v : f2.getVariablesList())
			verificatorId.add(v.getIdDomain());
		if (verificatorId.size() > 1)
			throw new DifferentDomainVariableException("formular are not in the same domain");
	}

	Formular add(Formular other) throws Throwable;

	Formular substract(Formular other) throws Throwable;

	Formular divide(Formular other) throws Throwable;

	Formular multiply(Formular other) throws Throwable;

	Formular apply(String functionName) throws Throwable;

	Collection<? extends Variable> getVariablesList();

	Double evaluate(LinkedHashMap<String, String> mapVariableNameValue) throws Throwable;

	BigDecimal StrongEvaluation();

	static Formular add(Formular f1, Formular f2) throws DifferentDomainVariableException {
		IFormular.checkSameDomain(f1, f2);
		StringBuilder schemaResult = new StringBuilder("( ");
		schemaResult.append(f1.getSchema());
		schemaResult.append(" + ");
		schemaResult.append(f2.getSchema());
		schemaResult.append(" )");
		Formular result = new Formular(schemaResult.toString());
		LinkedHashMap<String, Variable> variables = f1.getVariables();
		variables.putAll(f2.getVariables());
		result.setVariables(variables);
		return result;

	}

	static Formular substrat(Formular f1, Formular f2) throws DifferentDomainVariableException {
		IFormular.checkSameDomain(f1, f2);
		if (f1.equals(f2)) {
			Formular result = new Formular("0");
			LinkedHashMap<String, Variable> variables = f1.getVariables();
			variables.putAll(f2.getVariables());
			result.setVariables(variables);
			return result;
		} else {
			StringBuilder schemaResult = new StringBuilder("( ");
			schemaResult.append(f1.getSchema());
			schemaResult.append(" - ");
			schemaResult.append(f2.getSchema());
			schemaResult.append(" )");
			Formular result = new Formular(schemaResult.toString());
			LinkedHashMap<String, Variable> variables = f1.getVariables();
			variables.putAll(f2.getVariables());
			result.setVariables(variables);
			return result;
		}
	}

	static Formular divide(Formular f1, Formular f2) throws DifferentDomainVariableException {
		IFormular.checkSameDomain(f1, f2);
		if (f1.equals(f2)) {
			Formular result = new Formular("1");
			LinkedHashMap<String, Variable> variables = f1.getVariables();
			variables.putAll(f2.getVariables());
			result.setVariables(variables);
			return result;
		} else {
			StringBuilder schemaResult = new StringBuilder("( ");
			schemaResult.append(f1.getSchema());
			schemaResult.append(" / ");
			schemaResult.append(f2.getSchema());
			schemaResult.append(" )");
			Formular result = new Formular(schemaResult.toString());
			LinkedHashMap<String, Variable> variables = f1.getVariables();
			variables.putAll(f2.getVariables());
			result.setVariables(variables);
			return result;
		}
	}

	static Formular mutiply(Formular f1, Formular f2) throws DifferentDomainVariableException {
		IFormular.checkSameDomain(f1, f2);
		{
			StringBuilder schemaResult = new StringBuilder("( ");
			schemaResult.append(f1.getSchema());
			schemaResult.append(" * ");
			schemaResult.append(f2.getSchema());
			schemaResult.append(" )");
			Formular result = new Formular(schemaResult.toString());
			LinkedHashMap<String, Variable> variables = f1.getVariables();
			variables.putAll(f2.getVariables());
			result.setVariables(variables);
			return result;
		}
	}

	static Formular apply(Formular f, String name) {
		StringBuilder schemaResult = new StringBuilder(name + " ( ");
		schemaResult.append(f.getSchema());
		schemaResult.append(" )");
		Formular result = new Formular(schemaResult.toString());
		result.setVariables(f.getVariables());
		return result;
	}

	static double evaluate(String formular) {
		Stack<String> ops = new Stack<String>();
		Stack<Double> vals = new Stack<Double>();
		String op;
		double v;
		for (String s : formular.split(" ")) {
			if (s.equals("("))
				continue;
			if (s.equals("+") || s.equals("-") || s.equals("*") || s.equals("/") || s.equals("^")
					|| LMath.functionExists(s))
				ops.push(s);

			else if (s.equals(")")) {
				op = ops.pop();
				v = vals.pop();
				switch (op) {
					case "+":
						v = vals.pop() + v;
						break;
					case "-":
						v = vals.pop() - v;
						break;
					case "*":
						v = vals.pop() * v;
						break;
					case "/":
						v = vals.pop() / v;
						break;
					case "^":
						v = Math.pow(vals.pop(), v);
						break;
					default:
						v = LMath.getOneParameterFunction(op).compute(v);
						break;
				}
				vals.push(v);

			} else
				vals.push(Double.parseDouble(s));
		}
		return vals.pop();
	}

}
