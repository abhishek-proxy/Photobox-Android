package com.example.photbox_android;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;

import org.json.JSONObject;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.widget.TextView;

import com.cloudinary.Cloudinary;

public class UploadActivity extends Activity {

	private ProgressDialog dialog = null;
	Cloudinary cloudinary;
	JSONObject Result;
	String file_path;
	File file;

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
		@Override
		protected String doInBackground(String... urls) {
			String response = "";
			try {
				Result = cloudinary.uploader().upload(file,
						Cloudinary.emptyMap());
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			return response;
		}

		@Override
		protected void onPostExecute(String result) {
			TextView textView = (TextView) findViewById(R.id.text5);
			textView.setText("file uploaded");
		}
	}

	public void onClick(View view) {
		Upload task = new Upload();
		task.execute(new String[] { "http://www.vogella.com" });

	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.upload, menu);
		return true;
	}

}
