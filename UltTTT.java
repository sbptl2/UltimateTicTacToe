import javafx.application.Application;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.scene.control.Button;
import javafx.scene.layout.VBox;
import javafx.scene.control.Slider;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.StackPane;
import javafx.geometry.Pos;
import javafx.scene.layout.HBox;
import javafx.scene.control.Label;

public class UltTTT extends Application {
    public static boolean extremeAi;

    public void start(Stage primaryStage) {
        Image background = new Image("File:background.png");
        ImageView back = new ImageView(background);
        VBox buttons = new VBox(10);
        buttons.setAlignment(Pos.CENTER);
        Button onePlayer = new Button("One Player");
        onePlayer.setOnAction(e -> {
            Slider difficulty = new Slider(1.0, 15.0, 1.0);
            Label sliderLabel = new Label("AI difficulty");
            HBox difficultyBox = new HBox(10);
            difficultyBox.setAlignment(Pos.CENTER);
            difficultyBox.getChildren().addAll(sliderLabel, difficulty);
            difficulty.setMaxWidth(150);
            Button playerOne = new Button("Play as player one");
            playerOne.setOnAction(event -> {
                BoardFX.setAi(2, difficulty.getValue());
                if (15.0 - difficulty.getValue() < .0001) {
                    extremeAi = true;
                }
                primaryStage.setScene(new Scene(BoardFX.getInstance()));
            });
            Button playerTwo = new Button("Play as player two");
            playerTwo.setOnAction(event -> {
                BoardFX.setAi(1, difficulty.getValue());
                if (15.0 - difficulty.getValue() < .0001) {
                    extremeAi = true;
                }
                primaryStage.setScene(new Scene(BoardFX.getInstance()));
                MiniBoard.getAi().makeMove();
                MiniBoard.getAi().displayMove();
            });
            buttons.getChildren().clear();
            buttons.getChildren().addAll(playerOne, playerTwo, difficultyBox);
        });
        Button twoPlayer = new Button("Two Player");
        twoPlayer.setOnAction(e -> {
            primaryStage.setScene(new Scene(BoardFX.getInstance()));
        });
        buttons.getChildren().addAll(onePlayer, twoPlayer);
        StackPane screen = new StackPane();
        screen.getChildren().addAll(back, buttons);
        screen.setAlignment(Pos.BOTTOM_CENTER);
        Scene root = new Scene(screen);
        primaryStage.setScene(root);
        primaryStage.show();
    }
    public static void main(String[] args) {
        launch(args);
    }
    public static boolean getExtreme() {
        return extremeAi;
    }
}
