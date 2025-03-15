package dataaccess;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class DatabaseConnectionPool {
    private final String dbName;
    private final String username;
    private final String password;
    private final String url;
    private final int maxSize;
    private ArrayList<Connection> freeConnections;
    private ArrayList<Connection> ownedConnections;

    // Lifecycle logic
    private static DatabaseConnectionPool instance = null;
    public static DatabaseConnectionPool getInstance() {
        return instance;
    }
    private DatabaseConnectionPool(ConnPoolBuilder connPoolBuilder) throws DataAccessException {
        this.dbName = connPoolBuilder.dbName;
        this.username = connPoolBuilder.username;
        this.password = connPoolBuilder.password;
        this.url = connPoolBuilder.url;
        this.maxSize = connPoolBuilder.maxSize;

        ownedConnections = new ArrayList<>();
        freeConnections = new ArrayList<>();
        for (int i = 0; i < maxSize; i++) {
            freeConnections.add(createConnection());
        }
    }
    public static ConnPoolBuilder builder() {
        if (instance == null) {
            return new ConnPoolBuilder();
        } else {
            throw new RuntimeException("DatabaseConnectionPool instance exists, call getInstance");
        }
    }
    public static class ConnPoolBuilder {
        private String dbName;
        private String username;
        private String password;
        private String url;
        private int maxSize;

        private boolean validate() {
            return !List.of(dbName, url, password, url, maxSize).contains(null);
        }

        public DatabaseConnectionPool build() throws DataAccessException {
            if (instance == null) {
                if (validate()) {
                    instance = new DatabaseConnectionPool(this);
                    return instance;
                } else {
                    throw new RuntimeException("ConnPoolBuilder must have all fields set before building");
                }
            } else {
                throw new RuntimeException("DatabaseConnectionPool exists, call getInstance");
            }
        }

        public ConnPoolBuilder setDBName(String dbName) {
            this.dbName = dbName;
            return this;
        }

        public ConnPoolBuilder setUsername(String username) {
            this.username = username;
            return this;
        }

        public ConnPoolBuilder setPassword(String password) {
            this.password = password;
            return this;
        }

        public ConnPoolBuilder setUrl(String url) {
            this.url = url;
            return this;
        }

        public ConnPoolBuilder setMaxSize(int size) {
            this.maxSize = size;
            return this;
        }

    }

    // Functionality
    public Connection leaseConnection() throws DataAccessException {
        try {
            // Look for a valid, free connection
            for (var conn : freeConnections) {
                if (conn.isValid(10)) {
                    ownedConnections.add(conn);
                    return conn;
                } else {
                    // If we find an invalid connection, replace it
                    freeConnections.addLast(createConnection());
                }
                freeConnections.remove(conn);
            }
        } catch (SQLException ignored) {
            // This exception should only be thrown where isValid is passed some
            // int less than zero. I am passing a positive int literal. I think
            // it is reasonable to swallow this exception here.
        }
        throw new RuntimeException("No DB connections free in pool");
    }
    public void releaseConnection(Connection conn) {
        // How do I make sure the caller actually has given up the reference?
        // Perhaps I dispose of the connection and make a new one?
        // But then I would incur the same performance as creating a new
        // connection each time I lease one. This class is only useful if I
        // recycle connections, or refresh them asynchronously
        ownedConnections.remove(conn);
        freeConnections.add(conn);
    }
    private Connection createConnection() throws DataAccessException {
        try (var conn = DriverManager.getConnection(url, username, password)) {
            return conn;
        } catch (SQLException e) {
            throw new DataAccessException(e.getMessage());
        }
    }

    public void shutdown() throws DataAccessException {
        try {
            for (var conn : ownedConnections) {
                releaseConnection(conn);
            }
            for (var conn : freeConnections) {
                conn.close();
            }
        } catch (SQLException e) {
            throw new DataAccessException(e.getMessage());
        }
    }

}
