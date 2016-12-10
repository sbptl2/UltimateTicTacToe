import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;

public class Board {
    public static class Move {
        private int boardRow;
        private int boardColumn;
        private int moveRow;
        private int moveColumn;
        private boolean winningMove;
        public Move(int[][] move) {
            this(move[0][0], move[0][1], move[1][0], move[1][1]);
            if (move.length != 2 || move[0].length != 2 || move[1].length != 2) {
                throw new IllegalArgumentException("Invalid move dimensions");
            }
        }
        public Move(int boardRow, int boardColumn, int moveRow, int moveColumn) {
            this.boardRow = boardRow;
            this.boardColumn = boardColumn;
            this.moveRow = moveRow;
            this.moveColumn = moveColumn;
        }
        private Move(int boardRow, int boardColumn, int moveRow, int moveColumn,
            boolean winningMove) {
            this(boardRow, boardColumn, moveRow, moveColumn);
            this.winningMove = winningMove;
        }
        public boolean getWinningMove() {
            return winningMove;
        }
        public Move deepCopy() {
            return new Move(boardRow, boardColumn, moveRow, moveColumn,
                winningMove);
        }
        public int[][] toArray() {
            return new int[][] {{boardRow, boardColumn}, {moveRow, moveColumn}};
        }
        @Override
        public boolean equals(Object o) {
            if (o == null) {
                return false;
            }
            if (o == this) {
                return true;
            }
            if (!(o instanceof Move)) {
                return false;
            }
            Move other = (Move) o;
            return (other.boardRow == this.boardRow) &&
                (other.boardColumn == this.boardColumn) &&
                (other.moveRow == this.moveRow) &&
                (other.moveColumn == this.moveColumn);
        }
        @Override
        public int hashCode() {
            int hash = boardRow;
            hash = 17*hash + boardColumn;
            hash = 17*hash + moveRow;
            hash = 17*hash + moveColumn;
            return hash;
        }
        @Override
        public String toString() {
            return boardRow + ", " + boardColumn + ", " + moveRow + ", " + moveColumn;
        }
    }

    private boolean gameover;
    private boolean draw;
    private int[][] victories;
    private int[] nextboard;
    private int[][][][] board;
    private static int[] markers = {1, 2};
    private int marker;
    private ArrayList<ArrayList<HashSet<Move>>> legalMoves = new
        ArrayList<ArrayList<HashSet<Move>>>();
    private ArrayList<ArrayList<ArrayList<HashSet<Move>>>> goodMoves = new
        ArrayList<ArrayList<ArrayList<HashSet<Move>>>>();

