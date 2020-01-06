package dataBaseManager;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;

public interface IQueryExecutor {

    public void insert(String query) throws Throwable;

    ResultSet read(String query) throws Throwable;

    public void update(String query) throws Throwable;

    public void delete(String query) throws Throwable;

    public void startTransaction(Connection connection) throws SQLException;

    public void rollbackTransaction(Connection connection) throws SQLException;

    public void commitTransaction(Connection connection) throws SQLException;
}
