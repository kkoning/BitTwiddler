package twiddler;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class Main extends Application {

  @Override
  public void start(Stage primaryStage) throws Exception {

    Parent root = FXMLLoader.load(getClass().getClassLoader().getResource("bit_twiddler.fxml"));
    root.setStyle("-fx-font-size: 11;");

//        Parent root = FXMLLoader.load(getClass().getResource("../../resources/bit_twiddler.fxml"));
    primaryStage.setTitle("Bit Twiddler");
    primaryStage.setScene(new Scene(root, 700, 450));
    primaryStage.setMinHeight(450);
    primaryStage.setMinWidth(600);
    primaryStage.show();
    primaryStage.requestFocus();
  }


  public static void main(String[] args) {
    launch(args);
  }
}
