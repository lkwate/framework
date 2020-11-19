package dataBaseManager;

import dataDefinition.Rule;
import org.jooq.*;
import org.jooq.conf.RenderNameStyle;
import org.jooq.conf.Settings;
import org.jooq.conf.StatementType;
import org.jooq.impl.DSL;
import org.jooq.util.xml.jaxb.Column;

import java.sql.Connection;
import java.util.LinkedHashMap;
import java.util.List;

import static org.jooq.impl.DSL.*;

public class QueryBuilder implements IQueryBuilder {

    private static Settings settings = new Settings();
    static  {
        QueryBuilder.settings.setStatementType(StatementType.STATIC_STATEMENT);
        QueryBuilder.settings.withRenderNameStyle(RenderNameStyle.AS_IS);
    }

    /**
     * goals : avoid instantiation of class outside of package definition
     */
    QueryBuilder () {

    }
    /**
     *
     * @param con : connection supplied for configurations settings
     * @param catalogName : name of database
     * @param schema : name of schema which hold table
     * @param tableName : name of table from where query will be executed
     * @param numberOfRecords
     * @param columnDescriptor
     * @param whereClause
     * @param columns
     * @param columnsRules
     * @return
     */
    @Override
    public String readOnTable(Connection con, String catalogName, String schema, String tableName, int numberOfRecords, String columnDescriptor, String whereClause, List<String> columns, int limit, int offset, List<LinkedHashMap<String, Rule>> columnsRules) {
        if (con == null || tableName == null) throw new IllegalArgumentException();
        /**
         * configuration of parameters
         */
        DSLContext create = DSL.using(con, SQLDialect.MYSQL, QueryBuilder.settings);
        /**
         * building query
         */
        /**
         *  add catalog name to settings if it's not null
         */
        if (catalogName != null) {

        }
        /**
         *  add schema to the settings if it's not null
         */
        if (schema != null) {
            
        }
        if (columns == null || columns.size() == 0)
        {
            SelectJoinStep<Record> result = create.select().from(table(tableName));
            if (whereClause != null)
                result.where(condition(whereClause));
            else if (columnsRules != null){
                /**
                 * implementation of rule in query with a clausal form of boolean expression
                 */
               result.where(QueryBuilder.buildBooleanCondition(columnsRules));
            }
            if (limit > 0)
                result.limit(limit);
            if (offset > 0)
                result.offset(offset);
            return result.getSQL();
        }
        else {
            SelectField<String>[] fields = new SelectField[columns.size()];
            int i = 0;
            for (String s : columns)
                fields[i] = field(s, String.class);
            SelectJoinStep<Record> result = create.select(fields).from(table(tableName));
            if (whereClause != null)
                result.where(condition(whereClause));
            else if (columnsRules != null){
                /**
                 * implementation of rule in query
                 */
                result.where(QueryBuilder.buildBooleanCondition(columnsRules));
            }
            if (limit > 0)
                result.limit(limit);
            if (offset > 0)
                result.offset(offset);
            return result.getSQL();
        }
    }

    /**
     *
     * @param con
     * @param catalogName
     * @param schema
     * @param tableName
     * @param columnValues
     * @param columnRule
     * @return
     */
    @Override
    public String updateOnTable(Connection con, String catalogName, String schema, String tableName, LinkedHashMap<String, String> columnValues, List<LinkedHashMap<String, Rule>> columnRule) {
        if (con == null || tableName == null || columnValues == null || columnRule == null) throw new IllegalArgumentException();
        /**
         * configuration of parameters
         */
        DSLContext create = DSL.using(con, SQLDialect.MYSQL, QueryBuilder.settings);
        /**
         * building query
         */
        UpdateSetFirstStep<Record> resultFirstStep = create.update(table(tableName));
        UpdateFromStep<Record> resultSecondStep = null;
        for (String key : columnValues.keySet())
            resultSecondStep = resultFirstStep.set(field(key), columnValues.get(key));

        resultSecondStep.where(QueryBuilder.buildBooleanCondition(columnRule));
        return resultSecondStep.getSQL();
    }

    /**
     *
     * @param con
     * @param catalogName
     * @param schema
     * @param tableName
     * @param columnValues
     * @return
     */
    @Override
    public String insertIntoTable(Connection con, String catalogName, String schema, String tableName, LinkedHashMap<String, String> columnValues) {
        if (con == null || tableName == null || columnValues == null) throw new IllegalArgumentException();
        /**
         * configuration of parameters
         */
        DSLContext create = DSL.using(con, SQLDialect.MYSQL, QueryBuilder.settings);
        /**
         * building query
         */
        InsertSetStep<Record> resultInsertInto = create.insertInto(table(tableName));
        InsertSetMoreStep<Record> r = null;
        for (String s : columnValues.keySet())
        {
            r = resultInsertInto.set(field(s, String.class), columnValues.get(s));
        }
        return r.getSQL();
    }

