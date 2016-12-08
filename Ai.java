import java.util.Arrays;
public class Ai {
    private Board board;
    private long timeLimit;
    private PlayerNode root;
    private OpponentNode chosenChild;
    private int marker;
    public Ai(int marker, Board board, double timeLimit) {
        this.marker = marker;
        MonteCarloTreeNode.setMarker(marker);
        root = new PlayerNode(1, 1, new Board.Move(-1, -1, 0, 0));
        root.populate(board);
        chosenChild = new OpponentNode(1, 1, new Board.Move(-1, -1, 0, 0));
        this.board = board;
        this.timeLimit = (long) (timeLimit*1000);
    }
    public int getMarker() {
        return marker;
    }
    public void makeMove() {
        if (board.getGameover()) {
            throw new RuntimeException();
        }
        int iters = 0;
        long timer = System.currentTimeMillis();
        while (System.currentTimeMillis() - timer < timeLimit) {
            root.trial(board.deepCopy());
            iters++;
        }
        System.out.println(iters);
        chosenChild = root.maxNode();
        chosenChild.applyMove(board);
    }
    public void updateRoot(Board.Move opponentMove) {
        root = new PlayerNode(1, 1, opponentMove);
        for (MonteCarloTreeNode child : chosenChild.getChildren()) {
            if (child.getMove().equals(opponentMove)) {
                root = (PlayerNode) child;
                break;
             }
        }
        root.populate(board);
    }
    public void displayMove() {
        BoardFX.setTile(chosenChild.getMove().toArray(), marker);
    }
}
