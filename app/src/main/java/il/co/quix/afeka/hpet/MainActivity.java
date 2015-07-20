package il.co.quix.afeka.hpet;

import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.StrictMode;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;


import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONArray;
import org.json.JSONException;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.lang.reflect.Array;
import java.util.ArrayList;


public class MainActivity extends ActionBarActivity {

    private static final String TAG = "MainActivity";
    public DogsAdapter adapter;
    private boolean debug = false;
    private String res = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // this is fix the exeption for network calls
        if (android.os.Build.VERSION.SDK_INT > 9) {
            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
            StrictMode.setThreadPolicy(policy);
        }

        ArrayList<Dog> arrayOfDogs = new ArrayList<Dog>();
        this.adapter = new DogsAdapter(this, arrayOfDogs);
        ListView listView = (ListView) findViewById(R.id.adopt_dogs_list);
        listView.setAdapter(adapter);

        if(this.debug == true){
            arrayOfDogs.add(new Dog("55","Niso"));
            this.updateAdapterDogs(arrayOfDogs);
        } else {
            new HttpAsyncTask().execute("http://hpet.quix.co.il/api/dogs");
        }



    }

    public void updateAdapterDogs(ArrayList<Dog> dogs){
        // this.adapter.clear();
        adapter.addAll(dogs);
        Log.d("NISO","it works");
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_login) {
            Intent intent = new Intent(this, LoginV.class);
            startActivity(intent);
        } else if(id == R.id.adopt_dogs_list){
            Intent intent = new Intent(this,MainActivity.class);
            startActivity(intent);
        } else if(id == R.id.reported_dogs_list){
            Intent intent = new Intent(this,ReportedDogs.class);
            startActivity(intent);
        } else if(id == R.id.report_dog){
            Intent intent = new Intent(this,ReportForDog.class);
            startActivity(intent);
        } else if(id == R.id.about_us_page){
            Intent intent = new Intent(this,about_us.class);
            startActivity(intent);
        }




        return super.onOptionsItemSelected(item);
    }

/*    private class fetchDogsList extends AsyncTask<String, Void, String> {
        @Override
        protected String doInBackground(String... urls) {

            RestClient dogsRecource = new RestClient("http://hpet.quix.co.il/api/dogs");
            // startActivity(new Intent(this, RestClient.class));
            try {
                dogsRecource.Execute(RestClient.RequestMethod.GET);
            } catch (Exception e) {
                e.printStackTrace();
            }
            String response = dogsRecource.getResponse();
            Log.d("MAIN", response);
            return dogsRecource.getResponse();

        }

        // onPostExecute displays the results of the AsyncTask.
        @Override
        protected void onPostExecute(String result) {
            Log.d("MAIN", result);
        }
    }*/

    public static String GET(String url){
        InputStream inputStream = null;
        String result = "";
        try {

            // create HttpClient
            HttpClient httpclient = new DefaultHttpClient();

            // make GET request to the given URL
            HttpResponse httpResponse = httpclient.execute(new HttpGet(url));

            // receive response as inputStream
            inputStream = httpResponse.getEntity().getContent();

            // convert inputstream to string
            if(inputStream != null)
                result = convertInputStreamToString(inputStream);
            else
                result = "Did not work!";

        } catch (Exception e) {
            Log.d("InputStream", e.getLocalizedMessage());
        }

        return result;
    }

    private static String convertInputStreamToString(InputStream inputStream) throws IOException{
        BufferedReader bufferedReader = new BufferedReader( new InputStreamReader(inputStream));
        String line = "";
        String result = "";
        while((line = bufferedReader.readLine()) != null)
            result += line;

        inputStream.close();
        return result;

    }

    public boolean isConnected(){
        ConnectivityManager connMgr = (ConnectivityManager) getSystemService(this.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();
        if (networkInfo != null && networkInfo.isConnected())
            return true;
        else
            return false;
    }
    private class HttpAsyncTask extends AsyncTask<String, Void, String> {
        @Override
        protected String doInBackground(String... urls) {

            return GET(urls[0]);
        }
        // onPostExecute displays the results of the AsyncTask.
        @Override
        protected void onPostExecute(String result) {
            Toast.makeText(getBaseContext(), "Received!", Toast.LENGTH_LONG).show();
           //  etResponse.setText(result);
            JSONArray jsonArray = null;
            try {
                jsonArray = new JSONArray(result);
                ArrayList<Dog> newDogs = Dog.fromJson(jsonArray);
                Log.d("NISO",result);
                MainActivity.this.updateAdapterDogs(newDogs);

            } catch (JSONException e) {
                e.printStackTrace();
            }

        }
    }
}
