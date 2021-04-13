package com.est.app.utils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.prefs.Preferences;

/**
 * 
 * @version 1.0
 */
public class Options {

	private Logger logger = LoggerFactory.getLogger(getClass());

	private static String fileSeparator = System.getProperty("file.separator");

	public static String ApplicationName = "jyoutubedownloader";

	public static String ApplicationTempDirectory = System.getProperty("java.io.tmpdir")
			+ fileSeparator + ApplicationName;
	public static String ApplicationUserDirectory = System.getProperty("user.home")
			+ fileSeparator + "." + ApplicationName;


	public static String ApplicationSettingsFile = ApplicationUserDirectory  + fileSeparator + "settings.xml";

	// users preferences for this application
	private static Preferences preferences = Preferences.userRoot();

	/**
	 * The user's preferences.
	 * 
	 * @return
	 */
	public static Preferences getPreferences() {
		try {
			preferences.sync();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return preferences;
	}

	public static void savePreferences() {
		try {
			getPreferences().flush();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public synchronized static void clearPreferences ()
	{
		try {
			preferences = Preferences.userRoot();
			preferences.sync();
			preferences.clear();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			System.out.println("SystemPreferences.clearPreferences()" + e.toString());
		}
	}
	/**
	 *
	 * @return
	 */
	public synchronized static Locale getDefaultLanguage() {
		Locale sysDefault = Locale.getDefault();
		String language = getPreferences().get(ApplicationName + ".system.settings.language", sysDefault.getLanguage());
		return new Locale(language);
	}

	public synchronized static void setDefaultLanguage(Locale language) {
		getPreferences().put(ApplicationName + ".system.settings.language", language.getLanguage());
	}

	public synchronized static String getDefaultDirectory() {
		String directory = getPreferences().get(ApplicationName + ".system.settings.directory",
				System.getProperty("user.home") + fileSeparator + "videos" + fileSeparator + ApplicationName);
		return directory;
	}

	public synchronized static void setDefaultDirectory(String directory) {
		getPreferences().put(ApplicationName + ".system.settings.directory", directory);
	}

	public synchronized static String getDefaultLinks() {
		String link = getPreferences().get(ApplicationName + ".system.settings.link",
				"https://www.youtube.com/watch?v=QCF-LYKxnfs\n" +
						"https://www.youtube.com/watch?v=DH8mS70yf-8\n" +
						"https://www.youtube.com/watch?v=8XAmy9Nb9AU\n" +
						"https://www.youtube.com/watch?v=FigYY1nPZvU\n");
		return link;
	}

	public synchronized static void setDefaultLinks(String link) {
		getPreferences().put(ApplicationName + ".system.settings.link", link);
	}


}
