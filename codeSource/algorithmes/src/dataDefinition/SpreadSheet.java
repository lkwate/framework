package dataDefinition;

import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.*;
import java.util.function.Predicate;

import com.mysql.cj.exceptions.ConnectionIsClosedException;

import dataBaseManager.DataBaseAdministrator;
import exceptions.CodeOutOfBoundsException;
import exceptions.ImpossibleToInsertException;
import exceptions.NotUniqueTableInQueryException;
import javafx.beans.InvalidationListener;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.collections.transformation.SortedList;
import sun.reflect.generics.reflectiveObjects.NotImplementedException;

public class SpreadSheet implements ISpreadSheet, ObservableList<Row> {

	private SingleSpreadSheet singleSpreadSheet;
	private String tableName = "";
	private String catalogName = "";
	private String schemaName = "";
	private BidirectionalLinkedHashMap<String, Integer> indexColumnName = new BidirectionalLinkedHashMap<String, Integer>();
	private LinkedHashMap<String, ColumnMetaData> columnMetaData = new LinkedHashMap<>();
	private LinkedHashMap<String, ForeignKey> foreignKeys = new LinkedHashMap<>();
	private List<String> keyName = new ArrayList<>();
	private Connection con;
	private LinkedHashMap<String, Relation> relations;
	private List<LinkedHashMap<String, Rule>> rules;


	public SpreadSheet(Connection con, ResultSet resultSet) throws SQLException, NotUniqueTableInQueryException {
		// TODO Auto-generated constructor stub
		if (con == null || resultSet == null) throw new IllegalArgumentException();
		/**
		 * initialization of attributes
		 */

		ColumnMetaData columnTemp;
		this.con = con;
		ResultSetMetaData rsm = resultSet.getMetaData();
		Set<String> tempTable = new HashSet<>();
		Set<String> tempCatalog = new HashSet<>();
		Set<String> temSchema = new HashSet<>();
		for (int t = 1; t <= resultSet.getMetaData().getColumnCount(); t++) {
			this.indexColumnName.putKeyValue(resultSet.getMetaData().getColumnName(t), t - 1);
			// , rsm.getColumnLabel(t), rsm.getColumnTypeName(t),
			// rsm.getColumnDisplaySize(t)
			columnTemp = new ColumnMetaData(t - 1,rsm.getColumnName(t), rsm.getColumnLabel(t), rsm.getColumnTypeName(t),
					rsm.getColumnDisplaySize(t), rsm.getColumnClassName(t));
			columnTemp.setNullable(rsm.isNullable(t) == 1);
			columnTemp.setReadonly(rsm.isReadOnly(t));
			columnTemp.setWritable(rsm.isWritable(t));
			tempTable.add(rsm.getTableName(t));
			tempCatalog.add(rsm.getCatalogName(t));
			temSchema.add(rsm.getSchemaName(t));
			this.columnMetaData.put(columnTemp.getName(), columnTemp);
		}
		/**
		 * check whether all column come from the same table
		 */
		if (tempTable.size() == 1)
			for(String s : tempTable)
			this.tableName = s;
		if (tempCatalog.size() == 1)
			for(String s : tempCatalog)
				this.catalogName = s;
		if (temSchema.size() == 1)
			for(String s : temSchema)
				this.schemaName = s;
		this.retrievePrimaryAndForeignKey(con, this.catalogName, this.schemaName, this.tableName);
		/**
		 * fill row in spreasheet
		 */
		this.singleSpreadSheet = new SingleSpreadSheet(resultSet);
	}

	/**
	 *
	 * @param row
	 * @throws SQLException
	 * @throws CodeOutOfBoundsException
	 * @throws ImpossibleToInsertException
	 * @throws CloneNotSupportedException
	 */
	private void insertIntoDataBase(Row row) throws SQLException, CodeOutOfBoundsException, ImpossibleToInsertException, CloneNotSupportedException {

		DataBaseAdministrator.getQueryManager().insertIntoTable(DataBaseAdministrator.getConnection(),this.catalogName,this.schemaName,this.tableName,row,this.getIndexColumnName());
	}

	private void deleteFromDatabase(Row row) throws SQLException {
		if (row == null) throw new IllegalArgumentException();
		List<LinkedHashMap<String, Rule>> rules = new LinkedList<>();
		Rule rule;
		LinkedHashMap<String, Rule> r;
		for (String key : this.keyName) {
			r = new LinkedHashMap<>(1);
			rule = new Rule();
			rule.setValue(row.getStringValue(this.getIndexColumnName().getValueByKey(key)));
			rule.setOperator(Rule.EQUAL);
			r.put(key, rule);
			rules.add(r);
		}
		DataBaseAdministrator.getQueryManager().deleteFromTable(DataBaseAdministrator.getConnection(), this.catalogName, this.schemaName, this.tableName, rules);
	}

