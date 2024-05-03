package Model.pieces;

import Model.PlayerColor;
import Model.board.ChessBoard;
import Model.board.Square;

import java.util.ArrayList;
import java.util.List;

public abstract class Piece {
    protected final PlayerColor pieceColor;
    private int numMoves;
    protected boolean moved;
    private final char pieceName;

    public Piece(final PlayerColor pieceColor, final char pieceName) {
        this.pieceColor = pieceColor;
        this.moved = false;
        this.numMoves = 0;
        this.pieceName = pieceName;
    }

    public Piece(Piece piece) {
        this.pieceColor = piece.pieceColor;
        this.numMoves = piece.numMoves;
        this.moved = piece.moved;
        this.pieceName = piece.pieceName;
    }

    public PlayerColor getPieceColor() {
        return this.pieceColor;
    }

    public char getPieceName() {
        return this.pieceName;
    }

    public abstract boolean isValidMove(ChessBoard chessBoard, Square currentSquare, Square destinationSquare);
    public List<Square> getLegalMoves(ChessBoard chessBoard, Square currentSquare) {
        List<Square> legalMoves = new ArrayList<>();

        for (int i = 0; i < 8; i++) {
            for (int j = 0; j < 8; j++) {
                Square destinationSquare = chessBoard.getSquare(i, (char)('a' + j));

                if (currentSquare.getPiece().isValidMove(chessBoard, currentSquare, destinationSquare) ) {
                    legalMoves.add(destinationSquare);
                }
            }
        }

        return legalMoves;
    }

    public boolean getMoved() {
        return this.moved;
    }
    public void setMoved(boolean hasMoved) {
        this.moved = hasMoved;
    }
    public int getNumMoves (){
        return this.numMoves;
    }
    public void movePiece(){
        this.numMoves++;
    }
}
