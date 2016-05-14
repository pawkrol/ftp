package pl.pawkrol.academic.ftp.server;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import pl.pawkrol.academic.ftp.server.connection.ConnectionManager;
import pl.pawkrol.academic.ftp.server.db.DBConnector;

import java.nio.file.Path;
import java.nio.file.Paths;

public class Main extends Application {

    public static final Path rootPath = //Paths.get(".").toAbsolutePath();
                                        Paths.get("/home/pawkrol/Documents/ftp_test").toAbsolutePath();
    private ConnectionManager connectionManager;

    @Override
    public void start(Stage primaryStage) throws Exception{
        FXMLLoader fxmlLoader = new FXMLLoader();
        Parent root = fxmlLoader.load(getClass().getResource("/server_layout.fxml").openStream());
        Controller controller = fxmlLoader.getController();

        DBConnector dbConnector = new DBConnector();
        connectionManager = new ConnectionManager(2121, 4, dbConnector);
        controller.setConnectionManager(connectionManager);

        primaryStage.setTitle("FTPer Server");
        primaryStage.setScene(new Scene(root));
        primaryStage.setResizable(false);
        primaryStage.show();
    }

    @Override
    public void stop() throws Exception {
        connectionManager.close();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
