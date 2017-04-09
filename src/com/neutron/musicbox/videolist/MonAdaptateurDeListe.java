package com.neutron.musicbox.videolist;

import java.util.List;
import java.util.concurrent.TimeUnit;

import android.content.Context;
import android.graphics.Bitmap;
import android.media.MediaMetadataRetriever;
import android.media.ThumbnailUtils;
import android.provider.MediaStore.Video.Thumbnails;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.neutron.musicbox.R;

public class MonAdaptateurDeListe extends ArrayAdapter<String> {

	private CheckBox checkBox;

	public int counter = 0;

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		LayoutInflater inflater = (LayoutInflater) getContext()
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		View rowView = null;
		if("empty".equals(getItem(position))){
			rowView = inflater.inflate(R.layout.emptyrowlayout, parent, false);
			TextView videoName     = (TextView) rowView.findViewById(R.id.newVideo);
			
		}else{
		
			rowView = inflater.inflate(R.layout.rowlayout, parent, false);
		TextView videoName     = (TextView) rowView.findViewById(R.id.video_name);
		TextView videoDuration = (TextView) rowView.findViewById(R.id.video_duration);
		checkBox = (CheckBox) rowView.findViewById(R.id.checkbox);

		
		ImageView imageView = (ImageView) rowView.findViewById(R.id.thumbnail);
		//TODO verifier list vide
		String[] splitedUri = getItem(position).split("/");
		videoName.setText(splitedUri[splitedUri.length - 1]);
		videoDuration.setText(getVideoDuration(getItem(position)));

		Bitmap bmThumbnail = ThumbnailUtils.createVideoThumbnail(
				getItem(position), Thumbnails.MINI_KIND);
		imageView.setImageBitmap(bmThumbnail);
		
		}
		return rowView;
	}

	public String getVideoDuration(String fileUri) {

		MediaMetadataRetriever retriever = new MediaMetadataRetriever();
		// use one of overloaded setDataSource() functions to set your data
		// source
		retriever.setDataSource(fileUri);
		String time = retriever
				.extractMetadata(MediaMetadataRetriever.METADATA_KEY_DURATION);
		long timeInMillisec = Long.parseLong(time);

		/* convert millis to appropriate time */
		return String.format(
				"%d min, %d sec",
				TimeUnit.MILLISECONDS.toMinutes(timeInMillisec),
				TimeUnit.MILLISECONDS.toSeconds(timeInMillisec)
						- TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS
								.toMinutes(timeInMillisec)));
	}

	public MonAdaptateurDeListe(Context context, List values) {
		super(context, R.layout.rowlayout, values);
	}

	public CheckBox getCheckBox() {
		return checkBox;
	}

	public void setCheckBox(CheckBox checkBox) {
		this.checkBox = checkBox;
	}

}