import javafx.scene.layout.GridPane;
import javafx.application.Platform;
public class MiniBoard extends GridPane {
    private int row;
    private int column;
    private Tile[][] tiles = new Tile[3][3];
    private static Board board;
    private static Ai ai;
    public MiniBoard(int row, int column) {
        this.row = row;
        this.column = column;
        this.setGridLinesVisible(true);
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                Tile tile = new Tile(i, j);
                tiles[i][j] = tile;
                this.add(tile,j,i);
            }
        }
        this.enableClick();
    }
    public static void setBoard(Board b) {
        board = b;
    }
    public static void setAi(Ai computer) {
        ai = computer;
    }
    public static Ai getAi() {
        return ai;
    }
    public Tile getTile(int row, int column) {
        return tiles[row][column];
    }
    private int clickEvent(int row, int column) {
        if (board.getGameover()) {
            return 0;
        }
        Board.Move move = new Board.Move(this.row, this.column, row, column);
        if (board.legalMove(move)) {
            int marker = board.getMarker();
            board.applyMove(move);
            BoardFX.refresh();
            return marker;
        }
        return 0;
    }
    public void disableClick() {
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                tiles[i][j].setOnMouseClicked(null);
            }
        }
    }
    public void enableClick() {
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                Tile tile = tiles[i][j];
                tile.setOnMouseClicked(e -> {
                    tile.setMarker(clickEvent(tile.getRow(), tile.getColumn()));
                    if (!BoardFX.getTwoPlayer() && board.getMarker()
                        == ai.getMarker()) {
                        BoardFX.disableClick();
                        if (UltTTT.getExtreme()) {
                            ai.stopTrialThread();
                        }
                        ai.updateRoot(new Board.Move(this.row, this.column,
                            tile.getRow(), tile.getColumn()));
                        Thread thread = new Thread() {
                            public void run() {
                                ai.makeMove();
                                BoardFX.enableClick();
                            }
                        };
                        try {
                            thread.sleep(50);
                        } catch (InterruptedException excep) {
                            excep.printStackTrace();
                        }
                        thread.start();
                        Platform.runLater(() -> {
                            try {
                                thread.join();
                            } catch (InterruptedException excep) {
                                excep.printStackTrace();
                            }
                            ai.displayMove();
                            System.gc();
                            try {
                                ai.getTrialThread().sleep(100);
                            } catch (InterruptedException excep) {
                                excep.printStackTrace();
                            }
                            if (UltTTT.getExtreme()) {
                                ai.getTrialThread().start();
                            }
                        });
                    }
                });
            }
        }
    }
}
