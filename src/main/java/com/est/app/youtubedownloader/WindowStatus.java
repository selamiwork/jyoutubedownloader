package com.est.app.youtubedownloader;

import java.util.ArrayList;
import java.util.List;

public class WindowStatus {

	public String Handle = "";
	public String Lev = "";
	public String Pid = "";
	public String WindowMaxMinState = "";
	public String WindowActiveState = "";
	public String statusEnableState = "";
	public String statusVisibleState = "";
	public String left;
	public String top;
	public String width;
	public String height;
	public String Image = "";
	public String Caption = "";

	public long startTime = 0;
	public long stopTime = 0;
	
	public WindowStatus() {
		// TODO Auto-generated constructor stub
	}

	public int getLeft() {
		try {
			return Integer.parseInt(left);
		} catch (Exception e) {
			return 0;
		}
	}
	
	public int getTop() {
		try {
			return Integer.parseInt(top);
		} catch (Exception e) {
			return 0;
		}
	}
	
	public long getTimeStayedInMs() {
		return (stopTime - startTime);
	}
	
	public String toInfo() 
	{
		String info = ""
				+ "ActiveState : " + WindowActiveState
				+ "\tImage : " + Image
				+ "\tCaption : " + Caption
				+ "\n"
				;
		return info;
	}
	
	
	@Override
	public String toString() {
		// TODO Auto-generated method stub
		String info = ""
				+ "Handle : " + Handle
				+ " Lev : " + Lev
				+ " Pid : " + Pid
				+ " ActiveState : " + WindowActiveState
				+ " EnableState : " + statusEnableState
				+ " VisibleState : " + statusVisibleState
				+ " left : " + left
				+ " top : " + top
				+ " width : " + width
				+ " height : " + height
				+ " Image : " + Image
				+ " Caption : " + Caption
				;
		return info;
	}
	
}
