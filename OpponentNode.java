public class OpponentNode extends MonteCarloTreeNode {
    public OpponentNode(int alpha, int beta, Board.Move move) {
        super(alpha, beta, move);
    }
    @Override
    protected PlayerNode generateChild(Board.Move move) {
        return new PlayerNode(1, 1, move);
    }
    protected PlayerNode selectChild() {
        double minValue = getChildren().get(0).sampleBetaDist();
        double value;
        int selected = 0;
        for(int i = 1; i < super.getChildren().size(); i++) {
            value = super.getChildren().get(i).sampleBetaDist();
            if(value < minValue) {
                minValue = value;
                selected = i;
            }
        }
        return (PlayerNode) super.getChildren().get(selected);
    }
}
