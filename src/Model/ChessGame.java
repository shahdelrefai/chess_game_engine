package Model;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import Model.PlayerColor;
import Model.board.*;
import Model.pieces.*;

import static Model.PlayerColor.BLACK;
import static Model.PlayerColor.WHITE;

public class ChessGame {
    private ChessBoard chessBoard;
    private final ChessBoardGameStack chessBoardGameStack;
    private PlayerColor player1, player2;
    private final PlayerColor whitePlayer;
    private PlayerColor gameTurn;
    private PlayerColor gameWinner;
    public enum GameStatus{InProgress, Checkmate, Stalemate, InsufficientMaterial};
    private GameStatus gameStatus;
    private Piece capturedPiece;
    private int[] prints = new int[8]; //stalemate, invalid move = 0, castling, enpassant, captured piece, in check, won, insufficient material

    public ChessGame()
    {
        Random random = new Random();
        player1 = random.nextInt(2) == 0 ? WHITE : BLACK;
        player2 = player1 == WHITE ? BLACK : WHITE;
        this.gameTurn = player1 == WHITE? player1 : player2;
        this.whitePlayer = this.gameTurn;

        this.chessBoard = new ChessBoard();
        this.chessBoardGameStack = new ChessBoardGameStack(chessBoard);

        this.gameStatus = GameStatus.InProgress;

        this.resetPrints();
    }

    public ChessBoard getChessBoard() {
        return this.chessBoard;
    }

    public ChessBoardGameStack getChessBoardGameStack() {
        return chessBoardGameStack;
    }

    public PlayerColor getGameTurn() {
        return this.gameTurn;
    }
    public PlayerColor getGameWinner() {
        return this.gameWinner;
    }

    public GameStatus getGameStatus() {
        return this.gameStatus;
    }

    public int makeMove(int row, char col, int destinationRow, char destinationCol, char promotionPiece) {
        this.resetPrints();

        if (isStalemate(gameTurn)) {
            this.gameStatus = GameStatus.Stalemate;
            prints[0] = 1;
            return 0;
        }

        Square currentSquare = this.chessBoard.getSquare(row, col);
        Piece prevCurrentPiece = currentSquare.getPiece();
        Square destinationSquare = this.chessBoard.getSquare(destinationRow, destinationCol);
        Piece prevDestinationPiece = destinationSquare.getPiece();

        if (currentSquare.getPiece() == null) {
            return 1;
        }

        if (currentSquare.getPiece().getPieceColor() != (gameTurn == whitePlayer ? WHITE : BLACK)) {
            return 2;
        }

        if (!CheckIfValidMove(currentSquare, destinationSquare)) {
            return 3;
        }
        prints[1] = 1;

        if (destinationSquare.getPiece() != null) {
            destinationSquare.occupySquare(currentSquare.getPiece());
            currentSquare.emptySquare();
        } else if (currentSquare.getPiece() instanceof King && Math.abs(destinationCol - col) == 2) {
            Square rookSquare;
            if (col < destinationCol) {
                rookSquare = chessBoard.getSquare(row, 'h');
            } else {
                rookSquare = chessBoard.getSquare(row, 'a');
            }

            Piece rook = rookSquare.getPiece();
            // Update rook position during castling
            if (col < destinationCol) {
                Square newRookSquare = chessBoard.getSquare(row, (char) (destinationCol - 1));
                newRookSquare.occupySquare(rook);
                rookSquare.emptySquare();
            } else {
                Square newRookSquare = chessBoard.getSquare(row, (char) (destinationCol + 1));
                newRookSquare.occupySquare(rook);
                rookSquare.emptySquare();
            }

            // Update king position during castling
            Square kingSquare = chessBoard.findKingSquare(gameTurn);
            destinationSquare.occupySquare(kingSquare.getPiece());
            currentSquare.emptySquare();

            destinationSquare.getPiece().movePiece();
            prints[2] = 1;
        } else if (currentSquare.getPiece() instanceof Pawn && ((Pawn) currentSquare.getPiece()).canCapturenpassant(chessBoard, currentSquare, destinationSquare)) {
            // En passant capture
            int direction = (gameTurn == WHITE) ? 1 : -1;
            Square capturedPawnSquare = chessBoard.getSquare(destinationRow - direction, destinationCol);
            capturedPiece = capturedPawnSquare.getPiece();
            capturedPawnSquare.emptySquare();

            destinationSquare.occupySquare(currentSquare.getPiece());
            currentSquare.emptySquare();
            prints[3] = 1;
            prints[4]=1;

        } else {
            destinationSquare.occupySquare(currentSquare.getPiece());
            currentSquare.emptySquare();
        }
        destinationSquare.getPiece().movePiece();
        chessBoard.setLastMovedOnSquare(destinationSquare);

        if (chessBoard.isInCheck(gameTurn)) {
            currentSquare.occupySquare(prevCurrentPiece);
            destinationSquare.occupySquare(prevDestinationPiece);
            prints[5] = 1;
            return 4;
        }

        if(prevDestinationPiece != null)
        {
            prints[4] = 1;
            capturedPiece = prevDestinationPiece;
        }

        if (prevCurrentPiece instanceof Pawn && (destinationRow == 7 || destinationRow == 0)) {
            gameTurn = changePlayerTurn();
            return 5;
        }


        if (isCheckmate(changePlayerTurn())) {
            this.gameWinner = gameTurn;
            this.gameStatus = GameStatus.Checkmate;
            prints[6] = 1;
            return 200;
        }


        if(chessBoard.isInCheck(changePlayerTurn())) prints[5] = 1;

        if(insufficientMaterial()){
            prints[7]=1;
            return 0;
        }

        //this.chessBoardGameStack.addBoardToStack(new ChessBoard(this.chessBoard));
        this.gameTurn = changePlayerTurn();


        return 6;
    }

