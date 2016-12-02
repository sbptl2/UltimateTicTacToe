import java.util.Scanner;
public class OnePlayer {
    public static Board board = new Board();
    public static Scanner input = new Scanner(System.in);
    public static Ai bot;
    public static void main(String[] args) {
        System.out.println("Would you like to be: \n1: Player 1 \n" +
            "2: Player 2");
        System.out.println("Please enter 1 or 2 to select your choice.");
        switch (input.nextInt()) {
            case 1: bot = new Ai(2, board, 1);
                playerOne();
                break;
            case 2: bot = new Ai(1, board, 1);
                playerTwo();
                break;
        }
    }
    public static void playerOne() {
        while (!board.getGameover()) {
            playerTurn();
            if (board.getGameover()) {
                break;
            }
            bot.makeMove();
        }
        gameover();
    }
    public static void playerTwo() {
        while (!board.getGameover()) {
            bot.makeMove();
            if (board.getGameover()) {
                break;
            }
            playerTurn();
        }
        gameover();
    }
    public static void playerTurn() {
        int playerMarker = board.getMarker();
        int[][] opponentMove = {board.getNextBoard(), {}};
        while (board.getMarker() == playerMarker && !board.getGameover()) {
            try {
                board.display();
                if(!board.canMove()) {
                    do {
                        System.out.println("Choose a board.");
                        opponentMove[0] = new int[] {input.nextInt(), input
                            .nextInt()};
                    } while (!board.setBoard(opponentMove[0]));
                }
                do {
                    System.out.println("It's " + board.getMarker()
                        +  "'s turn. Make a move on board " + board
                            .printNextBoard());
                    opponentMove[1] = new int[] {input.nextInt(), input
                            .nextInt()};
                } while(!board.addMarker(opponentMove[1]));
                input.nextLine();
            } catch (ArrayIndexOutOfBoundsException e) {
            System.out.println("ERROR! Invalid selection! \n");
            }
        }
        bot.updateRoot(opponentMove);
    }
    public static void gameover() {
        if (board.getDraw()) {
            System.out.println("It's a draw!");
        } else {
        System.out.println("Player " + board.getMarker() + " has won.");
        }
    }
}
