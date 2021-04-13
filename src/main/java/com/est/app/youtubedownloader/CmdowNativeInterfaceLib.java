/* jSSC (Java Simple Serial Connector) - serial port communication library.
 * © Alexey Sokolov (scream3r), 2010-2013.
 *
 * This file is part of jSSC.
 *
 * jSSC is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * jSSC is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with jSSC.  If not, see <http://www.gnu.org/licenses/>.
 *
 * If you use jSSC in public project you can inform me about this by e-mail,
 * of course if you want it.
 *
 * e-mail: scream3r.org@gmail.com
 * web-site: http://scream3r.org | http://code.google.com/p/java-simple-serial-connector/
 */
package com.est.app.youtubedownloader;

import com.est.app.utils.BlankRemover;
import com.est.app.utils.RuntimeUtils;

import java.io.*;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 *
 * @author selami
 */
public class CmdowNativeInterfaceLib {

    private static final String libVersion = "1.0";
    private static final String libMinorSuffix = "0";

    public static final int OS_LINUX = 0;
    public static final int OS_WINDOWS = 1;
    public static final int OS_SOLARIS = 2;
    public static final int OS_MAC_OS_X = 3;

    private static int osType = -1;

    public static String libPath = "";

    private WindowStatus activeWindow = new WindowStatus();
    private WindowStatus lastActiveWindow = activeWindow;

