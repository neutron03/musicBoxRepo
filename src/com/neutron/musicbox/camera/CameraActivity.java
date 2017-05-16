package com.neutron.musicbox.camera;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.pm.PackageManager;
import android.hardware.Camera;
import android.hardware.Camera.CameraInfo;
import android.media.AudioManager;
import android.media.CamcorderProfile;
import android.media.MediaPlayer;
import android.media.MediaRecorder;
import android.media.MediaRecorder.OnInfoListener;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.WindowManager;
import android.view.animation.OvershootInterpolator;
import android.view.animation.ScaleAnimation;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.Toast;

import com.neutron.musicbox.R;
import com.neutron.musicbox.commun.AssetsPropertyReader;

public class CameraActivity extends Activity {
	private Camera mCamera;
	private CameraPreview mPreview;
	private MediaRecorder mediaRecorder;
	private Button capture, switchCamera;
	private Context myContext;
	private LinearLayout cameraPreview;
	private boolean cameraFront = false;
	private TableLayout tableLayout;
	MediaPlayer mPlayer;
	ImageView recordimageView;
	ImageView redSpotImageView;
	int redSpotVisibility = 1;
	public static AssetsPropertyReader assetsPropertyReader;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
		myContext = this;
		initialize();
		assetsPropertyReader = new AssetsPropertyReader(this);
		AssetsPropertyReader.initProperties();
	}

	private int findFrontFacingCamera() {
		int cameraId = -1;
		// Search for the front facing camera
		int numberOfCameras = Camera.getNumberOfCameras();
		for (int i = 0; i < numberOfCameras; i++) {
			CameraInfo info = new CameraInfo();
			Camera.getCameraInfo(i, info);
			if (info.facing == CameraInfo.CAMERA_FACING_FRONT) {
				cameraId = i;
				cameraFront = true;
				break;
			}
		}
		return cameraId;
	}

	private int findBackFacingCamera() {
		int cameraId = -1;
		// Search for the back facing camera
		// get the number of cameras
		int numberOfCameras = Camera.getNumberOfCameras();
		// for every camera check
		for (int i = 0; i < numberOfCameras; i++) {
			CameraInfo info = new CameraInfo();
			Camera.getCameraInfo(i, info);
			if (info.facing == CameraInfo.CAMERA_FACING_BACK) {
				cameraId = i;
				cameraFront = false;
				break;
			}
		}
		return cameraId;
	}

	public void onResume() {
		super.onResume();
		if (!hasCamera(myContext)) {
			Toast toast = Toast.makeText(myContext,
					"Sorry, your phone does not have a camera!",
					Toast.LENGTH_LONG);
			toast.show();
			finish();
		}
		if (mCamera == null) {
			// if the front facing camera does not exist
			if (findFrontFacingCamera() < 0) {
				Toast.makeText(this, "No front facing camera found.",
						Toast.LENGTH_LONG).show();
				switchCamera.setVisibility(View.GONE);
			}
			mCamera = Camera.open(findBackFacingCamera());
			mPreview.refreshCamera(mCamera);
		}
	}

	public void initCamera() {
		cameraPreview = (LinearLayout) findViewById(R.id.camera_preview);
		mPreview = new CameraPreview(myContext, mCamera);
		cameraPreview.addView(mPreview);
	}

	public void initRecordButton() {
		tableLayout = (TableLayout) findViewById(R.id.bottomTableLayout);
		TableRow row = (TableRow) tableLayout.getChildAt(0);
		recordimageView = (ImageView) row.getChildAt(1);

		int displayHeight = getWindowManager().getDefaultDisplay().getHeight();
		int displayWidth = getWindowManager().getDefaultDisplay().getWidth();
		recordimageView.getLayoutParams().height = displayWidth / 3;
		recordimageView = (ImageView) findViewById(R.id.recordButton);
		recordimageView.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub

			}
		});

	}

	public void initRedSpot() {
		tableLayout = (TableLayout) findViewById(R.id.topTableLayout);
		TableRow row = (TableRow) tableLayout.getChildAt(0);
		redSpotImageView = (ImageView) row.getChildAt(0);

		int displayHeight = getWindowManager().getDefaultDisplay().getHeight();
		int displayWidth = getWindowManager().getDefaultDisplay().getWidth();
		redSpotImageView.getLayoutParams().height = displayWidth / 11;
		redSpotImageView.getLayoutParams().width = displayWidth / 50;
		redSpotImageView = (ImageView) findViewById(R.id.redSpot);
	}

	public ScaleAnimation getScaleAnimation() {
		final ScaleAnimation scale = new ScaleAnimation(0, 1, 0, 1,
				ScaleAnimation.RELATIVE_TO_SELF, .5f,
				ScaleAnimation.RELATIVE_TO_SELF, .5f);
		scale.setDuration(300);
		scale.setInterpolator(new OvershootInterpolator());
		return scale;
	}

	public void showHideRedSpotAnimation() {
		WhileRecordingAsyncTask a = new WhileRecordingAsyncTask();
		a.execute();
	}

	public void initialize() {
		setVolumeToMax();
		initCamera();
		initRecordButton();
		initRedSpot();
		showHideRedSpotAnimation();
		recordimageView.setOnClickListener(captrureListener);
		// capture = (Button) findViewById(R.id.button_ChangeCamera);
		// capture.setOnClickListener(captrureListener);
		// switchCamera = (Button) findViewById(R.id.button_ChangeCamera);
		// switchCamera.setOnClickListener(switchCameraListener);
	}

	// OnClickListener switchCameraListener = new OnClickListener() {
	// @Override
	// public void onClick(View v) {
	// // get the number of cameras
	// if (!recording) {
	// int camerasNumber = Camera.getNumberOfCameras();
	// if (camerasNumber > 1) {
	// // release the old camera instance
	// // switch camera, from the front and the back and vice versa
	//
	// releaseCamera();
	// chooseCamera();
	// } else {
	// Toast toast = Toast.makeText(myContext,
	// "Sorry, your phone has only one camera!",
	// Toast.LENGTH_LONG);
	// toast.show();
	// }
	// }
	// }
	// };

	public void chooseCamera() {
		// if the camera preview is the front
		if (cameraFront) {
			int cameraId = findBackFacingCamera();
			if (cameraId >= 0) {
				// open the backFacingCamera
				// set a picture callback
				// refresh the preview

				mCamera = Camera.open(cameraId);
				// mPicture = getPictureCallback();
				mPreview.refreshCamera(mCamera);
			}
		} else {
			int cameraId = findFrontFacingCamera();
			if (cameraId >= 0) {
				// open the backFacingCamera
				// set a picture callback
				// refresh the preview

				mCamera = Camera.open(cameraId);
				// mPicture = getPictureCallback();
				mPreview.refreshCamera(mCamera);
			}
		}
	}

	@Override
	protected void onPause() {
		super.onPause();
		// when on Pause, release camera in order to be used from other
		// applications
		releaseCamera();
	}

	private boolean hasCamera(Context context) {
		// check if the device has camera
		if (context.getPackageManager().hasSystemFeature(
				PackageManager.FEATURE_CAMERA)) {
			return true;
		} else {
			return false;
		}
	}

	boolean recording = false;

	OnClickListener captrureListener = new OnClickListener() {
		@Override
		public void onClick(View v) {
			if (recording) {
				// stop recording and release camera
				mediaRecorder.stop(); // stop the recording
				releaseMediaRecorder(); // release the MediaRecorder object
				Toast.makeText(CameraActivity.this, "Video captured!",
						Toast.LENGTH_LONG).show();
				recording = false;

				SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd_HHmmss");
				String currentDateandTime = sdf.format(new Date());

				final EditText input = new EditText(CameraActivity.this);
				input.setText(currentDateandTime+".mp4");
				input.setSelectAllOnFocus(true);
				input.selectAll();
				final File dir = new File(assetsPropertyReader.VIDEO_FOLDER_URI);
				final File from = new File(dir,"newVideo.mp4");
				
				AlertDialog.Builder builder = new AlertDialog.Builder(
						CameraActivity.this);
				builder.setView(input);
				builder.setMessage("Video name")
						.setCancelable(false)
						.setPositiveButton("Yes",
								new DialogInterface.OnClickListener() {
									public void onClick(DialogInterface dialog,
											int id) {
										
										if(dir.exists()){
										    
										    File to = new File(dir,input.getText().toString());
										     if(from.exists())
										        from.renameTo(to);
										}	
										dialog.cancel();
									}
								})
						.setNegativeButton("Cancel",
								new DialogInterface.OnClickListener() {
									public void onClick(DialogInterface dialog,
											int id) {
										dialog.cancel();
									}
								});
				AlertDialog alert = builder.create();
				alert.show();

				// mediaRecorder.setOutputFile(AssetsPropertyReader.VIDEO_FOLDER_URI+"myvideo.mp4");

			} else {
				if (!prepareMediaRecorder()) {
					Toast.makeText(CameraActivity.this,
							"Fail in prepareMediaRecorder()!\n - Ended -",
							Toast.LENGTH_LONG).show();
					finish();
				}
				// work on UiThread for better performance
				runOnUiThread(new Runnable() {
					public void run() {
						// If there are stories, add them to the table

						try {

							mediaRecorder.start();

							String selectedSong = getIntent().getStringExtra(
									"selectedSong");

							Log.i("aaaaaaaaaa", selectedSong);
							mPlayer = new MediaPlayer();
							Uri myUri = Uri.parse("file://" + selectedSong);
							mPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);

							mPlayer.setDataSource(getApplicationContext(),
									myUri);
							mPlayer.prepare();
							mPlayer.start();

						} catch (final Exception ex) {
							Log.i("aze", "aze");
							// Log.i("---","Exception in thread");
						}
					}
				});

				recording = true;
			}
		}
	};

	private void releaseMediaRecorder() {
		if (mediaRecorder != null) {
			mediaRecorder.reset(); // clear recorder configuration
			mediaRecorder.release(); // release the recorder object
			mediaRecorder = null;
			mCamera.lock(); // lock camera for later use

			mPlayer.reset();
			mPlayer.release();
			mPlayer = null;

		}
	}

	private boolean prepareMediaRecorder() {

		mediaRecorder = new MediaRecorder();

		mCamera.unlock();
		mediaRecorder.setCamera(mCamera);

		mediaRecorder.setAudioSource(MediaRecorder.AudioSource.CAMCORDER);
		mediaRecorder.setVideoSource(MediaRecorder.VideoSource.CAMERA);

		mediaRecorder.setProfile(CamcorderProfile
				.get(CamcorderProfile.QUALITY_720P));

		//mediaRecorder.setOutputFile("/sdcard/myvideo.mp4"); 
		mediaRecorder.setOutputFile(assetsPropertyReader.VIDEO_FOLDER_URI+"newVideo.mp4");
		mediaRecorder.setMaxDuration(AssetsPropertyReader.VIDEO_MAX_DURATION); // Set
																				// max
																				// duration
																				// 60
																				// sec.
		mediaRecorder.setMaxFileSize(AssetsPropertyReader.VIDEO_MAX_SIZE); // Set
																			// max
																			// file
																			// size
																			// 50M

		mediaRecorder.setOnInfoListener(new OnInfoListener() {
			@Override
			public void onInfo(MediaRecorder mr, int what, int extra) {
				Log.e("LOGTAG", "onInfo " + what + " " + extra);

				if (what == MediaRecorder.MEDIA_RECORDER_INFO_MAX_FILESIZE_REACHED) {
					Log.e("LOGTAG", "Max file size reached");
					try {
						Thread.sleep(250);// just giving the system some time
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
					// stopRecording();// custom implementation
					// startRecording(false);// custom implementation
				}
			}
		});

		try {
			mediaRecorder.prepare();
		} catch (IllegalStateException e) {
			releaseMediaRecorder();
			return false;
		} catch (IOException e) {
			releaseMediaRecorder();
			return false;
		}
		return true;

	}

	private void setVolumeToMax() {
		AudioManager am = (AudioManager) getSystemService(Context.AUDIO_SERVICE);
		am.setStreamVolume(AudioManager.STREAM_MUSIC,
				am.getStreamMaxVolume(AudioManager.STREAM_MUSIC), 0);
	}

	private void releaseCamera() {
		// stop and release camera
		if (mCamera != null) {
			mCamera.release();
			mCamera = null;
		}
	}
}