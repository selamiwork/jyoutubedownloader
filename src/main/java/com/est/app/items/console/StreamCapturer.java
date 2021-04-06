package com.est.app.items.console;

import java.io.*;
import java.nio.charset.StandardCharsets;

public class StreamCapturer extends OutputStream {

    private String charsetName = StandardCharsets.UTF_8.name();
    private ByteArrayOutputStream bytes = new ByteArrayOutputStream();
    private PrintStream printStream = new PrintStream(bytes,true, charsetName);
    private String prefix;
    private Consumer consumer;
    private PrintStream oldPrintStream;
    private boolean isOldOutputEnable;
    private boolean isPrefixEnable;

    public StreamCapturer(String prefix, Consumer consumer, PrintStream oldPrintStream, boolean isOldOutputEnable, boolean isPrefixEnable) throws UnsupportedEncodingException {
        this.prefix = isPrefixEnable ? ("[" + prefix + "]") : "";
        this.isPrefixEnable = isPrefixEnable;
        this.isOldOutputEnable = isOldOutputEnable;
        this.oldPrintStream = oldPrintStream;
        this.consumer = consumer;
    }

    @Override
    public void write(int i) throws IOException {
        printStream.write(i);
        if(isOldOutputEnable) oldPrintStream.write(i);
        String value = Character.toString((char)i);
        if (value.equals("\n")) {
            String pBytes = bytes.toString(charsetName);
            /*
            String test = String.valueOf('\u011F');
            if(pBytes.contains(test))
            {
                consumer.append("test = "  + pBytes + " " + test );
            }
             */
            consumer.append(prefix + pBytes);
            //consumer.append(prefix + new String(pBytes.getBytes(StandardCharsets.UTF_8), StandardCharsets.UTF_8));
            bytes.reset();
        }
    }

} 