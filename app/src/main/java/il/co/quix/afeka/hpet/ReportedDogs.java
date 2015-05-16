package il.co.quix.afeka.hpet;

import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ArrayAdapter;
import android.widget.ListView;


public class ReportedDogs extends MainActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reported_dogs);
        String[] mylist = {"case1","case2"};
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_list_item_1, mylist);
        ListView listView = (ListView) findViewById(R.id.reported_dogs_list);
        listView.setAdapter(adapter);
    }

}
