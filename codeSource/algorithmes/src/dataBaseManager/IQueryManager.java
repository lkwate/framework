package dataBaseManager;

import dataDefinition.BidirectionalLinkedHashMap;
import dataDefinition.Row;
import dataDefinition.Rule;

import java.sql.Connection;
import java.sql.ResultSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Set;

public interface IQueryManager {

    /**
     * goals : retrieve data from table who belongs to a schema in a catalog
     * @param catalogName
     * @param schema
     * @param tableName
     * @param numberOfRecords
     * @param columnDescription
     * @return
     * @throws Throwable
     */
    public ResultSet readOnTable(Connection con, String catalogName, String schema, String tableName, int numberOfRecords, String columnDescription, String whereClause, List<String> columns, int limit, int offset, List<LinkedHashMap<String, Rule>> columnsRules) throws Throwable;

    /**
     * goals : recording of a given row in a table
     * @param catalogName
     * @param schema
     * @param tableName
     * @param row
     * @param indexColumnName
     * @throws Throwable
     */
    public void insertIntoTable(Connection con, String catalogName, String schema, String tableName, Row row, BidirectionalLinkedHashMap<String, Integer> indexColumnName) throws Throwable;

    /**
     * goals : update a given recording in a given table
     * @param catalogName
     * @param schema
     * @param tableName
     * @param row
     * @param keySet
     * @param indexColumnName
     * @throws Throwable
     */
    public void updateOnTable(Connection con, String catalogName, String schema, String tableName, Row row, List<String> keySet, BidirectionalLinkedHashMap<String, Integer> indexColumnName) throws Throwable;

    /**
     * goals : delete recording from table according where clause
     * @param catalogName
     * @param schema
     * @param tableName
     * @param columnRule
     * @throws Throwable
     */
    public void deleteFromTable(Connection con, String catalogName, String schema, String tableName, List<LinkedHashMap<String, Rule>> columnRule) throws Throwable;
}
