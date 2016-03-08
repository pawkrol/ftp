package pl.pawkrol.academic.ftp.server;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import pl.pawkrol.academic.ftp.server.connection.ConnectionManager;

public class Main extends Application {

    private ConnectionManager connectionManager;

    @Override
    public void start(Stage primaryStage) throws Exception{
        FXMLLoader fxmlLoader = new FXMLLoader();
        Parent root = fxmlLoader.load(getClass().getResource("resources/layout.fxml").openStream());
        Controller controller = fxmlLoader.getController();

        connectionManager = new ConnectionManager(2121);
        controller.setConnectionManager(connectionManager);

        primaryStage.setTitle("FTPer Server");
        primaryStage.setScene(new Scene(root));
        primaryStage.setResizable(false);
        primaryStage.show();
    }

    @Override
    public void stop() throws Exception {
        connectionManager.stop();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
