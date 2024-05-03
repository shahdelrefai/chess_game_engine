package Model.pieces;

import Model.PlayerColor;
import Model.board.ChessBoard;
import Model.board.Square;

public class King extends Piece{
    public King(final PlayerColor pieceColor, final char pieceName) {
        super(pieceColor, pieceName);
    }

    public King(Piece piece) {
        super(piece);
    }

    @Override
    public boolean isValidMove(ChessBoard chessBoard, Square currentSquare, Square destinationSquare) {
        int startRow = currentSquare.getRank();
        char startColumn = currentSquare.getCol();
        int endRow = destinationSquare.getRank();
        char endColumn = destinationSquare.getCol();
        int rowDifference  = Math.abs(startRow-endRow);
        int colDifference = Math.abs(startColumn-endColumn);
        int difference = rowDifference+colDifference;
        if(!destinationSquare.isWithinBounds()){
            return false;
        }
        if(((difference==1) ||(rowDifference ==1 && colDifference==1)) && (destinationSquare.isEmpty()||destinationSquare.getPiece().getPieceColor()!=this.getPieceColor())
                && chessBoard.isMoveGettingOutOfCheck(currentSquare, destinationSquare, currentSquare.getPiece().pieceColor)) {
            return true;
        }

        if(!(destinationSquare.getPiece() instanceof King)){ //A king cant checkmate a king
            if (this.getNumMoves()==0 && !chessBoard.isInCheck(currentSquare.getPiece().pieceColor) && destinationSquare.isWithinBounds() &&
                    Math.abs(startColumn - endColumn) == 2 && startRow == endRow) {

                // Check if there are no pieces between king and rook
                if (startColumn < endColumn) {
                    for (char col = (char) (startColumn + 1); col < endColumn; col++) {
                        Square square = chessBoard.getSquare(startRow, col);
                        if (!square.isEmpty()) {
                            return false;
                        }
                    }
                } else {
                    for (char col = (char) (endColumn + 1); col < startColumn; col++) {
                        Square square = chessBoard.getSquare(startRow, col);
                        if (!square.isEmpty()) {
                            return false;
                        }
                    }
                }
                // Check if the squares the king moves through and the destination square are not under attack
                for (char col = startColumn; col <= endColumn; col++) {
                    Square intermediateSquare = chessBoard.getSquare(startRow, col);
                    PlayerColor oppositePlayer;

                    if(this.getPieceColor()==PlayerColor.WHITE)
                        oppositePlayer = PlayerColor.BLACK;
                    else
                        oppositePlayer = PlayerColor.WHITE;
                    if (chessBoard.isSquareInDanger(intermediateSquare, oppositePlayer)) {
                        return false;
                    }
                }

                // Check if the rook has moved
                Square rookSquare;
                if (startColumn < endColumn) {
                    rookSquare = chessBoard.getSquare(startRow, 'h');
                } else {
                    rookSquare = chessBoard.getSquare(startRow, 'a');
                }

                Piece rook = rookSquare.getPiece();
                if (!(rook instanceof Rook) || ((Rook) rook).getMoved()) {
                    return false;
                }

                return true;
            }
            return false;
        }
        return false;
    }

}
