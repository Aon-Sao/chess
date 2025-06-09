package dataaccess;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

public class DatabaseManager {
    private static String databaseName;
    private static String dbUsername;
    private static String dbPassword;
    private static String connectionUrl;

    /*
     * Load the database information for the db.properties file.
     */
    static {
        loadPropertiesFromResources();
    }

    /**
     * Creates the database if it does not already exist.
     */
    static public void createDatabase() throws DataAccessException {
        var statement = "CREATE DATABASE IF NOT EXISTS " + databaseName;
        try (var conn = DriverManager.getConnection(connectionUrl, dbUsername, dbPassword);
             var preparedStatement = conn.prepareStatement(statement)) {
            preparedStatement.executeUpdate();
        } catch (SQLException ex) {
            throw new DataAccessException("failed to create database", ex);
        }
    }

    static public void dropDatabase() throws DataAccessException {
        var statement = "DROP DATABASE IF EXISTS " + databaseName;
        try (var conn = DriverManager.getConnection(connectionUrl, dbUsername, dbPassword);
             var preparedStatement = conn.prepareStatement(statement)) {
            preparedStatement.executeUpdate();
        } catch (SQLException ex) {
            throw new DataAccessException("failed to drop database", ex);
        }
    }

    private static void createTables() throws DataAccessException {
        var createStatements = new ArrayList<String>(List.of(
                "CREATE TABLE IF NOT EXISTS AuthData ( " +
                        "`id` int NOT NULL AUTO_INCREMENT, " +
                        "`authToken` varchar(256) NOT NULL, " +
                        "`username` varchar(256) NOT NULL, " +
                        "PRIMARY KEY (`id`) )",
                "CREATE TABLE IF NOT EXISTS UserData ( " +
                        "`id` int NOT NULL AUTO_INCREMENT, " +
                        "`username` varchar(256) NOT NULL, " +
                        "`password` varchar(256) NOT NULL, " +
                        "`email` varchar(256) NOT NULL, " +
                        "PRIMARY KEY (`id`) )",
                "CREATE TABLE IF NOT EXISTS GameData ( " +
                        "`id` int NOT NULL AUTO_INCREMENT, " +
                        "`gameID` int NOT NULL, " +
                        "`whiteUsername` varchar(256) NOT NULL, " +
                        "`blackUsername` varchar(256) NOT NULL, " +
                        "`gameName` varchar(256) NOT NULL, " +
                        "`game` text NOT NULL, " +
                        "PRIMARY KEY (`id`) )"
        ));

        try(var conn = DatabaseManager.getConnection()) {
            for (var statement : createStatements) {
                try (var preparedStatement = conn.prepareStatement(statement)) {
                    preparedStatement.execute();
                }
            }
        } catch (SQLException e) {
            throw new DataAccessException(e.getMessage());
        }
    }

    public static void init() throws DataAccessException {
        createDatabase();
        createTables();
    }


    /**
     * Create a connection to the database and sets the catalog based upon the
     * properties specified in db.properties. Connections to the database should
     * be short-lived, and you must close the connection when you are done with it.
     * The easiest way to do that is with a try-with-resource block.
     * <br/>
     * <code>
     * try (var conn = DatabaseManager.getConnection()) {
     * // execute SQL statements.
     * }
     * </code>
     */
    static Connection getConnection() throws DataAccessException {
        try {
            //do not wrap the following line with a try-with-resources
            var conn = DriverManager.getConnection(connectionUrl, dbUsername, dbPassword);
            conn.setCatalog(databaseName);
            return conn;
        } catch (SQLException ex) {
            throw new DataAccessException("failed to get connection", ex);
        }
    }

    private static void loadPropertiesFromResources() {
        try (var propStream = Thread.currentThread().getContextClassLoader().getResourceAsStream("db.properties")) {
            if (propStream == null) {
                throw new Exception("Unable to load db.properties");
            }
            Properties props = new Properties();
            props.load(propStream);
            loadProperties(props);
        } catch (Exception ex) {
            throw new RuntimeException("unable to process db.properties", ex);
        }
    }

    private static void loadProperties(Properties props) {
        databaseName = props.getProperty("db.name");
        dbUsername = props.getProperty("db.user");
        dbPassword = props.getProperty("db.password");

        var host = props.getProperty("db.host");
        var port = Integer.parseInt(props.getProperty("db.port"));
        connectionUrl = String.format("jdbc:mysql://%s:%d", host, port);
    }
}