	private void updateOnDataBase(Row row) throws SQLException {
		DataBaseAdministrator.getQueryManager().updateOnTable(DataBaseAdministrator.getConnection(), this.catalogName, this.schemaName, this.tableName, row, this.keyName, this.getIndexColumnName());
	}


	/**
	 * private Method to retrieve MetadataInformation about relations between tables
	 *
	 * @author kwate dassi loic
	 * @throws ConnectionIsClosedException, SQLException, NotUniqueTableInQueryException
	 */
	private void retrievePrimaryAndForeignKey(Connection con, String catalog, String schema, String tableName)
			throws ConnectionIsClosedException, SQLException, NotUniqueTableInQueryException {
		if (con == null || tableName == null)
			throw new IllegalArgumentException();
		DatabaseMetaData dbmd = con.getMetaData();
		ResultSet rs;
		try
		{
			rs = dbmd.getPrimaryKeys(catalog, schema, tableName);
			while (rs.next()) {
				this.keyName.add(rs.getString("COLUMN_NAME"));
			}
		} catch (NullPointerException e) {
			/**
			 * tableName is null
			 */
			throw new NotUniqueTableInQueryException("There are at least one table frow where come column in query");
		}

		try
		{
			rs = dbmd.getImportedKeys(catalog, schema, tableName);
			ForeignKey fk;
			while (rs.next()) {
				fk = new ForeignKey();
				fk.setFkColumnName(rs.getString("FKCOLUMN_NAME"));
				fk.setFkTableCatalog(rs.getString("FKTABLE_CAT"));
				fk.setFkTableName(rs.getString("FKTABLE_NAME"));
				fk.setFkTableSchema(rs.getString("FKTABLE_SCHEM"));
				fk.setPkColumnName(rs.getString("PKCOLUMN_NAME"));
				fk.setPkTableCatalog(rs.getString("PKTABLE_CAT"));
				fk.setPkTableName(rs.getString("PKTABLE_NAME"));
				fk.setPkTableSchema(rs.getString("PKTABLE_SCHEM"));
				this.foreignKeys.put(fk.getFkColumnName(), fk);
			}

		}catch (NullPointerException e) {
			/**
			 * tableName is null
			 */
			throw new NotUniqueTableInQueryException("There are at least one table frow where come column in query");
		}

	}

	private class SingleSpreadSheet {

		private ObservableList<Row> rows = FXCollections.observableArrayList(new ArrayList<Row>());
		private LinkedHashMap<String, Row> LinkedHashMap = new LinkedHashMap<>();
		// private LinkedList<StringProperty>[] columns;
		private ResultSet resultSet;
		private int numberOfColumns;

		private SingleSpreadSheet(ResultSet resultSet) throws SQLException {
			// TODO Auto-generated constructor stub
			if (resultSet == null)
				throw new IllegalArgumentException();
			this.resultSet = resultSet;
			this.numberOfColumns = resultSet.getMetaData().getColumnCount();
			/**
			 * fill row in spreadSheet
			 */
			Row tempRow;
			StringProperty tempString;
			while (this.resultSet.next()) {
				tempRow = new Row(this.numberOfColumns);
				for (int t = 0; t < this.numberOfColumns; t++) {
					tempString = new SimpleStringProperty(this.resultSet.getString(t + 1));
					tempRow.setValue(t, tempString);

				}
				this.rows.add(tempRow);
				/**
				 * add column in LinkedHashMap
				 */
				this.LinkedHashMap.put(tempRow.getStringValue(indexColumnName.getValueByKey("code")), tempRow);
			}
		}
	}

	public void loadEndOfRubric(String catalog, String tableName, String schema, List<String> columns, int limit, int offset, List<LinkedHashMap<String, Rule>> rules) {

	}
	public String getTableName() {
		return this.tableName;
	}

	@Override
	public int indexOfColumnName(String columnName) {
		return this.indexColumnName.getValueByKey(columnName);
	}

	@Override
	public String ColumnNameOfIndex(int index) {
		return this.indexColumnName.getKeyByValue(index);
	}

	@Override
	public void applyOnColumn(String columnName, String functionName) {
		// TODO Auto-generated method stub
		throw new NotImplementedException();
	}

