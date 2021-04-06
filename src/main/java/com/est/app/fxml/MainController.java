package com.est.app.fxml;

import com.est.app.items.ProgressbarTool;
import com.est.app.items.console.Console;
import com.est.app.utils.EffectUtils;
import com.est.app.utils.Options;
import com.est.app.utils.TimeUtils;
import com.est.app.youtubedownloader.YoutubeDownloader;
import javafx.application.Platform;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.StackPane;
import javafx.scene.text.Text;
import javafx.stage.DirectoryChooser;
import javafx.stage.Stage;
import org.fxmisc.flowless.VirtualizedScrollPane;
import org.fxmisc.richtext.CodeArea;
import org.fxmisc.richtext.LineNumberFactory;
import org.fxmisc.richtext.model.StyleSpans;
import org.fxmisc.richtext.model.StyleSpansBuilder;
import org.jnativehook.GlobalScreen;
import org.jnativehook.NativeHookException;
import org.jnativehook.mouse.NativeMouseEvent;
import org.jnativehook.mouse.NativeMouseListener;
import org.jnativehook.mouse.NativeMouseMotionListener;
import org.reactfx.Subscription;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.time.Duration;
import java.util.*;
import java.util.logging.Handler;
import java.util.logging.Level;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class MainController implements Initializable {

    private Logger logger = LoggerFactory.getLogger(getClass());

    @FXML
    private BorderPane root;

    @FXML
    private StackPane stackPaneContent;

    @FXML
    private ImageView imageviewBackground;

    @FXML
    private StackPane stackPaneTextArea;

    @FXML
    private Text textConsole;

    @FXML
    private FlowPane flowpaneProgressbar;

    @FXML
    private ImageView imageviewStop;

    @FXML
    private TextField textfieldPath;

    @FXML
    private ImageView imageviewSelectFolder;

    @FXML
    private ComboBox<String> comboboxQuality;

    @FXML
    private Text textDownload;

    @FXML
    private ImageView imageviewDownload;

    @FXML
    private Text textLinkCount;

    private CodeArea codeArea;

    private Console console;

    private ProgressbarTool progressbarTool = new ProgressbarTool(0);

    private StringProperty saveDirectory = new SimpleStringProperty("");

    private YoutubeDownloader youtubeDownloader = new YoutubeDownloader();

    private List<String> downloadList = new ArrayList<>();

    private final String[] KEYWORDS = new String[] {
            "http", "https", "youtube", "com", ".com"
    };
    public final String KEYWORD_PATTERN = "\\b(" + String.join("|", KEYWORDS) + ")\\b";
    public final String STRING_PATTERN = "\"([^\"\\\\]|\\\\.)*\"";

    @Override
    public void initialize(URL location, ResourceBundle resources) {

        console = new Console(textConsole);
        initializeTextArea(stackPaneTextArea);

        //listviewVariables.setCellFactory(param -> context.getBean(GPSValueListCellController.class));

        Platform.runLater(() -> {
            Stage primaryStage = (Stage) root.getScene().getWindow();
            primaryStage.getIcons().add(new Image("/icons/icons8_jyoutube_100px.png"));
            primaryStage.setOnCloseRequest(event -> {
                //event.consume();
                //logger.debug("Shutting down...");
                primaryStage.close();
                exit();
            });
        });

        Platform.runLater(() -> {
            //imageviewBackground.fitWidthProperty().bind(stackPaneContent.widthProperty());
            //imageviewBackground.fitHeightProperty().bind(stackPaneContent.heightProperty());
            imageviewBackground.setFitWidth(stackPaneContent.getWidth());
            imageviewBackground.setFitHeight(stackPaneContent.getHeight());
            stackPaneContent.heightProperty().addListener((observable, oldValue, newValue) -> imageviewBackground.setScaleY(newValue.doubleValue() / imageviewBackground.getFitHeight()));
            stackPaneContent.widthProperty().addListener((observable, oldValue, newValue) -> imageviewBackground.setScaleX(newValue.doubleValue() / imageviewBackground.getFitWidth()));

            textfieldPath.setText(Options.getDefaultDirectory());
            textfieldPath.positionCaret(textfieldPath.getText().length() - 1);
        });

        progressbarTool.visibleProperty().bind(progressbarTool.processingProperty());
        flowpaneProgressbar.getChildren().add(progressbarTool);

        EffectUtils.setEffect(imageviewStop);
        EffectUtils.setEffect(imageviewDownload);
        EffectUtils.setEffect(imageviewSelectFolder);
        EffectUtils.setEffect(textDownload);

        saveDirectory.bindBidirectional(textfieldPath.textProperty());

        comboboxQuality.getItems().addAll(youtubeDownloader.getQualityList());
        comboboxQuality.getSelectionModel().select(5);

        imageviewSelectFolder.setOnMousePressed(event -> {
            new Thread(()->{
                Platform.runLater(()->{
                    DirectoryChooser fileChooser = new DirectoryChooser();
                    fileChooser.setTitle("Directory to save");
                    //fileChooser.getExtensionFilters().add(extFilter);
                    File defaultFile = new File(Options.getDefaultDirectory());
                    fileChooser.setInitialDirectory(defaultFile.isDirectory() ? defaultFile : defaultFile.getParentFile());
                    //File file = fileChooser.showOpenDialog(buttonSelectImageFile.getScene().getWindow());
                    File file = fileChooser.showDialog(imageviewSelectFolder.getScene().getWindow());
                    if (file != null) {
                        Options.setDefaultDirectory(file.getAbsolutePath());
                        textfieldPath.setText(file.getAbsolutePath());
                        textfieldPath.positionCaret(textfieldPath.getText().length() - 1);
                    }
                });
            }).start();
        });

        imageviewDownload.setOnMousePressed(event -> download());
        textDownload.setOnMousePressed(event -> download());

        imageviewStop.setOnMousePressed(event -> {
            Platform.runLater(()->progressbarTool.setProcessing(false));
            youtubeDownloader.shutdown();
        });

        new Thread(()->{
            int i = 0;
            //while(true)
            {
                TimeUtils.waitInMilis(1000);
                logger.info("Initialized " + i);
               // System.out.println("i = " + i);
            }
        }).start();
        System.out.println("Initialized.");
    }

    private void initializeTextArea(StackPane stackPane){
        String text = Options.getDefaultLinks();

        if(codeArea == null){
            codeArea = new CodeArea();
        }
        codeArea.textProperty().addListener((obs, oldText, newText) -> {
            Options.setDefaultLinks(newText);
            setDownloadList();
        });

        // add line numbers to the left of area
        codeArea.setParagraphGraphicFactory(LineNumberFactory.get(codeArea));

        // recompute the syntax highlighting 500 ms after user stops editing area
        Subscription cleanupWhenNoLongerNeedIt = codeArea

                // plain changes = ignore style changes that are emitted when syntax highlighting is reapplied
                // multi plain changes = save computation by not rerunning the code multiple times
                //   when making multiple changes (e.g. renaming a method at multiple parts in file)
                .multiPlainChanges()

                // do not emit an event until 500 ms have passed since the last emission of previous stream
                .successionEnds(Duration.ofMillis(150))

                // run the following code block when previous stream emits an event
                .subscribe(ignore -> codeArea.setStyleSpans(0, computeHighlighting(codeArea.getText())));

        // when no longer need syntax highlighting and wish to clean up memory leaks
        // run: cleanupWhenNoLongerNeedIt.unsubscribe();

        codeArea.setEditable(true);
        //codeArea.replaceText(0, 0, Values.getRules());
        //codeArea.replaceText(Values.getRules());
        codeArea.replaceText("");
        codeArea.appendText(text);
        //codeArea.appendText(Values.getRules());

        codeArea.getStylesheets().add(getClass().getClassLoader().getResource("fxml/keywords.css").toExternalForm());
        //codeArea.getStylesheets().add(getClass().getResource("application.css").toExternalForm());
        //stackPane.getStylesheets().add(LogsController.class.getResource("keywords.css").toExternalForm());
        stackPane.getChildren().add(new VirtualizedScrollPane<>(codeArea));

        codeArea.requestFollowCaret();
    }

    /**
     * implementation of regular expression for rich text area
     * to handle keywords
     *
     * @param text
     * @return
     */
    private StyleSpans<Collection<String>> computeHighlighting(String text) {
        Pattern PATTERN = Pattern.compile(
                "(?<KEYWORD>" + KEYWORD_PATTERN + ")"
                        + "|(?<STRING>" + STRING_PATTERN + ")"
        );

        Matcher matcher = PATTERN.matcher(text);
        int lastKwEnd = 0;
        StyleSpansBuilder<Collection<String>> spansBuilder
                = new StyleSpansBuilder<>();
        while(matcher.find()) {
            String styleClass =
                    matcher.group("KEYWORD") != null ? "keyword" :
                                    matcher.group("STRING") != null ? "string" :
                                                            null; /* never happens */ assert styleClass != null;
            spansBuilder.add(Collections.emptyList(), matcher.start() - lastKwEnd);
            spansBuilder.add(Collections.singleton(styleClass), matcher.end() - matcher.start());
            lastKwEnd = matcher.end();
        }
        spansBuilder.add(Collections.emptyList(), text.length() - lastKwEnd);
        return spansBuilder.create();
    }

    /**
     * setText : directly set text of textfield in the Rules part.
     *
     * @param text
     */
    public void appendText(String text) {
        Platform.runLater(()->{
            if(codeArea == null) return;
            codeArea.appendText(text);
            codeArea.requestFollowCaret();
        });
    }

    /**
     * setText : directly set text of textfield in the Rules part.
     *
     * @param text
     */
    public void setText(String text) {
        Platform.runLater(()->{
            if(codeArea == null) return;
            codeArea.clear();
            codeArea.appendText(text);
        });
    }

    private void initializeListeners() {
        /* Note: JNativeHook does *NOT* operate on the event dispatching thread.
         * Because Swing components must be accessed on the event dispatching
         * thread, you *MUST* wrap access to Swing components using the
         * SwingUtilities.invokeLater() or EventQueue.invokeLater() methods.
         */
        try {
            GlobalScreen.registerNativeHook();
        } catch (NativeHookException e1) {
            // TODO Auto-generated catch block
            e1.printStackTrace();
        }

        System.setProperty("org.slf4j.simpleLogger.logFile", "System.out");
        System.setProperty("org.slf4j.appender.stdout.Target", "System.out");

        // Change the level for all handlers attached to the default logger.
        Handler[] handlers = java.util.logging.Logger.getLogger("").getHandlers();
        for (int i = 0; i < handlers.length; i++) {
            handlers[i].setLevel(Level.INFO);
        }
        // Get the logger for "org.jnativehook" and set the level to off.
        java.util.logging.Logger logger = java.util.logging.Logger.getLogger(GlobalScreen.class.getPackage().getName());
        logger.setLevel(Level.OFF);

        /*
		GlobalScreen.addNativeKeyListener(new NativeKeyListener() {
			public void nativeKeyPressed(NativeKeyEvent e) {

				System.out.println("Key Pressed: " + NativeKeyEvent.getKeyText(e.getKeyCode()));
			}

			public void nativeKeyReleased(NativeKeyEvent e) {
				System.out.println("Key Released: " + NativeKeyEvent.getKeyText(e.getKeyCode()));
			}

			public void nativeKeyTyped(NativeKeyEvent e) {
				System.out.println("Key Typed: " + NativeKeyEvent.getKeyText(e.getKeyCode()));
			}
		});
		*/
        GlobalScreen.addNativeMouseListener(new NativeMouseListener() {

            @Override
            public void nativeMouseReleased(NativeMouseEvent arg0) {
                // TODO Auto-generated method stub
                //System.out.println("MouseReleased()");
            }

            @Override
            public void nativeMousePressed(NativeMouseEvent arg0) {
                // TODO Auto-generated method stub
                //System.out.println("MousePressed() : X=" + arg0.getX() + " Y=" + arg0.getY());
            }

            @Override
            public void nativeMouseClicked(NativeMouseEvent arg0) {
                // TODO Auto-generated method stub
                //System.out.println("MouseClicked()");
            }
        });

        GlobalScreen.addNativeMouseMotionListener(new NativeMouseMotionListener() {

            @Override
            public void nativeMouseMoved(NativeMouseEvent arg0) {
                // TODO Auto-generated method stub
                youtubeDownloader.setX_Current(arg0.getX());
                youtubeDownloader.setY_Current(arg0.getY());
            }

            @Override
            public void nativeMouseDragged(NativeMouseEvent arg0) {
                // TODO Auto-generated method stub

            }
        });

    }

    private void setDownloadList(){
        if(codeArea.getText() == null || codeArea.getText().isEmpty()){
            return;
        }
        String[] links = codeArea.getText().split("\n");
        //downloadList = Arrays.asList(links);
        downloadList = new ArrayList<>();
        for(String link : links){
            if(link == null || link.isEmpty() || !link.toLowerCase().contains("youtube")){
                continue;
            }
            downloadList.add(link);
        }
        youtubeDownloader.setDownloadList(downloadList);
        Platform.runLater(()->textLinkCount.setText("Link count : " + downloadList.size()));
    }

    private void download(){
        if(!youtubeDownloader.isInitialized()){
            try {
                youtubeDownloader.initialize();
                youtubeDownloader.setProgressbarTool(progressbarTool);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        setDownloadList();
        youtubeDownloader.setQualityIndex(comboboxQuality.getSelectionModel().getSelectedIndex());
        youtubeDownloader.setSavePath(saveDirectory.get());
        youtubeDownloader.launch();
    }

    private void exit(){
        try {
            youtubeDownloader.shutdown();
        } catch (Exception e) {
            e.printStackTrace();
        }
        System.exit(0);
    }

}
