package com.est.app.utils;

import javafx.beans.property.BooleanProperty;
import javafx.beans.value.ChangeListener;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.Region;
import javafx.scene.layout.TilePane;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.Window;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.InputStream;
import java.net.URL;
import java.util.MissingResourceException;
import java.util.Optional;
import java.util.ResourceBundle;

import static java.util.ResourceBundle.getBundle;

public class FxmlUtils {

    private static Logger logger = LoggerFactory.getLogger(FxmlUtils.class);

    public static FXMLLoader getLoader(String fxmlPath){
        //URL resource = getClass().getClassLoader().getResource(fxmlPath);
        URL resource = FxmlUtils.class.getClassLoader().getResource(fxmlPath);
        String bundleName = fxmlPath.replace(".fxml", "");
        if(bundleName.startsWith("/"))
            bundleName = bundleName.replaceFirst("/", "");
        ResourceBundle bundle = null;
        FXMLLoader fxmlLoader;
        try {
            bundle = getBundle(bundleName);
        } catch (MissingResourceException ex) {
        }
        if(bundle == null)
        {
            fxmlLoader = new FXMLLoader(resource);
        }else
        {
            fxmlLoader = new FXMLLoader(resource, bundle);
        }
        return  fxmlLoader;
    }

    public static Parent load(Stage stage, String fxmlPath, String title)
    {
        return load(stage, fxmlPath, title, null);
    }

    public static Parent load(Stage stage, String fxmlPath, String title, Object parameter){
        FXMLLoader fxmlLoader = getLoader(fxmlPath);
        //fxmlLoader.setControllerFactory(context::getBean);
        try {
            Parent root = fxmlLoader.load();
            if(parameter != null && fxmlLoader.getController() instanceof ParametrizedController)
            {
                ((ParametrizedController) fxmlLoader.getController()).setParameter(parameter);
            }

            //String css = FxmlService.class.getResource("/fxml/masterSkin.css").toExternalForm();
            //String css = FxmlService.class.getResource("/fxml/bootstrap.css").toExternalForm();
            //String css = FxmlService.class.getResource("/fxml/win7.css").toExternalForm();
            //String css = FxmlService.class.getResource("/fxml/buttonPlayImage.css").toExternalForm();
            String css = FxmlUtils.class.getResource("/fxml/app.css").toExternalForm();

            if (stage != null) {
                if (title != null) {
                    stage.setTitle(title);
                }
                Scene scene = new Scene(root);
                scene.getStylesheets().clear();
                scene.getStylesheets().add(css);
                //Image image = new Image("/images/icon_logo.png");
                //stage.getIcons().add(image);
                stage.setScene(scene);
                stage.show();
            }else
            {
                root.getStylesheets().clear();
                root.getStylesheets().add(css);
            }
            return root;
        } catch (Exception e){
            logger.error("Error FXML", e);
            return null;
        }
    }



    public Parent load(String fxmlPath){
        return load(null, fxmlPath, null, null);
    }

    public Parent loadWithParameter(String fxmlPath, Object parameter){
        return load(null, fxmlPath, null, parameter);
    }

    public static Parent load(String fxmlPath, Object controller)
    {
        FXMLLoader fxmlLoader = getLoader(fxmlPath);
        if(controller != null)
            fxmlLoader.setController(controller);
        InputStream inputStream = null;
        try {
            inputStream = FxmlUtils.class.getResourceAsStream(fxmlPath);
            Parent root = fxmlLoader.load(inputStream);
            //new JMetro(JMetro.Style.LIGHT).applyTheme(root);
            //AquaFx.style();

            return root;
        } catch (Exception e) {
            LoggerFactory.getLogger(FxmlUtils.class).error("cannot " + fxmlPath, e);
        } finally {
            if (inputStream != null) {
                try {
                    inputStream.close();
                } catch (Exception e) {}
            }
        }
        return null;
    }

    public static void alert(String header, String content, Window owner) {
        Alert alert = new Alert(Alert.AlertType.WARNING);
        alert.setTitle("Warning");
        alert.setHeaderText(header);
        alert.setContentText(content);
        if (owner == null) {
            alert.initModality(Modality.APPLICATION_MODAL);
        } else {
            alert.initOwner(owner);
            alert.initModality(Modality.WINDOW_MODAL);
        }

        alert.showAndWait();
    }

    public static boolean alertConfirmation(String header, String content, Window owner) {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Confirmation");
        alert.setHeaderText(header);
        alert.setContentText(content);
        alert.getDialogPane().setMinHeight(Region.USE_COMPUTED_SIZE);
        if (owner == null) {
            alert.initModality(Modality.APPLICATION_MODAL);
        } else {
            alert.initOwner(owner);
            alert.initModality(Modality.WINDOW_MODAL);
        }

        Optional<ButtonType> result = alert.showAndWait();
        return result.get() == ButtonType.OK;
    }

