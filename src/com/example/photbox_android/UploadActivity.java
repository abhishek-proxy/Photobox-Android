package com.example.photbox_android;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.TextView;

import com.cloudinary.Cloudinary;

public class UploadActivity extends Activity {
	public String image_url = "";
	private ProgressDialog dialog = null;
	Cloudinary cloudinary;
	JSONObject Result;
	String file_path;
	File file;
	private ProgressDialog pDialog;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_upload);

		Intent i = getIntent();
		file_path = i.getStringExtra("file");
		HashMap config = new HashMap();
		config.put("cloud_name", "dbms");
		config.put("api_key", "929415813779859");
		config.put("api_secret", "WDc_N2BDh0MJPjvkzy8ncTNT374");
		cloudinary = new Cloudinary(config);
		TextView textView = (TextView) findViewById(R.id.text5);
		textView.setText(file_path);
		file = new File(file_path);
	}
	
	private class Upload extends AsyncTask<String, Void, String> {
		ProgressDialog Asycdialog = new ProgressDialog(UploadActivity.this);
		protected void onPreExecute() {
			Asycdialog.setMessage(getString(R.string.waiting));
            //show dialog
            Asycdialog.show();
            super.onPreExecute();
		}
	    public void postData(String image_url)
	    {
	    	
	    	HttpClient httpclient = new DefaultHttpClient();
	    	HttpPost httppost = new HttpPost("http://guarded-coast-9394.herokuapp.com/image/add?");
	    	
	        try {
	            // Add your data
	            List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(1);
	            nameValuePairs.add(new BasicNameValuePair("url",image_url ));
	            httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs));

	            // Execute HTTP Post Request
	            HttpResponse response = httpclient.execute(httppost);

	        } catch (ClientProtocolException e) {
	            // TODO Auto-generated catch block
	        } catch (IOException e) {
	            // TODO Auto-generated catch block
	        }
	    }
		@Override
		protected String doInBackground(String... urls) {
			String response = "";
			try {
				Result = cloudinary.uploader().upload(file,
						Cloudinary.emptyMap());
				image_url = Result.getString("url").toString();
				postData(image_url);
				Log.i("Notes",image_url);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			return response;
		}

		@Override
		protected void onPostExecute(String result) {
			TextView textView = (TextView) findViewById(R.id.text5);
			textView.setText(image_url);
			Asycdialog.dismiss();
		}
	}

	public void onClick(View view) {
		Upload task = new Upload();
		task.execute(new String[] { "http://www.google.com" });

	}
	/*public String getName(String file_path)
	{
		
		return file_name;
	}*/

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.upload, menu);
		return true;
	}

}