    public PlayerColor changePlayerTurn() {
        return this.gameTurn == WHITE ? BLACK : WHITE;
    }

    public boolean CheckIfValidMove(Square currentSquare, Square destinationSquare) {
        return currentSquare.getPiece().isValidMove(chessBoard, currentSquare, destinationSquare);
    }

    public boolean isStalemate(PlayerColor player) {
        if(chessBoard.isInCheck(player)) return false;

        for (int i = 0; i < 8; i++) {
            for (int j = 0; j < 8; j++) {
                Square currentSquare = this.chessBoard.getSquare(i, (char) ('a' + j));
                Piece currentPiece = currentSquare.getPiece();

                if (currentPiece != null && currentPiece.getPieceColor() == player) {
                    for (int x = 0; x < 8; x++) {
                        for (int y = 0; y < 8; y++) {
                            Square destinationSquare = this.chessBoard.getSquare(x, (char) ('a' + y));
                            if (currentPiece.isValidMove(chessBoard, currentSquare, destinationSquare)
                                    && chessBoard.isMoveGettingOutOfCheck(currentSquare,destinationSquare,player)) {
                                return false;
                            }
                        }
                    }
                }
            }
        }

        return true;
    }

    public boolean isCheckmate(PlayerColor player) {
        if (!chessBoard.isInCheck(player)) return false;

        return isStalemate(player);
    }

    public boolean insufficientMaterial(){
        for (int i = 0; i < 8; i++) {
            for (int j = 0; j < 8; j++) {
                Square square = this.chessBoard.getSquare(i, (char) ('a' + j));
                if (!square.isEmpty() && square.getPiece() instanceof Pawn) {
                    return false;
                }
            }
        }
        for (int i = 0; i < 8; i++) {
            for (int j = 0; j < 8; j++) {
                Square square = this.chessBoard.getSquare(i, (char) ('a' + j));
                if (!square.isEmpty() && ((square.getPiece() instanceof Rook) ||
                        (square.getPiece() instanceof Queen) )) {
                    return false;
                }
            }
        }
        int count=0; //count bishops, kings and knights
        for (int i = 0; i < 8; i++) {
            for (int j = 0; j < 8; j++) {
                Square square = this.chessBoard.getSquare(i, (char) ('a' + j));
                if (!square.isEmpty()) {
                    count++;
                }
            }
        }
        if(count>4)
            return false;
        int whiteKing=0;
        int blackKing=0;
        int blackBishop=0;
        int whiteBishop=0;
        int whiteKnight=0;
        int blackKnight=0;
        for (int i = 0; i < 8; i++) {
            for (int j = 0; j < 8; j++) {
                Square square = this.chessBoard.getSquare(i, (char) ('a' + j));
                if (!square.isEmpty() && square.getPiece().getPieceColor()== BLACK && square.getPiece() instanceof King) {
                    blackKing++;

                }
                else if (!square.isEmpty() && square.getPiece().getPieceColor()== BLACK && square.getPiece() instanceof Bishop) {
                    blackBishop++;

                }
                else if (!square.isEmpty() && square.getPiece().getPieceColor()== BLACK && square.getPiece() instanceof Knight) {
                    blackKnight++;

                }
                else if (!square.isEmpty() && square.getPiece().getPieceColor()== WHITE && square.getPiece() instanceof King) {
                    whiteKing++;

                }
                else if (!square.isEmpty() && square.getPiece().getPieceColor()== WHITE && square.getPiece() instanceof Bishop) {
                    whiteBishop++;

                }
                else if (!square.isEmpty() && square.getPiece().getPieceColor()== WHITE && square.getPiece() instanceof Knight) {
                    whiteKnight++;

                }

                if(blackKing==1 && whiteKing==1 && whiteBishop==0 && blackBishop==0 && whiteKnight==0 && blackKnight==0){ //lone king
                    this.gameStatus = GameStatus.InsufficientMaterial;
                    return true;
                }
                else if(blackKing==1 && whiteKing==1 && whiteBishop==1 && blackBishop==1 && whiteKnight==0 && blackKnight==0){ //a king and bishop
                    this.gameStatus = GameStatus.InsufficientMaterial;
                    return true;
                }
                else if(blackKing==1 && whiteKing==1 && whiteBishop==0 && blackBishop==0&& whiteKnight==1 && blackKnight==1){ //a king and knight
                    this.gameStatus = GameStatus.InsufficientMaterial;
                    return true;
                }
            }
        }
        return false;
    }

    private void resetPrints()
    {
        for (int i = 0; i < prints.length; i++) {
            prints[i] = 0;
        }

    }
    public int[] getPrints(){
        return this.prints;
    }
    public Piece getCapturedPiece(){
        return this.capturedPiece;
    }

    public boolean undoMove() {
        if(chessBoardGameStack.undoMove())
        {
            chessBoard = new ChessBoard(chessBoardGameStack.getCurrentBoard());
            gameTurn = changePlayerTurn();
            return true;
        }
        return false;
    }

    public boolean redoMove() {
        if(chessBoardGameStack.redoMove())
        {
            chessBoard = new ChessBoard(chessBoardGameStack.getCurrentBoard());
            gameTurn = changePlayerTurn();
            return true;
        }
        return false;
    }
}
