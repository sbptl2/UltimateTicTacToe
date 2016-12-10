import java.util.Arrays;
public class Ai {
    private Board board;
    private long timeLimit;
    private PlayerNode root;
    private OpponentNode chosenChild;
    private int marker;
    private MoveThread trialThread = new MoveThread();

    private class MoveThread extends Thread {
        Thread thread;
        public MoveThread() {
            thread = this;
        }
        public void run() {
            long timer = System.currentTimeMillis();
            while (System.currentTimeMillis() - timer < 120000 && thread == this) {
                root.trial(board.deepCopy());
            }
        }
        public void kill() {
            thread = null;
        }
    }

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
        long timer = System.currentTimeMillis();
        while (System.currentTimeMillis() - timer < timeLimit) {
            root.trial(board.deepCopy());
        }
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
    public Thread getTrialThread() {
        return trialThread;
    }
    public void stopTrialThread() {
        trialThread.kill();
        trialThread = new MoveThread();
    }
}
