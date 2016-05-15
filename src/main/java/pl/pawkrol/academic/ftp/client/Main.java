package pl.pawkrol.academic.ftp.client;

/**
 * Created by pawkrol on 4/24/16.
 */

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class Main extends Application {

    private Controller controller;

    @Override
    public void start(Stage primaryStage) throws Exception{
        FXMLLoader fxmlLoader = new FXMLLoader();
        Parent root = fxmlLoader.load(getClass().getResource("/client_layout.fxml").openStream());
        controller = fxmlLoader.getController();

        primaryStage.setTitle("FTPer Client");
        primaryStage.setScene(new Scene(root));
        primaryStage.setResizable(false);
        primaryStage.show();
    }

    @Override
    public void stop() throws Exception {
        controller.getConnectionManager().kill();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
