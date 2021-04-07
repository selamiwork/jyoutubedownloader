package com.est.app.youtubedownloader;

import java.awt.AWTException;
import java.awt.Dimension;
import java.awt.Robot;
import java.awt.Toolkit;
import java.awt.event.InputEvent;
import java.awt.event.KeyEvent;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import com.est.app.items.ProgressbarTool;
import com.est.app.utils.ClipBoard;
import com.est.app.utils.RuntimeUtils;
import com.est.app.utils.TimeUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author selami
 */
public class YoutubeDownloader implements Runnable {
	private Logger logger = LoggerFactory.getLogger(getClass());

	private String libFolderPath = "";
    public static String libPath = "";
	private CmdowNativeInterfaceLib cmdowNativeInterfaceLib = new CmdowNativeInterfaceLib();
	private Robot robot;
    private ClipBoard clipBoard = new ClipBoard();
	private boolean shutdownDownloader = false;
	private Thread serverThread;
	private boolean initialized = false;

	/**
	 * Handle : 0x0E0E08	Lev : 1	Pid : 7212	ActiveState : Act	left : 638	top : 287	width : 708	height : 505	Image : YouTubeD	Caption : YouTubeDo
	 */
	private Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
	private int x_left = 638, y_top = 287;
	private int X_videoURL = (800 - x_left);  		// left 960, url 800
	private int Y_videoURL = (480 - y_top);  		// top 540, url 480
	private int X_videoQuality = (800 - x_left);  	// left 960, url 800
	private int Y_videoQuality = (545 - y_top);  	// top 540, url 545
	private int X_Saveto = (800 - x_left);  		// left 960, url 800
	private int Y_Saveto = (635 - y_top);  			// top 540, url 635
	private int X_Download = (1290 - x_left);  		// left 960, url 1290
	private int Y_Download = (740 - y_top);  		// top 540, url 740
	private int X_DownloadComplete = (1120 - 809);  // left 809, url 1120
	private int Y_DownloadComplete = (585 - 467);  	// top 467, url 585

	private int x_Current, y_Current, x_Last, y_Last;
    private String fileSeparator = System.getProperty("file.separator");
    
	private List<String> libFiles = Arrays.asList("libeay32.dll", "license.txt", "OpenSSL License.txt", "ssleay32.dll", "YouTubeDownloaderHD.exe", "YouTubeDownloaderHD.ini" );
	private List<String> qualityList = Arrays.asList(
			"Low Quality - 240p",
			"Normal Quality - 360p",
			"High Quality - 480p",
			"HD - 720p",
			"HD - 720p 60FPS",
			"Full HD - 1080p",
			"Full HD - 1080p 60FPS",
			"Quad HD - 1440p 30/60FPS",
			"4K Ultra HD - 2160p 30/60FPS"
			);
	private List<String> downloadList = new ArrayList<>();

	private List<String> downloadListTest = Arrays.asList(
			"https://www.youtube.com/watch?v=QCF-LYKxnfs",
			"https://www.youtube.com/watch?v=DH8mS70yf-8",
			"https://www.youtube.com/watch?v=8XAmy9Nb9AU",
			"https://www.youtube.com/watch?v=FigYY1nPZvU"
	);

	private int qualityIndex = 5;
	private String defaultSavePath = System.getProperty("user.home") + fileSeparator + "videos" + fileSeparator + "jyoutubedownloader";
	private String savePath = defaultSavePath;
	private String windowCaption = "YouTube Downloader HD";
	private String windowNotifySinkImage = "YouTubeDownloaderHD";
	private String windowNotifySinkCaption = "Information";

	private ProgressbarTool progressbarTool;

	public YoutubeDownloader(){
	}

	public void initialize() throws IOException {
		setSavePath(defaultSavePath);
		try {
			robot = new Robot();
		} catch (AWTException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return;
		}

		String userHome = System.getProperty("user.home");
		String tmpFolder = System.getProperty("java.io.tmpdir");

		String libRootFolder = new File(userHome).canWrite() ? userHome : tmpFolder;
		libFolderPath = libRootFolder + fileSeparator + ".jyoutubedownloaderhd" + fileSeparator + "1.0";
		libPath = libFolderPath + fileSeparator + "YouTubeDownloaderHD.exe";

		for(String libName : libFiles) {
			URL url = getClass().getClassLoader().getResource("libs/youtubedownloaderhd/" + libName);
			if(!new File(libFolderPath + fileSeparator + libName).exists()){
				RuntimeUtils.copyFile(url, libFolderPath);
			}
		}

		initialized = true;
	}

