package il.co.quix.afeka.hpet;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

/**
 * Created by nisomazuz on 7/19/15.
 */
public class DogsAdapter extends ArrayAdapter<Dog> {
    public DogsAdapter(Context context, ArrayList<Dog> dogs) {

        super(context, 0, dogs);
        Log.d("NISO","MAZUZ");
    }


    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // Get the data item for this position
        Dog dog = getItem(position);
        // Check if an existing view is being reused, otherwise inflate the view
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.item_dog, parent, false);
        }
        // Lookup view for data population
        TextView dogName = (TextView) convertView.findViewById(R.id.dogName);
        TextView dogDescription = (TextView) convertView.findViewById(R.id.dogDescription);
        ImageView photo = (ImageView) convertView.findViewById(R.id.dogPhoto);
        // Populate the data into the template view using the data object
        // update view
        dogName.setText(dog.name);
        dogDescription.setText(dog.description);
        photo.setImageBitmap(this.getBitmapFromURL(dog.photo));
        // Return the completed view to render on screen
        return convertView;
    }


    public static Bitmap getBitmapFromURL(String src) {
        try {
            Log.e("src",src);
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
}