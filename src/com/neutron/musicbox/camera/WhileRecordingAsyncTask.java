package com.neutron.musicbox.camera;

import android.os.AsyncTask;
import android.util.Log;

public class WhileRecordingAsyncTask extends AsyncTask<Void, Integer, Void>
{
 
    @Override
    protected void onPreExecute() {
        super.onPreExecute();
    }
 
    @Override
    protected void onProgressUpdate(Integer... values){
        super.onProgressUpdate(values);
        
    }
 
    @Override
    protected Void doInBackground(Void... arg0) {
    	
    	for(int i=0;i<6;i++){
    		try {
				Thread.sleep(i*1000);
				Log.i("b","b");
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
    	
    	}
		return null;
    }
 
    @Override
    protected void onPostExecute(Void result) {
    }
}