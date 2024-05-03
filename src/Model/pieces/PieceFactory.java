package Model.pieces;

import Model.PlayerColor;

public class PieceFactory {

    public static Piece createPiece(PlayerColor pieceColor, char pieceName) {
        switch (pieceName) {
            case 'R':
                return new Rook(pieceColor, pieceName);
            case 'N':
                return new Knight(pieceColor, pieceName);
            case 'B':
                return new Bishop(pieceColor, pieceName);
            case 'P':
                return new Pawn(pieceColor, pieceName);
            case 'K':
                return new King(pieceColor, pieceName);
            case 'Q':
                return new Queen(pieceColor, pieceName);
            default:
                throw new IllegalArgumentException("Invalid piece name: " + pieceName);
        }
    }

    public static Piece copyPiece(Piece piece) {
        switch (piece.getPieceName()) {
            case 'R':
                return new Rook(piece);
            case 'N':
                return new Knight(piece);
            case 'B':
                return new Bishop(piece);
            case 'P':
                return new Pawn(piece);
            case 'K':
                return new King(piece);
            case 'Q':
                return new Queen(piece);
            default:
                throw new IllegalArgumentException("Invalid piece name: " + piece.getPieceName());
        }
    }
}