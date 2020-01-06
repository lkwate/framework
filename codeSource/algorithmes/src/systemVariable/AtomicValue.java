package systemVariable;

import exceptions.DifferentDomainVariableException;

public class AtomicValue implements IAtomicValue {

	private String name = "";
	private long idDomain;

	public long getIdDomain() {
		return idDomain;
	}

	public void setIdDomain(long idDomain) {
		this.idDomain = idDomain;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	@Override
	public Formular add(AtomicValue other) throws DifferentDomainVariableException {
		// TODO Auto-generated method stub
		return IAtomicValue.add(this, other);
	}

	@Override
	public Formular substract(AtomicValue other) throws DifferentDomainVariableException {
		// TODO Auto-generated method stub
		return IAtomicValue.substract(this, other);
	}

	@Override
	public Formular divide(AtomicValue other) throws DifferentDomainVariableException {
		// TODO Auto-generated method stub
		return IAtomicValue.divide(this, other);
	}

	@Override
	public Formular multiply(AtomicValue other) throws DifferentDomainVariableException {
		// TODO Auto-generated method stub
		return IAtomicValue.multiply(this, other);
	}		

	@Override
	public Formular apply(String functionName) throws DifferentDomainVariableException {
		// TODO Auto-generated method stub
		return IAtomicValue.apply(functionName, this);
	}

}
