package dataBaseManager;

import java.io.FileInputStream;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

/**
 * @author kwate
 * this class is the center to perform operation on database
 */
public class DataBaseAdministrator {

    /**
     * native attributes
     */
    private static Connection connection;
    private static Properties properties = new Properties();
    private static String PATHTOCONNECTIONFILE = "config.properties";
    private static String URLPATTERN = "jdbc:mysql://domain:port/dataBaseName?autoReconnect=autoReconnectValue&useSSL=useSSLValue";
    private static String DOMAIN = "domain";
    private static String PORT = "port";
    private static String DATABASENAME = "dataBaseName";
    private static String DRIVERNAME = "driverName";
    private static String USER = "user";
    private static String PASSWORD = "password";
    private static String AUTORECONNECTVALUE = "autoReconnectValue";
    private static String SSLVALUE = "useSSLValue";
    public static String catalog;
    public static String schema;


    /**
     * attributes for querying database
     */
    private static QueyManager queryManager;
    private static QueryBuilder queryBuilder;


    /**
     * static function to manage database
     */
    public static void loadConnection() throws IOException, ClassNotFoundException, SQLException {
        /**
         * Load data from properties file
         */
        FileInputStream fileInputStream = new FileInputStream(DataBaseAdministrator.PATHTOCONNECTIONFILE);
        DataBaseAdministrator.properties.load(fileInputStream);
        String url = new String(DataBaseAdministrator.URLPATTERN);
        url = url.replace(DataBaseAdministrator.DOMAIN, DataBaseAdministrator.properties.getProperty(DataBaseAdministrator.DOMAIN));
        url = url.replace(DataBaseAdministrator.PORT, DataBaseAdministrator.properties.getProperty(DataBaseAdministrator.PORT));
        url = url.replace(DataBaseAdministrator.DATABASENAME, DataBaseAdministrator.properties.getProperty(DataBaseAdministrator.DATABASENAME));
        url = url.replace(DataBaseAdministrator.AUTORECONNECTVALUE, DataBaseAdministrator.properties.getProperty(DataBaseAdministrator.AUTORECONNECTVALUE));
        url = url.replace(DataBaseAdministrator.SSLVALUE, DataBaseAdministrator.properties.getProperty(DataBaseAdministrator.SSLVALUE));
        String driverName = DataBaseAdministrator.properties.getProperty(DataBaseAdministrator.DRIVERNAME);
        Properties infos = new Properties();
        infos.setProperty(DataBaseAdministrator.USER, DataBaseAdministrator.properties.getProperty(DataBaseAdministrator.USER));
        infos.setProperty(DataBaseAdministrator.PASSWORD, DataBaseAdministrator.properties.getProperty(DataBaseAdministrator.PASSWORD));

        /**
         * Load connection
         */
        Class.forName(DataBaseAdministrator.properties.getProperty(DataBaseAdministrator.DRIVERNAME));
        DataBaseAdministrator.connection = DriverManager.getConnection(url, infos);

        /**
         * initialization of attributes need to query database
         */
        DataBaseAdministrator.queryBuilder = new QueryBuilder();
        DataBaseAdministrator.queryManager = new QueyManager(DataBaseAdministrator.connection);
        DataBaseAdministrator.catalog = DataBaseAdministrator.DATABASENAME;

    }

    /**
     * Private constructor to avoid instantiation of this class
     */
    private DataBaseAdministrator() {

    }

    /**
     * static getters
     */
    public static QueryBuilder getQueryBuilder() {
        return DataBaseAdministrator.queryBuilder;
    }

    public static QueyManager getQueryManager() {
        return DataBaseAdministrator.queryManager;
    }

    public static Connection getConnection() {
        return DataBaseAdministrator.connection;
    }
}
