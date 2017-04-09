package com.neutron.musicbox.camera.main;

import java.io.File;
import java.io.FileNotFoundException;

import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.StatFs;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import com.neutron.musicbox.R;
import com.neutron.musicbox.camera.CameraActivity;
import com.neutron.musicbox.commun.AssetsPropertyReader;

public class MainActivity extends Activity {

	private static int RESULT_LOAD_AUDIO = 1;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);

				
			
			try {
				createVideoFolder(AssetsPropertyReader.VIDEO_FOLDER);
			} catch (FileNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
		Button buttonLoadImage = (Button) findViewById(R.id.buttonLoadPicture);
		buttonLoadImage.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// Intent i = new Intent(
				// Intent.ACTION_PICK,
				// android.provider.MediaStore.Audio.Media.EXTERNAL_CONTENT_URI);
				//
				// startActivityForResult(i, RESULT_LOAD_AUDIO);

				getAvailaleStorage();
			}
		});
	}

	public void getAvailaleStorage() {
		StatFs stat = new StatFs(Environment.getExternalStorageDirectory()
				.getPath());
		long bytesAvailable = (long) stat.getBlockSize()
				* (long) stat.getAvailableBlocks();
		long megAvailable = bytesAvailable / (1024 * 1024);
		Log.e("", "Available MB 1: " + megAvailable);

		long freeBytesInternal = new File(getApplicationContext().getFilesDir()
				.getAbsoluteFile().toString()).getFreeSpace();
		long freeBytesExternal = new File(getExternalFilesDir(null).toString())
				.getFreeSpace();

		Log.e("", "Available MB 2: " + freeBytesInternal);
		Log.e("", "Available MB 3: " + freeBytesExternal);

		Log.e("", "Available MB 4: " + getAvailableInternalMemorySize());
		Log.e("", "Available MB 5: " + getAvailableExternalMemorySize());

		aa();
		bb();
	}

	public Boolean isSdCardPresent() {
		Boolean isSDPresent = android.os.Environment.getExternalStorageState()
				.equals(android.os.Environment.MEDIA_MOUNTED);
		return isSDPresent;
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);

		if (requestCode == RESULT_LOAD_AUDIO && resultCode == RESULT_OK
				&& null != data) {
			Uri selectedAudio = data.getData();
			String[] filePathColumn = { MediaStore.Audio.Media.DATA };

			Cursor cursor = getContentResolver().query(selectedAudio,
					filePathColumn, null, null, null);
			cursor.moveToFirst();

			int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
			String audioPath = cursor.getString(columnIndex);
			cursor.close();

			ImageView imageView = (ImageView) findViewById(R.id.imgView);
			imageView.setImageBitmap(BitmapFactory.decodeFile(audioPath));

			Intent i = new Intent(this, CameraActivity.class);
			i.putExtra("audioPath", audioPath);
			startActivity(i);
		}

	}

	public static boolean externalMemoryAvailable() {
		return android.os.Environment.getExternalStorageState().equals(
				android.os.Environment.MEDIA_MOUNTED);
	}

	public static String getAvailableInternalMemorySize() {
		File path = Environment.getDataDirectory();
		StatFs stat = new StatFs(path.getPath());
		long blockSize = stat.getBlockSize();
		long availableBlocks = stat.getAvailableBlocks();
		return formatSize(availableBlocks * blockSize);
	}

	public static String getTotalInternalMemorySize() {
		File path = Environment.getDataDirectory();
		StatFs stat = new StatFs(path.getPath());
		long blockSize = stat.getBlockSize();
		long totalBlocks = stat.getBlockCount();
		return formatSize(totalBlocks * blockSize);
	}

	public static String getAvailableExternalMemorySize() {
		if (externalMemoryAvailable()) {
			File path = Environment.getExternalStorageDirectory();
			StatFs stat = new StatFs(path.getPath());
			long blockSize = stat.getBlockSize();
			long availableBlocks = stat.getAvailableBlocks();
			return formatSize(availableBlocks * blockSize);
		} else {
			return "";
		}
	}

	public static String getTotalExternalMemorySize() {
		if (externalMemoryAvailable()) {
			File path = Environment.getExternalStorageDirectory();
			StatFs stat = new StatFs(path.getPath());
			long blockSize = stat.getBlockSize();
			long totalBlocks = stat.getBlockCount();
			return formatSize(totalBlocks * blockSize);
		} else {
			return "";
		}
	}

	public static String formatSize(long size) {
		String suffix = null;

		if (size >= 1024) {
			suffix = "KB";
			size /= 1024;
			if (size >= 1024) {
				suffix = "MB";
				size /= 1024;
				if (size >= 1024) {
					suffix = "GB";
					size /= 1024;
				}
			}
		}

		StringBuilder resultBuffer = new StringBuilder(Long.toString(size));

		int commaOffset = resultBuffer.length() - 3;
		while (commaOffset > 0) {
			resultBuffer.insert(commaOffset, ',');
			commaOffset -= 3;
		}

		if (suffix != null)
			resultBuffer.append(suffix);
		return resultBuffer.toString();
	}

	public void aa() {
		StatFs statFs = new StatFs(Environment.getExternalStorageDirectory()
				.getAbsolutePath());
		long blockSize = statFs.getBlockSize();
		long totalSize = statFs.getBlockCount() * blockSize;
		long availableSize = statFs.getAvailableBlocks() * blockSize;
		long freeSize = statFs.getFreeBlocks() * blockSize;

		Log.e("", "Available MB 6: " + formatSize(freeSize));
	}

	public void bb() {

		StatFs stat = new StatFs(Environment.getExternalStorageDirectory()
				.getPath());
		long bytesAvailable = (long) stat.getBlockSize()
				* (long) stat.getBlockCount();
		long megAvailable = bytesAvailable / 1048576;
		System.out.println("Megs :" + megAvailable);
		
		Log.e("", "Available MB 7: " + megAvailable);
		Log.e("", "Available MB 8: " + getAvailableSpaceInMB());
		Log.e("", "Available MB 9: " + getAvailableSpaceInGB());
		Log.e("", "Available MB 10: " + megabytesAvailable(new File(Environment.getExternalStorageDirectory().getPath())));
		
		
	}
	public static float megabytesAvailable(File file) {
	    StatFs stat = new StatFs(file.getPath());
	    long bytesAvailable;
	    if(Build.VERSION.SDK_INT >= 18){
	        bytesAvailable = getAvailableBytes(stat);
	    }
	    else{
	        //noinspection deprecation
	        bytesAvailable = stat.getBlockSize() * stat.getAvailableBlocks();
	    }

	    return bytesAvailable / (1024.f * 1024.f);
	}

	@TargetApi(Build.VERSION_CODES.JELLY_BEAN_MR2)
	private static long getAvailableBytes(StatFs stat) {
	    return stat.getBlockSizeLong() * stat.getAvailableBlocksLong();
	}
	public static long getAvailableSpaceInGB(){
	    final long SIZE_KB = 1024L;
	    final long SIZE_GB = SIZE_KB * SIZE_KB * SIZE_KB;
	    long availableSpace = -1L;
	    StatFs stat = new StatFs(Environment.getExternalStorageDirectory().getPath());
	    availableSpace = (long) stat.getAvailableBlocks() * (long) stat.getBlockSize();
	    return availableSpace/SIZE_GB;
	}
	
	/**
	 * @return Number of Mega bytes available on External storage
	 */
	public static long getAvailableSpaceInMB(){
	    final long SIZE_KB = 1024L;
	    final long SIZE_MB = SIZE_KB * SIZE_KB;
	    long availableSpace = -1L;
	    StatFs stat = new StatFs(Environment.getExternalStorageDirectory().getPath());
	    availableSpace = (long) stat.getAvailableBlocks() * (long) stat.getBlockSize();
	    return availableSpace/SIZE_MB;
	}
	/**
	 * @param dirName
	 * @throws FileNotFoundException
	 */
	public void createVideoFolder(String dirName) throws FileNotFoundException{
		File myDirectory;
		if(isSdCardPresent())
			myDirectory = new File(Environment.getExternalStorageDirectory(), dirName);
		else
			myDirectory = new File(getApplicationContext().getFilesDir()+"/", dirName);
		
		if(!myDirectory.exists()) {                                 
		  myDirectory.mkdirs();
		}
		
	}
}