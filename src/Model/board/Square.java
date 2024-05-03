package Model.board;

import Model.pieces.Piece;

public class Square {
    protected final char col;
    protected final int rank;

    private Piece piece;

    public Square(Piece piece, final int rank, final char col) {
        this.piece = piece;
        this.col = col;
        this.rank = rank;
    }

    public char getCol() {
        return this.col;
    }

    public int getRank() {
        return this.rank;
    }

    public boolean isEmpty() {
        return this.piece == null;
    }

    public void emptySquare() {
        this.piece = null;
    }
    public Piece getPiece() {
        return this.piece;
    }
    public void occupySquare(Piece piece){
        this.piece = piece;
    }

    public boolean isWithinBounds(){
        return this.rank >= 0 && this.rank <= 7 && this.col >= 'a' && this.col <= 'h';
    }
}
