package il.co.quix.afeka.hpet;

        import android.animation.Animator;
        import android.animation.AnimatorListenerAdapter;
        import android.annotation.TargetApi;
        import android.app.AlertDialog;
        import android.app.LoaderManager;
        import android.content.ContentValues;
        import android.content.Context;
        import android.content.CursorLoader;
        import android.content.DialogInterface;
        import android.content.Intent;
        import android.content.Loader;
        import android.database.Cursor;
        import android.graphics.Bitmap;
        import android.location.Address;
        import android.location.Criteria;
        import android.location.Geocoder;
        import android.location.Location;
        import android.location.LocationListener;
        import android.location.LocationManager;
        import android.net.Uri;
        import android.os.AsyncTask;
        import android.os.Build;
        import android.provider.ContactsContract;
        import android.provider.MediaStore;
        import android.support.v7.app.ActionBarActivity;
        import android.os.Bundle;
        import android.util.Base64;
        import android.util.Log;
        import android.view.Menu;
        import android.view.MenuItem;
        import android.view.View;
        import android.widget.ArrayAdapter;
        import android.widget.Button;
        import android.widget.EditText;
        import android.widget.ImageButton;
        import android.widget.ImageView;
        import android.widget.TextView;

        import org.apache.http.HttpEntity;
        import org.apache.http.HttpResponse;
        import org.apache.http.NameValuePair;
        import org.apache.http.client.ClientProtocolException;
        import org.apache.http.client.HttpClient;
        import org.apache.http.client.entity.UrlEncodedFormEntity;
        import org.apache.http.client.methods.HttpPost;
        import org.apache.http.impl.client.DefaultHttpClient;
        import org.apache.http.message.BasicNameValuePair;

        import java.io.BufferedReader;
        import java.io.ByteArrayOutputStream;
        import java.io.IOException;
        import java.io.InputStream;
        import java.io.InputStreamReader;
        import java.io.UnsupportedEncodingException;
        import java.util.ArrayList;
        import java.util.List;
        import java.util.Locale;

        import static android.util.Base64.*;


public class ReportForDog extends MainActivity implements View.OnClickListener,LocationListener,LoaderManager.LoaderCallbacks<Cursor> {

    private Button reportBtn;
    private ImageButton captureBtn;
    private EditText commentsEdit;
    private ImageView previewImage;
    private String imageStr = null;
    protected LocationManager locationManager;
    protected LocationListener locationListener;
    protected Context context;
    private AddReportTask addDogTask = null;
    private View mProgressView;
    private View mReportFormView;
    private AlertDialog.Builder alertDialogBuilder;
    Double lat;
    Double lng;
    String address;
    String notes;
    TextView addressView;

