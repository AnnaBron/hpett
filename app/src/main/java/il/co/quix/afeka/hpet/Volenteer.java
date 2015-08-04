package il.co.quix.afeka.hpet;

import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;

import java.util.ArrayList;


public class Volenteer extends MainActivity {


    public ReportsAdapter vadapter;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_volenteer);
        ArrayList<Report> arrayOfReport = new ArrayList<Report>();
        this.vadapter = new ReportsAdapter(this, arrayOfReport);
        ListView listView = (ListView) findViewById(R.id.report_dogs_list);
        listView.setAdapter(vadapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Toast.makeText(Volenteer.this,
                        "Item in position " + position + " clicked", Toast.LENGTH_LONG).show();
                Log.d("NI","NIS");
                Report reportItem = vadapter.getItem(position);
                // Intent intent = new Intent(Volenteer.this,LoginV.class);
                Intent intent = new Intent(Volenteer.this,ReportView.class);
                // intent.putExtra("report", );
                startActivity(intent);
            }
        });
        new HttpAsyncTask().execute("http://hpet.quix.co.il/api/reports");
    }
    public void updateAdapterReports(ArrayList<Report> reports){
        vadapter.clear();
        vadapter.addAll(reports);
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
            JSONArray jsonArray = null;
            try {
                jsonArray = new JSONArray(result);
                ArrayList<Report> newReports = Report.fromJson(jsonArray);
                Volenteer.this.updateAdapterReports(newReports);

            } catch (JSONException e) {
                e.printStackTrace();
            }

        }
    }
}

