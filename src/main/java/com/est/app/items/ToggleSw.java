package com.est.app.items;

import javafx.animation.FillTransition;
import javafx.animation.ParallelTransition;
import javafx.animation.TranslateTransition;
import javafx.application.Platform;
import javafx.beans.binding.Bindings;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.scene.Parent;
import javafx.scene.effect.BoxBlur;
import javafx.scene.effect.DropShadow;
import javafx.scene.effect.Effect;
import javafx.scene.effect.Light.Distant;
import javafx.scene.effect.Lighting;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.util.Duration;

public class ToggleSw extends Parent {

    private final BooleanProperty switchedOn = new SimpleBooleanProperty(false);
    private BooleanProperty disableProperty = new SimpleBooleanProperty(false);

    private TranslateTransition translateAnimation = new TranslateTransition(Duration.seconds(0.25));
    private FillTransition fillAnimation = new FillTransition(Duration.seconds(0.25));

    private final ParallelTransition animation = new ParallelTransition(translateAnimation, fillAnimation);
    private ImageView trigger;
    private Image imageOn, imageOff;
    private Color colorOn, colorOff;
    public final Text text;
    private String textOn, textOff;

    public BooleanProperty getDisableProperty() {
        return disableProperty;
    }

    public BooleanProperty switchedOnProperty() {
        return switchedOn;
    }
    
    public boolean isSwitchedOn()
    {
    	return switchedOn.get();
    }
    
    public void setSwitchEnable(boolean state)
    {
    	synchronized (switchedOn) {
    		try {
            	switchedOn.set(state);
			} catch (Exception e) { }
		}
    }

    private int width = 100;
    private int height = 50;
    
    Runnable action;


    public ToggleSw(double width, double height)
    {
        this(0, width, height, null, null);
    }

    public ToggleSw(double width, double height, Image imageOn, Image ImageOff)
    {
    	this(0, width, height, imageOn, ImageOff);
    }

    public ToggleSw(double x, double width, double height, Image imageOn, Image ImageOff)
    {    	
    	//this(x, width, height, imageOn, ImageOff, Color.web("#092f57", 0.9), Color.WHITE, "A\u00C7IK", "KAPALI");
        this(x, width, height, imageOn, ImageOff, Color.web("#092f57", 0.9), Color.WHITE, "ON", "OFF");
    }
    
    public ToggleSw(double width, double height, Image imageOn, Image ImageOff, String textOn, String textOff)
    {
    	this(width, height, imageOn, ImageOff ,Color.web("#092f57", 0.9), Color.WHITE, textOn, textOff);
    }

    public ToggleSw(double x, double width, double height, Image imageOn, Image ImageOff, String textOn, String textOff)
    {
    	this(x, width, height, imageOn, ImageOff, Color.web("#092f57", 0.9), Color.WHITE, textOn, textOff);
    }

    public ToggleSw(double width, double height, Image imageOn, Image ImageOff, Color colorOn, Color colorOff, String textOn, String textOff)
    {
    	this(0, width, height, imageOn, ImageOff ,colorOn, colorOff, textOn, textOff);
    }

