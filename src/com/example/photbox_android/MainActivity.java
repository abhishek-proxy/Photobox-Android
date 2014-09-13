package com.example.photbox_android;

import java.io.File;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.provider.MediaStore.Images;
import android.support.v4.content.CursorLoader;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

public class MainActivity extends Activity {

	Button btnSelFile;
	TextView text1, text2, text3, note;
	ImageView image;

	Uri orgUri, uriFromPath;
	String convertedPath;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		btnSelFile = (Button) findViewById(R.id.selfile);
		btnSelFile.setOnClickListener(new OnClickListener() {

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
		return cursor.getString(column_index);
	}

	/*@Override
	public boolean onMenuItemSelected(int featureId, MenuItem item) {
		// TODO Auto-generated method stub
		if (item.getItemId() == R.id.action_sub) {
			//showDialog(DIALOG_ALERT);
		}
		return super.onMenuItemSelected(featureId, item);
	}*/
}
