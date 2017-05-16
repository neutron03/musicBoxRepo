package com.neutron.musicbox;

import android.annotation.SuppressLint;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Point;
import android.media.MediaPlayer;
import android.net.Uri;
import android.opengl.Visibility;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Display;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;

import com.neutron.musicbox.camera.CameraActivity;
import com.neutron.musicbox.database.data.Song;

public class MusicSelectionActivity extends AppCompatActivity {

	Button browseHit;
	Button play;
	Button startRecording;
	TextView musicTitle;
	TextView artiste;
	MediaPlayer mpObject;
	Uri selectedSongUri;
	Song pickedSong;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_music_selection);
		mpObject = new MediaPlayer();
		pickedSong = new Song();
		initViews();
		// play = (Button) findViewById(R.id.play);
		startRecording = (Button) findViewById(R.id.Startrecording);
		musicTitle = (TextView) findViewById(R.id.music_title);
		artiste = (TextView) findViewById(R.id.artiste);

		browseHit.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				browseMusic();

			}
		});

	}

	public void browseMusic() {
		Intent i = new Intent(Intent.ACTION_PICK,
				android.provider.MediaStore.Audio.Media.EXTERNAL_CONTENT_URI);
		startActivityForResult(i, 1);
	}

	@SuppressLint("NewApi")
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {

		if (requestCode == 1) {

			if (resultCode == RESULT_OK) {

				// the selected audio.
				final Uri uri = data.getData();

				setSelectedSongUri(uri);
				pickedSong = getSongFromURI(getApplicationContext(), uri);

				if (pickedSong != null){
					musicTitle.setVisibility(View.VISIBLE);
					artiste.setVisibility(View.VISIBLE);
					musicTitle.setText("Song: " + pickedSong.getTitle());
					artiste.setText("Artiste :"+ pickedSong.getArtiste());
					startRecording.setEnabled(true);
					startRecording.setOnClickListener(new OnClickListener() {
						
						@Override
						public void onClick(View v) {
							Intent intent = new Intent(getBaseContext(), CameraActivity.class);
							intent.putExtra("selectedSong", getRealPathFromURI(getApplicationContext(), uri));
							startActivity(intent);
						}
					});
				}
			}
		}


		super.onActivityResult(requestCode, resultCode, data);
	}
	
	public String getRealPathFromURI(Context context, Uri contentUri) {
		  Cursor cursor = null;
		  try { 
		    String[] proj = { MediaStore.Images.Media.DATA };
		    cursor = context.getContentResolver().query(contentUri,  proj, null, null, null);
		    int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
		    cursor.moveToFirst();
		    return cursor.getString(column_index);
		  } finally {
		    if (cursor != null) {
		      cursor.close();
		    }
		  }
		}

	public static void PlayMusic(Context c, Uri DataStream) {
		MediaPlayer mpObject = new MediaPlayer();
		if (DataStream == null)
			return;
		try {
			mpObject.setDataSource(c, DataStream);
			mpObject.prepare();
			mpObject.start();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public Song getSongFromURI(Context context, Uri contentUri) {
		Cursor musicCursor = null;
		Song s = new Song();
		try {
			ContentResolver musicResolver = context.getContentResolver();
			musicCursor = musicResolver.query(contentUri, null, null, null,
					null);

			if (musicCursor != null) {

				musicCursor.moveToNext();
				// get columns
				int titleColumn = musicCursor
						.getColumnIndex(android.provider.MediaStore.Audio.Media.TITLE);
				int idColumn = musicCursor
						.getColumnIndex(android.provider.MediaStore.Audio.Media._ID);
				int artistColumn = musicCursor
						.getColumnIndex(android.provider.MediaStore.Audio.Media.ARTIST);
				int albumId = musicCursor
						.getColumnIndex(MediaStore.Audio.Media.ALBUM_ID);
				int data = musicCursor
						.getColumnIndex(MediaStore.Audio.Media.DATA);
				int albumkey = musicCursor
						.getColumnIndex(MediaStore.Audio.Media.ALBUM_KEY);

				s.setId(musicCursor.getLong(idColumn));
				s.setTitle(musicCursor.getString(titleColumn));
				s.setArtiste(musicCursor.getString(artistColumn));
				s.setAlbumId(musicCursor.getLong(albumId));
				s.setPath(musicCursor.getString(data));
			}

		} finally {
			if (musicCursor != null) {
				musicCursor.close();
			}
		}

		return s;
	}

	public Uri getSelectedSongUri() {
		return selectedSongUri;
	}

	public void setSelectedSongUri(Uri selectedSongUri) {
		this.selectedSongUri = selectedSongUri;
	}

	public void initViews() {

		Display display = getWindowManager().getDefaultDisplay();
		Point size = new Point();
		display.getSize(size);
		int width1 = size.x;
		int height1 = size.y;
		browseHit = (Button) findViewById(R.id.choosehit);
		browseHit.setHeight(height1 / 3);
		browseHit.setWidth(width1 / 2);
	}
	
	
	
}
