package dataDefinition;

import javafx.beans.property.StringProperty;

public interface IRow {

	public StringProperty getValue(int indexOfColumn);
	
	public String getStringValue(int indexOfColumn);
	
	public void setValue(int indexOfColumn, String value);
	
	public void setValue(int indexOfColumn, StringProperty value);

	public int size();
}
