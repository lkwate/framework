package dataBaseManager;

import dataDefinition.Rule;

import java.sql.Connection;
import java.util.LinkedHashMap;
import java.util.List;

public interface IQueryBuilder {

    String readOnTable(Connection con, String catalogName, String schema, String tableName, int numberOfRecords, String columnDescriptor, String whereClause, List<String> columns, int limit, int offset, List<LinkedHashMap<String, Rule>> columnsRules) throws Throwable;

    String updateOnTable(Connection con, String catalogName, String schema, String tableName, LinkedHashMap<String, String> columnValues, List<LinkedHashMap<String, Rule>> columnRule) throws Throwable;

    String insertIntoTable(Connection con, String catalogName, String schema, String tableName, LinkedHashMap<String, String> columnValues) throws Throwable;

    String deleteFromTable(Connection con, String catalogName, String schema, String tableName, String whereClause) throws Throwable;

    String deleteFromTable(Connection con, String catalogName, String schema, String tableName, LinkedHashMap<String, String> keyValue) throws Throwable;

    String deleteFromTableWithRule(Connection con, String catalogName, String schema, String tableName, List<LinkedHashMap<String, Rule>> columnRule) throws Throwable;
}
