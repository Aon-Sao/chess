package model;

import chess.ChessGame;

public record GameDataRec(int gameID, String whiteUsername, String blackUsername, String gameName, ChessGame game) {

    public GameDataRec changeGameID(int gameID) {
        return new GameDataRec(gameID, whiteUsername, blackUsername, gameName, game);
    }

    public GameDataRec changeWhiteUsername(String whiteUsername) {
        return new GameDataRec(gameID, whiteUsername, blackUsername, gameName, game);
    }

    public GameDataRec changeBlackUsername(String blackUsername) {
        return new GameDataRec(gameID, whiteUsername, blackUsername, gameName, game);
    }

    public GameDataRec changeGameName(String gameName) {
        return new GameDataRec(gameID, whiteUsername, blackUsername, gameName, game);
    }

    public GameDataRec changeGameObj(ChessGame game) {
        return new GameDataRec(gameID, whiteUsername, blackUsername, gameName, game);
    }
}
