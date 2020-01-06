package systemVariable;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.List;

import exceptions.DifferentDomainVariableException;
import exceptions.MissingValueofVariableException;
import sun.reflect.generics.reflectiveObjects.NotImplementedException;

/**
 * Description of formular consist of mathematical operation on variable and
 * constant
 * 
 * @author kwate
 *
 */
public class Formular implements IFormular {

	private String schema;
	private long idDomain;
	private LinkedHashMap<String, Variable> variables = new LinkedHashMap<>();

	public Formular(String schema) {
		this.schema = schema;
	}

	public String getSchema() {
		return schema;
	}
	
	public void setSchema(String schema) {
		this.schema = schema;
	}

	public long getIdDomain() {
		return idDomain;
	}

	public void setIdDomain(long idDomain) {
		this.idDomain = idDomain;
	}

	public void addVariable(Variable value) throws DifferentDomainVariableException {
		if (value == null)
			throw new IllegalArgumentException();
		if (this.idDomain == 0) {
			this.idDomain = value.getIdDomain();
			this.variables.put(value.getName(), value);
		} else if (this.idDomain != value.getIdDomain())
			throw new DifferentDomainVariableException("variable is not in the same domain");
		this.variables.put(value.getName(), value);
		
	}

	public LinkedHashMap<String, Variable> getVariables() {
		return variables;
	}

	public void setVariables(LinkedHashMap<String, Variable> variables) {
		this.variables = variables;
	}

	@Override
	public Collection<? extends Variable> getVariablesList() {
		// TODO Auto-generated method stub
		List<Variable> result = new ArrayList<>();
		result.addAll(this.variables.values());
		return result;
	}

	@Override
	public Formular add(Formular other) throws DifferentDomainVariableException {
		// TODO Auto-generated method stub
		return IFormular.add(this, other);
	}

	@Override
	public Formular substract(Formular other) throws DifferentDomainVariableException {
		// TODO Auto-generated method stub
		return IFormular.substrat(this, other);
	}

	@Override
	public Formular divide(Formular other) throws DifferentDomainVariableException {
		// TODO Auto-generated method stub
		return IFormular.divide(this, other);
	}

	@Override
	public Formular multiply(Formular other) throws DifferentDomainVariableException {
		// TODO Auto-generated method stub
		return IFormular.mutiply(this, other);
	}

	@Override
	public Formular apply(String functionName) {
		// TODO Auto-generated method stub
		return IFormular.apply(this, functionName);
	}

	@Override
	public boolean equals(Object obj) {
		// TODO Auto-generated method stub
		if (obj == null)
			return false;
		if (obj instanceof Formular) {
			if (this == obj)
				return true;
			Formular other = (Formular) obj;
			try {
				IFormular.checkSameDomain(this, other);
				return this.getSchema().equals(other.getSchema());
			} catch (DifferentDomainVariableException e) {
				// TODO Auto-generated catch block
				return false;
			}
		}
		return false;
	}

	@Override
	public Double evaluate(LinkedHashMap<String, String> mapValue) throws MissingValueofVariableException {
		// TODO Auto-generated method stub
		if (mapValue == null) throw new IllegalArgumentException();
		String copySchema = this.schema;
		if (this.variables.size() == 0) 
			return IFormular.evaluate(copySchema);
		
		for (String name : this.variables.keySet()) {
			if (!mapValue.containsKey(name)) throw new MissingValueofVariableException("There is a variable which does not have value before evaluation");
			copySchema = copySchema.replace(name, mapValue.get(name));
		}
		return IFormular.evaluate(copySchema);
	}

	@Override
	public BigDecimal StrongEvaluation() {
		// TODO Auto-generated method stub
		throw new NotImplementedException();
	}

	public Object clone() throws CloneNotSupportedException {
		// TODO Auto-generated method stub
		Formular twin = new Formular(this.getSchema());
		twin.setIdDomain(idDomain);
		twin.setVariables((LinkedHashMap<String, Variable>)this.variables.clone());
		return super.clone();
	}

	
}