    static final int REQUEST_IMAGE_CAPTURE = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_report_for_dog);

        reportBtn = (Button) findViewById(R.id.sendReportBtn);
        captureBtn = (ImageButton) findViewById(R.id.captureBtn);
        commentsEdit = (EditText) findViewById(R.id.commentsEdit);
        previewImage = (ImageView) findViewById(R.id.previewImage);
        mReportFormView =  (View) findViewById(R.id.reports_form);
        mProgressView =  (View) findViewById(R.id.report_progress);

        context = ReportForDog.this;
        alertDialogBuilder = new AlertDialog.Builder(context);
        captureBtn.setOnClickListener(this);

        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, this);
        List<String> providers  = locationManager.getProviders(true);


        reportBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                sendForm();
            }
        });

        // Log.d("Latitude", "disable");

    }

    private boolean validateForm() {
        if ( !locationManager.isProviderEnabled( LocationManager.GPS_PROVIDER ) ) {
            alertMessage("אנא הפעל את שרותי ה gps במכשיר כדי לשלוח דיווח");
            return false;
        }
        if ( imageStr == null) {
            alertMessage("לא צולמה תמונה של הכלב המדווח");
            return false;
        }
        if ( address == null) {
            alertMessage("כתובת הדיווח טרם זוהתה");
            return false;
        }
        return true;
    }

    private void alertMessage(String msg) {
        new AlertDialog.Builder(context)
                .setTitle("שגיאה בטופס")
                .setMessage(msg)
                .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        // continue with delete
                    }
                })

                .setIcon(android.R.drawable.ic_dialog_alert)
                .show();
    }



    private void sendForm() {
        if (addDogTask != null) {
            return;
        }
        if(!validateForm()){
            // create alert dialog

            return;
        }


       showProgress(true);
       addDogTask = new AddReportTask(imageStr, address ,commentsEdit.getText().toString(),lat,lng);
       addDogTask.execute((Void) null);

        }



    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK) {
            Bundle extras = data.getExtras();
            Bitmap imageBitmap = (Bitmap) extras.get("data");
            previewImage.setImageBitmap(imageBitmap);

            ByteArrayOutputStream stream = new ByteArrayOutputStream();
            imageBitmap.compress(Bitmap.CompressFormat.PNG, 90, stream); //compress to which format you want.
            byte [] byte_arr = stream.toByteArray();
            imageStr = Base64.encodeToString(byte_arr,Base64.DEFAULT);

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

    @TargetApi(Build.VERSION_CODES.HONEYCOMB_MR2)
    public void showProgress(final boolean show) {
        // On Honeycomb MR2 we have the ViewPropertyAnimator APIs, which allow
        // for very easy animations. If available, use these APIs to fade-in
        // the progress spinner.
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB_MR2) {
            int shortAnimTime = getResources().getInteger(android.R.integer.config_shortAnimTime);

            mReportFormView.setVisibility(show ? View.GONE : View.VISIBLE);
            mReportFormView.animate().setDuration(shortAnimTime).alpha(
                    show ? 0 : 1).setListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    mReportFormView.setVisibility(show ? View.GONE : View.VISIBLE);
                }
            });

            mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
            mProgressView.animate().setDuration(shortAnimTime).alpha(
                    show ? 1 : 0).setListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
                }
            });
        } else {
            // The ViewPropertyAnimator APIs are not available, so simply show
            // and hide the relevant UI components.
            mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
            mReportFormView.setVisibility(show ? View.GONE : View.VISIBLE);
        }
    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        return new CursorLoader(this,
                // Retrieve data rows for the device user's 'profile' contact.
                Uri.withAppendedPath(ContactsContract.Profile.CONTENT_URI,
                        ContactsContract.Contacts.Data.CONTENT_DIRECTORY), ProfileQuery.PROJECTION,

                // Select only email addresses.
                ContactsContract.Contacts.Data.MIMETYPE +
                        " = ?", new String[]{ContactsContract.CommonDataKinds.Email
                .CONTENT_ITEM_TYPE},

                // Show primary email addresses first. Note that there won't be
                // a primary email address if the user hasn't specified one.
                ContactsContract.Contacts.Data.IS_PRIMARY + " DESC");
    }

    @Override
    public void onLoadFinished(Loader<Cursor> cursorLoader, Cursor cursor) {
        List<String> emails = new ArrayList<String>();
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            emails.add(cursor.getString(ProfileQuery.ADDRESS));
            cursor.moveToNext();
        }

        // addEmailsToAutoComplete(emails);
    }

    @Override
    public void onLoaderReset(Loader<Cursor> cursorLoader) {

    }

    private interface ProfileQuery {
        String[] PROJECTION = {
                ContactsContract.CommonDataKinds.Email.ADDRESS,
                ContactsContract.CommonDataKinds.Email.IS_PRIMARY,
        };

        int ADDRESS = 0;
        int IS_PRIMARY = 1;
    }




    public class AddReportTask extends AsyncTask<Void, Void, Boolean> {

        private final String image;
        private final String address;
        private final String notes;
        private final String lat;
        private final String lng;

        AddReportTask(String image, String address, String notes,Double lat, Double lng) {
            this.image = image;
            this.address = address;
            this.notes = notes;
            this.lat = String.valueOf(lat);
            this.lng = String.valueOf(lng);
        }

        @Override
        protected Boolean doInBackground(Void... params) {
            // TODO: attempt authentication against a network service.
            String s="";
            try {

                HttpClient httpClient=new DefaultHttpClient();

                HttpPost httpPost=new HttpPost("http://hpet.quix.co.il/api/addreport");

                List<NameValuePair> list=new ArrayList<NameValuePair>();
                list.add(new BasicNameValuePair("description", notes));
                list.add(new BasicNameValuePair("lat",lat));
                list.add(new BasicNameValuePair("lng",lng));
                list.add(new BasicNameValuePair("photo_stream",image));
                list.add(new BasicNameValuePair("address",address));
                UrlEncodedFormEntity form = new UrlEncodedFormEntity(list,"UTF-8");
                httpPost.setEntity(form);
                HttpResponse httpResponse=  httpClient.execute(httpPost);

                HttpEntity httpEntity=httpResponse.getEntity();
                Log.d("DOG",readResponse(httpResponse));
                //  ReportForDog.this.setLoginStatus(Integer.parseInt(readResponse(httpResponse)) > 0 ? true : false);

                Thread.sleep(2000);
            } catch (InterruptedException e) {
                return false;
            } catch (ClientProtocolException e) {
                e.printStackTrace();
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
            return true;

/*            for (String credential : DUMMY_CREDENTIALS) {
                String[] pieces = credential.split(":");
                if (pieces[0].equals(mEmail)) {
                    // Account exists, return true if the password matches.
                    return pieces[1].equals(mPassword);
                }
            }*/

            // TODO: register the new account here.
        }

        @Override
        protected void onPostExecute(final Boolean success) {
            addDogTask = null;
            showProgress(false);

            Intent i = new Intent(getApplicationContext(), Volenteer.class);
            i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

            // Add new Flag to start new Activity
            i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(i);
            finish();
        }

        @Override
        protected void onCancelled() {
            addDogTask = null;
            showProgress(false);
        }

        public String readResponse(HttpResponse res) {
            InputStream is=null;
            String return_text="";
            try {
                is=res.getEntity().getContent();
                BufferedReader bufferedReader=new BufferedReader(new InputStreamReader(is));
                String line="";
                StringBuffer sb=new StringBuffer();
                while ((line=bufferedReader.readLine())!=null)
                {
                    sb.append(line);
                }
                return_text=sb.toString();
            } catch (Exception e)
            {

            }
            return return_text;

        }
    }
}

