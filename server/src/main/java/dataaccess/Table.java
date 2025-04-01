package dataaccess;

import model.hasFromMap;

import java.lang.reflect.RecordComponent;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;

public class Table {
    private final String name;
    private final ArrayList<String> headers;
    private final String creationString;
    private final hasFromMap record;

    public Table(String name, hasFromMap record, String creationString) {
        this.name = name;
        this.headers = new ArrayList<String>(Arrays.stream(record.getClass().getRecordComponents()).map(RecordComponent::getName).toList());
        this.creationString = creationString;
        this.record = record;
    }

    public boolean createInDatabase() throws DataAccessException {
        Connection conn = null;
        boolean ret;
        try {
            conn = DatabaseConnectionPool.getInstance().leaseConnection();
            // TODO: Horrible security risk
            ret = conn.prepareStatement(this.creationString).execute();
        } catch (SQLException | DataAccessException e) {
            throw new DataAccessException(e.getMessage());
        } finally {
            DatabaseConnectionPool.getInstance().releaseConnection(conn);
        }
        return ret;
    }

    public boolean insert(List<String> data) throws DataAccessException {
        Connection conn = null;
        boolean ret;
        try {
            conn = DatabaseConnectionPool.getInstance().leaseConnection();
            // TODO: String interpolation bad
            var statement = conn.prepareStatement(
                    "INSERT INTO " + name + " (" +
                            String.join(", ", headers) +
                            ") VALUES(" +
                            String.join(", ", headers.stream().map((h) -> "?").toList()) +
                            ")"
            );
            int i = 1;
            for (var d : data) {
                statement.setString(i, d);
                i++;
            }
            ret = statement.execute();
        } catch (SQLException | DataAccessException e) {
            throw new DataAccessException(e.getMessage());
        } finally {
            DatabaseConnectionPool.getInstance().releaseConnection(conn);
        }
        return ret;
    }

    public Collection<Object> list() throws DataAccessException {
        Connection conn = null;
        var ret = new ArrayList<Object>();
        try {
            conn = DatabaseConnectionPool.getInstance().leaseConnection();
            // TODO: String interpolation bad
            var statement = conn.prepareStatement("SELECT * FROM " + name);
            statement.execute();
            var results = statement.getResultSet();
            while (results.next()) {
                ret.add(record.fromMap(resultToMap(results)));
            }
        } catch (SQLException | DataAccessException e) {
            throw new DataAccessException(e.getMessage());
        } finally {
            DatabaseConnectionPool.getInstance().releaseConnection(conn);
        }
        return ret;
    }

    private Map<String, Object> resultToMap(ResultSet res) throws SQLException {
        var metaData = res.getMetaData();
        Map<String, Object> data = new HashMap<>();
        for (int i = 1; i <= metaData.getColumnCount(); i++) {
            var type = metaData.getColumnTypeName(i);
            var name = metaData.getColumnName(i);
            if (!name.equals("id")) { // Skip the id column
                if (type.equals("INT")) {
                    data.put(name, res.getInt(i));
                } else if (type.equals("VARCHAR")) {
                    data.put(name, res.getString(i));
                }
            }
        }
        return data;
    };

    public boolean truncate() throws DataAccessException {
        Connection conn = null;
        var ret = false;
        try {
            conn = DatabaseConnectionPool.getInstance().leaseConnection();
            // TODO: String interpolation bad
            var statement = conn.prepareStatement("TRUNCATE " + name);
            ret = statement.execute();
        } catch (SQLException | DataAccessException e) {
            throw new DataAccessException(e.getMessage());
        } finally {
            DatabaseConnectionPool.getInstance().releaseConnection(conn);
        }
        return ret;
    }

    public boolean drop() throws DataAccessException {
        Connection conn = null;
        var ret = false;
        try {
            conn = DatabaseConnectionPool.getInstance().leaseConnection();
            var statement = conn.prepareStatement("DROP table " + name);
            ret = statement.execute();
        } catch (SQLException | DataAccessException e) {
            throw new DataAccessException(e.getMessage());
        } finally {
            DatabaseConnectionPool.getInstance().releaseConnection(conn);
        }
        return ret;
    }

}