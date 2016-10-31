import java.util.Scanner;
public class GameTester {
    public static void main(String[] args) {
        Board board = new Board();
        Scanner input = new Scanner(System.in);
        int turn = -1;
        int[] markers = {1,2};
        while (!board.getGameover()) {
            turn++;
            board.display();
            try {
                if(!board.canMove()) {
                    do {
                        System.out.println("Choose a board.");
                    } while (!board.setBoard(input.nextInt(), input.nextInt()));
                }
                do {
                    System.out.println("It's " + markers[turn % 2]
                        +  "'s turn. Make a move on board " + board.getNextBoard());
                } while(!board.addMarker(input.nextInt(), input.nextInt(),
                    markers[turn % 2]));
                input.nextLine();
        } catch (ArrayIndexOutOfBoundsException e) {
            System.out.println("ERROR! Invalid selection! \n");
        }
    }
    }
}
