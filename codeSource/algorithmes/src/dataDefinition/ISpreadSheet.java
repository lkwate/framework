package dataDefinition;

import java.util.*;

import javafx.beans.property.StringProperty;
import javafx.collections.ObservableList;

public interface ISpreadSheet {

	public int indexOfColumnName(String columnName);

	public String ColumnNameOfIndex(int index);

	void applyOnColumn(String columnName, String functionName) throws Throwable;
	
	void hideColumn(String columnName) throws Throwable;
	
	void hideRow(BiPredicate<String, Rule> predicate, String colunName) throws Throwable;
	
	void hideRow(LinkedHashMap<String, BiPredicate<String, Rule>> setOfPredicate) throws Throwable;
	
	Row selectRow(int indexOfRow) throws Throwable;

	ObservableList<Row> getDataForEditor(String targetColumn);
	
	ObservableList<String> selectColumn(int indexOfColumn) throws Throwable;

	ObservableList<String> selectColumn(String columnName) throws Throwable;
	
	ObservableList<Row> searchRowByColumnValue(String columnName, String value) throws Throwable;
	
	ObservableList<Row> searchRowByColumnMatch(String columnName, BiPredicate<String, Rule> matchFunction);
	
	ObservableList<Row> searchRowContainsValue(String value, LinkedHashMap<String, String> renderInformation);
	
	ObservableList<Row> getRows() throws Throwable;
	
	Collection<ColumnMetaData> getColumnsMetaData() throws Throwable;
	
	String getString(String columnName, int indexRow) throws Throwable;
	
	double getDouble(String columnName, int indexRow) throws Throwable;
	
	float getFloat(String columnName, int indexRow) throws Throwable;
	
	int getInt(String columnName, int indexRow) throws Throwable;
	
	byte getByte(String columnName, int indexRow) throws Throwable;
	
	void setValue(String columnName, int indexRow) throws Throwable;
	
	void insertRow(Row row) throws Throwable;

	void deleteRow(Row row) throws Throwable;

	void updateRow(Row row) throws Throwable;
	
	int numberOfRow();
	
	int numberOfColumn();

	void loadEndOfRubric(String catalog, String tableName, String schema, List<String> columns, int limit, int offset, List<LinkedHashMap<String, Rule>> rules);

}
