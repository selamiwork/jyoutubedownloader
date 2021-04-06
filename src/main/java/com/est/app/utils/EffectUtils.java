package com.est.app.utils;

import javafx.application.Platform;
import javafx.beans.binding.Bindings;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ToggleButton;
import javafx.scene.effect.*;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;

@SuppressWarnings("Duplicates")
public class EffectUtils {
    public static void setEffect(ImageView imageView) {
        Effect shadow = new DropShadow(5, Color.CORNFLOWERBLUE);
        Effect shadowPressed = new DropShadow(5, Color.DARKBLUE);
        Effect blur = new BoxBlur(1, 1, 3);
        Effect shadowHover = new DropShadow(5, Color.CORNSILK);

        Light.Distant light = new Light.Distant();
        light.setAzimuth(30.0f);
        light.setElevation(35.0f);
        Lighting lighting = new Lighting(light);
        lighting.setSurfaceScale(0.0f);


        imageView.effectProperty().bind(
                Bindings.when(imageView.pressedProperty())
                        .then(shadow)
                        .otherwise(Bindings.when(imageView.hoverProperty())
                                .then(shadowPressed)
                                .otherwise(shadowHover))
        );

        setOpacityProperty(imageView);
    }

    public static void setEffect(Text imageView) {
        Effect shadow = new DropShadow(5, Color.CORNFLOWERBLUE);
        Effect shadowPressed = new DropShadow(5, Color.DARKBLUE);
        Effect blur = new BoxBlur(1, 1, 3);
        Effect shadowHover = new DropShadow(5, Color.CORNSILK);

        Light.Distant light = new Light.Distant();
        light.setAzimuth(30.0f);
        light.setElevation(35.0f);
        Lighting lighting = new Lighting(light);
        lighting.setSurfaceScale(0.0f);


        imageView.effectProperty().bind(
                Bindings.when(imageView.pressedProperty())
                        .then(shadow)
                        .otherwise(Bindings.when(imageView.hoverProperty())
                                .then(shadowPressed)
                                .otherwise(shadowHover))
        );

        setOpacityProperty(imageView);
    }
    public static void setEffect(Button imageView) {
        Effect shadow = new DropShadow(5, Color.CORNFLOWERBLUE);
        Effect shadowPressed = new DropShadow(5, Color.DARKBLUE);
        Effect blur = new BoxBlur(1, 1, 3);
        Effect shadowHover = new DropShadow(5, Color.CORNSILK);

        Light.Distant light = new Light.Distant();
        light.setAzimuth(30.0f);
        light.setElevation(35.0f);
        Lighting lighting = new Lighting(light);
        lighting.setSurfaceScale(0.0f);


        imageView.effectProperty().bind(
                Bindings.when(imageView.pressedProperty())
                        .then(shadow)
                        .otherwise(Bindings.when(imageView.hoverProperty())
                                .then(shadowPressed)
                                .otherwise(shadowHover))
        );

        setOpacityProperty(imageView);
    }
    public static void setEffect(ToggleButton imageView) {
        Effect shadow = new DropShadow(5, Color.CORNFLOWERBLUE);
        Effect shadowPressed = new DropShadow(5, Color.DARKBLUE);
        Effect blur = new BoxBlur(1, 1, 3);
        Effect shadowHover = new DropShadow(5, Color.CORNSILK);

        Light.Distant light = new Light.Distant();
        light.setAzimuth(30.0f);
        light.setElevation(35.0f);
        Lighting lighting = new Lighting(light);
        lighting.setSurfaceScale(0.0f);


        imageView.effectProperty().bind(
                Bindings.when(imageView.pressedProperty())
                        .then(shadow)
                        .otherwise(Bindings.when(imageView.hoverProperty())
                                .then(shadowPressed)
                                .otherwise(shadowHover))
        );

        setOpacityProperty(imageView);
    }