    /**
     * @param con
     * @param catalogName
     * @param schema
     * @param tableName
     * @param whereClause
     * @return
     */
    @Override
    public String deleteFromTable(Connection con, String catalogName, String schema, String tableName, String whereClause) {
        if (con == null || tableName == null || whereClause == null) throw new IllegalArgumentException();
        /**
         * configuration of parameters
         */
        DSLContext create = DSL.using(con, SQLDialect.MYSQL, QueryBuilder.settings);
        /**
         * building query
         */
        String result = create.deleteFrom(table(tableName)).where(condition(whereClause)).getSQL();
        return result;
    }

    /**
     *
     * @param con
     * @param catalogName
     * @param schema
     * @param tableName
     * @param keyValue
     * @return
     */
    @Override
    public String deleteFromTable(Connection con, String catalogName, String schema, String tableName, LinkedHashMap<String, String> keyValue) {
        if (con == null || tableName == null || keyValue == null) throw new IllegalArgumentException();
        /**
         * configuration of parameters
         */
        DSLContext create = DSL.using(con, SQLDialect.MYSQL, QueryBuilder.settings);
        /**
         * building query
         */
        DeleteWhereStep<Record> result = create.deleteFrom(table(tableName));
        for (String key : keyValue.keySet()) {
            result.where(field(key).eq(keyValue.get(key)));
        }
        return result.getSQL();
    }

    /**
     *
     * @param con
     * @param catalogName
     * @param schema
     * @param tableName
     * @param columnRule
     * @return
     */
    @Override
    public String deleteFromTableWithRule(Connection con, String catalogName, String schema, String tableName, List<LinkedHashMap<String, Rule>> columnRule) {
        if (con == null || tableName == null || columnRule == null) throw new IllegalArgumentException();
        /**
         * configuration of parameters
         */
        DSLContext create = DSL.using(con, SQLDialect.MYSQL, QueryBuilder.settings);
        /**
         * building query
         */
        DeleteWhereStep<Record> result = create.deleteFrom(table(tableName));
        result.where(QueryBuilder.buildBooleanCondition(columnRule));
        return result.getSQL();
    }

    private static Condition buildBooleanCondition(List<LinkedHashMap<String, Rule>> rules) {
        Condition condition = null;
        Condition conditionAux;
        /**
         * iteration over clauses of clausal form of
         */
        for (LinkedHashMap<String, Rule> hashMap : rules) {
            conditionAux = null;
            for (String s : hashMap.keySet()) {
                if (conditionAux == null) {
                    conditionAux = QueryBuilder.singleCondition(s, hashMap.get(s).getOperator(), hashMap.get(s).getValue());
                }
                else {
                    conditionAux = conditionAux.or(QueryBuilder.singleCondition(s, hashMap.get(s).getOperator(), hashMap.get(s).getValue()));
                }
            }
            if (condition == null)
                condition = conditionAux;
            else
                condition = condition.and(conditionAux);
        }
        return condition;
    }

    /**
     * goal : function use to make binding value in where clause
     * @param column
     * @param operator
     * @param value
     * @return
     */
    private static Condition singleCondition(String column, String operator, String value) {
        if (column == null || operator == null || value == null) throw new IllegalArgumentException();
        Field<String> field;
        field = field(column, String.class);
        switch (operator) {
            case Rule.EQUAL :
                return field.equal(param(QueryBuilder.buildParamName(column), value));
            case Rule.GREATER :
                return field.greaterThan(param(QueryBuilder.buildParamName(column), value));
            case Rule.GREATEROREQUAL:
                return field.greaterOrEqual(param(QueryBuilder.buildParamName(column), value));
            case Rule.LESS :
                return field.lessThan(param(QueryBuilder.buildParamName(column), value));
            case Rule.LESSOREQUAL:
                return field.lessOrEqual(param(QueryBuilder.buildParamName(column), value));
            case Rule.LIKE:
                return field.like(param(QueryBuilder.buildParamName(column), value));
            case Rule.NOTEQUAL:
                return field.notEqual(param(QueryBuilder.buildParamName(column), value));
        }
        return null;
    }

    private static String buildParamName(String name) {
        return "p"+name;
    }
}