    static {
        String libFolderPath;
        String libName;
        String nativeLibName = "CMDOW";

        String osName = System.getProperty("os.name");
        String architecture = System.getProperty("os.arch");
        String userHome = System.getProperty("user.home");
        String fileSeparator = System.getProperty("file.separator");
        String tmpFolder = System.getProperty("java.io.tmpdir");

        String libRootFolder = new File(userHome).canWrite() ? userHome : tmpFolder;
        String javaLibPath = System.getProperty("java.library.path");

        if(osName.equals("Linux")){
            osName = "linux";
            osType = OS_LINUX;
        }
        else if(osName.startsWith("Win")){
            osName = "windows";
            osType = OS_WINDOWS;
        }
        else if(osName.equals("SunOS")){
            osName = "solaris";
            osType = OS_SOLARIS;
        }
        else if(osName.equals("Mac OS X") || osName.equals("Darwin")){//os.name "Darwin" since 2.6.0
            osName = "mac_os_x";
            osType = OS_MAC_OS_X;
        }

        if(architecture.equals("i386") || architecture.equals("i686")){
            architecture = "x86";
        }
        else if(architecture.equals("amd64") || architecture.equals("universal")){//os.arch "universal" since 2.6.0
            architecture = "x86_64";
        }
        else if(architecture.equals("arm")) {
            String floatStr = "sf";
            if(javaLibPath.toLowerCase().contains("gnueabihf") || javaLibPath.toLowerCase().contains("armhf")){
                floatStr = "hf";
            }
            else {
                try {
                    Process readelfProcess =  Runtime.getRuntime().exec("readelf -A /proc/self/exe");
                    BufferedReader reader = new BufferedReader(new InputStreamReader(readelfProcess.getInputStream()));
                    String buffer = "";
                    while((buffer = reader.readLine()) != null && !buffer.isEmpty()){
                        if(buffer.toLowerCase().contains("Tag_ABI_VFP_args".toLowerCase())){
                            floatStr = "hf";
                            break;
                        }
                    }
                    reader.close();
                }
                catch (Exception ex) {
                    //Do nothing
                }
            }
            architecture = "arm" + floatStr;
        }
        
        libFolderPath = libRootFolder + fileSeparator + ".jcmdow" + fileSeparator + osName;
        libName = "CMDOW-" + libVersion + "_" + architecture;
        libName = System.mapLibraryName(libName);

        libPath = libFolderPath + fileSeparator + libName;
        
        if(libName.endsWith(".dylib")){//Since 2.1.0 MacOSX 10.8 fix
            libName = libName.replace(".dylib", ".jnilib");
        }

        URL url = CmdowNativeInterfaceLib.class.getClassLoader().getResource("libs/cmdow/" + libName);
        if(!new File(libFolderPath + fileSeparator + libName).exists()){
            try {
                RuntimeUtils.copyFile(url, libFolderPath);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

    }

    /**
     * Get OS type (OS_LINUX || OS_WINDOWS || OS_SOLARIS)
     * 
     * @since 0.8
     */
    public static int getOsType() {
        return osType;
    }

    /**
     * Get jSSC version. The version of library is <b>Base Version</b> + <b>Minor Suffix</b>
     *
     * @since 0.8
     */
    public static String getLibraryVersion() {
        return libVersion + "." + libMinorSuffix;
    }

    /**
     * Get jSSC Base Version
     *
     * @since 0.9.0
     */
    public static String getLibraryBaseVersion() {
        return libVersion;
    }

    /**
     * Get jSSC minor suffix. For example in version 0.8.1 - <b>1</b> is a minor suffix
     *
     * @since 0.9.0
     */
    public static String getLibraryMinorSuffix() {
        return libMinorSuffix;
    }

    private List<WindowStatus> list(String table) {
        // TODO Auto-generated constructor stub

        List<WindowStatus> windows = new ArrayList<>();

        try {
            String[] dataTable = table.split("\n");
            for(String  data : dataTable)
            {
                data = BlankRemover.itrim(BlankRemover.lrtrim(data));
                while(data.contains("  ")) {
                    data = data.replace("  ", " ");
                }
                String[] params = data.split(" ");
                if(params[0].toLowerCase().contains("handle"))
                    continue;
                WindowStatus window = new WindowStatus();
                window.Handle 				= params[0];
                window.Lev 					= params[1];
                window.Pid 					= params[2];
                window.WindowMaxMinState 	= params[3];
                window.WindowActiveState 	= params[4];
                window.statusEnableState 	= params[5];
                window.statusVisibleState 	= params[6];
                window.left 				= params[7];
                window.top 					= params[8];
                window.width 				= params[9];
                window.height 				= params[10];
                window.Image 				= params[11];
                for(int i=12; i< params.length; i++)
                    window.Caption += params[i];

                windows.add(window);
            }
        } catch (Exception e) {}

        for(int i = 0; i<windows.size(); i++)
        {
            WindowStatus window = windows.get(i);
            if(window.WindowActiveState.toLowerCase().equals("act"))
            {
                if(!isWindowsEqualsActiveWindow(window))
                {
                    window.startTime = System.currentTimeMillis();
                    activeWindow.stopTime = System.currentTimeMillis();

                    lastActiveWindow = activeWindow;
                    activeWindow = window;
                    // isActiveWindowChanged = true;
                    break;
                }
            }
        }
        return windows;
    }

    private boolean isWindowsEqualsActiveWindow(WindowStatus window) {

        if(activeWindow == null)
            return false;
        if( !window.Handle.toLowerCase().equals(activeWindow.Handle.toLowerCase()))
            return false;
        if( !window.Pid.toLowerCase().equals(activeWindow.Pid.toLowerCase()))
            return false;
        if( !window.Image.toLowerCase().equals(activeWindow.Image.toLowerCase()))
            return false;
        if( !window.Caption.toLowerCase().equals(activeWindow.Caption.toLowerCase()))
            return false;
        return true;
    }

    public WindowStatus getActiveWindow () {
        return activeWindow;
    }

    public WindowStatus getLastActiveWindow () {
        return lastActiveWindow;
    }

    public String run(String params) {
    	String result = "";
		try {
			Process readelfProcess = Runtime.getRuntime().exec(libPath + " " + params);
			BufferedReader reader = new BufferedReader(new InputStreamReader(readelfProcess.getInputStream()));
			String buffer = "";
			while ((buffer = reader.readLine()) != null && !buffer.isEmpty()) {
				result += buffer + "\n";
			}
			reader.close();
		} catch (Exception ex) {
			// Do nothing
		}
		return result;
	}
    
    public String getWindowsList () {
    	String result = run("/T /P"); 
    	return result;
    }

    public String getWindowsList (String caption) {
    	String result = run( "\"" + caption + "\"" + " /F /P");
    	return result;
    }

    public String getWindowsListAll () {
        String result = run( " /F /P");
        return result;
    }

    public void runApp(String filePath) {
    	String result = run("/RUN " + "\"" + filePath + "\"" ); 
    }

    public boolean closeApp(String handle) {
    	String result = run(handle + " /END");
    	return result.length() == 0;
    }

    public boolean hideApp(String handle) {
    	String result = run(handle + " /MIN /DIS /HID /NOT /INA");
    	return result.length() == 0;
    }

    public boolean setOnTop (String handle) {
    	String result = run(handle + " /ENA /VIS /RES /TOP /ACT");
    	return result.length() == 0;
    }

    public boolean actApp(String handle) {
    	String result = run(handle + " /ACT"); 
    	return result.length() == 0;
    }

    public boolean closeWindow(String handle) {
        String result = run(handle + " /CLS");
        return result.length() == 0;
    }

    public WindowStatus getWindow(String caption) {
        List<WindowStatus> windows = getWindows(caption);
        if(windows.isEmpty()) {
            return null;
        }
        return windows.get(0);
    }

    public WindowStatus getWindow(String image, String caption) {
        List<WindowStatus> windows = getWindows(image, caption);
        if(windows.isEmpty()) {
            return null;
        }
        return windows.get(0);
    }

    public List<WindowStatus> getWindows(String caption) {
		String table = getWindowsList(caption);
		//System.out.println(table);		
		return list(table);
	}

    public List<WindowStatus> getWindows(String image, String caption) {
        List<WindowStatus> windowsAll = getWindows(caption);
        List<WindowStatus> windows = windowsAll.stream().filter(window -> window.Image.equals(image) && window.Caption.equals(caption)).collect(Collectors.toList());
        return windows;
    }

    public List<WindowStatus> getWindowsContains(String caption) {
        String table = getWindowsListAll();
        List<WindowStatus> windowsAll = list(table);
        List<WindowStatus> windows = windowsAll.stream().filter(window ->BlankRemover.removeAllBlanks(window.Caption.toLowerCase()).equals(BlankRemover.removeAllBlanks(caption.toLowerCase()))).collect(Collectors.toList());
        return windows;
    }

}
