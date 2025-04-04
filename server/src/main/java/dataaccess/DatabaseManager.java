package dataaccess;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

public class DatabaseManager {
    private static final String DATABASE_NAME;
    private static final String USER;
    private static final String PASSWORD;
    private static final String CONNECTION_URL;

    /*
     * Load the database information for the db.properties file.
     */
    static {
        try {
            try (var propStream = Thread.currentThread().getContextClassLoader().getResourceAsStream("db.properties")) {
                if (propStream == null) {
                    throw new Exception("Unable to load db.properties");
                }
                Properties props = new Properties();
                props.load(propStream);
                DATABASE_NAME = props.getProperty("db.name");
                USER = props.getProperty("db.user");
                PASSWORD = props.getProperty("db.password");

                var host = props.getProperty("db.host");
                var port = Integer.parseInt(props.getProperty("db.port"));
                CONNECTION_URL = String.format("jdbc:mysql://%s:%d", host, port);
            }
        } catch (Exception ex) {
            throw new RuntimeException("unable to process db.properties. " + ex.getMessage());
        }
    }

    /**
     * Creates the database if it does not already exist.
     */
    static void createDatabase() throws DataAccessException {
        try {
            var statement = "CREATE DATABASE IF NOT EXISTS " + DATABASE_NAME;
            var conn = DriverManager.getConnection(CONNECTION_URL, USER, PASSWORD);
            try (var preparedStatement = conn.prepareStatement(statement)) {
                preparedStatement.executeUpdate();
            }
        } catch (SQLException e) {
            throw new DataAccessException(e.getMessage());
        }
    }

    public static void dropDatabase() throws DataAccessException {
        try {
            var statement = "DROP DATABASE IF EXISTS " + DATABASE_NAME;
            var conn = DriverManager.getConnection(CONNECTION_URL, USER, PASSWORD);
            try (var prepareStatement = conn.prepareStatement(statement)) {
                prepareStatement.execute();
            }
        } catch (SQLException e) {
            throw new DataAccessException(e.getMessage());
        }
    }

    public static void init() throws DataAccessException {
        createDatabase();
        if (DatabaseConnectionPool.getInstance() == null) {
            DatabaseConnectionPool.builder()
                    .setDBName(DATABASE_NAME)
                    .setUsername(USER)
                    .setPassword(PASSWORD)
                    .setUrl(CONNECTION_URL)
                    .setMaxSize(5)
                    .build();
        }
        createTables();
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

    /**
     * Create a connection to the database and sets the catalog based upon the
     * properties specified in db.properties. Connections to the database should
     * be short-lived, and you must close the connection when you are done with it.
     * The easiest way to do that is with a try-with-resource block.
     * <br/>
     * <code>
     * try (var conn = DbInfo.getConnection(databaseName)) {
     * // execute SQL statements.
     * }
     * </code>
     */
    static Connection getConnection() throws DataAccessException {
        return DatabaseConnectionPool.getInstance().leaseConnection();
    }
}
