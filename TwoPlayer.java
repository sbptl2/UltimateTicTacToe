import java.util.Scanner;
import org.apache.commons.math3.distribution.BetaDistribution;
public class TwoPlayer {
    public static void main(String[] args) {
        BetaDistribution b = new BetaDistribution(1,.5);
        System.out.println(b.sample());
        Board board = new Board();
        Scanner input = new Scanner(System.in);
        while (!board.getGameover()) {
            board.display();
            try {
                if(!board.canMove()) {
                    do {
                        System.out.println("Choose a board.");
                    } while (!board.setBoard(input.nextInt(), input.nextInt()));
                }
                do {
                    System.out.println("It's " + board.getMarker()
                        +  "'s turn. Make a move on board " + board.getNextBoard());
                } while(!board.addMarker(input.nextInt(), input.nextInt()));
                input.nextLine();
            } catch (ArrayIndexOutOfBoundsException e) {
            System.out.println("ERROR! Invalid selection! \n");
            }
        }
        if (board.getDraw()) {
            System.out.println("It's a draw");
        } else {
            System.out.println("Player " + board.getMarker() + " has won.");
        }
    }
}
