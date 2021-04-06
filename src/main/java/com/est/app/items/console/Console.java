package com.est.app.items.console;

import javafx.application.Platform;
import javafx.scene.effect.DropShadow;
import javafx.scene.effect.Effect;
import javafx.scene.effect.Lighting;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.Text;

import java.io.PrintStream;
import java.io.UnsupportedEncodingException;
import java.text.SimpleDateFormat;
import java.util.Calendar;

public class Console implements Consumer {

    private Text output;
    private Lighting lighting = new Lighting();
    private Effect shadow = new DropShadow(5, Color.GRAY);

    public Console() {
        this(new Text());
        setFont(new Font("Arial", 12));
    }

    public Console(Text output) {
        setOutput(output);
        try
        {
            PrintStream ps = System.out;
            try {
                System.setOut(new PrintStream(new StreamCapturer("STDOUT", this, ps, true, false)));
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
        }
        catch (SecurityException se)
        {
            append("Couldn't redirect STDOUT to this console\n" + se.getMessage());
        }
    }

    public void write(final String text)
    {
        Calendar now = Calendar.getInstance();
        long timeMs = now.getTimeInMillis();
        SimpleDateFormat formatter = new SimpleDateFormat("HH" + ":" + "mm" + ":" + "ss.SSS");
        String time = formatter.format(timeMs);

        final String pText = text.contains("0;39m") ? text.substring(text.lastIndexOf("0;39m") + 5) : text;
        Platform.runLater(() -> {output.setText(time + " |" + pText); });
    }

    @Override
    public void append(final String text) {

        //if(text.contains("ERROR") || text.contains("TRACE") || text.contains("DEBUG") || text.contains("WARN")) return;
        //if(!text.contains("0;39m")) return;  //spring application

        write(text);
    }

    public void  setText(final String text)
    {
        output.setText(text);
    }

    public void setOutput(Text output) {
        this.output = output;
        this.output.setEffect(shadow);
    }
    public void setFont(Font font){
        this.output.setFont(font);
    }
}
 