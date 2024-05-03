package Model.pieces;

import Model.PlayerColor;
import Model.board.ChessBoard;
import Model.board.Square;

import static Model.pieces.Bishop.isValidBishopMove;
import static Model.pieces.Rook.isValidRookMove;

public class Queen extends Piece{
    public Queen(final PlayerColor pieceColor, final char pieceName) {
        super(pieceColor, pieceName);
    }

    public Queen(Piece piece) {
        super(piece);
    }

    @Override
    public boolean isValidMove(ChessBoard chessBoard, Square currentSquare, Square destinationSquare) {
        if(!destinationSquare.isWithinBounds())
            return false;
        if(!destinationSquare.isEmpty() && destinationSquare.getPiece().getPieceColor()==this.getPieceColor())
            return false;


        return (isValidBishopMove(chessBoard, currentSquare, destinationSquare)||isValidRookMove(chessBoard, currentSquare, destinationSquare));
    }
}
