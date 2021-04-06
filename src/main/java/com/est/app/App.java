package com.est.app;

import com.est.app.utils.FxmlUtils;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

/**
 * @author selami
 */
public class App extends Application {

    @Override
    public void start(final Stage primaryStage) throws IOException {
        FxmlUtils.load(primaryStage, "fxml/main.fxml", null);
    }

    /**
     * @param args the command line arguments
     */
    public static void main(final String[] args) {
        Application.launch(args);
    }
}
