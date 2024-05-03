package Model.board;

import java.util.Stack;

public class ChessBoardGameStack {
    private Stack<ChessBoardMemento> movesStack;
    private Stack<ChessBoardMemento> redoStack;

    public ChessBoardGameStack(ChessBoard initialBoard) {
        movesStack = new Stack<>();
        redoStack = new Stack<>();
        movesStack.add(new ChessBoardMemento(initialBoard));
    }

    public void addBoardToStack(ChessBoard board) {
        movesStack.add(new ChessBoardMemento(board));
        redoStack.clear();
    }

    public boolean undoMove() {
        if (movesStack.size() == 1) return false;
        ChessBoardMemento removed = movesStack.pop();
        redoStack.add(removed);
        return true;
    }

    public boolean redoMove() {
        if (redoStack.isEmpty()) return false;
        ChessBoardMemento added = redoStack.pop();
        movesStack.add(added);
        return true;
    }


    public ChessBoard getCurrentBoard() {
        return movesStack.peek().getBoardState();
    }

    private static class ChessBoardMemento {
        private final ChessBoard boardState;

        public ChessBoardMemento(ChessBoard boardStateToSave) {
            boardState = new ChessBoard(boardStateToSave);
        }

        public ChessBoard getBoardState() {
            return boardState;
        }
    }
}
