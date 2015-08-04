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

        // this is how we know what to do when item list is clicked
       // listView.setOnItemClickListener(OnListClick);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view,int position, long id) {
                Intent intent = new Intent(ReportedDogs.this,ReportView.class);

                // i.putExtra(ID_EXTRA,String.valueOf(i));
                startActivity(intent);
            }
        });
    }
/*
    private AdapterView.onItemClickListener OnListClick = new AdapterView.onItemClickListener (){
        public void onItemClick(AdapterView<?> parent , View view , int position , long id)
        {
            Intent intent = new Intent(ReportedDogs.this,save_dog.class);

           // i.putExtra(ID_EXTRA,String.valueOf(i));
            startActivity(intent);

        }
    };
    */
    
}
