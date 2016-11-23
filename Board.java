import java.util.ArrayList;
public class Board implements Cloneable {
    private boolean gameover;
    private boolean draw;
    private int[][] victories;
    private int[] nextboard;
    private int[][][][] board;
    private static int[] markers = {1, 2};
    private int marker;
    public Board() {
        gameover = false;
        victories = new int[][] {{0,0,0}, {0,0,0}, {0,0,0}};
        nextboard = new int[] {1,1};
        board = new int[3][3][][];
        for (int i =0; i < 3; i++) {
            for(int j = 0; j < 3; j++) {
                board[i][j] = new int[][] {{0,0,0}, {0,0,0}, {0,0,0}};
            }
        }
        marker = 1;
    }
    private Board(boolean gameover, int[][] victories, int[] nextboard,
        int[][][][] board, int marker){
            this();
            this.gameover = gameover;
            this.marker = marker;
            for (int i = 0; i < victories.length; i++) {
                for (int j = 0; j < victories[i].length; j++) {
                    this.victories[i][j] = victories[i][j];
                }
            }
            for (int i = 0; i < nextboard.length; i++) {
                this.nextboard[i] = nextboard[i];
            }
            for (int i = 0; i < board.length; i++) {
                for (int j = 0; j < board[i].length; j++) {
                    for (int k = 0; k < board[i][j].length; k++) {
                        for (int l = 0; l < board[i][j][k].length; l++) {
                            this.board[i][j][k][l] = board[i][j][k][l];
                        }
                    }
                }
            }
        }
    @Override
    public Board clone() {
        return new Board(gameover, victories, nextboard, board, marker);
    }
    public int getMarker() {
        return marker;
    }
    public boolean getDraw() {
        return draw;
    }
    public boolean getGameover() {
        return gameover;
    }
    public String getNextBoard() {
        return nextboard[0] + " , " + nextboard[1];
    }
    public boolean setBoard(int x, int y) {
        if (!canMove()) {
            nextboard = new int[] {x,y};
        }
        return canMove();
    }
    public boolean setBoard(int[] board) {
        if (board.length != 2) {
            throw new IllegalArgumentException("Invalid dimensions");
        }
        return setBoard(board[0], board[1]);
    }
    public boolean canMove() {
        if (victories[nextboard[0]][nextboard[1]] != 0) {
            return false;
        }
        for (int i = 0; i < board[nextboard[0]][nextboard[1]].length; i++) {
            for (int j = 0; j < board[nextboard[0]][nextboard[1]][i].length;
                j++) {
                if (board[nextboard[0]][nextboard[1]][i][j] == 0) {
                    return true;
                }
            }
        }
        return false;
    }
    public boolean legalMove(int w, int x, int y, int z) {
        int[] original = {nextboard[0], nextboard[1]};
        boolean out = setBoard(w, x) && legalMove(y, z);
        nextboard = original;
        return out;
    }
    public boolean legalMove(int x, int y) {
        return board[nextboard[0]][nextboard[1]][x][y] == 0;
    }
    public boolean addMarker(int x, int y) {
        if (legalMove(x,y)) {
            board[nextboard[0]][nextboard[1]][x][y] = marker;
            if (boardWin(board[nextboard[0]][nextboard[1]], x, y, marker)) {
                victories[nextboard[0]][nextboard[1]] = marker;
                totalWin(marker);
                if (gameover) {
                    return true;
                }
            }
            isDraw();
            nextboard = new int[] {x, y};
            marker = markers[marker % 2];
            return true;
        }
        return false;
    }
    public boolean addMarker(int[] move) {
        if (move.length != 2) {
            throw new IllegalArgumentException();
        }
        return addMarker(move[0], move[1]);
    }
    public void applyMove(int[][] move) {
        if(move.length != 2 || move[0].length != 2 || move[1].length != 2) {
            throw new IllegalArgumentException("Move has invalid dimensions.");
        }
        setBoard(move[0][0], move[0][1]);
        addMarker(move[1][0], move[1][1]);
    }
    private boolean boardWin(int[][] boardIn, int x, int y, int marker) {
        if (boardIn[x][(y + 1) % 3] == marker
            && boardIn[x][(3 + (y - 1) % 3) % 3] == marker) {
            return true;
        }
        if (boardIn[(x + 1) % 3][y] == marker
            && boardIn[(3 + (x - 1) % 3) % 3][y] == marker) {
            return true;
        }
        if (boardIn[0][0] == marker && boardIn[1][1] == marker
                && boardIn[2][2] == marker) {
                    return true;
        }
        if (boardIn[2][0] == marker && boardIn[1][1] == marker
                && boardIn[0][2] == marker) {

                    return true;
        }
        return false;
    }
    private void totalWin(int marker) {
        if (boardWin(victories, nextboard[0], nextboard[1], marker)) {
            gameover = true;
        }
    }
    private boolean isDraw() {
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                nextboard = new int[] {i, j};
                if (canMove()) {
                    return false;
                }
            }
        }
        draw = true;
        gameover = true;
        return true;
    }
    public ArrayList<int[][]> generateMoves() {
        ArrayList<int[][]> possibleMoves= new ArrayList<>();
        if (!canMove()) {
            for (int i = 0; i < 3; i++) {
                for(int j = 0; j < 3; j++) {
                    for(int k = 0; k < 3; k++) {
                        for(int l = 0; l < 3; l++) {
                            if (legalMove(i, j, k, l)) {
                                possibleMoves.add(new int[][] {{i, j}, {k, l}});
                            }
                        }
                    }
                }
            }
        } else {
            for (int i = 0; i < 3; i++) {
                for (int j = 0; j < 3; j++) {
                    if (legalMove(i, j)) {
                        possibleMoves.add(new int[][] {{-1, -1},{i, j}});
                    }
                }
            }
        }
        return possibleMoves;
    }
    public void display() {
        for (int i = 0; i < board.length; i++) {
            for (int j = 0; j < board[i][0].length; j++) {
                for (int k = 0; k < board[i].length; k++) {
                    for (int l = 0; l < board[i][k][j].length; l++) {
                        System.out.print(board[i][k][j][l]);
                    }
                    System.out.print(" ");
                }
                System.out.println();
            }
            System.out.println();
        }
    }
}
