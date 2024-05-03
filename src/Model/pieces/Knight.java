package Model.pieces;

import Model.PlayerColor;
import Model.board.ChessBoard;
import Model.board.Square;

public class Knight extends Piece{
    public Knight(final PlayerColor pieceColor, final char pieceName) {
        super(pieceColor, pieceName);
    }

    public Knight(Piece piece) {
        super(piece);
    }

    @Override
    public boolean isValidMove(ChessBoard chessBoard, Square currentSquare, Square destinationSquare) {
        if(!destinationSquare.isWithinBounds())
            return false;
        if (!destinationSquare.isEmpty() && destinationSquare.getPiece().getPieceColor()==this.getPieceColor())
            return false;

        int currentRank = currentSquare.getRank();
        char currentCol = currentSquare.getCol();
        int destinationRank = destinationSquare.getRank();
        char destinationCol = destinationSquare.getCol();
        int multiplication  = Math.abs(currentRank-destinationRank)*Math.abs(currentCol-destinationCol);

        if(multiplication==2 && destinationSquare.isEmpty())
            return true;

        else return multiplication == 2 && destinationSquare.getPiece().getPieceColor() != this.getPieceColor();
    }

}
