public class Board implements Cloneable {
    private boolean gameover;
    private int[][] victories;
    private int[] nextboard;
    private int[][][][] board;
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
    }
    private Board(boolean gameover, int[][] victories, int[] nextboard,
        int[][][][] board){
            this.gameover = gameover;
            this.victories = victories;
            this.nextboard = nextboard;
            this.board = board;
        }
    @Override
    public Board clone() {
        return new Board(gameover, victories, nextboard, board);
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
    public boolean setBoard(int x, int y) {
        nextboard = new int[] {x,y};
        return canMove();
    }
    public boolean addMarker(int x, int y, int marker) {
        if (marker != 0 && legalMove(x,y)) {
            board[nextboard[0]][nextboard[1]][x][y] = marker;
            if (boardWin(board[nextboard[0]][nextboard[1]], x, y, marker)) {
                victories[nextboard[0]][nextboard[1]] = marker;
                totalWin(marker);
            }
            nextboard = new int[] {x, y};
            return true;
        }
        return false;
    }
    public boolean legalMove(int x, int y) {
        return board[nextboard[0]][nextboard[1]][x][y] == 0;
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
    public boolean getGameover() {
        return gameover;
    }
    public String getNextBoard() {
        return nextboard[0] + " , " + nextboard[1];
    }
}
