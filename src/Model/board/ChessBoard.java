package Model.board;

import Model.PlayerColor;
import Model.pieces.*;

public class ChessBoard {
    public Square[][] chessBoard = new Square[8][8];
    Square lastMovedOnSquare = null;

    public ChessBoard() {
        this.chessBoard[7][0] = new Square(PieceFactory.createPiece(PlayerColor.BLACK, 'R'), 7, 'a');
        this.chessBoard[7][1] = new Square(PieceFactory.createPiece(PlayerColor.BLACK, 'N'), 7, 'b');
        this.chessBoard[7][2] = new Square(PieceFactory.createPiece(PlayerColor.BLACK, 'B'), 7, 'c');
        this.chessBoard[7][3] = new Square(PieceFactory.createPiece(PlayerColor.BLACK, 'Q'), 7, 'd');
        this.chessBoard[7][4] = new Square(PieceFactory.createPiece(PlayerColor.BLACK, 'K'), 7, 'e');
        this.chessBoard[7][5] = new Square(PieceFactory.createPiece(PlayerColor.BLACK, 'B'), 7, 'f');
        this.chessBoard[7][6] = new Square(PieceFactory.createPiece(PlayerColor.BLACK, 'N'), 7, 'g');
        this.chessBoard[7][7] = new Square(PieceFactory.createPiece(PlayerColor.BLACK, 'R'), 7, 'h');

        for (int i = 0; i < 8; i++)
            this.chessBoard[6][i] = new Square(PieceFactory.createPiece(PlayerColor.BLACK, 'P'), 6, (char) ('a' + i));

        for (int i = 2; i < 6; i++) {
            for (int j = 0; j < 8; j++) {
                this.chessBoard[i][j] = new Square(null, i, (char) ('a' + j));
            }
        }

        for (int i = 0; i < 8; i++) {
            this.chessBoard[1][i] = new Square(PieceFactory.createPiece(PlayerColor.WHITE, 'P'), 1, (char) ('a' + i));
        }
        this.chessBoard[0][0] = new Square(PieceFactory.createPiece(PlayerColor.WHITE, 'R'), 0, 'a');
        this.chessBoard[0][1] = new Square(PieceFactory.createPiece(PlayerColor.WHITE, 'N'), 0, 'b');
        this.chessBoard[0][2] = new Square(PieceFactory.createPiece(PlayerColor.WHITE, 'B'), 0, 'c');
        this.chessBoard[0][3] = new Square(PieceFactory.createPiece(PlayerColor.WHITE, 'Q'), 0, 'd');
        this.chessBoard[0][4] = new Square(PieceFactory.createPiece(PlayerColor.WHITE, 'K'), 0, 'e');
        this.chessBoard[0][5] = new Square(PieceFactory.createPiece(PlayerColor.WHITE, 'B'), 0, 'f');
        this.chessBoard[0][6] = new Square(PieceFactory.createPiece(PlayerColor.WHITE, 'N'), 0, 'g');
        this.chessBoard[0][7] = new Square(PieceFactory.createPiece(PlayerColor.WHITE, 'R'), 0, 'h');
    }

    public ChessBoard(ChessBoard toCopy) {
        for (int i = 0; i < 8; i++) {
            for (int j = 0; j < 8; j++) {
                Piece piece = toCopy.chessBoard[i][j].getPiece();
                if(piece != null)
                    this.chessBoard[i][j] = new Square(PieceFactory.copyPiece(piece), i, (char) ('a'+j));
                else
                    this.chessBoard[i][j] = new Square(null, i, (char) ('a' + j));
            }
        }
        if(toCopy.lastMovedOnSquare != null)
        {
            this.lastMovedOnSquare = getSquare(toCopy.lastMovedOnSquare.getRank(), toCopy.lastMovedOnSquare.getCol());
        }
    }

    public Square getSquare(int rank, char col) {
        return chessBoard[rank][col - 'a'];
    }
    public Square findKingSquare(PlayerColor player) {
        for (int i = 0; i < 8; i++)
            for(int j = 0; j < 8; j++)
            {
                Piece piece = this.getSquare(i, (char)('a'+j)).getPiece();
                if (piece instanceof King && piece.getPieceColor() == player)
                    return this.getSquare(i, (char)('a'+j));

            }
        return null;
    }

    public boolean isSquareInDanger(Square square, PlayerColor attackerColour) {
        for (int i = 0; i < 8; i++) {
            for (int j = 0; j < 8; j++) {
                Square currentSquare = this.getSquare(i, (char) ('a' + j));
                Piece piece = currentSquare.getPiece();

                if (!currentSquare.isEmpty() && piece.getPieceColor() == attackerColour &&
                        piece.isValidMove(this, currentSquare, square)) {
                    return true;
                }
            }
        }

        return false;
    }

    public boolean isMoveGettingOutOfCheck(Square currentSquare, Square destinationSquare, PlayerColor player) {
        Piece movedPiece = currentSquare.getPiece();
        Piece capturedPiece = destinationSquare.getPiece();

        destinationSquare.occupySquare(movedPiece);
        currentSquare.emptySquare();

        boolean isOutOfCheck = !isInCheck(player);

        currentSquare.occupySquare(movedPiece);
        destinationSquare.emptySquare();
        destinationSquare.occupySquare(capturedPiece);

        return isOutOfCheck;
    }

    public boolean isInCheck(PlayerColor player) {
        Square kingSquare = findKingSquare(player);
        PlayerColor opponent = (player == PlayerColor.WHITE) ? PlayerColor.BLACK : PlayerColor.WHITE;

        return isSquareInDanger(kingSquare, opponent);
    }

    public Square getLastMovedOnSquare() {
        return this.lastMovedOnSquare;
    }

    public void setLastMovedOnSquare(Square lastMovedOnSquare) {
        this.lastMovedOnSquare = lastMovedOnSquare;
    }
}