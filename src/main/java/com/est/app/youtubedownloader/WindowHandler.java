package com.est.app.youtubedownloader;

import com.est.app.utils.BlankRemover;

import java.util.ArrayList;
import java.util.List;

public class WindowHandler {

	public boolean isActiveWindowChanged = false;
	private List<WindowStatus> windows = new ArrayList<>();
	private WindowStatus activeWindow = new WindowStatus();	
	private WindowStatus lastActiveWindow = activeWindow;	

	public WindowHandler() {
		// TODO Auto-generated constructor stub
	}

	public List<WindowStatus> list(String table) {
		// TODO Auto-generated constructor stub

		windows.clear();
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
					isActiveWindowChanged = true;
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
	
	public WindowStatus getWindow(String caption) {
		for(int i = 0; i<windows.size(); i++)
		{
			WindowStatus window = windows.get(i);
			if( BlankRemover.removeAllBlanks(window.Caption).toLowerCase().contains(BlankRemover.removeAllBlanks(caption).toLowerCase()))
				return window;
		}
		return null;
	}

}