    public static boolean alertConfirmation(String header, Node parent, Window owner) {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Confirmation");
        alert.setHeaderText(header);
        alert.getDialogPane().setContent(parent);
        alert.getDialogPane().setMinHeight(Region.USE_PREF_SIZE);
        //alert.getDialogPane().getChildren().stream().filter(node -> node instanceof Label).forEach(node -> ((Label)node).setMinHeight(Region.USE_PREF_SIZE));
        //alert.setGraphic(parent);
        if (owner == null) {
            alert.initModality(Modality.APPLICATION_MODAL);
        } else {
            alert.initOwner(owner);
            alert.initModality(Modality.WINDOW_MODAL);
        }

        Optional<ButtonType> result = alert.showAndWait();
        return result.get() == ButtonType.OK;
    }

    public static boolean alertConfirmation(String header, Node parent, double width, double height, Window owner, boolean resizable) {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Confirmation");
        alert.setHeaderText(header);
        alert.getDialogPane().setContent(parent);
        //alert.setGraphic(parent);
        alert.setResizable(resizable);
        //alert.getDialogPane().setPrefSize(width, height);
        if(width > 0) alert.getDialogPane().setPrefWidth(width);
        if(height > 0) alert.getDialogPane().setPrefHeight(height);
        if (owner == null) {
            alert.initModality(Modality.APPLICATION_MODAL);
        } else {
            alert.initOwner(owner);
            alert.initModality(Modality.WINDOW_MODAL);
        }

        Optional<ButtonType> result = alert.showAndWait();
        return result.get() == ButtonType.OK;
    }

    public static boolean alertConfirmation(String title, String header, Node parent, double width, double height, Window owner, boolean resizable) {
        Alert alert = new Alert(header == null ? Alert.AlertType.NONE : Alert.AlertType.CONFIRMATION);
        if(header == null){
            alert = new Alert(Alert.AlertType.NONE, "", ButtonType.OK, ButtonType.CANCEL);
            parent.setTranslateX(-10);
        }else{
            alert = new Alert(Alert.AlertType.CONFIRMATION);
        }

        alert.setTitle(title);
        alert.setHeaderText(header);
        alert.getDialogPane().setContent(parent);
        //alert.setGraphic(parent);
        alert.setResizable(resizable);
        //alert.getDialogPane().setPrefSize(width, height);
        if(resizable){
            alert.getDialogPane().setMinWidth(width);
            alert.getDialogPane().setMinHeight(height);
        }else
        {
            alert.getDialogPane().setPrefWidth(width);
            alert.getDialogPane().setPrefHeight(height);
            alert.getDialogPane().setMaxWidth(width);
            alert.getDialogPane().setMaxHeight(height);
        }
        //if(width > 0) alert.getDialogPane().setPrefWidth(width);
        //if(height > 0) alert.getDialogPane().setPrefHeight(height);
        if (owner == null) {
            alert.initModality(Modality.APPLICATION_MODAL);
        } else {
            alert.initOwner(owner);
            alert.initModality(Modality.WINDOW_MODAL);
        }

        Optional<ButtonType> result = alert.showAndWait();
        return result.get() == ButtonType.OK;
    }

    public static boolean alertConfirmationTest(String header, Node parent, Window owner) {

        Dialog<Window> dialog = new Dialog<>();
        try {
            dialog.setTitle(header);
            //dialog.getDialogPane().getButtonTypes().addAll(ButtonType.OK, ButtonType.CANCEL);
            dialog.getDialogPane().setContent(parent);
            parent.setTranslateX(10);
            dialog.setResizable(true);
            if (owner == null) {
                dialog.initModality(Modality.APPLICATION_MODAL);
            } else {
                dialog.initOwner(owner);
                dialog.initModality(Modality.WINDOW_MODAL);
            }
            dialog.showAndWait();
        } finally {
            dialog.close();
        }


        if(true)return true;

        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle(header);
        alert.setHeaderText(header);
        alert.getDialogPane().setContent(parent);
        alert.getDialogPane().setPrefWidth(250);
        parent.setTranslateX(10);
        //alert.setGraphic(parent);
        if (owner == null) {
            alert.initModality(Modality.APPLICATION_MODAL);
        } else {
            alert.initOwner(owner);
            alert.initModality(Modality.WINDOW_MODAL);
        }

        Optional<ButtonType> result = alert.showAndWait();
        return result.get() == ButtonType.OK;
    }

    public static void alertNode(String header,Parent node,  Window owner){
        Scene scene = new Scene(node);
        Stage stage = new Stage();
        //stage.initStyle(StageStyle.UTILITY);
        stage.setScene(scene);
        stage.setTitle(header);
        if (owner == null) {
            stage.initModality(Modality.APPLICATION_MODAL);
        } else {
            stage.initOwner(owner);
            stage.initModality(Modality.WINDOW_MODAL);
        }
        stage.showAndWait();
    }

