package dataBaseManager;

import dataDefinition.BidirectionalLinkedHashMap;
import dataDefinition.Row;
import dataDefinition.Rule;
import exceptions.CodeOutOfBoundsException;
import exceptions.ImpossibleToInsertException;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;

public class QueyManager implements IQueryManager {

    /**
     * attributes
     */
    private QueryBuilder queryBuilder = new QueryBuilder();
    private Connection con;
    private QueryExecutor queryExecutor;

    /**
     * goal : avoid instantiation of class outside of package definition
     */
     QueyManager(Connection con) throws SQLException {
        /**
         * check whether connection is available
         */

        /**
         * attribute assignation
         */
        this.con = con;
        this.queryExecutor = new QueryExecutor(con);
    }
    /**
     * goals : retrieve data from table who belongs to a schema in a catalog
     *
     * @param catalogName
     * @param schema
     * @param tableName
     * @param numberOfRecords
     * @param columnDescription
     * @return
     * @throws SQLException
     */
    @Override
    public ResultSet readOnTable(Connection con, String catalogName, String schema, String tableName, int numberOfRecords, String columnDescription, String whereClause, List<String> columns, int limit, int offset, List<LinkedHashMap<String, Rule>> columnsRules) throws SQLException {
        /**
         * check if connection is available
         */

        /**
         * first step : building query
         */
        String query = this.queryBuilder.readOnTable(this.con, catalogName, schema, tableName, numberOfRecords, columnDescription, whereClause, columns, limit, offset, columnsRules);
        /**
         * check whether query builded is not null and execute it via queryExcutor object
         */
        ResultSet resultSet = null;
        if (query != null) {
            resultSet = this.queryExecutor.read(query);
        }
        return resultSet;
    }

    /**
     * goals : recording of a given row in a table
     *
     * @param catalogName
     * @param schema
     * @param tableName
     * @param row
     * @param indexColumnName
     * @throws CodeOutOfBoundsException, CloneNotSupportedException, SQLException, ImpossibleToInsertException
     */
    @Override
    public void insertIntoTable(Connection con, String catalogName, String schema, String tableName, Row row, BidirectionalLinkedHashMap<String, Integer> indexColumnName) throws CodeOutOfBoundsException, CloneNotSupportedException, SQLException, ImpossibleToInsertException {
        /**
         * check if connection is available
         */

        /**
         * check illegal arguments
         */
        if (row == null || indexColumnName == null) throw new IllegalArgumentException();
        /**
         * assignation of key value in the row to insert
         */
        LinkedHashMap<String, String> keyValue = KeyGenerator.generateNextKey(con, catalogName, schema, tableName);
        for (String s: keyValue.keySet())
            if (indexColumnName.containsKey(s))
                row.setValue(indexColumnName.getValueByKey(s), keyValue.get(s));
        /**
         * building argument for query building
         */
        keyValue.clear();
        for (int i = 0; i < row.size(); i++)
            keyValue.put(indexColumnName.getKeyByValue(i), row.getStringValue(i));

        /**
         * first step : building query
         */
        String query = this.queryBuilder.insertIntoTable(this.con,catalogName, schema, tableName, keyValue);

        /**
         * execution of query
         */
        this.queryExecutor.insert(query);
    }

    /**
     * goals : update a given recording in a given table
     *
     * @param catalogName
     * @param schema
     * @param tableName
     * @param row
     * @param keySet
     * @param indexColumnName
     * @throws SQLException
     */
    @Override
    public void updateOnTable(Connection con, String catalogName, String schema, String tableName, Row row, List<String> keySet, BidirectionalLinkedHashMap<String, Integer> indexColumnName) throws SQLException {
        /**
         * check if connection is available
         */

        /**
         * check illegal arguments
         */
        if (row == null || indexColumnName == null || keySet == null) throw new IllegalArgumentException();
        /**
         * build argument for query building
         */
        List<LinkedHashMap<String, Rule>> columnRule = new LinkedList<>();
        Rule rule;
        LinkedHashMap<String, Rule> r;
        for (String key : keySet)
        {
            r = new LinkedHashMap<>(1);
            rule = new Rule();
            rule.setOperator(Rule.EQUAL);
            rule.setValue(row.getStringValue(indexColumnName.getValueByKey(key)));
            r.put(key, rule);
            columnRule.add(r);
        }
        LinkedHashMap<String, String> columnValue = new LinkedHashMap<>();
        for (int i = 0; i < row.size(); i++) {
            if (!keySet.contains(indexColumnName.getKeyByValue(i))) {
                columnValue.put(indexColumnName.getKeyByValue(i), row.getStringValue(i));
            }
        }
        /**
         * query building
         */
        String query = this.queryBuilder.updateOnTable(con, catalogName, schema, tableName, columnValue, columnRule);
        /**
         * query execution
         */
        this.queryExecutor.update(query);
    }



    /**
     * goals : delete recording from table according where clause
     *
     * @param catalogName
     * @param schema
     * @param tableName
     * @param columnRule
     * @throws SQLException
     */
    @Override
    public void deleteFromTable(Connection con, String catalogName, String schema, String tableName, List<LinkedHashMap<String, Rule>> columnRule) throws SQLException {
        /**
         * check if connection is available
         */

        /**
         * build argument for query building
         */

        /**
         * query building
         */
        String query = this.queryBuilder.deleteFromTableWithRule(con, catalogName, schema, tableName, columnRule);
        /**
         * query execution
         */

        this.queryExecutor.delete(query);
    }
}
