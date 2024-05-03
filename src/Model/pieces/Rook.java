package Model.pieces;

import Model.PlayerColor;
import Model.board.ChessBoard;
import Model.board.Square;

public class Rook extends Piece{
    public Rook(final PlayerColor pieceColor, final char pieceName) {
        super(pieceColor, pieceName);
    }

    public Rook(Piece piece) {
        super(piece);
    }

    @Override
    public boolean isValidMove(ChessBoard chessBoard, Square currentSquare, Square destinationSquare) {
        if(!destinationSquare.isWithinBounds())
            return false;
        if(!destinationSquare.isEmpty() && destinationSquare.getPiece().getPieceColor()==this.getPieceColor())
            return false;
        if(currentSquare.getRank()==destinationSquare.getRank() && !isRookPathBlocked(chessBoard,currentSquare,destinationSquare) )
            return true;
        else if(currentSquare.getCol()==destinationSquare.getCol() && !isRookPathBlocked(chessBoard,currentSquare,destinationSquare) )
            return true;

        return false;
    }

    static boolean isValidRookMove(ChessBoard chessBoard, Square currentSquare, Square destinationSquare){
        if(!destinationSquare.isWithinBounds()){
            return false;
        }
        if(currentSquare.getRank()==destinationSquare.getRank() && !isRookPathBlocked(chessBoard,currentSquare,destinationSquare) ){

            return true;
        }
        else if(currentSquare.getCol()==destinationSquare.getCol() && !isRookPathBlocked(chessBoard,currentSquare,destinationSquare) ){

            return true;
        }
        return false;
    }

    private static boolean isRookPathBlocked(ChessBoard chessBoard, Square start, Square end){
        int startRow = start.getRank();
        char startColumn = start.getCol();
        int endRow = end.getRank();
        char endColumn = end.getCol();

        if (startRow == endRow) {
            int step = (startColumn < endColumn) ? 1 : -1;
            for (char columnChecker = (char) (startColumn + step); columnChecker != endColumn; columnChecker += step) {
                if (!chessBoard.getSquare(startRow, columnChecker).isEmpty()) {

                    return true;

                }
            }
        }

        if (startColumn == endColumn) {
            int step = (startRow < endRow) ? 1 : -1;
            for (int rowChecker = startRow + step; rowChecker != endRow; rowChecker += step) {
                if (!chessBoard.getSquare(rowChecker, startColumn).isEmpty()) {
                    if(rowChecker==endRow)
                        return(end.getPiece().getPieceColor()!=start.getPiece().getPieceColor());
                    return true;
                }
            }
        }

        return false;
    }
}
