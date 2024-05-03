import Controller.ChessController;
import View.GuiBoard;

public class RunChess {

    public static void main(String[] args) {
        ChessController board = new ChessController();
        GuiBoard guiBoard = new GuiBoard(board.getGuiBoardContains());

    }
}