    public ToggleSw(double x, double width, double height, Image pImageOn, Image pImageOff, Color colorOn, Color colorOff,
                    String pTextOn, String pTextOff) {
    	this.imageOn = pImageOn;
    	this.imageOff = pImageOff;
    	this.colorOn = colorOn;	
    	this.colorOff = colorOff;	
    	this.textOn = pTextOn;
    	this.textOff = pTextOff;

    	setTranslateX(x);
	
        Rectangle background = new Rectangle(width, height);
        background.setArcWidth(height);
        background.setArcHeight(height);
        background.setFill(Color.WHITE);
        background.setStroke(Color.web("#092f57", 0.9));

        Circle ctrigger = new Circle(height / 2);
        ctrigger.setCenterX(height / 2);
        ctrigger.setCenterY(height / 2);
        ctrigger.setFill(Color.WHITE);
        ctrigger.setStrokeWidth(2);
        ctrigger.setStroke(Color.web("#092f57", 0.9));

        trigger = new ImageView(this.imageOff);
        trigger.setFitWidth(height);
        trigger.setFitHeight(height);
        trigger.setX(0);
        trigger.setY(0);
       // setEffect(trigger);
        DropShadow shadow = new DropShadow();
        shadow.setRadius(2);
        trigger.setEffect(shadow);

        text = new Text(textOff.length() > textOn.length() ? textOff : textOn);
        //text.setFont(Font.font ("Tahoma", FontWeight.BOLD, 20));
        text.setFont(Font.font ("Tahoma", FontWeight.BOLD, height/2));
        text.setStrokeWidth(5);
        text.setFill(Color.web("#092f57", 0.9));
        //text.setWrappingWidth(width - height);

        double fontSize = height/2;
        double textWidth = text.getLayoutBounds().getWidth();
        while(textWidth > (width - height) - 10)
        {
            textWidth = text.getLayoutBounds().getWidth();
        	fontSize -= 0.5;
            text.setFont(Font.font ("Tahoma", FontWeight.BOLD, fontSize));
        }
        text.setText(textOff);
        
        //text.maxWidth(width - height - 3);
        textWidth = text.getLayoutBounds().getWidth();
        double textHeigth = text.getLayoutBounds().getHeight();
        text.setTranslateX(height + (width - height - textWidth) / 2 );
        text.setTranslateY((height / 2) + (fontSize / 2) - 1);

        translateAnimation.setNode(imageOn == null ? ctrigger : trigger);
        fillAnimation.setShape(background);

        getChildren().addAll(background, imageOn == null ? ctrigger : trigger, text);

        
        //FF6495ED
        switchedOn.addListener((obs, oldState, newState) -> {
            Platform.runLater(()->{
                synchronized (animation) {
                    synchronized (switchedOn) {
                        synchronized (text) {
                            animation.stop();
                            boolean isOn = newState;
                            translateAnimation.setToX(isOn ? width - height : 0);
                            fillAnimation.setFromValue(isOn ? colorOff : colorOn);
                            fillAnimation.setToValue(isOn ? colorOn : colorOff);
                            trigger.setImage(isOn ? imageOn : imageOff);
                            Platform.runLater(()-> {
                                text.setText(isOn ? textOn : textOff);
                                text.setTranslateX( (isOn ? 0 : height) + (width - height - text.getLayoutBounds().getWidth()) / 2 );
                                text.setFill(isOn ? (colorOn.equals(Color.WHITE ) ? Color.web("#092f57", 0.9) : Color.WHITE) : Color.web("#092f57", 0.9));
                            });
                            animation.play();
                        }
                    }
                }
            });
        });

        setOnMouseClicked(event -> {
        	if(disableProperty.get()) return;
    		synchronized (animation) {
    			synchronized (switchedOn) {
    				synchronized (text) {
    					Platform.runLater(()->{
                            switchedOn.set(!switchedOn.get());
                            try { action.run(); } catch (Exception e) { }
    					});
					}
        		}
			}
        });
        
        //disableProperty.set(true);
    }

    public void setImageScale(double scale){
        double scaled = trigger.getFitWidth() * scale;
        trigger.setY(Math.abs(trigger.getFitWidth() - scaled) / 2);
        trigger.setFitWidth(scaled);
        trigger.setFitHeight(scaled);
    }

    public Runnable getOnAction() {
    	return action;
    }
    
    public void setOnAction(Runnable runnable) {
    	action = runnable;
    }
    
    public void setEffect(ImageView imageView) {
        Effect shadow = new DropShadow(5, Color.CORNFLOWERBLUE);
        Effect shadowPressed = new DropShadow(5, Color.DARKBLUE);
	    Effect blur = new BoxBlur(1, 1, 3);
	    
        Distant light = new Distant();
        light.setAzimuth(30.0f);
        light.setElevation(35.0f);
        Lighting lighting = new Lighting(light);
        lighting.setSurfaceScale(0.0f);
        

        imageView.effectProperty().bind(
                Bindings.when(imageView.pressedProperty())
                        .then(shadow)
                        .otherwise(Bindings.when(imageView.hoverProperty())
                                .then(shadowPressed)
                                .otherwise(blur))
        );
	}
}