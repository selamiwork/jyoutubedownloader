package com.est.app.utils;

import javafx.application.Platform;
import javafx.scene.layout.StackPane;
import org.fxmisc.flowless.VirtualizedScrollPane;
import org.fxmisc.richtext.CodeArea;
import org.fxmisc.richtext.LineNumberFactory;
import org.fxmisc.richtext.model.StyleSpans;
import org.fxmisc.richtext.model.StyleSpansBuilder;
import org.reactfx.Subscription;

import java.time.Duration;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class RichTextArea {
    /**
     * lib name : jfxchartext.jar
     * imported from org.fxmisc.richtext.CodeArea
     *
     * this is rich text area can handle all user text as your requirement like developing a code in a IDE.
     * like eclipse or Netbeans.
     *
     */
    private CodeArea codeArea;
    /**
     * map : word , pattern
     */
    private Map<String, String> cssWords = new HashMap<>();

    public RichTextArea() {
    }

    public RichTextArea(List<String> words, List<String>  patterns) {
        for(int i=0; i<words.size(); i++)
            addCSSWords(words.get(i), patterns.get(i));
        initialize();
    }

    private void initialize(){
        codeArea = new CodeArea();
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
        // run: `cleanupWhenNoLongerNeedIt.unsubscribe();`
        codeArea.setEditable(false);
        codeArea.replaceText("");
        codeArea.getStylesheets().add(RichTextArea.class.getResource("/fxml/utils/keywords.css").toExternalForm());
        //codeArea.getStylesheets().add(getClass().getResource("application.css").toExternalForm());
        //stackPane.getStylesheets().add(LogsController.class.getResource("keywords.css").toExternalForm());
    }
    /**
     * setText : directly set text of textfield.
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

    public void addCSSWords(String word, String  pattern){
        cssWords.put(word.toLowerCase(), pattern);
    }
    /**
     * createTextArea :
     *
     * build rich text area in Rules part wiht handle all keywords.
     *
     * Colour of Keywords explained in "keyword.css" file.
     *
     * @return
     */
    public void createTextArea(StackPane stackPane, String text) {
        if(codeArea == null)
        {
            initialize();
        }
        codeArea.appendText(text);
        stackPane.getChildren().add(new VirtualizedScrollPane<>(codeArea));
    }

    /**
     * implementation of regular expression for rich text area
     * to handle keywords
     *
     * @param text
     * @return
     */
    private StyleSpans<Collection<String>> computeHighlighting(String text) {
        String patternWords = "";
        for(String word : cssWords.keySet())
            patternWords += "|(?<"+word.toUpperCase()+">" + cssWords.get(word) + ")";
        patternWords = patternWords.substring(1);

        Pattern PATTERN = Pattern.compile(patternWords );

        Matcher matcher = PATTERN.matcher(text);
        int lastKwEnd = 0;
        StyleSpansBuilder<Collection<String>> spansBuilder
                = new StyleSpansBuilder<>();
        while(matcher.find()) {
            String styleClass = null;
            for(String word : cssWords.keySet())
            {
                if(matcher.group(word.toUpperCase()) != null)
                    styleClass = word;
            }
            /* never happens */ assert styleClass != null;
            spansBuilder.add(Collections.emptyList(), matcher.start() - lastKwEnd);
            spansBuilder.add(Collections.singleton(styleClass), matcher.end() - matcher.start());
            lastKwEnd = matcher.end();
        }
        spansBuilder.add(Collections.emptyList(), text.length() - lastKwEnd);
        return spansBuilder.create();
    }

    public CodeArea getCodeArea() {
        return codeArea;
    }
}
