package dataaccess;

import chess.ChessGame;
import com.google.gson.Gson;
import model.GameDataRec;

import java.sql.SQLException;
import java.util.ArrayList;

public class GameDAODB implements GameDAO {

    public GameDAODB() {

    }

    @Override
    public void createGame(GameDataRec data) throws DataAccessException {
        try (var conn = DatabaseManager.getConnection()) {
            var statement = conn.prepareStatement("INSERT INTO GameData (gameID, whiteUsername, blackUsername, gameName, game) VALUES (?, ?, ?, ?, ?)");
            statement.setInt(1, data.gameID());
            statement.setString(2, data.whiteUsername());
            statement.setString(3, data.blackUsername());
            statement.setString(4, data.gameName());
            statement.setString(5, new Gson().toJson(data.game()));
            statement.execute();
        } catch (SQLException e) {
            throw new DataAccessException(e.getMessage());
        }
    }

    @Override
    public GameDataRec getGame(int gameID) throws DataAccessException {
        try (var conn = DatabaseManager.getConnection()) {
            var statement = conn.prepareStatement("SELECT gameID, whiteUsername, blackUsername, gameName, game FROM GameData WHERE gameID=?");
            statement.setInt(1, gameID);
            var results = statement.executeQuery();
            results.next();
            return new GameDataRec(
                    results.getInt("gameID"),
                    results.getString("whiteUsername"),
                    results.getString("blackUsername"),
                    results.getString("gameName"),
                    new Gson().fromJson(results.getString("game"), ChessGame.class));
        } catch (SQLException e) {
            throw new DataAccessException(e.getMessage());
        }
    }

    @Override
    public ArrayList<GameDataRec> listGames() throws DataAccessException {
        var games = new ArrayList<GameDataRec>();
        try (var conn = DatabaseManager.getConnection()) {
            var results = conn.prepareStatement("SELECT gameID, whiteUsername, blackUsername, gameName, game FROM GameData").executeQuery();
            while (results.next()) {
                games.add(new GameDataRec(
                        results.getInt("gameID"),
                        results.getString("whiteUsername"),
                        results.getString("blackUsername"),
                        results.getString("gameName"),
                        new Gson().fromJson(results.getString("game"), ChessGame.class)));
            }
        } catch (SQLException e) {
            throw new DataAccessException(e.getMessage());
        }
        return games;
    }

    @Override
    public void changeUsername(int gameID, ChessGame.TeamColor teamColor, String newUsername) throws DataAccessException {
        try (var conn = DatabaseManager.getConnection()) {
            var username = teamColor.equals(ChessGame.TeamColor.WHITE) ? "whiteUsername" : "blackUsername";
            var statement = conn.prepareStatement("UPDATE GameData SET " + username + "=? WHERE gameID=?");
            statement.setString(1, newUsername);
            statement.setInt(2, gameID);
            statement.execute();
        } catch (SQLException e) {
            throw new DataAccessException(e.getMessage());
        }
    }

    @Override
    public void removeGame(int gameID) throws DataAccessException {
        try (var conn = DatabaseManager.getConnection()) {
            var statement = conn.prepareStatement("DELETE FROM GameData WHERE gameID=?");
            statement.setInt(1, gameID);
            statement.execute();
        } catch (SQLException e) {
            throw new DataAccessException(e.getMessage());
        }
    }

    @Override
    public void clearAll() throws DataAccessException {
        try (var conn = DatabaseManager.getConnection()) {
            conn.prepareStatement("TRUNCATE GameData").execute();
        } catch (SQLException e) {
            throw new DataAccessException(e.getMessage());
        }
    }
}