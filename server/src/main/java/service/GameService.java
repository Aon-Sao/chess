package service;

import chess.ChessGame;
import com.google.gson.Gson;
import dataaccess.DataAccessException;
import dataaccess.MemoryGameDAO;
import io.javalin.http.Context;
import model.GameDataRec;

import java.util.List;
import java.util.Map;

import static service.ServiceHelpers.*;

public class GameService {
    private static int makeGameID() throws DataAccessException {
        throw new RuntimeException("NYI: makeGameID");
    }

    private static boolean createGameChecks(Context context) {
        var body = getRequiredFields(context, List.of("gameName"));
        return (boolean) body.get("success");
    }

    private static int createGameAction(String gameName) throws DataAccessException {
        var gameID = makeGameID();
        new MemoryGameDAO().createGame(new GameDataRec(
                gameID,
                null,
                null,
                gameName,
                new ChessGame()
        ));
        return gameID;
    }

    public static void createGameHandler(Context context) throws DataAccessException {
        authWrapper(exceptionWrapper((cxt) -> {
            if (createGameChecks(context)) {
                var body = getRequiredFields(context, List.of("gameName"));
                var gameID = createGameAction((String) body.get("gameName"));
                Map msg = Map.of("gameID", Integer.toString(gameID));
                context.json(new Gson().toJson(msg));
                context.status(200);
            }
        })).apply(context);
    }

    public static void listGames(Context context) throws DataAccessException {
        authWrapper(exceptionWrapper((cxt) -> {
            throw new RuntimeException("NYI");
        })).apply(context);
    }

}