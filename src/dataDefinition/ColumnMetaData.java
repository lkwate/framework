package dataDefinition;

import java.util.LinkedHashMap;
import java.util.HashSet;
import java.util.Set;

/**
 * goal : description of
 */
public class ColumnMetaData {

	/**
	 * list of sql TYPE
	 */
	public static final String sqlCHAR = "CHAR";
	public static final String sqlVARCHAR = "VARCHAR";
	public static final String sqlDATE = "DATE";
	public static final String sqlTEXT = "TEXT";

	/**
	 * field Name keyword
	 */


	public static final Set<String> systemColumn = new HashSet<>();

	static {
		ColumnMetaData.systemColumn.add("code");
		ColumnMetaData.systemColumn.add("code_unique");
		ColumnMetaData.systemColumn.add("date_activation");
		ColumnMetaData.systemColumn.add("user_modificateur");
		ColumnMetaData.systemColumn.add("user_createur");
		ColumnMetaData.systemColumn.add("date_creation");
		ColumnMetaData.systemColumn.add("date_modification");
		ColumnMetaData.systemColumn.add("heure_creation");
		ColumnMetaData.systemColumn.add("heure_modification");
	}

	/**
	 * attributes
	 */
	private int index;
	private String name = "";
	private String label = "";
	private String typeName = "";
	private int columnDisplaySize;
	private String columnClassName = "";
	private boolean nullable;
	private boolean readonly;
	private boolean writable;

	public ColumnMetaData() {
		// TODO Auto-generated constructor stub
	}

	public ColumnMetaData(int index, String name, String label, String typeName, int columnDisplaySize, String columnClassName) {

		if (name == null || label == null || typeName == null || columnClassName == null)
			throw new IllegalArgumentException();
		this.index = index;
		this.name = name;
		this.label = label;
		this.typeName = typeName;
		this.columnClassName = columnClassName;
		this.columnDisplaySize = columnDisplaySize;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getLabel() {
		return label;
	}

	public void setLabel(String label) {
		this.label = label;
	}

	public String getTypeName() {
		return typeName;
	}

	public void setTypeName(String typeName) {
		this.typeName = typeName;
	}

	public int getColumnDisplaySize() {
		return columnDisplaySize;
	}

	public void setColumnDisplaySize(int columnDisplaySize) {
		this.columnDisplaySize = columnDisplaySize;
	}

	public String getColumnClassName() {
		return columnClassName;
	}

	public void setColumnClassName(String columnClassName) {
		this.columnClassName = columnClassName;
	}

	public boolean isNullable() {
		return nullable;
	}

	public void setNullable(boolean nullable) {
		this.nullable = nullable;
	}

	public boolean isReadonly() {
		return readonly;
	}

	public void setReadonly(boolean readonly) {
		this.readonly = readonly;
	}

	public boolean isWritable() {
		return writable;
	}

	public void setWritable(boolean writable) {
		this.writable = writable;
	}

	public int getIndex() {
		return index;
	}
}