	@Override
	public void hideColumn(String columnName) {
		// TODO Auto-generated method stub
		throw new NotImplementedException();
	}

	@Override
	public void hideRow(BiPredicate<String, Rule> predicate, String colunName) {
		// TODO Auto-generated method stub
		throw new NotImplementedException();
	}

	@Override
	public void hideRow(LinkedHashMap<String, BiPredicate<String, Rule>> setOfPredicate) {
		// TODO Auto-generated method stub
		throw new NotImplementedException();
	}

	@Override
	public Row selectRow(int indexOfRow) {
		// TODO Auto-generated method stub
		if (indexOfRow < 0 || indexOfRow > this.numberOfRow())
			throw new IllegalArgumentException();
		return this.singleSpreadSheet.rows.get(indexOfRow);
	}

	@Override
	public ObservableList<String> selectColumn(int indexOfColumn) {
		// TODO Auto-generated method stub
		if (indexOfColumn < 0 || indexOfColumn >= this.numberOfColumn())
			throw new IllegalArgumentException();
		/**
		 * return this.singleSpreadSheet.columns[indexOfColumn];
		 */
		throw new NotImplementedException();
	}

	@Override
	public ObservableList<String> selectColumn(String columnName) {
		// TODO Auto-generated method stub
		if (columnName == null || !this.indexColumnName.containsKey(columnName))
			throw new IllegalArgumentException();
		/**
		 * return
		 * this.singleSpreadSheet.columns[this.indexColumnName.getValueByKey(columnName)];
		 */
		throw new NotImplementedException();
	}

	@Override
	public ObservableList<Row> searchRowByColumnValue(String columnName, String value) {
		// TODO Auto-generated method stub
		throw new NotImplementedException();
		// return null;
	}

	@Override
	public ObservableList<Row> searchRowByColumnMatch(String columnName, BiPredicate<String, Rule> matchFunction) {
		// TODO Auto-generated method stub
		throw new NotImplementedException();
		// return null;
	}

	@Override
	public ObservableList<Row> searchRowContainsValue(String value, LinkedHashMap<String, String> renderInformation) {
		// TODO Auto-generated method stub
		throw new NotImplementedException();
		// return null;
	}

	@Override
	public ObservableList<Row> getRows() {
		// TODO Auto-generated method stub
		return this.singleSpreadSheet.rows;
	}

	@Override
	public Collection<ColumnMetaData> getColumnsMetaData() {
		// TODO Auto-generated method stub
		return this.columnMetaData.values();
	}

	@Override
	public String getString(String columnName, int indexRow) {
		// TODO Auto-generated method stub
		throw new NotImplementedException();
	}

	@Override
	public double getDouble(String columnName, int indexRow) {
		// TODO Auto-generated method stub
		throw new NotImplementedException();
	}

	@Override
	public float getFloat(String columnName, int indexRow) {
		// TODO Auto-generated method stub
		throw new NotImplementedException();
	}

	@Override
	public int getInt(String columnName, int indexRow) {
		// TODO Auto-generated method stub
		throw new NotImplementedException();
	}

	@Override
	public byte getByte(String columnName, int indexRow) {
		// TODO Auto-generated method stub
		throw new NotImplementedException();
	}

	@Override
	public void setValue(String columnName, int indexRow) {
		// TODO Auto-generated method stub
		throw new NotImplementedException();
	}

	@Override
	public int numberOfRow() {
		// TODO Auto-generated method stub
		return this.singleSpreadSheet.rows.size();
	}

	@Override
	public int numberOfColumn() {
		// TODO Auto-generated method stub
		return this.singleSpreadSheet.numberOfColumns;
	}

	/**
	 *
	 */
	/**
	 * implementation of observableList interface
	 */
	@Override
	public int size() {
		// TODO Auto-generated method stub
		return this.numberOfRow();
	}

	@Override
	public boolean isEmpty() {
		// TODO Auto-generated method stub
		return this.singleSpreadSheet.rows.isEmpty();
	}

	@Override
	public boolean contains(Object o) {
		// TODO Auto-generated method stub
		return this.singleSpreadSheet.rows.contains(o);
	}

	@Override
	public Iterator<Row> iterator() {
		// TODO Auto-generated method stub
		return this.singleSpreadSheet.rows.iterator();
	}

	@Override
	public Object[] toArray() {
		// TODO Auto-generated method stub
		return this.singleSpreadSheet.rows.toArray();
	}

