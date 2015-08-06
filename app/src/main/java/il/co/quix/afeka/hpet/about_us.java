package il.co.quix.afeka.hpet;

import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.net.Uri;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;


public class about_us extends MainActivity implements View.OnClickListener {

    private Button callBtn;
    private EditText phoneNumber;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about_us);

        callBtn = (Button) findViewById(R.id.callBtn);
        phoneNumber = (EditText) findViewById(R.id.phoneNum);
        callBtn.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {

        if (view.getId() == R.id.callBtn) {
            try {
                Intent callIntent = new Intent(Intent.ACTION_CALL);
                callIntent.setData(Uri.parse("tel:" + phoneNumber.getText().toString()));
                startActivity(callIntent);
            } catch (ActivityNotFoundException activityException) {
                Log.e("Calling a Phone Number", "Call failed", activityException);
            }
        }

        }

    }
