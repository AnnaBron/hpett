package il.co.quix.afeka.hpet;

        import android.content.ContentValues;
        import android.content.Context;
        import android.content.Intent;
        import android.graphics.Bitmap;
        import android.location.Address;
        import android.location.Criteria;
        import android.location.Geocoder;
        import android.location.Location;
        import android.location.LocationListener;
        import android.location.LocationManager;
        import android.provider.MediaStore;
        import android.support.v7.app.ActionBarActivity;
        import android.os.Bundle;
        import android.util.Log;
        import android.view.Menu;
        import android.view.MenuItem;
        import android.view.View;
        import android.widget.Button;
        import android.widget.EditText;
        import android.widget.ImageView;
        import android.widget.TextView;

        import java.io.IOException;
        import java.util.List;
        import java.util.Locale;


public class ReportForDog extends MainActivity implements View.OnClickListener,LocationListener {

    private Button reportBtn;
    private Button captureBtn;
    private EditText commentsEdit;
    private ImageView previewImage;
    protected LocationManager locationManager;
    protected LocationListener locationListener;
    protected Context context;
    Double lat;
    Double lng;
    String address;
    TextView addressView;

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

        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, this);
        List<String> providers  = locationManager.getProviders(true);

        // Log.d("Latitude", "disable");

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

    @Override
    public void onLocationChanged(Location location) {
       //  txtLat = (TextView) findViewById(R.id.textview1);
        this.lat = location.getLatitude();
        this.lng = location.getLongitude();
        this.address = getAddress();
        addressView = (TextView) findViewById(R.id.address);
        addressView.setText(this.address);
    }

    @Override
    public void onProviderDisabled(String provider) {
        Log.d("Latitude", "disable");
    }

    @Override
    public void onProviderEnabled(String provider) {
        Log.d("Latitude","enable");
    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {
        Log.d("Latitude","status");
    }

    private String getAddress(){
        String provider = locationManager.getBestProvider(new Criteria(), true);
        Location locations = locationManager.getLastKnownLocation(provider);
        List<String>  providerList = locationManager.getAllProviders();
        if(null!=locations && null!=providerList && providerList.size()>0){
            double longitude = locations.getLongitude();
            double latitude = locations.getLatitude();
            Geocoder geocoder = new Geocoder(getApplicationContext(), Locale.getDefault());
            try {
                List<Address> listAddresses = geocoder.getFromLocation(latitude, longitude, 1);
                if(null!=listAddresses&&listAddresses.size()>0){
                    return listAddresses.get(0).getAddressLine(0) + "," + listAddresses.get(0).getAddressLine(1);
                }
            } catch (IOException e) {
                e.printStackTrace();
            }

        }
        return null;
    }
}