	private void closeApp(){
		List<WindowStatus> windows = cmdowNativeInterfaceLib.getWindows(windowCaption);
		for(WindowStatus window : windows) {
			cmdowNativeInterfaceLib.closeApp(window.Handle);
		}
	}

    private WindowStatus load () {
		closeApp();
    	cmdowNativeInterfaceLib.runApp(libPath);
    	WindowStatus window = null;
		while(window == null) {
			window = cmdowNativeInterfaceLib.getWindow(windowCaption);
			TimeUtils.waitInMilis(250);
		}

    	return window;
    }

	private void click(int x_Move, int y_Move) {
    	x_Last = x_Current;
    	y_Last = y_Current;
    	int retry = 0;
    	while(retry ++ < 10 && x_Current != x_Move && y_Current != y_Move){
			robot.mouseMove(x_Move, y_Move);
			TimeUtils.waitInMilis(10);
		}
		robot.mousePress(InputEvent.BUTTON1_MASK);
		TimeUtils.waitInMilis(100);
		robot.mouseRelease(InputEvent.BUTTON1_MASK);
		if(x_Last > 0 && y_Last > 0) {
			robot.mouseMove(x_Last, y_Last);
		}
    }

	private void write(WindowStatus window, String content, int x, int y) {
	    clipBoard.setClipboardContents(content);
		click(window.getLeft() + x, window.getTop() + y);
		robot.keyPress(KeyEvent.VK_CONTROL);
		robot.keyPress(KeyEvent.VK_A);
		robot.keyRelease(KeyEvent.VK_A);
		TimeUtils.waitInMilis(10);
		robot.keyPress(KeyEvent.VK_V);
		robot.keyRelease(KeyEvent.VK_CONTROL);
		robot.keyRelease(KeyEvent.VK_V);
    }

	public void launch(){
		shutdownDownloader = false;
		serverThread = new Thread(this);
		serverThread.setDaemon(false);
		serverThread.start();
	}

	/**
	 * Stop the udp server (and any currently running transfers) and release all
	 * opened network resources.
	 */
	public void shutdown() {
		shutdownDownloader = true;
		closeApp();
		try {
			if(serverThread != null){
				serverThread.join();
			}
		} catch (InterruptedException e) {
			// we've done the best we could, return
		}
		System.out.println("shutdownDownloader = " + shutdownDownloader);
	}

