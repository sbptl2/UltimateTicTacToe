public class PlayerNode extends MonteCarloTreeNode {
    public PlayerNode(int alpha, int beta, int[][] move) {
        super(alpha, beta, move);
    }
    @Override
    protected OpponentNode generateChild(int move[][]) {
        return new OpponentNode(1, 1, move);
    }
    protected OpponentNode selectChild() {
        double maxValue = getChildren().get(0).sampleBetaDist();
        double value;
        int selected = 0;
        for(int i = 1; i < super.getChildren().size(); i++) {
            value = super.getChildren().get(i).sampleBetaDist();
            if(value > maxValue) {
                maxValue = value;
                selected = i;
            }
        }
        return (OpponentNode) super.getChildren().get(selected);
    }
    public OpponentNode maxNode() {
        double maxValue = getChildren().get(0).getAlpha() + getChildren().get(0).getBeta();
        double value = 0;
        int selected = 0;
        for(int i = 1; i < getChildren().size(); i++) {
            value = getChildren().get(i).getAlpha() + getChildren().get(i).getBeta();
            if(value > maxValue) {
                maxValue = value;
                selected = i;
            }
        }
        System.out.println(value);
        return (OpponentNode) super.getChildren().get(selected);
    }
}
