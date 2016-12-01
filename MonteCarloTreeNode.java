import java.util.Random;
import java.util.ArrayList;
import java.util.Arrays;
import org.apache.commons.math3.distribution.BetaDistribution;
public abstract class MonteCarloTreeNode {
    private ArrayList<MonteCarloTreeNode> children = new
        ArrayList<MonteCarloTreeNode>(0);
    private boolean visited;
    private boolean childrenVisited;
    private double alpha;
    private double beta;
    private int[][] move;
    private static int myMarker;
    public MonteCarloTreeNode(int alpha, int beta, int[][] move) {
        this.alpha = alpha;
        this.beta = beta;
        this.move = move;
    }
    protected double getAlpha() {
        return alpha;
    }
    protected double getBeta() {
        return beta;
    }
    private void updateAlphaBeta(double[] increment) {
        alpha += increment[0];
        beta += increment[1];
    }
    public int[][] getMove() {
        return move;
    }
    public static void setMarker(int marker) {
        myMarker = marker;
    }
    private boolean visitedAll() {
        if (childrenVisited) {
            return true;
        }
        for (MonteCarloTreeNode child : children) {
            if (!child.visited) {
                return false;
            }
        }
        childrenVisited = true;
        return true;
    }
    private double[] randomlyPlay(Board board) {
        Random rand = new Random();
        while (!board.getGameover()) {
                ArrayList<int[][]> moveSet = board.generateMoves();
                int[][] move = moveSet.get(rand.nextInt(moveSet.size()));
                board.applyMove(move);
            }
        if (board.getDraw()) {
            return new double[] {.5, .5};
        } else if (board.getMarker() == myMarker) {
            return new double[] {1, 0};
        } else {
            return new double[] {0, 1};
        }
    }
    protected abstract MonteCarloTreeNode generateChild(int[][] move);
    protected abstract MonteCarloTreeNode selectChild();
    public void populate(Board board) {
        if (children.size() == 0) {
             ArrayList<int[][]> moveSet = board.generateMoves();
             for (int[][] move : moveSet) {
                 children.add(generateChild(move));
             }
         }
    }
    protected double sampleBetaDist() {
        return (new BetaDistribution(getAlpha(), getBeta())).sample();
    }
    public void applyMove(Board board) {
        board.applyMove(move);
    }
    public ArrayList<MonteCarloTreeNode> getChildren() {
        return children;
    }
    public double[] trial(Board board) {
        double[] update;
        if (children.size() == 0) {
            update = this.randomlyPlay(board);
        } else {
            MonteCarloTreeNode node = selectChild();
            node.applyMove(board);
            if (node.visited) {
                node.populate(board);
            }
            node.visited = true;
            update = node.trial(board);
        }
        updateAlphaBeta(update);
        return update;
    }
}
