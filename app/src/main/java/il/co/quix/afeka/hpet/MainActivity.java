package il.co.quix.afeka.hpet;

import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.StrictMode;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
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
    UserSessionManager session;
    private Toolbar toolbar;
    public static Dog selectedDog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        session = new UserSessionManager(getApplicationContext());
        // this is fix the exeption for network calls
        if (android.os.Build.VERSION.SDK_INT > 9) {
            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
            StrictMode.setThreadPolicy(policy);
        }

        // session.isUserLoggedIn();

        ArrayList<Dog> arrayOfDogs = new ArrayList<Dog>();
        this.adapter = new DogsAdapter(this, arrayOfDogs);
        ListView listView = (ListView) findViewById(R.id.adopt_dogs_list);
        TextView search_label = (TextView) findViewById(R.id.search_result_label);


        listView.setAdapter(adapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            selectedDog = adapter.getItem(position);
            // Intent intent = new Intent(Volenteer.this,LoginV.class);
            Intent intent = new Intent(MainActivity.this,Adopt.class);
            // intent.putExtra("report", );
            startActivity(intent);
        }
    });

        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            String result = extras.getString("search_result");
            if(!result.equals("null")) {
                JSONArray jsonArray = null;
                try {
                    jsonArray = new JSONArray(result);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                ArrayList<Dog> dogsResult = Dog.fromJson(jsonArray);

                search_label.setText("תוצאות חיפוש");
                updateAdapterDogs(dogsResult);
            } else {
                search_label.setText("לא נמצאו תוצאות");
                updateAdapterDogs(arrayOfDogs);

            }
        } else {
            new HttpAsyncTask().execute("http://146.148.123.153/api/dogs"); //http://omriglam.netau.net/hpet/api/dogs
           // Log.e("Pos", "klum");
        }

//146.148.123.153

    }

    public void updateAdapterDogs(ArrayList<Dog> dogs){
        adapter.clear();
        adapter.addAll(dogs);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // this.toolbar = (Toolbar) findViewById(R.id.tool_bar); // Attaching the layout to the toolbar object
        // setSupportActionBar(toolbar);
        // Inflate the menu; this adds items to the action bar if it is present.
        if(!session.isUserLoggedIn()){
            getMenuInflater().inflate(R.menu.menu_main, menu);
        } else {
            getMenuInflater().inflate(R.menu.menu_volenteer, menu);
        }
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        Intent intent;
        //noinspection SimplifiableIfStatement
        if (id == R.id.action_login) {
            if(!session.isUserLoggedIn()){
                intent = new Intent(this, LoginV.class);
            } else {
                intent = new Intent(this,Volenteer.class);
            }
            startActivity(intent);
        } else if(id == R.id.adopt_dogs_list){
            intent = new Intent(this,MainActivity.class);
            startActivity(intent);
        } else if(id == R.id.reported_dogs_list){
            intent = new Intent(this,MainActivity.class);
            startActivity(intent);
        } else if(id == R.id.action_search){
            intent = new Intent(this,SearchDog.class);
            startActivity(intent);
        } else if(id == R.id.report_dog){
            intent = new Intent(this,ReportForDog.class);
            startActivity(intent);
        } else if(id == R.id.about_us_page){
            intent = new Intent(this,about_us.class);
            startActivity(intent);
        } else if(id == R.id.loged_out){
            session.logoutUser();
            intent = new Intent(this,MainActivity.class);
            startActivity(intent);
        }

        return super.onOptionsItemSelected(item);
    }


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
            // Toast.makeText(getBaseContext(), "Received!", Toast.LENGTH_LONG).show();
           //  etResponse.setText(result);

            JSONArray jsonArray = null;
            try {
                jsonArray = new JSONArray(result);
                ArrayList<Dog> newDogs = Dog.fromJson(jsonArray);
                 Log.e("NISO",result);
                MainActivity.this.updateAdapterDogs(newDogs);

            } catch (JSONException e) {
                e.printStackTrace();
            }

        }
    }
}
