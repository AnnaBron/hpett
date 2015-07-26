package il.co.quix.afeka.hpet;

import android.content.ActivityNotFoundException;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;


public class ReportForDog extends MainActivity implements View.OnClickListener, LocationListener {

    private Button sendReportBtn;
    private Button captureBtn;
    private EditText commentsEdit;
    private ImageView previewImage;
    private String comment;

    private double latutitude;
    private double longtitude;
    double currentLat;
    double currentLon;

    static final int REQUEST_IMAGE_CAPTURE = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_report_for_dog);

        sendReportBtn = (Button) findViewById(R.id.sendReportBtn);
        captureBtn = (Button) findViewById(R.id.captureBtn);
        commentsEdit = (EditText) findViewById(R.id.commentsEdit);
        previewImage = (ImageView) findViewById(R.id.previewImage);

        captureBtn.setOnClickListener(this);
        sendReportBtn.setOnClickListener(this);
    }

    @Override
    protected void onStart() {
        super.onStart();  // Always call the superclass method first

        LocationManager locationManager = (LocationManager) this.getSystemService(Context.LOCATION_SERVICE);
        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, this);

       // sendReportBtn.setVisibility(View.INVISIBLE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK) {
            Bundle extras = data.getExtras();
            Bitmap imageBitmap = (Bitmap) extras.get("data");
            previewImage.setImageBitmap(imageBitmap);

            currentLat = latutitude;
            currentLon = longtitude;
        }
    }

    @Override
    public void onClick(View view) {
        if (view.getId() == R.id.captureBtn) {
            dispatchTakePictureIntent();
        }
        if (view.getId() == R.id.sendReportBtn) {
             comment = commentsEdit.getText().toString();
            //TODO sending form
            commentsEdit.setText(null);

        }
    }

    private void dispatchTakePictureIntent() {

        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);

        if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
            startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE);
        }
    }

    private void launchWaze() {
        try {
            String url = String.format("waze://?ll=<%s>,<%s>&navigate=yes", String.valueOf(latutitude), String.valueOf(longtitude));
            Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
            startActivity(intent);
        } catch (ActivityNotFoundException ex) {
            Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=com.waze"));
            startActivity(intent);
        }
    }



        @Override
    public void onResume() {
        super.onResume();  // Always call the superclass method first
        commentsEdit.setText(comment);

    }


    @Override
    public void onDestroy() {
        super.onDestroy();  // Always call the superclass

        // Stop method tracing that the activity started during onCreate()
        android.os.Debug.stopMethodTracing();
    }

    @Override
    public void onLocationChanged(Location location) {

        latutitude = location.getLatitude();
        longtitude = location.getLongitude();
    }

    @Override
    public void onStatusChanged(String s, int i, Bundle bundle) {

    }

    @Override
    public void onProviderEnabled(String s) {

    }

    @Override
    public void onProviderDisabled(String s) {

    }
}