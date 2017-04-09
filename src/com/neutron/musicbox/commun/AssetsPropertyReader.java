package com.neutron.musicbox.commun;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

import android.content.Context;
import android.content.res.AssetManager;
import android.util.Log;

public class AssetsPropertyReader {
	private static Context context;
	public static Properties properties;

	public static String VIDEO_FOLDER;
	public static String VIDEO_TYPE;
	public static String VIDEO_FOLDER_URI;
	public static int VIDEO_MAX_DURATION;
	public static long VIDEO_MAX_SIZE;

	public AssetsPropertyReader(Context context) {
		this.context = context;
		properties = new Properties();
		
	}
	
	public static Properties getProperties() {

		try {
			AssetManager assetManager = getContext().getAssets();
			InputStream inputStream = assetManager.open("config.properties");
			properties.load(inputStream);

		} catch (IOException e) {
			Log.e("AssetsPropertyReader", e.toString());
		}
		return properties;
	}
	
	public static void initProperties(){
		properties = getProperties();
		VIDEO_FOLDER = properties.getProperty("videoFolder");
		VIDEO_FOLDER_URI = properties.getProperty("videoFolderURI");
		VIDEO_TYPE = properties.getProperty("videoType");
		VIDEO_MAX_DURATION = Integer.parseInt(properties.getProperty("videoMaxDuration"));
	    VIDEO_MAX_SIZE = Long.parseLong(properties.getProperty("videoMaxSize"));
	}

	public static Context getContext() {
		return context;
	}

	public void setContext(Context context) {
		this.context = context;
	}

	public static void setProperties(Properties properties) {
		AssetsPropertyReader.properties = properties;
	}
	
}
