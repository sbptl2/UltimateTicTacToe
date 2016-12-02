import javafx.scene.layout.GridPane;
import javafx.geometry.Insets;
import javafx.scene.text.Text;
import javafx.scene.layout.StackPane;
import javafx.geometry.Pos;
public class BoardFX extends GridPane{
    private static Board board = new Board();
    private static MiniBoard[][] bigBoard = new MiniBoard[3][3];
    private static BoardFX instance = new BoardFX();
    private static boolean twoPlayer = true;
    private BoardFX() {
        this.setHgap(30);
        this.setVgap(30);
        this.setAlignment(Pos.CENTER);
        board = new Board();
        MiniBoard.setBoard(board);
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                MiniBoard miniBoard = new MiniBoard(i, j);
                if (i == 1 && j == 1) {
                    miniBoard.setStyle("-fx-background-color: red");
                }
                this.setMargin(miniBoard, new Insets(10));
                bigBoard[i][j] = miniBoard;
                this.add(miniBoard, j, i);
            }
        }
    }
    public static BoardFX getInstance() {
        return instance;
    }
    public static void refresh() {
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                bigBoard[i][j].setStyle("-fx-background-color:white");
            }
        }
        if (board.canMove()) {
            bigBoard[board.getNextBoard()[0]][board.getNextBoard()[1]]
                .setStyle("-fx-background-color:red");
        }
        reinstallize();
    }
    private static void reinstallize() {
        double width = instance.getChildren().get(0).getBoundsInParent().getWidth();
        double height = instance.getChildren().get(0).getBoundsInParent().getHeight();
        instance.getChildren().clear();
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                StackPane pane = new StackPane();
                pane.setMaxSize(width, height);
                pane.getChildren().add(bigBoard[i][j]);
                switch (board.getVictories()[i][j]) {
                    case 1: Text marker = new Text("X");
                        pane.getChildren().clear();
                        marker.setStyle("-fx-font-size:100");
                        pane.getChildren().add(marker);
                        break;
                    case 2: marker = new Text("O");
                        pane.getChildren().clear();
                        marker.setStyle("-fx-font-size:100");
                        pane.getChildren().add(marker);
                        break;
                }
                instance.setMargin(pane, new Insets(10));
                instance.add(pane, j, i);
            }
        }
    }
    public static void setTile(int[][] coord, int marker) {
        bigBoard[coord[0][0]][coord[0][1]].getTile(coord[1][0], coord[1][1])
            .setMarker(marker);
        refresh();
    }
    public static boolean getTwoPlayer() {
        return twoPlayer;
    }
    public static void setAi(int marker, double timeLimit) {
        twoPlayer = false;
        Ai ai = new Ai(marker, board, timeLimit);
        MiniBoard.setAi(ai);
    }
    public static void disableClick() {
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                bigBoard[i][j].disableClick();
            }
        }
    }
    public static void enableClick() {
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                bigBoard[i][j].enableClick();
            }
        }
    }
}
