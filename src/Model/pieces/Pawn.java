package Model.pieces;

import Model.PlayerColor;
import Model.board.BoardUtilities;
import Model.board.ChessBoard;
import Model.board.Square;

public class Pawn extends Piece{
    public Pawn(final PlayerColor pieceColor, final char pieceName) {
        super(pieceColor, pieceName);
    }

    public Pawn(Piece piece) {
        super(piece);
    }

    @Override
    public boolean isValidMove(ChessBoard chessBoard, Square currentSquare, Square destinationSquare) {
        int startRow = currentSquare.getRank();
        char startColumn = currentSquare.getCol();
        int endRow = destinationSquare.getRank();
        char endColumn = destinationSquare.getCol();
        PlayerColor colour = this.getPieceColor();
        int direction = (colour== PlayerColor.WHITE)? 1:-1; //white moves up, black moves down


        if (!destinationSquare.isWithinBounds()) {
            return false;
        }

        if(endRow == startRow+direction && startColumn == endColumn && destinationSquare.getPiece() == null)
            return true;

        else if(endRow == startRow+2*direction && startColumn == endColumn &&  destinationSquare.isEmpty() &&
                chessBoard.getSquare(startRow+direction, endColumn).isEmpty()){
            if(this.getPieceColor() == PlayerColor.WHITE && startRow ==1)
                return true;
            else if(this.getPieceColor() == PlayerColor.BLACK && startRow ==6)
                return true;

        }

        else if(endRow == startRow+direction && Math.abs(startColumn- endColumn)==1 && !destinationSquare.isEmpty() && destinationSquare.getPiece().getPieceColor()!=colour ){
            return true;
        }

        return canCapturenpassant(chessBoard, currentSquare, destinationSquare);
    }

    public boolean canCapturenpassant(ChessBoard chessBoard, Square currentSquare, Square destinationSquare) {
        Square lastMovedOnSquare = chessBoard.getLastMovedOnSquare();
        if(lastMovedOnSquare == null) return false;
        int direction = (currentSquare.getPiece().getPieceColor() == PlayerColor.WHITE)? 1:-1;
        return lastMovedOnSquare.getPiece() instanceof Pawn && lastMovedOnSquare.getPiece().getNumMoves() == 1
                && lastMovedOnSquare.getRank() == currentSquare.getRank()
                && (lastMovedOnSquare.getCol() == destinationSquare.getCol() && lastMovedOnSquare.getRank()+direction == destinationSquare.getRank());
    }

    public boolean promotePawn(ChessBoard chessBoard, Square pawnSquare,char promotionPiece) {
        if(promotionPiece=='Q')
            chessBoard.getSquare(pawnSquare.getRank(), pawnSquare.getCol()).occupySquare(new Queen(this.getPieceColor(), 'Q'));
        else if(promotionPiece=='K')
            chessBoard.getSquare(pawnSquare.getRank(), pawnSquare.getCol()).occupySquare(new Knight(this.getPieceColor(), 'N'));
        else if(promotionPiece=='B')
            chessBoard.getSquare(pawnSquare.getRank(), pawnSquare.getCol()).occupySquare(new Bishop(this.getPieceColor(), 'B'));
        else if(promotionPiece=='R')
            chessBoard.getSquare(pawnSquare.getRank(), pawnSquare.getCol()).occupySquare(new Rook(this.getPieceColor(), 'R'));
        else
            return false;
        chessBoard.getSquare(pawnSquare.getRank(), pawnSquare.getCol()).getPiece().movePiece();
        chessBoard.setLastMovedOnSquare(pawnSquare);
        return true;
    }
}
