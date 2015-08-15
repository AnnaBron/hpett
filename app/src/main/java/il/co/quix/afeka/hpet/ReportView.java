package il.co.quix.afeka.hpet;


import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;


/**
 * Created by ��� on 02/08/2015.
 */
public class ReportView extends MainActivity implements View.OnClickListener {

    private Button wazeBtn;
    private TextView sendersComments;
    private TextView addressTxt;
    private ImageView dogsImage;
    private Report selected;


    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
         setContentView(R.layout.save_dog);
         selected = Volenteer.selectedReport;

         wazeBtn = (Button) findViewById(R.id.wazeBtn);
         sendersComments = (TextView) findViewById(R.id.sendersComments);
         addressTxt = (TextView) findViewById(R.id.lastAddress);
         dogsImage = (ImageView) findViewById(R.id.dogsImage);

            getData(selected);

         wazeBtn.setOnClickListener(this);


    }


    private void getData(Report selected) {
        addressTxt.setText(selected.address);
        sendersComments.setText(selected.description);
        dogsImage.setImageBitmap(getBitmapFromURL(selected.photo));
    }

    public static Bitmap getBitmapFromURL(String src) {
        try {
            Log.e("src", src);
            URL url = new URL(src);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setDoInput(true);
            connection.connect();
            InputStream input = connection.getInputStream();
            Bitmap myBitmap = BitmapFactory.decodeStream(input);
            Log.e("Bitmap","returned");
            return myBitmap;
        } catch (IOException e) {
            e.printStackTrace();
            Log.e("Exception",e.getMessage());
            return null;
        }
    }

    @Override
    public void onClick(View view) {
        if (view.getId() == R.id.wazeBtn) {
            String lat =selected.lat,lng = selected.lng;
            launchWaze(lat,lng);
        }
    }

    private void launchWaze(String lat,String lng) {
        try
        {
            //String lat =selected.lat,lng = selected.lng;
            String url = String.format("waze://?ll=<%s>,<%s>&navigate=yes",lat,lng);
            Intent intent = new Intent( Intent.ACTION_VIEW, Uri.parse(url) );
            startActivity( intent );
        }
        catch ( ActivityNotFoundException ex  )
        {
            Intent intent =
                    new Intent( Intent.ACTION_VIEW, Uri.parse( "market://details?id=com.waze" ) );
            startActivity(intent);
        }
    }

}
