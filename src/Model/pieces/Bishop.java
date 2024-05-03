package Model.pieces;

import Model.PlayerColor;
import Model.board.ChessBoard;
import Model.board.Square;

public class Bishop extends Piece{
    public Bishop(final PlayerColor pieceColor, final char pieceName) {
        super(pieceColor, pieceName);
    }

    public Bishop(Piece piece) {
        super(piece);
    }

    @Override
    public boolean isValidMove(ChessBoard chessBoard, Square currentSquare, Square destinationSquare) {
        if(!destinationSquare.isWithinBounds())
            return false;
        if (!destinationSquare.isEmpty() && destinationSquare.getPiece().getPieceColor()==this.getPieceColor())
            return false;

        return isValidBishopMove(chessBoard, currentSquare, destinationSquare);
    }

    static boolean isValidBishopMove(ChessBoard chessBoard, Square currentSquare, Square destinationSquare){
        if(!destinationSquare.isWithinBounds())
            return false;

        int currentRank = currentSquare.getRank();
        char currentCol = currentSquare.getCol();
        int destinationRank = destinationSquare.getRank();
        char destinationCol = destinationSquare.getCol();

        return Math.abs(currentRank - destinationRank) == Math.abs(currentCol - destinationCol) && !isBishopPathBlocked(chessBoard, currentSquare, destinationSquare);
    }

    static boolean isBishopPathBlocked(ChessBoard chessBoard, Square currentSquare, Square destinationSquare) {
        int currentRank = currentSquare.getRank();
        char currentCol = currentSquare.getCol();
        int destinationRank = destinationSquare.getRank();
        char destinationCol = destinationSquare.getCol();

        int rowStep = (currentRank < destinationRank) ? 1 : -1;
        int colStep = (currentCol < destinationCol) ? 1 : -1;

        for (int rankChecker = currentRank + rowStep, colChecker = (char) (currentCol + colStep);
             rankChecker != destinationRank && colChecker != destinationCol;
             rankChecker += rowStep, colChecker += colStep) {

            if (!chessBoard.getSquare(rankChecker, (char) colChecker).isEmpty()) {
                return true;
            }
        }

        return false;
    }
}