	@Override
	public <T> T[] toArray(T[] a) {
		// TODO Auto-generated method stub
		return this.singleSpreadSheet.rows.toArray(a);
	}

	@Override
	public boolean add(Row e) {
		// TODO Auto-generated method stub
		return this.singleSpreadSheet.rows.add(e);
	}

	@Override
	public boolean remove(Object o) {
		// TODO Auto-generated method stub
		return this.singleSpreadSheet.rows.remove(o);
	}

	@Override
	public boolean containsAll(Collection<?> c) {
		// TODO Auto-generated method stub
		return this.singleSpreadSheet.rows.containsAll(c);
	}

	@Override
	public boolean addAll(Collection<? extends Row> c) {
		// TODO Auto-generated method stub
		return this.singleSpreadSheet.rows.addAll(c);
	}

	@Override
	public boolean addAll(int index, Collection<? extends Row> c) {
		// TODO Auto-generated method stub
		return this.singleSpreadSheet.rows.addAll(c);
	}

	@Override
	public boolean removeAll(Collection<?> c) {
		// TODO Auto-generated method stub
		return this.singleSpreadSheet.rows.removeAll(c);
	}

	@Override
	public boolean retainAll(Collection<?> c) {
		// TODO Auto-generated method stub
		return this.singleSpreadSheet.rows.retainAll(c);
	}

	@Override
	public void clear() {
		// TODO Auto-generated method stub
		this.singleSpreadSheet.rows.clear();
	}

	@Override
	public Row get(int index) {
		// TODO Auto-generated method stub
		return this.singleSpreadSheet.rows.get(index);
	}

	@Override
	public Row set(int index, Row element) {
		// TODO Auto-generated method stub
		return this.singleSpreadSheet.rows.set(index, element);
	}

	@Override
	public void add(int index, Row element) {
		// TODO Auto-generated method stub
		this.singleSpreadSheet.rows.add(index, element);
	}

	@Override
	public Row remove(int index) {
		// TODO Auto-generated method stub
		return this.singleSpreadSheet.rows.remove(index);
	}

	@Override
	public int indexOf(Object o) {
		// TODO Auto-generated method stub
		return this.singleSpreadSheet.rows.indexOf(o);
	}

	@Override
	public int lastIndexOf(Object o) {
		// TODO Auto-generated method stub
		return this.singleSpreadSheet.rows.lastIndexOf(o);
	}

	@Override
	public ListIterator<Row> listIterator() {
		// TODO Auto-generated method stub
		return this.singleSpreadSheet.rows.listIterator();
	}

	@Override
	public ListIterator<Row> listIterator(int index) {
		// TODO Auto-generated method stub
		return this.singleSpreadSheet.rows.listIterator(index);
	}

	@Override
	public List<Row> subList(int fromIndex, int toIndex) {
		// TODO Auto-generated method stub
		return this.singleSpreadSheet.rows.subList(fromIndex, toIndex);
	}

	@Override
	public void addListener(InvalidationListener listener) {
		// TODO Auto-generated method stub
		this.singleSpreadSheet.rows.addListener(listener);
	}

	@Override
	public void removeListener(InvalidationListener listener) {
		// TODO Auto-generated method stub
		this.singleSpreadSheet.rows.removeListener(listener);
	}

	@Override
	public void addListener(ListChangeListener<? super Row> listener) {
		// TODO Auto-generated method stub
		this.singleSpreadSheet.rows.addListener(listener);
	}

	@Override
	public void removeListener(ListChangeListener<? super Row> listener) {
		// TODO Auto-generated method stub
		this.singleSpreadSheet.rows.removeListener(listener);
	}

	@Override
	public boolean addAll(Row... elements) {
		// TODO Auto-generated method stub
		return this.singleSpreadSheet.rows.addAll(elements);
	}

	@Override
	public boolean setAll(Row... elements) {
		// TODO Auto-generated method stub
		return this.singleSpreadSheet.rows.setAll(elements);
	}

	@Override
	public boolean setAll(Collection<? extends Row> col) {
		// TODO Auto-generated method stub
		return this.singleSpreadSheet.rows.setAll(col);
	}

	@Override
	public boolean removeAll(Row... elements) {
		// TODO Auto-generated method stub
		return this.singleSpreadSheet.rows.removeAll(elements);
	}

	@Override
	public boolean retainAll(Row... elements) {
		// TODO Auto-generated method stub
		return this.singleSpreadSheet.rows.retainAll(elements);
	}

	@Override
	public void remove(int from, int to) {
		// TODO Auto-generated method stub
		this.singleSpreadSheet.rows.remove(from, to);
	}

