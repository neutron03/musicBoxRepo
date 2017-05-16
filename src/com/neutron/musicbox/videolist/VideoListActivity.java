package com.neutron.musicbox.videolist;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ListActivity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Vibrator;
import android.util.Log;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.MenuItem.OnMenuItemClickListener;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.ListView;
import android.widget.TextView;

import com.neutron.musicbox.R;
import com.neutron.musicbox.commun.AssetsPropertyReader;

public class VideoListActivity extends ListActivity {

	private List<String> videosList;
	private AlertDialog.Builder builder;
	public static AssetsPropertyReader assetsPropertyReader;
	private Context context;
	private Menu menu;
	public int count;
	MonAdaptateurDeListe adaptateur;
	private boolean isDeleteMenuActivated = false;
	private static List<String> detailsDialogItemsTitles;
	private List<String> detailsDialogItemsDescription;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		context = this;
		this.getListView().setBackgroundColor(Color.WHITE);
		detailsDialogItemsTitles = new ArrayList<String>();
		detailsDialogItemsTitles.add(this.getString(R.string.name));
		detailsDialogItemsTitles.add(this.getString(R.string.duration));
		detailsDialogItemsTitles.add(this.getString(R.string.creationdate));
		
		assetsPropertyReader = new AssetsPropertyReader(context);
		AssetsPropertyReader.initProperties();

