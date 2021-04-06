package com.est.app.utils;

public class BlankRemover {
  
    private BlankRemover () {}

    /* remove leading whitespace */
    public static String ltrim(String source) {
        return source.replaceAll("^\\s+", "");
    }

    /* remove trailing whitespace */
    public static String rtrim(String source) {
        return source.replaceAll("\\s+$", "");
    }

    /* replace multiple whitespaces between words with single blank */
    public static String itrim(String source) {
        return source.replaceAll("\\b\\s{2,}\\b", " ");
    }

    /* remove all superfluous whitespaces in source string */
    public static String trim(String source) {
        return itrim(ltrim(rtrim(source)));
    }

    public static String lrtrim(String source){
        return ltrim(rtrim(source));
    }

    public static String removeAllBlanks(String source)
    {
    	return BlankRemover.itrim(BlankRemover.lrtrim(source)).replaceAll(" ", "");
    }
    
    public static void main(String[] args){
        String oldStr =
         "------[1-2-1-2-1-2-1-2-1-2-1-----2-1-2-1-2-1-2-1-2-1-2-1-2]----";
        String newStr = oldStr.replaceAll("-", " ");
        System.out.println(newStr);
        System.out.println("*" + BlankRemover.ltrim(newStr) + "*");
        System.out.println("*" + BlankRemover.rtrim(newStr) + "*");
        System.out.println("*" + BlankRemover.itrim(newStr) + "*");
        System.out.println("*" + BlankRemover.lrtrim(newStr) + "*");

        System.out.println("*" + BlankRemover.itrim(BlankRemover.lrtrim(newStr)) + "*");
}
    /*
    output :
          [1 2 1 2 1 2 1 2 1 2 1     2 1 2 1 2 1 2 1 2 1 2 1 2]    
    *[1 2 1 2 1 2 1 2 1 2 1     2 1 2 1 2 1 2 1 2 1 2 1 2]    *
    *      [1 2 1 2 1 2 1 2 1 2 1     2 1 2 1 2 1 2 1 2 1 2 1 2]*
    *      [1 2 1 2 1 2 1 2 1 2 1 2 1 2 1 2 1 2 1 2 1 2 1 2]    *
    *[1 2 1 2 1 2 1 2 1 2 1     2 1 2 1 2 1 2 1 2 1 2 1 2]*
    
    */
}
