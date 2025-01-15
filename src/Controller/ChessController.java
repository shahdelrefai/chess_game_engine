package Controller;

import Model.ChessGame;
import Model.board.ChessBoard;
import Model.board.Square;
import Model.pieces.King;
import Model.pieces.Pawn;
import Model.pieces.Piece;
import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static javax.swing.SwingUtilities.isLeftMouseButton;
import static javax.swing.SwingUtilities.isRightMouseButton;

public class ChessController {
    private final ChessGame chessGame;
    private final GuiBoardContains guiBoardContains;
    private static final Color lightSquare = Color.decode("#cf8949");
    private static final Color darkSquare = Color.decode("#ffcc9b");
    private static final Color redSquare = Color.decode("#cc4834");
    private static final Color yellowSquare = Color.decode("#f6f76b");
    private static String piecesFile = "piecesGifs/";

    private Square currentSquare;
    private Square destinationSquare;
    protected Piece movedPiece;
    private boolean successfulMove = false;
    public ChessController()
    {
        this.chessGame = new ChessGame();
        this.guiBoardContains = new GuiBoardContains();
    }

    public GuiBoardContains getGuiBoardContains() {
        return guiBoardContains;
    }

    public class GuiBoardContains extends JPanel {
        java.util.List<GuiSquare> guiSquares;

        GuiBoardContains() {
            super(new GridLayout(8,8));
            this.guiSquares = new ArrayList<>();
            for(int rank = 7; rank >= 0; rank--)
            {
                for(char col = 'a'; col <= 'h'; col++)
                {
                    GuiSquare guiSquare = new GuiSquare(this, col, rank);
                    guiSquare.setSquareColor();
                    this.guiSquares.add(guiSquare);
                    add(guiSquare);
                }
            }
            setPreferredSize(new Dimension(400, 350));
            validate();
        }

        public void printChessBoard() {
            boolean isInCheck = chessGame.getChessBoard().isInCheck(chessGame.getGameTurn());
            boolean checkmate = chessGame.getGameStatus() == ChessGame.GameStatus.Checkmate;
            List<Square> legalMoves = movedPiece != null && movedPiece.getPieceColor() == chessGame.getGameTurn()? currentSquare.getPiece().getLegalMoves(chessGame.getChessBoard(), currentSquare) : Collections.emptyList();
            removeAll();
            for(GuiSquare guiSquare : guiSquares) {
                Square currSquare = chessGame.getChessBoard().getSquare(guiSquare.rank, guiSquare.col);
                Piece currentPiece = currSquare.getPiece();
                if(isInCheck && currentPiece instanceof King && currentPiece.getPieceColor() == chessGame.getGameTurn())
                    guiSquare.drawSquare(redSquare);
                else if (checkmate && currentPiece instanceof King && currentPiece.getPieceColor() == chessGame.changePlayerTurn()) {
                    guiSquare.drawSquare(redSquare);
                } else if(legalMoves.stream().anyMatch(square -> square.equals(currSquare)))
                    guiSquare.drawSquare(yellowSquare);
                else
                    guiSquare.drawSquare(null);
                add(guiSquare);
            }
            validate();
            repaint();
            if(chessGame.getGameStatus() != ChessGame.GameStatus.InProgress) endGame();
        }

        public void undoMove() {
            if(chessGame.undoMove())
            {
                Collections.reverse(guiBoardContains.guiSquares);
                printChessBoard();
            }
        }

        public void redoMove() {
            if(chessGame.redoMove())
            {
                Collections.reverse(guiBoardContains.guiSquares);
                printChessBoard();
            }
        }

        private void showGameOverPopup(String message) {
            System.out.println("hereeeeee");
            JOptionPane.showMessageDialog(null, message, "Game Over", JOptionPane.INFORMATION_MESSAGE);
            System.exit(0);
        }

        public void endGame() {
            switch (chessGame.getGameStatus()) {
                case Checkmate ->
                        showGameOverPopup("Checkmate! " + chessGame.getGameWinner().toString() + " wins!");
                case Stalemate ->
                        showGameOverPopup("Stalemate! The game is a draw.");
                case InsufficientMaterial ->
                        showGameOverPopup("Insufficient material. The game is a draw.");
            }
        }
    }

    private class GuiSquare extends JPanel {
        private final char col;
        private final int rank;