	/**
	 * Creates a {@link FilteredList} wrapper of this list using
	 * the specified predicate.
	 *
	 * @param predicate the predicate to use
	 * @return new {@code FilteredList}
	 * @since JavaFX 8.0
	 */
	@Override
	public FilteredList<Row> filtered(Predicate<Row> predicate) {
		return this.singleSpreadSheet.rows.filtered(predicate);
	}

	/**
	 * Creates a {@link SortedList} wrapper of this list using
	 * the specified comparator.
	 *
	 * @param comparator the comparator to use or null for unordered List
	 * @return new {@code SortedList}
	 * @since JavaFX 8.0
	 */
	@Override
	public SortedList<Row> sorted(Comparator<Row> comparator) {
		return this.singleSpreadSheet.rows.sorted(comparator);
	}

	/**
	 * Creates a {@link SortedList} wrapper of this list with the natural
	 * ordering.
	 *
	 * @return new {@code SortedList}
	 * @since JavaFX 8.0
	 */
	@Override
	public SortedList<Row> sorted() {
		return this.singleSpreadSheet.rows.sorted();
	}

	@Override
	public String toString() {
		StringBuilder result = new StringBuilder();
		for (int t = 1; t <= this.singleSpreadSheet.numberOfColumns; t++)
			result.append(this.indexColumnName.getKeyByValue(t) + "\t");
		result.append("\n");
		for (Row r : this.singleSpreadSheet.rows)
			result.append(r.toString());
		return result.toString();
	}

	public BidirectionalLinkedHashMap<String, Integer> getIndexColumnName() {
		return indexColumnName;
	}

	public LinkedHashMap<String, ForeignKey> getForeignKeys() {
		return this.foreignKeys;
	}

	public LinkedHashMap<String, ColumnMetaData> getColumnMetaData() {
		return this.columnMetaData;
	}


	@Override
	public void insertRow(Row row) throws SQLException, CodeOutOfBoundsException, ImpossibleToInsertException, CloneNotSupportedException {
		// TODO Auto-generated method stub
		if (row == null)
			throw new IllegalArgumentException();
		/**
		 * insertion and validation in Database
		 */
		this.insertIntoDataBase(row);

		/**
		 * perform some operation to ensure consistence between GUI and database
		 */
		this.singleSpreadSheet.rows.add(row);
		this.singleSpreadSheet.LinkedHashMap.put(row.getStringValue(this.getIndexColumnName().getValueByKey("code")), row);
	}

	@Override
	public void deleteRow(Row row) throws SQLException {
		if (row == null)
			throw new IllegalArgumentException();
		/**
		 * insertion and validation in Database
		 */
		this.deleteFromDatabase(row);

		/**
		 * perform some operation to ensure consistence between GUI and database
		 */
		this.singleSpreadSheet.rows.remove(row);
		this.singleSpreadSheet.LinkedHashMap.remove(row.getStringValue(this.getIndexColumnName().getValueByKey("code")));
	}

	@Override
	public void updateRow(Row row) throws SQLException {
		if (row == null)
			throw new IllegalArgumentException();
		/**
		 * insertion and validation in Database
		 */
		this.updateOnDataBase(row);

		/**
		 * perform some operation to ensure consistence between GUI and database
		 */

	}

	public LinkedHashMap<String, Relation> getRelations() {
		return relations;
	}

	public void setRelations(LinkedHashMap<String, Relation> relations) {
		this.relations = relations;
	}

	@Override
	public ObservableList<Row> getDataForEditor(String targetColumn) {
		if (targetColumn == null || this.indexColumnName.getValueByKey(targetColumn) == null) throw new IllegalArgumentException();
		int index = this.indexColumnName.getValueByKey(targetColumn);
		int indexcode = this.indexColumnName.getValueByKey("code");
		ObservableList<Row> result = FXCollections.observableArrayList();
		Row r;
		for (Row row : this.singleSpreadSheet.rows) {
			r = new Row(2);
			r.setValue(0, row.getStringValue(indexcode));
			r.setValue(1, row.getStringValue(index));
			result.add(r);
		}
		return result;
	}

	public Row getRowByCode(String code) {
		if (code == null) throw new IllegalArgumentException();
		return this.singleSpreadSheet.LinkedHashMap.get(code);
	}

	public List<LinkedHashMap<String, Rule>> getRules() {
		return rules;
	}

	public void setRules(List<LinkedHashMap<String, Rule>> rules) {
		this.rules = rules;
	}
}
