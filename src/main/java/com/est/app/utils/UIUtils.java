package com.est.app.utils;

import javafx.geometry.Orientation;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCombination;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.scene.text.TextAlignment;
import javafx.stage.Stage;
import javafx.util.Callback;

public class UIUtils {

    public static Pane newButton(String name, String icon, Runnable action) {
        Pane pane = new Pane();
        ImageView imageView = new ImageView(icon);
        return newButton(name , imageView , 55, action);
    }

    public static Pane newButton(String name, ImageView imageView, double size, Runnable action) {
        Pane pane = new Pane();
        imageView.setFitWidth(size);
        imageView.setFitHeight(size);

        EffectUtils.setEffect(pane);

        Text text = new Text(name);
        text.setTextAlignment(TextAlignment.CENTER);
        text.setWrappingWidth(size);
        text.setY(size + 10);
        text.setFont(Font.font ("Tahoma", FontWeight.NORMAL,  10));
        //text.setFont(Font.loadFont(Civ6MenuApp.class.getResource("res/Penumbra-HalfSerif-Std_35114.ttf").toExternalForm(), 36));
        text.setFill(Color.web("#1675A9", 0.95));

        pane.getChildren().addAll(imageView, text);
        if(action != null)
            pane.setOnMouseClicked(event -> action.run());
        return pane;
    }

    public static CheckBox newCheckBox(String name, double fontSize, Runnable action){
        CheckBox checkBox = new CheckBox(name);
        return newCheckBox(name, checkBox, fontSize, action);
    }

    public static CheckBox newCheckBox(String name, CheckBox checkBox, double fontSize, Runnable action) {
        checkBox.setText(name);

        EffectUtils.setEffect(checkBox);
        checkBox.setPrefWidth(100);
        checkBox.setWrapText(true);
        checkBox.setFont(Font.font ("Tahoma", FontWeight.NORMAL,  fontSize));
        checkBox.setTextFill(Color.web("#1675A9", 0.95));

        if(action != null)
            checkBox.setOnMouseClicked(event -> action.run());
        return checkBox;
    }

    public static void setFullScreen(Stage primaryStage){
        primaryStage.onCloseRequestProperty().setValue(null);
        primaryStage.setFullScreenExitHint("");
        primaryStage.setFullScreenExitKeyCombination(KeyCombination.NO_MATCH);
        primaryStage.setFullScreen(true);
    }

    public static <T> void setAutoWidth(ComboBox<T> comboBox){

        comboBox.setCellFactory(new Callback<ListView<T>, ListCell<T>>() {

            @Override
            public ListCell<T> call(ListView<T> param) {
                ListCell cell = new ListCell<T>() {
                    @Override
                    public void updateItem(T item, boolean empty) {
                        super.updateItem(item, empty);

                        getListView().setMaxWidth(100);
                        if (!empty) {
                            setText(item.toString());
                        } else {
                            setText(null);
                        }
                    }
                };
                return cell;
            }
        });
    }

    public static <T> void setAutoWidth2(ComboBox<T> comboBox){

        //DoubleProperty widthProperty = new SimpleDoubleProperty(0);
        final double[] width = {0.0};
        comboBox.setCellFactory(new Callback<ListView<T>, ListCell<T>>() {

            @Override
            public ListCell<T> call(ListView<T> param) {

                ListCell cell = new ListCell<T>() {
                    @Override
                    public void updateItem(T item, boolean empty) {
                        super.updateItem(item, empty);

                        //getListView().setMaxWidth(300);
                        if (!empty) {
                            setText(item.toString());
                        } else {
                            setText(null);
                        }
                        /*
                        widthProperty().addListener((observable, oldValue, newValue) -> {
                            Platform.runLater(() -> {
                                double value = Math.min(getListView().getMaxWidth(), newValue.doubleValue());
                                System.out.println("newValue = " + newValue.doubleValue()+ " / " + getListView().getPrefWidth());
                                System.out.println("setValue = " + value);
                                getListView().setPrefWidth(value);
                                getListView().setMaxWidth(value);
                            });
                        });
                         */
                        insetsProperty().addListener((observable, oldValue, newValue) -> {

                            width[0] = Math.max(width[0], prefWidth(-1));

                            double cellInset = newValue.getLeft() + newValue.getRight();
                            width[0]  = width[0]  + getListView().getInsets().getLeft() + getListView().getInsets().getRight() + cellInset;
                            ScrollBar bar = getVerticalScrollbar(getListView());
                            width[0] += bar.getWidth();
                            getListView().setPrefWidth(width[0]);
                        });
                    }
                };
                return cell;
            }
        });
    }

    private static <T> ScrollBar getVerticalScrollbar(ListView<T> listView) {
        ScrollBar result = null;
        for (Node n : listView.lookupAll(".scroll-bar")) {
            if (n instanceof ScrollBar) {
                ScrollBar bar = (ScrollBar) n;
                if (bar.getOrientation().equals(Orientation.VERTICAL)) {
                    result = bar;
                }
            }
        }
        return result;
    }

}
