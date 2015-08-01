package il.co.quix.afeka.hpet;


import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.annotation.TargetApi;
import android.app.LoaderManager;
import android.content.CursorLoader;
import android.content.Intent;
import android.content.Loader;
import android.database.Cursor;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

public class SearchDog extends MainActivity implements LoaderManager.LoaderCallbacks<Cursor> {

    private View sProgressView;
    private View sFormView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_dog);

        sFormView =  (View) findViewById(R.id.search_form);
        sProgressView =  (View) findViewById(R.id.search_progress);

        Spinner spinner1 = (Spinner) findViewById(R.id.areaSelect);
        // Create an ArrayAdapter using the string array and a default spinner layout
        ArrayAdapter<CharSequence> adapterArea = ArrayAdapter.createFromResource(this,
           R.array.dogs_area, android.R.layout.simple_spinner_item);
        // Specify the layout to use when the list of choices appears
        adapterArea.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        // Apply the adapter to the spinner
        spinner1.setAdapter(adapterArea);

        Spinner spinner2 = (Spinner) findViewById(R.id.typeSelect);
        // Create an ArrayAdapter using the string array and a default spinner layout
        ArrayAdapter<CharSequence> adapterType = ArrayAdapter.createFromResource(this,
                R.array.dogs_type, android.R.layout.simple_spinner_item);
        // Specify the layout to use when the list of choices appears
        adapterType.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        // Apply the adapter to the spinner
        spinner2.setAdapter(adapterType);

        Spinner spinner3 = (Spinner) findViewById(R.id.sizeSelect);
        // Create an ArrayAdapter using the string array and a default spinner layout
        ArrayAdapter<CharSequence> adapterSize = ArrayAdapter.createFromResource(this,
                R.array.dogs_size, android.R.layout.simple_spinner_item);
        // Specify the layout to use when the list of choices appears
        adapterSize.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        // Apply the adapter to the spinner
        spinner3.setAdapter(adapterSize);

        Button mEmailSignInButton = (Button) findViewById(R.id.freeSearch);
        mEmailSignInButton.setOnClickListener(new View.OnClickListener() {
            private View mProgressView;
            private View mSearchFormView;
            private SearchTask mAuthTask;

            @Override
            public void onClick(View view) {
                mSearchFormView = findViewById(R.id.search_form);
                mProgressView = findViewById(R.id.search_progress);
                doSearch();
            }

            private void doSearch() {
               // showProgress(true);
                showProgress(true);
                EditText dogName = (EditText)findViewById(R.id.dogNameEdit);
                Spinner dogArea = (Spinner)findViewById(R.id.areaSelect);
                Spinner dogType = (Spinner)findViewById(R.id.typeSelect);
                Spinner dogSize = (Spinner)findViewById(R.id.sizeSelect);
                mAuthTask = new SearchTask(dogName.getText().toString(),
                        dogArea.getSelectedItemId(),
                        dogType.getSelectedItemId(),
                        dogSize.getSelectedItemId());
                mAuthTask.execute((Void) null);
            }

        });

    }

    @TargetApi(Build.VERSION_CODES.HONEYCOMB_MR2)
    public void showProgress(final boolean show) {
        // On Honeycomb MR2 we have the ViewPropertyAnimator APIs, which allow
        // for very easy animations. If available, use these APIs to fade-in
        // the progress spinner.
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB_MR2) {
            int shortAnimTime = getResources().getInteger(android.R.integer.config_shortAnimTime);

            sFormView.setVisibility(show ? View.GONE : View.VISIBLE);
            sFormView.animate().setDuration(shortAnimTime).alpha(
                    show ? 0 : 1).setListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    sFormView.setVisibility(show ? View.GONE : View.VISIBLE);
                }
            });

            sProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
            sProgressView.animate().setDuration(shortAnimTime).alpha(
                    show ? 1 : 0).setListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    sProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
                }
            });
        } else {
            // The ViewPropertyAnimator APIs are not available, so simply show
            // and hide the relevant UI components.
            sProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
            sFormView.setVisibility(show ? View.GONE : View.VISIBLE);
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
    }

    public class SearchTask extends AsyncTask<Void, Void, Boolean> {

        private String dogNameSearch;
        private Long dogAreaId;
        private Long dogTypeId;
        private Long dogSizeId;
        private String result;

        SearchTask(String dogName, Long dogArea , Long dogType ,Long dogSize) {
            dogNameSearch = dogName;
            dogAreaId = dogArea;
            dogTypeId = dogType;
            dogSizeId = dogSize;
            result = null;
        }

        @Override
        protected Boolean doInBackground(Void... params) {
            // TODO: attempt authentication against a network service.
            String s="";
            try {
                HttpClient httpClient=new DefaultHttpClient();

                HttpPost httpPost=new HttpPost("http://hpet.quix.co.il/api/search");

                List<NameValuePair> list=new ArrayList<NameValuePair>();
                list.add(new BasicNameValuePair("name", dogNameSearch));
                list.add(new BasicNameValuePair("area",dogAreaId.toString()));
                list.add(new BasicNameValuePair("type",dogTypeId.toString()));
                list.add(new BasicNameValuePair("size",dogSizeId.toString()));
                httpPost.setEntity(new UrlEncodedFormEntity(list));
                HttpResponse httpResponse=  httpClient.execute(httpPost);
                result = readResponse(httpResponse);
                Log.d("MAZUZ", readResponse(httpResponse));


                HttpEntity httpEntity=httpResponse.getEntity();
                // SearchDog.this.setLoginStatus(Integer.parseInt(readResponse(httpResponse)) > 0 ? true : false);

                Thread.sleep(2000);
            } catch (IOException e) {
                return false;
            } catch (InterruptedException e) {
                return false;
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
            if(success){
                Intent main = new Intent(getApplicationContext(), MainActivity.class);
                main.putExtra("search_result",result);
                startActivity(main);
            }

        }

        @Override
        protected void onCancelled() {

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