		Log.i("in oncreate", "in oncreate");
		refrechList();
		itemsLongClick();

	}

	public void refrechList() {
		videosList = getVideosListInFolder();
		Log.i("in Refrech", "in Refrech");
		Log.i("videosList size = ", "" + videosList.size());
		adaptateur = new MonAdaptateurDeListe(this, videosList);
		setListAdapter(adaptateur);
		adaptateur.notifyDataSetChanged();
	}

	@Override
	protected void onStop() {
		// TODO Auto-generated method stub
		super.onStop();

		Log.i("in onStop", "in onStop");
		videosList = null;
	}

	@Override
	protected void onResume() {
		super.onResume();

		Log.i("in onResume", "in onResume");
		refrechList();

	}

	public void itemsLongClick() {
		 builder = new AlertDialog.Builder(
					VideoListActivity.this,R.style.MyAlertDialogStyle);
		final CharSequence[] alertDialogItems = {
				this.getString(R.string.play_video),
				this.getString(R.string.delete_video),
				this.getString(R.string.share_video),
				this.getString(R.string.details_video) };

		this.getListView().setOnItemLongClickListener(
				new OnItemLongClickListener() {
					@Override
					public boolean onItemLongClick(AdapterView<?> arg0,
							View arg1, final int pos, long id) {
						
						

						builder.setTitle(getApplicationContext().getString(
								R.string.choose_action));
						builder.setItems(alertDialogItems,
								new DialogInterface.OnClickListener() {
									public void onClick(DialogInterface dialog,
											int item) {
										switch (item) {
										case 0:
											dialog.dismiss();
											readVideo(pos);
											break;
										case 1:
											dialog.dismiss();
											// CheckBox ch = (CheckBox)
											// getListView()
											// .getChildAt(pos)
											// .findViewById(R.id.checkbox)
											// ;
											// ch.setChecked(true);
											// count=1;
											isDeleteMenuActivated = true;
											enableDeleteMenu();
											break;
										case 2:
											shareVideo((String) getListView()
													.getItemAtPosition(pos));

											break;
										case 3:
											detailsDialogItemsDescription = new ArrayList<String>();
											TextView txtName = (TextView)getListView().getChildAt(pos).findViewById(R.id.video_name);
											TextView txtDuration = (TextView)getListView().getChildAt(pos).findViewById(R.id.video_duration);
											detailsDialogItemsDescription.add(txtName.getText().toString());
											detailsDialogItemsDescription.add(txtDuration.getText().toString());
											detailsDialogItemsDescription.add(new Date(new File(assetsPropertyReader.VIDEO_FOLDER_URI+txtName.getText().toString()).lastModified()).toString());
											AlertDialog.Builder builder = new AlertDialog.Builder(
													VideoListActivity.this,R.style.MyAlertDialogStyle);
											
											builder.setAdapter(
													new DetailsDialog(), null);
											builder.setPositiveButton(
													"Close",
													new DialogInterface.OnClickListener() {
														public void onClick(
																DialogInterface dialog,
																int id) {

														}
													});

											TextView title = new TextView(
													getApplicationContext());
											// You Can Customise your Title here
											title.setText("Details");
											//title.setBackgroundColor(Color.DKGRAY);
											title.setPadding(10, 10, 10, 10);
											title.setGravity(Gravity.LEFT);
											title.setTextColor(Color.BLACK);
											title.setTextSize(20);

											builder.setCustomTitle(title);
											Dialog d = builder.show();
											
											// to set title color
											// int textViewId =
											// d.getContext().getResources().getIdentifier("android:id/alertTitle",
											// null, null);
											// TextView tv = (TextView)
											// d.findViewById(textViewId);
											// tv.setTextColor(getResources().getColor(R.color.red));

											break;

										default:
											break;
										}

									}

								});
						AlertDialog alert = builder.create();
						alert.show();
						// vibration for 800 milliseconds
						((Vibrator) getSystemService(VIBRATOR_SERVICE))
								.vibrate(80);

						return true;
					}
				});
	}

	@Override
	protected void onListItemClick(ListView l, View v, int position, long id) {
		readVideo(position);
	}

	private void readVideo(int position) {

		Intent intent = new Intent(android.content.Intent.ACTION_VIEW);
		Uri data = Uri.parse(videosList.get(position));
		intent.setDataAndType(data, AssetsPropertyReader.VIDEO_TYPE);
		startActivity(intent);

		Log.i("uriii", "" + data.getPath());
	}

	@Override
	public boolean onCreateOptionsMenu(Menu iMenu) {
		MenuInflater inflater = getMenuInflater();
		inflater.inflate(R.menu.activity_videolist_actions, iMenu);
		menu = iMenu;
		menu.findItem(R.id.selected).setVisible(false);
		menu.findItem(R.id.delete).setVisible(false);
		return super.onCreateOptionsMenu(menu);
	}

	/**
	 * On selecting action bar icons
	 * */
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Take appropriate action for each action item click
		switch (item.getItemId()) {
		case R.id.refrech:
			final ProgressDialog dialog = new ProgressDialog(this,AlertDialog.THEME_DEVICE_DEFAULT_LIGHT);
			dialog.setMessage(getApplicationContext().getString(R.string.refreching));
			dialog.show();
			refrechList();
			Handler handler = new Handler();
			handler.postDelayed(new Runnable() {
				public void run() {
					dialog.dismiss();
				}
			}, 1000); // 1000 milliseconds delay

			return true;
		case R.id.deleteall:

			AlertDialog.Builder builder = new AlertDialog.Builder(this,R.style.MyAlertDialogStyle);

			builder.setTitle(VideoListActivity.this.getString(R.string.confirm));
			builder.setMessage(VideoListActivity.this
					.getString(R.string.areyousure));

			builder.setPositiveButton(
					VideoListActivity.this.getString(R.string.yes),
					new DialogInterface.OnClickListener() {

						public void onClick(DialogInterface dialog, int which) {

							try {
								deleteAllVideos();
							} catch (IOException e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							}
							dialog.dismiss();
						}
					});

			builder.setNegativeButton(
					VideoListActivity.this.getString(R.string.no),
					new DialogInterface.OnClickListener() {

						@Override
						public void onClick(DialogInterface dialog, int which) {
							dialog.dismiss();
						}
					});

			AlertDialog alert = builder.create();
			alert.show();

			return true;
		default:
			return super.onOptionsItemSelected(item);
		}
	}

	private void deleteAllVideos() throws IOException {
		final List<Integer> listPos = new ArrayList<Integer>();
		for (int i = 0; i < getListView().getChildCount(); i++) {
			listPos.add(i);
		}
		Collections.sort(listPos);
		Collections.reverse(listPos);
		ArrayList<String> listFilePathToDelete = new ArrayList<String>();
		if (!listPos.isEmpty()) {
			TextView txtVideoName;
			for (int i = 0; i < listPos.size(); i++) {
				txtVideoName = (TextView) getListView().getChildAt(
						listPos.get(i)).findViewById(R.id.video_name);
				listFilePathToDelete.add(AssetsPropertyReader.VIDEO_FOLDER_URI
						+ txtVideoName.getText());
				adaptateur.remove(adaptateur.getItem(listPos.get(i)));
			}
			adaptateur.notifyDataSetChanged();

			for (String string : listFilePathToDelete) {
				File f = new File(string);
				f.delete();
				getApplicationContext().deleteFile(f.getName());
			}

			// DeleteFileAsyncTask task = new DeleteFileAsyncTask(
			// VideoListActivity.this);
			// task.execute(listFilePathToDelete);

		}
		disableDeleteMenu();
		refrechList();
	}

	public void disableDeleteMenu() {
		isDeleteMenuActivated = false;
		menu.findItem(R.id.selected).setVisible(false);
		menu.findItem(R.id.delete).setVisible(false);
		menu.findItem(R.id.newvideo).setVisible(true);
		menu.findItem(R.id.refrech).setVisible(true);
		menu.findItem(R.id.deleteall).setVisible(true);
	}

	public void enableDeleteMenu() {

		// count = 1;
		menu.findItem(R.id.newvideo).setVisible(false);
		menu.findItem(R.id.refrech).setVisible(false);
		menu.findItem(R.id.deleteall).setVisible(false);

		menu.findItem(R.id.selected).setVisible(true)
				.setShowAsAction(MenuItem.SHOW_AS_ACTION_ALWAYS);
		menu.findItem(R.id.delete).setVisible(true).setEnabled(false)
				.setShowAsAction(MenuItem.SHOW_AS_ACTION_ALWAYS);

		for (int i = 0; i < this.getListView().getChildCount(); i++) {
			this.getListView().getChildAt(i).findViewById(R.id.checkbox)
					.setVisibility(View.VISIBLE);
		}

		this.getListView().setOnItemClickListener(new OnItemClickListener() {
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {

				CheckBox ch = (CheckBox) parent.getChildAt(position)
						.findViewById(R.id.checkbox);
				if (ch.isChecked()) {
					ch.setChecked(false);
					count = count - 1;
				} else {
					ch.setChecked(true);
					count = count + 1;
				}
				menu.findItem(R.id.selected).setTitle(
						VideoListActivity.this.getString(R.string.selected)
								+ getchekcedCheckboxCount());
				deleteSelectedItems();
			}
		});

		menu.findItem(R.id.selected).setTitle(
				VideoListActivity.this.getString(R.string.selected) + "0");
	}

	public void deleteSelectedItems() {

		if (getchekcedCheckboxCount() <= 0) {
			menu.findItem(R.id.delete).setEnabled(false);
		} else {
			final List<Integer> listPos = new ArrayList<Integer>();
			menu.findItem(R.id.delete).setEnabled(true)
					.setOnMenuItemClickListener(new OnMenuItemClickListener() {

						@Override
						public boolean onMenuItemClick(MenuItem item) {
							for (int i = 0; i < getListView().getChildCount(); i++) {
								CheckBox ch = (CheckBox) getListView()
										.getChildAt(i).findViewById(
												R.id.checkbox);
								if (ch.isChecked())
									listPos.add(i);
							}
							Collections.sort(listPos);
							Collections.reverse(listPos);
							ArrayList<String> listFilePathToDelete = new ArrayList<String>();

							if (!listPos.isEmpty()) {
								TextView txtVideoName;
								for (int i = 0; i < listPos.size(); i++) {
									txtVideoName = (TextView) getListView()
											.getChildAt(listPos.get(i))
											.findViewById(R.id.video_name);
									listFilePathToDelete
											.add(AssetsPropertyReader.VIDEO_FOLDER_URI
													+ txtVideoName.getText());
									adaptateur.remove(adaptateur
											.getItem(listPos.get(i)));
								}
								adaptateur.notifyDataSetChanged();

								DeleteFileAsyncTask task = new DeleteFileAsyncTask(
										VideoListActivity.this);
								task.execute(listFilePathToDelete);

								disableDeleteMenu();
							}
							return false;
						}
					});
		}
	}

	public void setActivityTitle(int count) {
		this.setTitle(String.valueOf(count));
	}

	public int getchekcedCheckboxCount() {

		int count = 0;
		for (int i = 0; i < this.getListView().getChildCount(); i++) {
			CheckBox ch = (CheckBox) getListView().getChildAt(i).findViewById(
					R.id.checkbox);
			if (ch.isChecked())
				count++;
		}
		return count;
	}

	public void selectAllCheckBox() {
		for (int i = 0; i < this.getListView().getChildCount(); i++) {
			CheckBox ch = (CheckBox) this.getListView().getChildAt(i)
					.findViewById(R.id.checkbox);
			ch.setChecked(true);
		}
		menu.getItem(0).setTitle("all");
		menu.getItem(2).setEnabled(true);
		count = this.getListView().getChildCount();

	}

	public List<String> getVideosListInFolder() {

		List<String> videosUriList = new ArrayList<String>();

		// Uri uri = MediaStore.Video.Media.EXTERNAL_CONTENT_URI;
		// String[] projection = { MediaStore.Video.VideoColumns.DATA };
		// String selection = MediaStore.Video.Media.DATA + " like?";
		// String[] selectionArgs = new String[] { "%"
		// + AssetsPropertyReader.VIDEO_FOLDER + "%" };
		// Cursor c = getApplicationContext().getContentResolver().query(uri,
		// projection, selection, selectionArgs, null);
		// if (c != null) {
		// while (c.moveToNext()) {
		// videosUriList.add(c.getString(0));
		// }
		// }
		// c.close();

		File sdCardRoot = Environment.getExternalStorageDirectory();
		File yourDir = new File(sdCardRoot, "musicBoxVideo");
		for (File f : yourDir.listFiles()) {
			if (f.isFile()) {

				Log.i("file absolute path", f.getAbsolutePath());
				Log.i("file names", f.getName());
				Log.i("file path", f.getPath());
				videosUriList.add(f.getPath());

			}

		}

		if (videosUriList.isEmpty())
			videosUriList.add("empty");

		return videosUriList;
	}

	public void deleteVideo(int pos) {
		Log.i("pos", "" + pos);
		Log.i("item", "" + adaptateur.getItem(pos));
		adaptateur.remove(adaptateur.getItem(pos));
		// adaptateur.notifyDataSetChanged();
		// File f = new File(videosList.get(pos));
		// f.delete();
	}

	// TODO to be continued ...
	public void shareVideo(String videoPath) {
		String imagePath = Environment.getExternalStorageDirectory()
				+ "/musicBoxVideo/" + "/Sample.mkv";

		Log.i("uriii", "" + imagePath);
		File imageFileToShare = new File(videoPath);
		// Get the URI of the image 'share_this_image.png' exists under pgguru
		// right under SD card
		Uri uri = Uri.fromFile(imageFileToShare);
		// Set the action to be performed i.e 'Send Data'
		Intent sendIntent = new Intent(Intent.ACTION_SEND);
		// Add the URI holding a stream of data
		sendIntent.putExtra(Intent.EXTRA_STREAM, uri);
		// Set the type of data i.e 'image/* which means image/png, image/jpg,
		// image/jpeg etc.,'
		sendIntent.setType("image/*");
		// Launches the activity; Open 'Gallery' if you set it as default app to
		// handle Image
		startActivity(Intent.createChooser(sendIntent, "send to"));
	}

	class DeleteFileAsyncTask extends
			AsyncTask<ArrayList<String>, Void, ArrayList<String>> {

		private ProgressDialog dialog;

		public DeleteFileAsyncTask(VideoListActivity activity) {
			dialog = new ProgressDialog(activity);
		}

		@Override
		protected void onPreExecute() {
			dialog.setMessage(VideoListActivity.this
					.getString(R.string.pd_deleting));
			dialog.show();

		}

		@Override
		protected ArrayList<String> doInBackground(ArrayList<String>... params) {

			ArrayList<String> passed = params[0]; // get passed arraylist

			try {

				for (String file : passed) {
					File f = new File(file);
					f.delete();
				}
				Thread.sleep(1500);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}

			return new ArrayList<String>();
		}

		@Override
		protected void onPostExecute(ArrayList<String> result) {

			if (dialog.isShowing()) {
				dialog.dismiss();
			}
		}
	}

	@Override
	public void onBackPressed() {
		if (isDeleteMenuActivated) {
			disableDeleteMenu();

			for (int i = 0; i < this.getListView().getChildCount(); i++) {
				this.getListView().getChildAt(i).findViewById(R.id.checkbox)
						.setVisibility(View.INVISIBLE);
			}
		} else
			moveTaskToBack(true);
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
	}

	class DetailsDialog extends BaseAdapter {

		@Override
		public int getCount() {
			return getDetailsDialogItemsTitles().size();
		}

		@Override
		public Object getItem(int position) {
			// this isn't great
			return getDetailsDialogItemsTitles().get(position);
		}

		@Override
		public long getItemId(int position) {
			return position;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			if (convertView == null) {
				convertView = getLayoutInflater().inflate(
						R.layout.details_dialog_row, null);
			}

			((TextView) convertView.findViewById(R.id.txttitle))
					.setText(getDetailsDialogItemsTitles().get(position));
			((TextView) convertView.findViewById(R.id.txtdesc))
					.setText(getDetailsDialogItemsDescription().get(position));

			return convertView;
		}

	}

	public static List<String> getDetailsDialogItemsTitles() {
		return detailsDialogItemsTitles;
	}

	public static void setDetailsDialogItemsTitles(
			List<String> detailsDialogItemsTitles) {
		VideoListActivity.detailsDialogItemsTitles = detailsDialogItemsTitles;
	}

	public List<String> getDetailsDialogItemsDescription() {
		return detailsDialogItemsDescription;
	}

	public void setDetailsDialogItemsDescription(
			List<String> detailsDialogItemsDescription) {
		this.detailsDialogItemsDescription = detailsDialogItemsDescription;
	}
	
	
}