package model;

import chess.ChessGame;

public record GameDataRec(int gameID, String whiteUsername, String blackUsername, String gameName, ChessGame game) {

    public GameDataRec changeWhiteUsername(String whiteUsername) {
        return new GameDataRec(gameID, whiteUsername, blackUsername, gameName, game);
    }

    public GameDataRec changeBlackUsername(String blackUsername) {
        return new GameDataRec(gameID, whiteUsername, blackUsername, gameName, game);
    }

    public GameDataRec changeGameObj(ChessGame game) {
        return new GameDataRec(gameID, whiteUsername, blackUsername, gameName, game);
    }

    public GameDataRec copy() {
        return new GameDataRec(gameID, whiteUsername, blackUsername, gameName, game);
    }
}
