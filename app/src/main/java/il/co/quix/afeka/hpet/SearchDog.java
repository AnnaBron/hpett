package il.co.quix.afeka.hpet;


import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.annotation.TargetApi;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
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

public class SearchDog extends MainActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_dog);

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
        spinner1.setAdapter(adapterSize);

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

    public class SearchTask extends AsyncTask<Void, Void, Boolean> {

        private String dogNameSearch;
        private Long dogAreaId;
        private Long dogTypeId;
        private Long dogSizeId;

        SearchTask(String dogName, Long dogArea , Long dogType ,Long dogSize) {
            dogNameSearch = dogName;
            dogAreaId = dogArea;
            dogTypeId = dogType;
            dogSizeId = dogSize;
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
