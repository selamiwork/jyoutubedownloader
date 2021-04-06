package com.est.app.utils;

import org.apache.commons.io.FileUtils;
import org.reflections8.Reflections;
import org.reflections8.scanners.ResourcesScanner;

import java.awt.*;
import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Set;
import java.util.regex.Pattern;

public class RuntimeUtils {
    public static void shutdownPC(){
        try {
            Runtime runtime = Runtime.getRuntime();
            Process proc = runtime.exec("shutdown -s -t 0");
            TimeUtils.waitInMilis(1000);
            System.exit(0);
        } catch (IOException e) {
            System.out.println("Shutdown:" +  Arrays.toString(e.getStackTrace()));
        }
    }

    public static void openFileLocation(File file) throws IOException {
        Runtime.getRuntime().exec("explorer.exe /select," + file.getAbsolutePath());
    }

    public static void openFile(File file) throws IOException {
        Desktop desktop = null;
        if (Desktop.isDesktopSupported()) {
            desktop = Desktop.getDesktop();
            desktop.open(file);
        }else
        {
            //Runtime.getRuntime().exec("cmd /c start \"\" " + file.getAbsolutePath() + "\"");
            Runtime.getRuntime().exec("cmd /c start " + file.getAbsolutePath());
        }
    }

    public static void openFile(String app, File file) throws IOException {
        //Runtime.getRuntime().exec("cmd /c start \"\" " + file.getAbsolutePath() + "\"");
        Runtime.getRuntime().exec(app + " " + file.getAbsolutePath());
    }

    public static void openFile(URL resource) throws IOException {
        //open the folder and files in windows defaults
        File open = copyFile(resource);
        openFile(open);
    }

    public static void openFile(String app, URL resource) throws IOException {
        //open the folder and files in windows defaults
        File open = copyFile(resource);
        openFile(app, open);
    }

    public static File copyFile(URL resource) throws IOException {
        String tempDirectory = System.getProperty("java.io.tmpdir")
                + System.getProperty("file.separator") + "jyoutubedownloader" + System.getProperty("file.separator");
        File open = copyFile(resource, tempDirectory);
        return open;
    }

    public static File copyFile(URL resource, String targetDirectory) throws IOException {
        File file = FileUtils.getFile(resource.getPath());
        if (!FileHelper.fileExists(targetDirectory))
            FileHelper.createDirectory(targetDirectory);
        File open = new File(targetDirectory + System.getProperty("file.separator") + file.getName());
        //if (!FileHelper.fileExists(open.getAbsolutePath()))
        try {
            FileHelper.copy(resource.openStream(), open);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return open;
    }

    public static List<File> getFiles(String directory) throws IOException {
        List<File> files = new ArrayList<>();
        Reflections reflections = new Reflections(directory, new ResourcesScanner());
        //Set<String> paths = reflections.getAllTypes();
        Set<String> paths = reflections.getResources(Pattern.compile("(.*$)"));
       // Set<String> pathsPDF = reflections.getResources(Pattern.compile("(.*\\.pdf$)"));
        for(String path : paths)
        {
            System.out.println("found path = " + path);
            URL resource = RuntimeUtils.class.getClassLoader().getResource(path);
            File file = copyFile(resource);
            files.add(file);
        }
        return files;
    }

    private static String getOutputs(Process process) throws IOException {
        String result = "";
        BufferedReader stdInput = new BufferedReader(new
                InputStreamReader(process.getInputStream()));

        BufferedReader stdError = new BufferedReader(new
                InputStreamReader(process.getErrorStream()));

        //result += "Here is the standard output of the command:\n";
        String s = null;
        while ((s = stdInput.readLine()) != null) {
            result += s + "\n";
        }
        //result += "Here is the standard error of the command (if any):\n";
        while ((s = stdError.readLine()) != null) {
            result += s + "\n";
        }
        return result;
    }
    public static String runBatchAsAdministrator(String text) throws IOException {
        Process process = Runtime.getRuntime().exec("cmd /c Rundll32.exe " + text + "");
        return getOutputs(process);
    }
    public static String runBatch(String text) throws IOException {
        Process process = Runtime.getRuntime().exec("cmd /c " + text + "");
        return getOutputs(process);
    }
    public static String runBatchFileAsAdministrator(File file) throws IOException {
        //Process process = Runtime.getRuntime().exec("cmd /c Rundll32.exe \"\"" + file.getAbsolutePath() + "\"");
        //Process process = Runtime.getRuntime().exec("runas /profile /user:Administrator /savecred \"cmd.exe /c \"\"" + file.getAbsolutePath() + "\"");
        Process process = Runtime.getRuntime().exec("Elevate.exe cmd /c  \"\"" + file.getAbsolutePath() + "\"");
        return getOutputs(process);
    }
    public static String runBatchFile(File file) throws IOException {
        Process process = Runtime.getRuntime().exec("cmd /c  \"\"" + file.getAbsolutePath() + "\"");
        return getOutputs(process);
    }

    public static String sleep() throws IOException {
        return runBatch("rundll32.exe powrprof.dll,SetSuspendState 0,1,0");
    }

    public static String disableAllSleepParamters() throws IOException {
        String cmd = "powercfg /x -hibernate-timeout-ac 0\n" +
                "powercfg /x -hibernate-timeout-dc 0\n" +
                "powercfg /x -disk-timeout-ac 0\n" +
                "powercfg /x -disk-timeout-dc 0\n" +
                "powercfg /x -monitor-timeout-ac 0\n" +
                "powercfg /x -monitor-timeout-dc 0\n" +
                "Powercfg /x -standby-timeout-ac 0\n" +
                "powercfg /x -standby-timeout-dc 0";

        String result = "";
        String lines[] = cmd.split("\n");
        for(String line : lines){
            result += runBatch(line);
        }
        return result;
    }

    public static void main(String[] args) {
        System.out.println("Working Directory = " + System.getProperty("user.dir"));
    }

}
