package dataBaseManager;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class QueryExecutor implements IQueryExecutor {

    private Connection con;
    private Statement statement;

    /**
     * goals : default constructor avoid instantiation of class outside of package's definition
     * @param con
     * @throws SQLException
     */
    QueryExecutor(Connection con) throws SQLException {
        if (con == null) throw new IllegalArgumentException("Connection is null");
        /**
         * check whether connection is open before assignation
         */

        /**
         * assignation of connection
         */
        this.con = con;
        this.statement = con.createStatement();
    }

    /**
     * DML operation
     */
    /**
     *
     * @param query
     * @return
     * @throws SQLException
     */
    @Override
    public void insert(String query) throws SQLException {
        if (query == null) throw new IllegalArgumentException();
        /**
         * check whether connection is open before execution of query
         */
        try {
            /**
             * start transaction
             */
            this.startTransaction(this.con);
            this.statement.execute(query);
            /**
             * end transaction
             */
            this.commitTransaction(this.con);
        } catch (SQLException ex) {
            this.rollbackTransaction(this.con);
            throw ex;
        }
    }

    /**
     *
     * @param query
     * @return
     * @throws SQLException
     */
    @Override
    public ResultSet read(String query) throws SQLException {
        if (query == null) throw new IllegalArgumentException();
        /**
         * check whether connection is open before execution of query
         */
        return this.statement.executeQuery(query);
    }

    /**
     *
     * @param query
     * @throws SQLException
     */
    @Override
    public void update(String query) throws SQLException {
        if (query == null) throw new IllegalArgumentException();
        /**
         * check whether connection is open before execution of query
         */
        try {
            /**
             * start transaction
             */
            this.startTransaction(this.con);
            this.statement.execute(query);
            /**
             * end transaction
             */
            this.commitTransaction(this.con);
        } catch (SQLException ex) {
            this.rollbackTransaction(this.con);
            throw ex;
        }

    }

    /**
     *
     * @param query
     * @throws SQLException
     */
    @Override
    public void delete(String query) throws SQLException {
        if (query == null) throw new IllegalArgumentException();
        /**
         * check whether connection is open before execution of query
         */
        this.statement.execute(query);
    }

    @Override
    public void startTransaction(Connection connection) throws SQLException {
        connection.setAutoCommit(false);
    }

    @Override
    public void rollbackTransaction(Connection connection) throws SQLException {
        connection.rollback();
    }

    @Override
    public void commitTransaction(Connection connection) throws SQLException {
        connection.commit();
    }
}