	public void run() {
		int  wait = 500, flag = 0;	

		try {
			WindowStatus window = load();
			TimeUtils.waitInMilis(100);

			progressbarTool.setTotalProgress(downloadList.size());
			progressbarTool.setProcessing(true);
			progressbarTool.setProgress(0);

			while(!shutdownDownloader)
			{
				window = cmdowNativeInterfaceLib.getWindow(windowCaption);
				if(window != null) {
					System.out.println(window.toString());
					//if(true) continue;
					cmdowNativeInterfaceLib.setOnTop(window.Handle);
					TimeUtils.waitInMilis(100);
					window = cmdowNativeInterfaceLib.getWindow(windowCaption);
					if(window != null) {
						click(window.getLeft() + X_videoQuality, window.getTop() + Y_videoQuality);
						TimeUtils.waitInMilis(100);
						click(window.getLeft() + X_videoQuality, window.getTop() + Y_videoQuality + qualityIndex * 21);

						write(window, savePath, X_Saveto, Y_Saveto);
						cmdowNativeInterfaceLib.hideApp(window.Handle);

						break;
					}
				}
				TimeUtils.waitInMilis(wait);
			}

			int i = 0;
			while(!shutdownDownloader)
			{
				window = cmdowNativeInterfaceLib.getWindow(windowCaption);
				if(window != null) {
					System.out.println(window.toString());
					cmdowNativeInterfaceLib.setOnTop(window.Handle);
					TimeUtils.waitInMilis(200);
					window = cmdowNativeInterfaceLib.getWindow(windowCaption);
					if(window != null) {
						progressbarTool.setTotalProgress(downloadList.size()); //update total
						String link = downloadList.get(i);
						System.out.println("Downloading: " + link);
						write(window, link, X_videoURL, Y_videoURL);
						click(window.getLeft() + X_Download, window.getTop() + Y_Download);

						cmdowNativeInterfaceLib.hideApp(window.Handle);

						while(!shutdownDownloader) {
							List<WindowStatus> windows = cmdowNativeInterfaceLib.getWindows(windowCaption);
							if(windows.get(1).statusEnableState.toLowerCase().equals("ena")) {
								break;
							}
							TimeUtils.waitInMilis(500);
						}

						while(!shutdownDownloader) {
							TimeUtils.waitInMilis(1000);
							//cmdowNativeInterfaceLib.actApp(window.Handle);
							WindowStatus windowNotify = cmdowNativeInterfaceLib.getWindow(windowNotifySinkImage, windowNotifySinkCaption);
							if(windowNotify == null){
								continue;
							}
							cmdowNativeInterfaceLib.setOnTop(windowNotify.Handle);
							TimeUtils.waitInMilis(200);
							click(windowNotify.getLeft() + X_DownloadComplete, windowNotify.getTop() + Y_DownloadComplete);
							System.out.println("completed: " + link);
							progressbarTool.addProgress(1);
							if(++ i >= downloadList.size()) {
								System.out.println("Download all completed.");
								removeUnprintableCharsFromFileName();
								shutdownDownloader = true;
							}
							break;
						}

					}
				}
				TimeUtils.waitInMilis(wait);
			}

			// TEST
			while(!shutdownDownloader)
			{
				window = cmdowNativeInterfaceLib.getWindow(windowCaption);
				if(window != null) {
					System.out.println(window.toString());
					cmdowNativeInterfaceLib.setOnTop(window.Handle);
					TimeUtils.waitInMilis(100);
					window = cmdowNativeInterfaceLib.getWindow(windowCaption);
					if(window != null) {
						write(window, "blah, blah, blah " + flag++, X_videoURL, Y_videoURL);

						TimeUtils.waitInMilis(3000);
						cmdowNativeInterfaceLib.hideApp(window.Handle);
					}
				}
				TimeUtils.waitInMilis(wait);
			}

			closeApp();

		} catch (Exception e) {
			e.printStackTrace();
		}
		shutdownDownloader = true; // set this to true, so the launching thread can
		progressbarTool.setProcessing(false);
	}

	private void removeUnprintableCharsFromFileName(){
		// Creates an array in which we will store the names of files and directories
		String[] pathnames;

		// Creates a new File instance by converting the given pathname string
		// into an abstract pathname
		File f = new File(savePath);

		// Populates the array with names of files and directories
		pathnames = f.list();

		// For each pathname in the pathnames array
		for (String pathname : pathnames) {
			String rename = pathname.replaceAll("[\\p{Cc}\\p{Cf}\\p{Co}\\p{Cn}]", "");
			//System.out.println(pathname  + " / " + rename );
			try {
				new File(savePath + fileSeparator + pathname).renameTo(new File(savePath + fileSeparator + rename));
			} catch (Exception e) {
				//e.printStackTrace();
			}
		}
	}

	public void setX_Current(int x_Current) {
		this.x_Current = x_Current;
	}

	public void setY_Current(int y_Current) {
		this.y_Current = y_Current;
	}

	public List<String> getQualityList() {
		return qualityList;
	}

	public List<String> getDownloadList() {
		return downloadList;
	}

	public void setDownloadList(List<String> downloadList) {
		this.downloadList = downloadList;
	}

	public void setQualityIndex(int qualityIndex) {
		this.qualityIndex = qualityIndex;
	}

	public void setProgressbarTool(ProgressbarTool progressbarTool) {
		this.progressbarTool = progressbarTool;
	}

	public String getSavePath() {
		return savePath;
	}

	public void setSavePath(String savePath) {
		this.savePath = savePath == null || savePath.isEmpty() ? defaultSavePath : savePath;
		try {
			File file = new File(savePath);
			if(!file.exists()){
				file.mkdirs();
			}
		} catch (Exception e) {
			// TODO: handle exception
			System.out.println("Ecception occured : FileHelper.createDirectory() : " + savePath + " : "+ e);
		}
	}

	public boolean isInitialized() {
		return initialized;
	}

	/**
	 * TEST purpose
	 * @param args
	 * @throws IOException
	 */
	public static void main(String[] args) throws IOException {
		new YoutubeDownloader().removeUnprintableCharsFromFileName();
	}
	
}
