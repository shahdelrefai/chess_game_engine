package Model.board;

public class BoardUtilities {
    public BoardUtilities() {
    }
    public static boolean isWithinBounds(char col, int rank) {
        return rank >= 0 && rank <= 7 && col >= 'a' && col <= 'h';
    }
}