    public static void setEffect(Pane imageView) {
        Effect shadow = new DropShadow(5, Color.CORNFLOWERBLUE);
        Effect shadowPressed = new DropShadow(5, Color.DARKBLUE);
        Effect blur = new BoxBlur(1, 1, 3);
        Effect shadowHover = new DropShadow(5, Color.CORNSILK);

        Light.Distant light = new Light.Distant();
        light.setAzimuth(30.0f);
        light.setElevation(35.0f);
        Lighting lighting = new Lighting(light);
        lighting.setSurfaceScale(0.0f);


        imageView.effectProperty().bind(
                Bindings.when(imageView.pressedProperty())
                        .then(shadow)
                        .otherwise(Bindings.when(imageView.hoverProperty())
                                .then(shadowPressed)
                                .otherwise(shadowHover))
        );

        setOpacityProperty(imageView);
    }

    public static void setEffect(Parent imageView) {
        Effect shadow = new DropShadow(5, Color.CORNFLOWERBLUE);
        Effect shadowPressed = new DropShadow(5, Color.DARKBLUE);
        Effect blur = new BoxBlur(1, 1, 3);
        Effect shadowHover = new DropShadow(5, Color.CORNSILK);

        Light.Distant light = new Light.Distant();
        light.setAzimuth(30.0f);
        light.setElevation(35.0f);
        Lighting lighting = new Lighting(light);
        lighting.setSurfaceScale(0.0f);


        imageView.effectProperty().bind(
                Bindings.when(imageView.pressedProperty())
                        .then(shadow)
                        .otherwise(Bindings.when(imageView.hoverProperty())
                                .then(shadowPressed)
                                .otherwise(shadowHover))
        );

        setOpacityProperty(imageView);
    }

    public static void setEffect(CheckBox imageView) {
        Effect shadow = new DropShadow(5, Color.CORNFLOWERBLUE);
        Effect shadowPressed = new DropShadow(5, Color.DARKBLUE);
        Effect blur = new BoxBlur(1, 1, 3);
        Effect shadowHover = new DropShadow(5, Color.CORNSILK);

        Light.Distant light = new Light.Distant();
        light.setAzimuth(30.0f);
        light.setElevation(35.0f);
        Lighting lighting = new Lighting(light);
        lighting.setSurfaceScale(0.0f);


        imageView.effectProperty().bind(
                Bindings.when(imageView.pressedProperty())
                        .then(shadow)
                        .otherwise(Bindings.when(imageView.hoverProperty())
                                .then(shadowPressed)
                                .otherwise(shadowHover))
        );

        setOpacityProperty(imageView);
    }

    public static void setEffectWithoutOpacity(Pane imageView) {
        Effect shadow = new DropShadow(5, Color.CORNFLOWERBLUE);
        Effect shadowPressed = new DropShadow(5, Color.DARKBLUE);
        Effect blur = new BoxBlur(1, 1, 3);
        Effect shadowHover = new DropShadow(5, Color.CORNSILK);

        Light.Distant light = new Light.Distant();
        light.setAzimuth(30.0f);
        light.setElevation(35.0f);
        Lighting lighting = new Lighting(light);
        lighting.setSurfaceScale(0.0f);


        imageView.effectProperty().bind(
                Bindings.when(imageView.pressedProperty())
                        .then(shadow)
                        .otherwise(Bindings.when(imageView.hoverProperty())
                                .then(shadowPressed)
                                .otherwise(shadowHover))
        );
    }

    public static void setOpacityProperty(Node node){
        node.disableProperty().addListener((observable, oldValue, newValue) -> Platform.runLater(() -> node.setOpacity(newValue ? 0.35 : 1.0)));
    }

    public static void setLightEffect(Node node, double azimuth,double elevation, double surfaceScale){
        Light.Distant light = new Light.Distant();
        light.setAzimuth(azimuth);
        light.setElevation(elevation);
        Lighting lighting = new Lighting(light);
        lighting.setSurfaceScale(surfaceScale);

        node.setEffect(lighting);
    }
}
