package il.co.quix.afeka.hpet;

import android.content.ContentValues;
import android.content.Intent;
import android.graphics.Bitmap;
import android.provider.MediaStore;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;


public class ReportForDog extends MainActivity implements View.OnClickListener {

    private Button reportBtn;
    private Button captureBtn;
    private EditText commentsEdit;
    private ImageView previewImage;

    static final int REQUEST_IMAGE_CAPTURE = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_report_for_dog);

        reportBtn = (Button) findViewById(R.id.sendReportBtn);
        captureBtn = (Button) findViewById(R.id.captureBtn);
        commentsEdit = (EditText) findViewById(R.id.commentsEdit);
        previewImage = (ImageView) findViewById(R.id.previewImage);

        captureBtn.setOnClickListener(this);
    }

    @Override
    protected void onStart() {
        super.onStart();  // Always call the superclass method first

       /* captureBtn.setText("Blah!");
        reportBtn.setVisibility(View.INVISIBLE);
        previewImage.setImageResource(R.mipmap.ic_launcher);*/

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK) {
            Bundle extras = data.getExtras();
            Bitmap imageBitmap = (Bitmap) extras.get("data");
            previewImage.setImageBitmap(imageBitmap);

            //TODO get GPS data
        }
    }

    @Override
    public void onClick(View view) {

        if (view.getId() == R.id.captureBtn) {

            dispatchTakePictureIntent();

        }
    }

    private void dispatchTakePictureIntent() {

        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);

        if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
            startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE);
        }
    }
/*
    @Override
    public void onPause() {
        super.onPause();  // Always call the superclass method first

        // Release the Camera because we don't need it when paused
        // and other activities might need to use it.
        if (mCamera != null) {
            mCamera.release();
            mCamera = null;
        }
    }

    @Override
public void onResume() {
    super.onResume();  // Always call the superclass method first

    // Get the Camera instance as the activity achieves full user focus
    if (mCamera == null) {
        initializeCamera(); // Local method to handle camera init
    }
}


*/
@Override
protected void onStop() {
    super.onStop();  // Always call the superclass method first

    // Save the note's current draft, because the activity is stopping
    // and we want to be sure the current note progress isn't lost.
    ContentValues values = new ContentValues();
    //values.put(NotePad.Notes.COLUMN_NAME_NOTE, commentsEdit.getText());
    //values.put(NotePad.Notes.COLUMN_NAME_TITLE,);

    /*getContentResolver().update(
            mUri,    // The URI for the note to update.
            values,  // The map of column names and new values to apply to them.
            null,    // No SELECT criteria are used.
            null     // No WHERE columns are used.
    );
    */
}

    @Override
    public void onDestroy() {
        super.onDestroy();  // Always call the superclass

        // Stop method tracing that the activity started during onCreate()
        android.os.Debug.stopMethodTracing();
    }
}