    public static void alertNode(String header,Parent node, double width, double height, Window owner, boolean wait, boolean resizable){
        if(node == null) return;
        if(node.getScene() != null && node.getScene().getRoot() != null)
        {
            if(node.getScene().getWindow().isShowing())
                return;
            else
                node.getScene().setRoot(new Region());
        }
        Scene scene = new Scene(node);
        Stage stage = new Stage();
        //stage.initStyle(StageStyle.UTILITY);
        if(resizable){
            stage.setMinWidth(width);
            stage.setMinHeight(height);
        }else
        {
            stage.setMaxWidth(width);
            stage.setMaxHeight(height);
        }
        stage.setResizable(resizable);
        stage.setScene(scene);
        stage.setTitle(header);
        if(wait)
        {
            if (owner == null) {
                stage.initModality(Modality.APPLICATION_MODAL);
            } else {
                stage.initOwner(owner);
                stage.initModality(Modality.WINDOW_MODAL);
            }
            stage.showAndWait();
        }else
        {
            if (owner != null)
                stage.initOwner(owner);
            stage.initModality(Modality.NONE);
            stage.show();
        }
    }

    public static String alertInput(String header, String defaultInput,  Window owner) {
        TextInputDialog alert = new TextInputDialog(defaultInput == null ? "" : defaultInput);
        alert.setTitle("Input");
        alert.setHeaderText(header);
        // create a tile pane
        TilePane r = new TilePane();

        //alert.setGraphic(parent);
        if (owner == null) {
            alert.initModality(Modality.APPLICATION_MODAL);
        } else {
            alert.initOwner(owner);
            alert.initModality(Modality.WINDOW_MODAL);
        }
        Optional<String> result = alert.showAndWait();
        return result.orElse(null);
    }

    public static void alertWarning(String header, String content) {
        Alert alert = new Alert(Alert.AlertType.WARNING);
        alert.setTitle("Warning");
        alert.setHeaderText(header);
        alert.setContentText(content);

        //alert.initModality(Modality.APPLICATION_MODAL);
        alert.initModality(Modality.WINDOW_MODAL);

        alert.showAndWait();
    }

    public static void alertWarning(String header, String content, BooleanProperty close) {
        Alert alert = new Alert(Alert.AlertType.WARNING);
        alert.setTitle("Warning");
        alert.setHeaderText(header);
        alert.setContentText(content);

        //alert.initModality(Modality.APPLICATION_MODAL);
        alert.initModality(Modality.WINDOW_MODAL);
        if(close != null){
            ChangeListener<Boolean> changeListener = (observable, oldValue, newValue) -> alert.close();
            close.addListener(changeListener);
            alert.showAndWait();
            close.removeListener(changeListener);
        }else{
            alert.showAndWait();
        }
    }

    public static void alertWarningWithoutWaiting(String header, String content) {
        Alert alert = new Alert(Alert.AlertType.WARNING);
        alert.setTitle("Warning");
        alert.setHeaderText(header);
        alert.setContentText(content);
        alert.initModality(Modality.NONE);
        alert.show();
    }

    public static void alertWarning(String header, String content, Runnable runnable) {
        Alert alert = new Alert(Alert.AlertType.WARNING);
        alert.setTitle("Warning");
        alert.setHeaderText(header);
        alert.setContentText(content);

        //alert.initModality(Modality.APPLICATION_MODAL);
        alert.initModality(Modality.WINDOW_MODAL);

        alert.showAndWait();
        runnable.run();
    }

    public static void alertInfo(String header, String content) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Information");
        alert.setHeaderText(header);
        alert.setContentText(content);

        alert.showAndWait();
    }

    public static void alertInfoNoWait(String header, String content) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Information");
        alert.setHeaderText(header);
        alert.setContentText(content);

        alert.initModality(Modality.NONE);

        alert.show();
    }

    public static void alertInfo(String header, Node parent, double width, double height, Window owner, boolean resizable) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Information");
        alert.setHeaderText(header);
        alert.getDialogPane().setContent(parent);
        //alert.setGraphic(parent);
        alert.setResizable(resizable);
        //alert.getDialogPane().setPrefSize(width, height);
        if(width > 0) alert.getDialogPane().setPrefWidth(width);
        if(height > 0) alert.getDialogPane().setPrefHeight(height);
        if (owner == null) {
            alert.initModality(Modality.APPLICATION_MODAL);
        } else {
            alert.initOwner(owner);
            alert.initModality(Modality.WINDOW_MODAL);
        }

        alert.showAndWait();
    }

    public static interface ParametrizedController{
        public void setParameter(Object object);
    }
}
