package dataDefinition;

import exceptions.ImplicantException;
import exceptions.NotUniqueTableInQueryException;
import system.SystemAdministrator;

import java.sql.SQLException;
import java.util.LinkedHashMap;

public class FieldImplicant {

    /**
     * key words of FieldImplicant
     */
    public static final String ROW = "row";
    public static final String BIDIRECTIONALMAP = "bidirectionalmap";
    /**
     * foreign key witch contains
     */
    private String valueColumnSource = new String();
    private LinkedHashMap<String, String> mappingColumn = new LinkedHashMap<>();

    public void addMapping(String columnSource, String columnTarget) {
        if (columnSource == null || columnTarget == null) throw new IllegalArgumentException();
        this.mappingColumn.put(columnSource, columnTarget);
    }

    public String getValueColumnSource() {
        return valueColumnSource;
    }

    public void setValueColumnSource(String valueColumnSource) {
        this.valueColumnSource = valueColumnSource;
    }

    public LinkedHashMap<String, String> getMappingColumn() {
        return mappingColumn;
    }

    /**
     * goal : process on foreignKey and implicant to output the corresponding Spreasheet and row.
     * @param foreignKey
     * @param fieldImplicant
     * @return
     * @throws SQLException
     * @throws NotUniqueTableInQueryException
     * @throws ImplicantException
     */
    public static LinkedHashMap<String, Object> processFieldImplicant(ForeignKey foreignKey, FieldImplicant fieldImplicant) throws SQLException, NotUniqueTableInQueryException, ImplicantException {
        if (foreignKey == null || fieldImplicant == null) throw new IllegalArgumentException();
        LinkedHashMap<String, Object> result = new LinkedHashMap<>();
        result.put(FieldImplicant.ROW, SystemAdministrator.loadRubricNativelyByTableName(foreignKey.getPkTableName()).getRowByCode(fieldImplicant.getValueColumnSource()));
        result.put(FieldImplicant.BIDIRECTIONALMAP, SystemAdministrator.loadRubricNativelyByTableName(foreignKey.getPkTableName()).getIndexColumnName());
        return result;
    }
}
