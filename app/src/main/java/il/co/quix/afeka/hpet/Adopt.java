package il.co.quix.afeka.hpet;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Created by ��� on 07/08/2015.
 */
public class Adopt extends MainActivity {

    private TextView ageTxt;
    private TextView dogsInfoTxt;
    private TextView genderTxt;
    private TextView typeTxt;
    private ImageView dogsImage;
    private Dog selected;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.save_dog);
        selected = MainActivity.selectedDog;

        dogsInfoTxt = (TextView) findViewById(R.id.dogsInfoTxt);
        ageTxt = (TextView) findViewById(R.id.ageTxt);
        genderTxt = (TextView) findViewById(R.id.genderTxt);
        typeTxt = (TextView) findViewById(R.id.typeTxt);
        dogsImage = (ImageView) findViewById(R.id.dogsImage);

       getData(selected);

    }
    private void getData(Dog selected) {
        dogsInfoTxt.setText(selected.description);
        ageTxt.setText(selected.age);
        genderTxt.setText(selected.gender);
        typeTxt.setText(selected.type);
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
            Log.e("Bitmap", "returned");
            return myBitmap;
        } catch (IOException e) {
            e.printStackTrace();
            Log.e("Exception", e.getMessage());
            return null;
        }
    }
}
