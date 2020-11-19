package system;

import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;

public class EditorMetaData {

    private String tableName = new String();
    private String columnName = new String();
    private String targetColumn = new String();

    public String getTableName() {
        return tableName;
    }

    public void setTableName(String tableName) {
        this.tableName = tableName;
    }

    public String getColumnName() {
        return columnName;
    }

    public void setColumnName(String columnName) {
        this.columnName = columnName;
    }

    public String getTargetColumn() {
        return targetColumn;
    }

    public void setTargetColumn(String targetColumn) {
        this.targetColumn = targetColumn;
    }
}
