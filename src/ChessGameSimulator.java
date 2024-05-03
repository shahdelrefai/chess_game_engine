import java.io.*;
import Model.ChessGame;
import Model.PlayerColor;
import Model.pieces.*;

public class ChessGameSimulator {
    final static String inputFilePath = "ChessGame.txt";
    final static String outputFilePath = "Output.txt";
    ChessGame chessGame = new ChessGame();
    public ChessGameSimulator() {

    }
    boolean isValidInput(String[] inputSquares){
        if(inputSquares.length!=2 && inputSquares.length!=3)
            return false;
        if (inputSquares[0].length() != 2 && inputSquares[1].length() != 2)
            return false;


        char firstChar1 = inputSquares[0].charAt(0);
        char firstChar2 = inputSquares[1].charAt(0);
        if (!(firstChar1 >= 'a' && firstChar1 <= 'h') || !(firstChar2 >= 'a' && firstChar2 <= 'h')) {
            return false;
        }

        char secondChar1 = inputSquares[0].charAt(1);
        char secondChar2 = inputSquares[1].charAt(1);
        if (!(secondChar1 >= '1' && secondChar1 <= '8') && !(secondChar2 >= '1' && secondChar2 <= '8')) {
            return false;
        }
        if(inputSquares.length==3){
            char promotionPiece = inputSquares[2].charAt(0);
            if(promotionPiece!='Q' && promotionPiece!='R' &&promotionPiece!='B' &&promotionPiece!='K')
                return false;
        }
        return true;
    }

    void runGame(){
        try (BufferedReader reader = new BufferedReader(new FileReader(inputFilePath));
             BufferedWriter writer = new BufferedWriter(new FileWriter(outputFilePath))) {

            String line;

            // Read lines until the end of the file
            while ((line = reader.readLine()) != null) {


                String[] inputSquares = line.split(",");
                if(isValidInput(inputSquares)==false){
                    writer.write("Invalid move");
                    writer.newLine();
                }

                if(inputSquares.length==3)
                    chessGame.makeMove(Character.getNumericValue(inputSquares[0].charAt(1))-1, inputSquares[0].charAt(0),
                            Character.getNumericValue(inputSquares[1].charAt(1))-1, inputSquares[1].charAt(0), inputSquares[2].charAt(0));
                else
                    chessGame.makeMove(Character.getNumericValue(inputSquares[0].charAt(1))-1, inputSquares[0].charAt(0),
                            Character.getNumericValue(inputSquares[1].charAt(1))-1, inputSquares[1].charAt(0), 'X');

                int[] prints = chessGame.getPrints();

                if(prints[0]==1){
                    writer.write("Stalemate");

                    writer.newLine();
                    break;
                }
                if(prints[1]==0){
                    writer.write("Invalid move");

                    writer.newLine();
                }
                if(prints[2]==1){
                    writer.write("Castle");

                    writer.newLine();
                }
                if(prints[3]==1){
                    writer.write("Enpassant");

                    writer.newLine();
                }
                if(prints[4]==1){
                    Piece capturedPiece = chessGame.getCapturedPiece();
                    String pieceName = null;
                    if(capturedPiece instanceof Pawn)
                        pieceName = "Pawn";
                    else if(capturedPiece instanceof Bishop)
                        pieceName = "Bishop";
                    else if(capturedPiece instanceof Queen)
                        pieceName = "Queen";
                    else if(capturedPiece instanceof Rook)
                        pieceName = "Rook";
                    else if(capturedPiece instanceof Knight)
                        pieceName = "Knight";
                    writer.write("Captured "+pieceName);

                    writer.newLine();
                }
                if(prints[5]==1){
                    if(chessGame.getGameTurn()== PlayerColor.WHITE){
                        writer.write("White in check");

                        writer.newLine();
                    }
                    else if(chessGame.getGameTurn()==PlayerColor.BLACK){
                        writer.write("Black in check");

                        writer.newLine();
                    }
                }
                if(prints[6]==1){
                    if(chessGame.getGameWinner()==PlayerColor.WHITE){
                        writer.write("White Won");

                        writer.newLine();

                    }
                    else if(chessGame.getGameWinner()==PlayerColor.BLACK){
                        writer.write("Black Won");

                        writer.newLine();
                    }
                    break;
                }
                if(prints[7]==1){
                    writer.write("Insufficient Material");

                    writer.newLine();
                    break;
                }

            }
            while((line = reader.readLine()) != null){
                writer.write("Game already ended");
                writer.newLine();
            }

        } catch (IOException e) {
            e.printStackTrace(); // Handle IOException, such as file not found or read/write error
        }
    }

    public static void main(String[] args) {

        ChessGameSimulator simulator = new ChessGameSimulator();
        simulator.runGame();

    }
}
