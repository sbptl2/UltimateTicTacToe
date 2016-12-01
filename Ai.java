import java.util.Arrays;
public class Ai {
    Board board;
    long timeLimit;
    PlayerNode root;
    OpponentNode chosenChild;
    public Ai(int marker, Board board, double timeLimit) {
        MonteCarloTreeNode.setMarker(marker);
        root = new PlayerNode(1, 1, new int[][] {{-1 ,-1},{0,0}});
        root.populate(board);
        chosenChild = new OpponentNode(1, 1, new int[][] {{-1,-1},{0,0}});
        this.board = board;
        this.timeLimit = (long) (timeLimit*1000);
    }
    public void makeMove() {
        long timer = System.currentTimeMillis();
        while (System.currentTimeMillis() - timer < timeLimit) {
            root.trial(board.clone());
        }
        chosenChild = root.maxNode();
        chosenChild.applyMove(board);
    }
    public void updateRoot(int[][] opponentMove) {
        root = new PlayerNode(1, 1, opponentMove);
        for (MonteCarloTreeNode child : chosenChild.getChildren()) {
            if (Arrays.deepEquals(child.getMove(), opponentMove)) {
                root = (PlayerNode) child;
                break;
             }
        }
        root.populate(board);
    }
}
