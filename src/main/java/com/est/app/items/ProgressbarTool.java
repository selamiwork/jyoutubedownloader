package com.est.app.items;

import javafx.application.Platform;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressBar;
import javafx.scene.control.ProgressIndicator;
import javafx.scene.layout.Pane;
import javafx.scene.text.Font;

public class ProgressbarTool extends Pane{

	private ProgressBar progressBar; 
	private ProgressIndicator progressIndicator;
	private Label text;
	private Label textRemaining;
	private double totalProgress;
	private double progress = 0;
	private BooleanProperty processing = new SimpleBooleanProperty(false);

	private double totalProgressSave = 0;
	private double progressSave = 0;
	private String textRemainingSave = "";

	public ProgressbarTool (long totalProgress) 
	{
		this.totalProgress = totalProgress;
		progressBar = new ProgressBar();
    	progressBar.setMinHeight(20);
    	progressBar.setMaxHeight(20);
    	progressBar.setMinWidth(160);
    	//progressBar.setMaxWidth(697);
    	
    	progressIndicator = new ProgressIndicator(0);
    	text = new Label("% 0");
    	text.setFont(Font.font ("Tahoma", 11));
    	text.setTranslateX(65);
    	text.setTranslateY(3);

		textRemaining = new Label("");
		textRemaining.setFont(Font.font ("Tahoma", 10));
		textRemaining.setTranslateX(5);
		textRemaining.setTranslateY(3);

		getChildren().addAll(progressBar, text, textRemaining);

		setProgress(0);
	}

	public void setTotalProgress(long totalProgress) {
		this.totalProgress = totalProgress;
		progress = 0;
	}


	/**
	 * 
	 * @param pProgress
	 */
	public void setProgress(double pProgress)
	{
		try {
			progress = (100 * ( pProgress / totalProgress ));
		} catch (Exception e) {
			// TODO: handle exception
			progress = 0;
		}
		Platform.runLater(() -> {
			progressBar.setProgress(progress / 100);
			progressIndicator.setProgress(progress / 100);
			text.setText("% " + (int)progress);
		});
	}
	
	/**
	 * 
	 * @param step
	 */
	public void addProgress(double step)
	{
		progress += step;
		double value = (100 * ( progress / totalProgress ));
		//final double fValue = value; 
		Platform.runLater(() -> {
			progressBar.setProgress( value / 100);
			progressIndicator.setProgress(value / 100);
			text.setText("% " + (int)value );
		});
	}

	public void save(){
		progressSave = progress;
		totalProgressSave = totalProgress;
		textRemainingSave = textRemaining.getText();
	}

	public void restore(){
		setTotalProgress((long) totalProgressSave);
		addProgress(progressSave);
		Platform.runLater(()->textRemaining.setText(textRemainingSave));
	}

	public void setRemaining(String remaining)
	{
		Platform.runLater(()->textRemaining.setText(remaining));
	}

	public boolean isProcessing() {
		return processing.get();
	}

	public BooleanProperty processingProperty() {
		return processing;
	}

	public void setProcessing(boolean processing) {
		this.processing.set(processing);
	}

	public double getProgressPercent() {
		double value = (100 * ( progress / totalProgress ));
		return value;
	}

}
