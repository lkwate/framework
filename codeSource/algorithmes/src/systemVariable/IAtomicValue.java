package systemVariable;

import exceptions.DifferentDomainVariableException;

public interface IAtomicValue {

	/**
	 * arithmetical operation
	 */
	/**
	 * 
	 * @param other
	 * @return
	 */
	Formular add(AtomicValue other) throws Throwable;

	static Formular add(AtomicValue var1, AtomicValue var2) throws DifferentDomainVariableException {
		if (var1 == null || var2 == null)
			throw new IllegalArgumentException();
		/*
		 * check whether variable are in the same domain.
		 */
		if (var1.getIdDomain() != var2.getIdDomain())
			throw new DifferentDomainVariableException("variable are not in the same domain");
		String value1, value2;
		/**
		 * building block of value of atomicValue parameters
		 */
		{
			if (var1 instanceof Variable) {
				value1 = var1.getName();
			} else
				value1 = ((Constant) var1).getValue();
			if (var2 instanceof Variable) {
				value2 = var2.getName();
			} else
				value2 = ((Constant) var2).getValue();
		}
		Formular result = new Formular("( " + value1 + " + " + value2 + " )");
		if (var1 instanceof Variable)
			result.addVariable((Variable) var1);
		if (var2 instanceof Variable)
			result.addVariable((Variable) var1);
		result.setIdDomain(var1.getIdDomain());
		return result;
	}

	Formular substract(AtomicValue other) throws Throwable;

	static Formular substract(AtomicValue var1, AtomicValue var2) throws DifferentDomainVariableException {
		if (var1 == null || var2 == null)
			throw new IllegalArgumentException();
		/*
		 * check whether variable are in the same domain.
		 */
		if (var1.getIdDomain() != var2.getIdDomain())
			throw new DifferentDomainVariableException("variable are not in the same domain");

		if (var1.getName().equals(var2.getName())) {
			Formular result = new Formular("0");
			if (var1 instanceof Variable)
				result.addVariable((Variable) var1);
			if (var2 instanceof Variable)
				result.addVariable((Variable) var1);
			return result;
		} else {
			String value1, value2;
			/**
			 * building block of value of atomicValue parameters
			 */
			{
				if (var1 instanceof Variable) {
					value1 = var1.getName();
				} else
					value1 = ((Constant) var1).getValue();
				if (var2 instanceof Variable) {
					value2 = var2.getName();
				} else
					value2 = ((Constant) var2).getValue();
			}
			Formular result = new Formular("( " + value1 + " - " + value2 + " )");
			if (var1 instanceof Variable)
				result.addVariable((Variable) var1);
			if (var2 instanceof Variable)
				result.addVariable((Variable) var1);
			result.setIdDomain(var1.getIdDomain());
			return result;
		}

	}

	Formular divide(AtomicValue other) throws Throwable;

	static Formular divide(AtomicValue var1, AtomicValue var2) throws DifferentDomainVariableException {
		if (var1 == null || var2 == null)
			throw new IllegalArgumentException();
		/*
		 * check whether variable are in the same domain.
		 */
		if (var1.getIdDomain() != var2.getIdDomain())
			throw new DifferentDomainVariableException("variable are not in the same domain");

		if (var1.getName().equals(var2.getName())) {
			Formular result = new Formular("1");
			if (var1 instanceof Variable)
				result.addVariable((Variable) var1);
			if (var2 instanceof Variable)
				result.addVariable((Variable) var1);
			return result;
		} else {
			String value1, value2;
			/**
			 * building block of value of atomicValue parameters
			 */
			{
				if (var1 instanceof Variable) {
					value1 = var1.getName();
				} else
					value1 = ((Constant) var1).getValue();
				if (var2 instanceof Variable) {
					value2 = var2.getName();
				} else
					value2 = ((Constant) var2).getValue();
			}
			Formular result = new Formular("( " + value1 + " / " + value2 + " )");
			if (var1 instanceof Variable)
				result.addVariable((Variable) var1);
			if (var2 instanceof Variable)
				result.addVariable((Variable) var1);
			result.setIdDomain(var1.getIdDomain());
			return result;
		}

	}

	Formular multiply(AtomicValue other) throws Throwable;

	static Formular multiply(AtomicValue var1, AtomicValue var2) throws DifferentDomainVariableException {
		if (var1 == null || var2 == null)
			throw new IllegalArgumentException();
		/*
		 * check whether variable are in the same domain.
		 */
		if (var1.getIdDomain() != var2.getIdDomain())
			throw new DifferentDomainVariableException("variable are not in the same domain");

		/**
		 * building block of formular
		 */
		{
			String value1, value2;
			/**
			 * building block of value of atomicValue parameters
			 */
			{
				if (var1 instanceof Variable) {
					value1 = var1.getName();
				} else
					value1 = ((Constant) var1).getValue();
				if (var2 instanceof Variable) {
					value2 = var2.getName();
				} else
					value2 = ((Constant) var2).getValue();
			}
			Formular result;
			if (value1.trim().equals("0") || value2.trim().equals("0")) {
				result = new Formular("0");
			} else {
				result = new Formular("( " + value1 + " * " + value2 + " )");
			}
			result.setIdDomain(var1.getIdDomain());
			if (var1 instanceof Variable)
				result.addVariable((Variable) var1);
			if (var2 instanceof Variable)
				result.addVariable((Variable) var1);
			return result;
		}

	}

	/**
	 * cette fonction permet d'appliquer une fonction f dont le nom est
	 * <b>functionName</b> à la variable var et produit en résultat une formule.
	 * fonctional Transformation
	 */

	Formular apply(String functionName) throws Throwable;

	static Formular apply(String functionName, AtomicValue var) throws DifferentDomainVariableException {
		if (functionName == null)
			throw new NullPointerException();
		String value;
		/**
		 * building block of value of atomicValue parameters
		 */
		{
			if (var instanceof Variable) {
				value = var.getName();
			} else
				value = ((Constant) var).getValue();
		}
		Formular result = new Formular(functionName + "( " + value + " )");
		if (var instanceof Variable)
			result.addVariable((Variable) var);
		return result;
	}
}
