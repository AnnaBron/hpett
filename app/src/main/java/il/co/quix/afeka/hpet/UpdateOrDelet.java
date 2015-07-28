package il.co.quix.afeka.hpet;

import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;


public class UpdateOrDelet extends MainActivity implements View.OnClickListener {
    private Button searchBtn;
    private EditText numForSearch;
    private String numSearch;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_or_delet);

        searchBtn = (Button) findViewById(R.id.searchBtn);
        searchBtn.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        if (view.getId() == R.id.searchBtn) {
            numSearch = numForSearch.getText().toString();
            //TODO search form by id number of the dog

            numForSearch.setText(null);
        }
    }
}
