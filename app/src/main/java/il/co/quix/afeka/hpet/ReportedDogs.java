package il.co.quix.afeka.hpet;

import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.util.ArrayList;


public class ReportedDogs extends MainActivity {
    public static Report selected;
    public static String test;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reported_dogs);
        String[] mylist = {"case1","case2"};
        final ArrayList<String> list = new ArrayList<>();
        final ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_list_item_1, list);
        ListView listView = (ListView) findViewById(R.id.reported_dogs_list);
        listView.setAdapter(adapter);

        // this is how we know what to do when item list is clicked
       // listView.setOnItemClickListener(OnListClick);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(ReportedDogs.this, ReportView.class);

                test = list.get(position);

                // i.putExtra(ID_EXTRA,String.valueOf(i));
                startActivity(intent);
            }
        });
    }

    
}
