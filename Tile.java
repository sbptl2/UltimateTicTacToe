import javafx.scene.layout.StackPane;
import javafx.scene.control.Label;
import javafx.geometry.Pos;
public class Tile extends StackPane {
    private Label marker = new Label();
    private int row;
    private int column;
    public Tile(int row, int column) {
        this.row = row;
        this.column = column;
        this.setPrefSize(50, 50);
        this.setAlignment(marker, Pos.CENTER);
        this.getChildren().add(marker);
    }
    public void setMarker(int n) {
        switch (n) {
            case 1: marker.setText("X");
                break;
            case 2: marker.setText("O");
                break;
        }
    }
    public int getRow() {
        return row;
    }
    public int getColumn() {
        return column;
    }
}
