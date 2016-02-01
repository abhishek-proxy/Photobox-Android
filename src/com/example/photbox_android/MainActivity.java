package com.example.photbox_android;

import java.io.File;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.HashMap;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONException;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.MediaStore;
import android.provider.MediaStore.Images;
import android.support.v4.content.CursorLoader;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

public class MainActivity extends Activity {

	Button btnSelFile;
	TextView text1, text2, text3, note;
	ImageView image;
	ListView list;
	LazyAdapter adapter;
	ArrayList<String> stringArrayList = new ArrayList<String>();
	// public String[] mStrings=new String[100];;
	JSONArray contacts = null;

	ArrayList<HashMap<String, String>> contactList;
	Uri orgUri, uriFromPath;
	String convertedPath;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		//setContentView(R.layout.activity_main);
		setContentView(R.layout.main);
        new GetImage().execute(); 
        list=(ListView)findViewById(R.id.list);
		
		Button b=(Button)findViewById(R.id.button1);
		b.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				Intent intent = new Intent(Intent.ACTION_PICK,
						Images.Media.EXTERNAL_CONTENT_URI);
				startActivityForResult(intent, 0);
			}
		});

	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);

		if (resultCode == RESULT_OK) {

			// image.setImageBitmap(null);

			// Uri return from external activity
			orgUri = data.getData();
			// text1.setText("Returned Uri: " + orgUri.toString() + "\n");

			// path converted from Uri
			convertedPath = getRealPathFromURI(orgUri);
			Log.i("Notes", convertedPath);
			// text2.setText("Real Path: " + convertedPath + "\n");

			// Uri convert back again from path
			uriFromPath = Uri.fromFile(new File(convertedPath));

			// text3.setText("Back Uri: " + uriFromPath.toString() + "\n");
			Intent i = new Intent(this, UploadActivity.class);
			i.putExtra("file", convertedPath);
			startActivity(i);
		}

	}

	public String getRealPathFromURI(Uri contentUri) {
		String[] proj = { MediaStore.Images.Media.DATA };

		// This method was deprecated in API level 11
		// Cursor cursor = managedQuery(contentUri, proj, null, null, null);

		CursorLoader cursorLoader = new CursorLoader(this, contentUri, proj,
				null, null, null);
		Cursor cursor = cursorLoader.loadInBackground();

		int column_index = cursor
				.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
		cursor.moveToFirst();
		String filename = cursor.getString(column_index);
		Log.i("NOTES", filename);
		return cursor.getString(column_index);
	}

	/*
	 * @Override public boolean onMenuItemSelected(int featureId, MenuItem item)
	 * { // TODO Auto-generated method stub if (item.getItemId() ==
	 * R.id.action_sub) { //showDialog(DIALOG_ALERT); } return
	 * super.onMenuItemSelected(featureId, item); }
	 */




	private class GetImage extends AsyncTask<Void, Void, Void> {
    	private ProgressDialog pDialog; 
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            // Showing progress dialog
            pDialog = new ProgressDialog(MainActivity.this);
            pDialog.setMessage("Please wait...");
            pDialog.setCancelable(false);
            pDialog.show();
 
        }
 
        @Override
        protected Void doInBackground(Void... arg0) {
            // Creating service handler class instance
        	String response_s = "";
        	String tmp = "";
        	try {
        		HttpClient client = new DefaultHttpClient();
                HttpGet httpget = new HttpGet("http://guarded-coast-9394.herokuapp.com/image/show?");

                // connect
                HttpResponse response = client.execute(httpget);

                // get response
                HttpEntity httpEntity = response.getEntity();
                response_s = EntityUtils.toString(httpEntity);
     
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            } catch (ClientProtocolException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
        	String jsonStr = response_s;
        	
 
            Log.i("notes", jsonStr);
 
            if (jsonStr != null) {
                try {
                	JSONArray mArray = new JSONArray(jsonStr);
                     
                    // Getting JSON Array node
                    //contacts = jsonObj.getJSONArray(TAG_CONTACTS);
 
                    // looping through All Contacts
                	for (int i = 0; i<mArray.length(); i++) {
                		tmp = mArray.getJSONObject(i).getString("url");
                		Log.i("Notes",tmp);
                		stringArrayList.add(tmp);
                		
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            } else {
                Log.e("ServiceHandler", "Couldn't get any data from the url");
            }
 
            return null;
        }
 
        @Override
        protected void onPostExecute(Void result) {
            super.onPostExecute(result);
            // Dismiss the progress dialog
            if (pDialog.isShowing())
                pDialog.dismiss();
            String [] mStrings = stringArrayList.toArray(new String[stringArrayList.size()]);
            adapter=new LazyAdapter(MainActivity.this, mStrings);
            list.setAdapter(adapter);
            
            
        }
 
    }
 











}
