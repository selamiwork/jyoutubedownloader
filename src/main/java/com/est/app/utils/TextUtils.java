package com.est.app.utils;

import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import org.apache.commons.lang3.StringUtils;

import java.io.UnsupportedEncodingException;
import java.nio.charset.StandardCharsets;
import java.text.DecimalFormat;
import java.util.List;

public class TextUtils {

    private static String UTF_8 = StandardCharsets.UTF_8.name();

    public static String fixedLengthString(String string, int length) {
        return String.format("%1$"+length+ "s", string);
    }
    public static String leftpad(String text, int length) {
        return String.format("%" + length + "." + length + "s", text);
    }

    public static String rightpad(String text, int length) {
        return String.format("%-" + length + "." + length + "s", text);
    }

    public static String rightPad(final String str, final int size, String padStr) {
        return StringUtils.rightPad(str, size, padStr);
    }

    private static String rightPad(final String str, final double width, String padStr, Text text) {
        new Scene(new Group(text));
        double pWidth = text.getLayoutBounds().getWidth();
        int size = 0;
        while(pWidth < width)
        {
            text.setText(rightPad(str, size++, padStr));
            pWidth = text.getLayoutBounds().getWidth();
        }
        return text.getText();
    }

    public static String rightPad(final String str, final double width, String padStr, Font font) {
        Text text = new Text(str);
        text.setFont(font);
        return rightPad(str, width, padStr, text);
    }

    public static String rightPad(final String str, final double width, String padStr, String style) {
        Text text = new Text(str);
        text.setStyle(style);
        return rightPad(str, width, padStr, text);
    }

    public static String format(String value){
        return value.replaceAll(",",".");
    }


    /**
     *
     * @param value
     * @return
     */
    public static DecimalFormat getDecimatFormat(double value){

        DecimalFormat decimalFormat = new DecimalFormat("##.########");

        String text = Double.toString(Math.abs(value)).replaceAll(",", ".");
        int integerPlaces = text.indexOf('.');
        if(integerPlaces > 0 || !text.contains("."))
        {
            decimalFormat = new DecimalFormat("##.##");
        }else
        {
            String diesis = "##.##";
            double pValue = value;
            int count = 0;
            while(true)
            {
                pValue *= 10;
                text = Double.toString(Math.abs(pValue));
                integerPlaces = text.replaceAll(",", ".").indexOf('.');
                if(integerPlaces > 0)
                {
                    decimalFormat = new DecimalFormat(diesis);
                    break;
                }else
                {
                    diesis += "#";
                }
                if(count ++ > 8)
                    break;
            }
        }
        return  decimalFormat;
    }

    /**
     *
     * @param value
     * @return
     */
    public static String format(double value){

        DecimalFormat decimalFormat = getDecimatFormat(value);
        //return decimalFormat.format(value).replaceAll(",", ".");
        return decimalFormat.format(value);
    }

    public static String formatDotless(double value){
        return new DecimalFormat("##").format(value).replaceAll(",",".");
    }

    public static String format6(double value){
        return new DecimalFormat("##.######").format(value).replaceAll(",",".");
    }

    public static String toAsciiLetters(String text){
        return text.replaceAll("\u0131", "i").replaceAll("\u0130", "I")
                .replaceAll("\u00FC", "u").replaceAll("\u00DC", "U")
                .replaceAll("\u00F6", "o").replaceAll("\u00D6", "O")
                .replaceAll("\u00E7", "c").replaceAll("\u00C7", "C")
                .replaceAll("\u011F", "g").replaceAll("\u011E", "G")
                .replaceAll("u015F", "s").replaceAll("\u015E", "S")
                ;
    }

    public static String reverse(String text){
        String reverse = StringUtils.reverse(text);
        return reverse;
    }

    /**
     * split equaly size
     * https://stackoverflow.com/questions/3760152/split-string-to-equal-length-substrings-in-java
     * example : "Thequickbrownfoxjumps".split("(?<=\\G.{4})")
     * @param text
     * @param size
     * @return
     */
    public static String[] splitToArray(String text, int size){
        return text.split("(?<=\\G.{"+ size +"})");
    }

    /**
     * split equaly size
     * https://stackoverflow.com/questions/3760152/split-string-to-equal-length-substrings-in-java
     * example : "Thequickbrownfoxjumps".split("(?<=\\G.{4})")
     * @param text
     * @param size
     * @param delimeter
     * @return
     */
    public static String splitToString(String text, int size, String delimeter){
        return listToString(splitToArray(text, size), delimeter);
    }

    public static String listToString(List<String> list, String delimeter){
        String result = "";
        for(String text: list){
            result += text + delimeter;
        }
        return result;
    }

    public static String listToString(String[] list, String delimeter){
        String result = "";
        for(String text: list){
            result += text + delimeter;
        }
        return result;
    }

    public static void testUTF8() throws UnsupportedEncodingException {
        String test = "a" + String.valueOf('\u011F');
        byte[] bytes = test.getBytes();
        byte[] bytes_UTF8 = test.getBytes(UTF_8);
        String hex = NumberUtils.hex(bytes);
        String hex_UTF8 = NumberUtils.hex(bytes_UTF8);
        System.out.println("hex = " + hex + " / hex_UTF8 = " + hex_UTF8);

        test = new String("ağ".getBytes(StandardCharsets.UTF_8), StandardCharsets.ISO_8859_1);
        bytes = test.getBytes();
        bytes_UTF8 = test.getBytes(UTF_8);
        hex = NumberUtils.hex(bytes);
        hex_UTF8 = NumberUtils.hex(bytes_UTF8);
        System.out.println("hex = " + hex + " / hex_UTF8 = " + hex_UTF8);


    }

    public static String String_ISO_8859_1To_UTF_8(byte[] bytes_UTF8) {
        String strA_ISO_8859_1_i = new String(bytes_UTF8, StandardCharsets.ISO_8859_1);
        return String_ISO_8859_1To_UTF_8(strA_ISO_8859_1_i);
    }

    public static String String_ISO_8859_1To_UTF_8(String strISO_8859_1) {
        final StringBuilder stringBuilder = new StringBuilder();
        for (int i = 0; i < strISO_8859_1.length(); i++) {
            final char ch = strISO_8859_1.charAt(i);
            if (ch <= 127)
            {
                stringBuilder.append(ch);
            }
            else
            {
                stringBuilder.append(String.format("%02x", (int)ch));
            }
        }
        String s = stringBuilder.toString();
        int len = s.length();
        byte[] data = new byte[len / 2];
        for (int i = 0; i < len; i += 2) {
            data[i / 2] = (byte) ((Character.digit(s.charAt(i), 16) << 4)
                    + Character.digit(s.charAt(i+1), 16));
        }
        String strUTF_8 =new String(data, StandardCharsets.UTF_8);
        return strUTF_8;
    }

    public static String cleanUnprintableChars(String text, boolean multilanguage)
    {
        String regex = multilanguage ? "[^\\x00-\\xFF]" : "[^\\x00-\\x7F]";
        // strips off all non-ASCII characters
        text = text.replaceAll(regex, "");

        // erases all the ASCII control characters
        text = text.replaceAll("[\\p{Cntrl}&&[^\r\n\t]]", "");

        // removes non-printable characters from Unicode
        text = text.replaceAll("\\p{C}", "");

        return text.trim();
    }

    public static void main(String[] args) {
        try {
            testUTF8();
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
    }
}