        GuiSquare(final GuiBoardContains guiBoardContains, final char col, final int rank) {
            super(new GridLayout());
            this.col = col;
            this.rank = rank;
            setPreferredSize(new Dimension(10, 10));
            setSquarePieceImage(chessGame.getChessBoard());

            addMouseListener(new MouseListener() {
                @Override
                public void mouseClicked(final MouseEvent e) {
                    if(isRightMouseButton(e)) {
                        currentSquare = null;
                        destinationSquare = null;
                        movedPiece = null;
                        successfulMove = false;
                    } else if(isLeftMouseButton(e)) {
                        if (currentSquare == null) {
                            currentSquare = chessGame.getChessBoard().getSquare(rank, col);
                            movedPiece = currentSquare.getPiece();
                            if (movedPiece == null)
                                currentSquare = null;
                        } else if (chessGame.getChessBoard().getSquare(rank, col).getPiece() != null && chessGame.getChessBoard().getSquare(rank, col).getPiece().getPieceColor() == chessGame.getGameTurn()) {
                            currentSquare = chessGame.getChessBoard().getSquare(rank, col);
                            movedPiece = currentSquare.getPiece();
                            if (movedPiece == null)
                                currentSquare = null;
                        } else {
                            destinationSquare = chessGame.getChessBoard().getSquare(rank, col);
                            int moveOutput = chessGame.makeMove(currentSquare.getRank(), currentSquare.getCol(), destinationSquare.getRank(), destinationSquare.getCol(), 'X');
                            if (moveOutput == 5) {
                                askForPromotionPiece();
                                chessGame.getChessBoardGameStack().addBoardToStack(new ChessBoard(chessGame.getChessBoard()));
                                successfulMove = true;
                            }
                            else if(moveOutput == 6)
                                successfulMove = true;
                            else
                                successfulMove = false;

                            currentSquare = null;
                            destinationSquare = null;
                            movedPiece = null;
                        }
                    }
                    SwingUtilities.invokeLater(new Runnable() {
                        @Override
                        public void run() {
                            if (successfulMove) {
                                Collections.reverse(guiBoardContains.guiSquares);
                                chessGame.getChessBoardGameStack().addBoardToStack(new ChessBoard(chessGame.getChessBoard()));
                                successfulMove = false;
                            }
                            guiBoardContains.printChessBoard();
                        }
                    });
                }

                @Override
                public void mousePressed(final MouseEvent e) {

                }

                @Override
                public void mouseReleased(final MouseEvent e) {

                }

                @Override
                public void mouseEntered(final MouseEvent e) {

                }

                @Override
                public void mouseExited(final MouseEvent e) {

                }
            });

            validate();
        }

        private void setSquareColor() {
            if(this.rank % 2 == 0)
                setBackground(this.col % 2 == 0 ? darkSquare : lightSquare);
            else
                setBackground(this.col % 2 == 0 ? lightSquare : darkSquare);
        }

        private void setSquareColor(Color color)
        {
            setBackground(color);
        }

        private void setSquarePieceImage(final ChessBoard chessBoard) {
            this.removeAll();
            if(!chessBoard.getSquare(this.rank, this.col).isEmpty()) {
                try {
                    String imagePath = piecesFile + chessBoard.getSquare(this.rank, this.col).getPiece().getPieceColor().toString() + "_" +
                            chessBoard.getSquare(this.rank, this.col).getPiece().getPieceName() + ".gif";
                    final BufferedImage image = ImageIO.read(new File(imagePath));
                    add(new JLabel(new ImageIcon(image)));
                }
                catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }

        public void drawSquare(Color color) {
            if(color != null)
                setSquareColor(color);
            else
                setSquareColor();
            setSquarePieceImage(chessGame.getChessBoard());
            validate();
            repaint();
        }

        private void askForPromotionPiece() {
            Object[] options = {"Queen", "Knight", "Bishop", "Rook"};
            int choice = JOptionPane.showOptionDialog(null, "Choose a piece to promote the pawn to:", "Pawn Promotion",
                    JOptionPane.DEFAULT_OPTION, JOptionPane.QUESTION_MESSAGE, null, options, options[0]);

            switch (choice) {
                case 0:
                    ((Pawn) movedPiece).promotePawn(chessGame.getChessBoard(), destinationSquare, 'Q');
                    break;
                case 1:
                    ((Pawn) movedPiece).promotePawn(chessGame.getChessBoard(), destinationSquare, 'N');
                    break;
                case 2:
                    ((Pawn) movedPiece).promotePawn(chessGame.getChessBoard(), destinationSquare, 'B');
                    break;
                case 3:
                    ((Pawn) movedPiece).promotePawn(chessGame.getChessBoard(), destinationSquare, 'R');
                    break;
            }
        }

    }

}