    public Board() {
        gameover = false;
        victories = new int[3][3];
        nextboard = new int[] {1,1};
        board = new int[3][3][3][3];
        goodMoves.add(new ArrayList<ArrayList<HashSet<Move>>>());
        goodMoves.add(new ArrayList<ArrayList<HashSet<Move>>>());
        for (int i = 0; i < 3; i++) {
            legalMoves.add(new ArrayList<HashSet<Move>>());
            goodMoves.get(0).add(new ArrayList<HashSet<Move>>());
            goodMoves.get(1).add(new ArrayList<HashSet<Move>>());
            for(int j = 0; j < 3; j++) {
                legalMoves.get(i).add(new HashSet<Move>());
                goodMoves.get(0).get(i).add(new HashSet<Move>());
                goodMoves.get(1).get(i).add(new HashSet<Move>());
            }
        }
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                for (int k = 0; k < 3; k++) {
                    for (int l = 0; l < 3; l++) {
                        legalMoves.get(i).get(j).add(new Move(i, j, k, l));
                        goodMoves.get(0).get(i).get(j).add(new Move(i, j, k, l));
                        goodMoves.get(1).get(i).get(j).add(new Move(i, j, k, l));
                    }
                }
            }
        }
        marker = 1;
    }
    private Board(boolean gameover, boolean draw, int[][] victories, int[]
        nextboard, int[][][][] board, int marker,
            ArrayList<ArrayList<HashSet<Move>>> legalMoves,
            ArrayList<ArrayList<ArrayList<HashSet<Move>>>> goodMoves) {
        this.gameover = gameover;
        this. draw = draw;
        this.marker = marker;
        this.victories = new int[3][3];
        this.nextboard = new int[] {1,1};
        this.board = new int[3][3][3][3];
        this.goodMoves.add(new ArrayList<ArrayList<HashSet<Move>>>());
        this.goodMoves.add(new ArrayList<ArrayList<HashSet<Move>>>());
        for (int i = 0; i < 3; i++) {
            this.legalMoves.add(new ArrayList<HashSet<Move>>());
            this.goodMoves.get(0).add(new ArrayList<HashSet<Move>>());
            this.goodMoves.get(1).add(new ArrayList<HashSet<Move>>());
            for(int j = 0; j < 3; j++) {
                this.legalMoves.get(i).add(new HashSet<Move>());
                this.goodMoves.get(0).get(i).add(new HashSet<Move>());
                this.goodMoves.get(1).get(i).add(new HashSet<Move>());
            }
        }
        for (int i = 0; i < 2; i++) {
            this.nextboard[i] = nextboard[i];
        }
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                this.victories[i][j] = victories[i][j];
                for (int k = 0; k < 3; k++) {
                    for (int l = 0; l < 3; l++) {
                        this.board[i][j][k][l] = board[i][j][k][l];
                    }
                }
            }
        }
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                for (Move move : legalMoves.get(i).get(j)) {
                    this.legalMoves.get(i).get(j).add(move.deepCopy());
                }
                for (Move move : goodMoves.get(0).get(i).get(j)) {
                    this.goodMoves.get(0).get(i).get(j).add(move.deepCopy());
                }
                for (Move move : goodMoves.get(1).get(i).get(j)) {
                    this.goodMoves.get(1).get(i).get(j).add(move.deepCopy());
                }
            }
        }
    }
    public Board deepCopy() {
        return new Board(gameover, draw, victories, nextboard, board, marker ,
            legalMoves, goodMoves);
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
    public int[] getNextBoard() {
        return nextboard;
    }
    public boolean canMove() {
        return !legalMoves.get(nextboard[0]).get(nextboard[1]).isEmpty();
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
    private void checkDraw() {
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                if (!legalMoves.get(i).get(j).isEmpty()) {
                    return;
                }
            }
        }
        draw = true;
        gameover = true;
    }
    public boolean legalMove(Move move) {
        if (canMove()) {
            return legalMoves.get(move.boardRow).get(move.boardColumn).contains(move)
                && (move.boardRow == nextboard[0])
                && (move.boardColumn == nextboard[1]);
        } else {
            return board[move.boardRow][move.boardColumn][move.moveRow][move.moveColumn] == 0
                && !legalMoves.get(move.boardRow).get(move.boardColumn).isEmpty();
        }
    }
    public boolean applyMove(Move move) {
        if (legalMove(move)) {
            board[move.boardRow][move.boardColumn][move.moveRow]
                [move.moveColumn] = marker;
            legalMoves.get(move.boardRow).get(move.boardColumn).remove(move);
            goodMoves.get(0).get(move.boardRow).get(move.boardColumn).remove(move);
            goodMoves.get(1).get(move.boardRow).get(move.boardColumn).remove(move);
            if (boardWin(board[move.boardRow][move.boardColumn], move.moveRow,
                move.moveColumn, marker)) {
                legalMoves.get(move.boardRow).get(move.boardColumn).clear();
                goodMoves.get(0).get(move.boardRow).get(move.boardColumn).clear();
                goodMoves.get(1).get(move.boardRow).get(move.boardColumn).clear();
                victories[move.boardRow][move.boardColumn] = marker;
                totalWin(marker);
                if (gameover) {
                    return true;
                }
                refreshWinningMoves();
            }
            checkDraw();
            nextboard = new int[] {move.moveRow, move.moveColumn};
            marker = markers[marker % 2];
            return true;
        }
        return false;
    }
    private boolean updateWinningMove(Move move) {
        if (gameover || move.winningMove) {
            return false;
        }
        if (legalMove(move)) {
            board[move.boardRow][move.boardColumn][move.moveRow]
                [move.moveColumn] = marker;
            if (boardWin(board[move.boardRow][move.boardColumn], move.moveRow,
                move.moveColumn, marker)) {
                victories[move.boardRow][move.boardColumn] = marker;
                totalWin(marker);
                if (gameover) {
                    move.winningMove = true;
                    gameover = false;
                }
                victories[move.boardRow][move.boardColumn] = 0;
            }
            board[move.boardRow][move.boardColumn][move.moveRow]
                [move.moveColumn] = 0;
        }
        return move.winningMove;
    }
    private void refreshWinningMoves() {
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                for (Move move : goodMoves.get(marker % 2).get(i).get(j)) {
                    updateWinningMove(move);
                }
            }
        }
    }
    public int[][] getVictories() {
        return victories;
    }
    public HashSet<Move> getGoodMoves() {
        HashSet<Move> moves = new HashSet<>();
        if (canMove()) {
            for (Move move : goodMoves.get(marker % 2).get(nextboard[0]).get(nextboard[1])) {
                if (move.getWinningMove()) {
                    Move goodMove = move;
                    moves.clear();
                    moves.add(goodMove);
                    break;
                }
                moves.add(move);
            }
            return moves;
        } else {
            outerloop:
            for (int i = 0; i < 3; i++) {
                for (int j = 0; j < 3; j++) {
                    for (Move move : goodMoves.get(marker % 2).get(i).get(j)) {
                        if (move.getWinningMove()) {
                            Move goodMove = move;
                            moves.clear();
                            moves.add(goodMove);
                            break outerloop;
                        }
                        moves.add(move);
                    }
                }
            }
            return moves;
        }
    }
    public void display() {
       System.out.println();
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
