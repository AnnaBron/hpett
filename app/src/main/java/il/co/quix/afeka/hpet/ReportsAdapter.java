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
public class ReportsAdapter extends ArrayAdapter<Report> {
    public ReportsAdapter(Context context, ArrayList<Report> dogs) {

        super(context, 0, dogs);
    }


    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // Get the data item for this position
        Report report = getItem(position);
        // Check if an existing view is being reused, otherwise inflate the view
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.item_report, parent, false);
        }
        // Lookup view for data population
        TextView reportTitle = (TextView) convertView.findViewById(R.id.reportTitle);
        TextView reportDescription = (TextView) convertView.findViewById(R.id.reportDescription);
        ImageView reportPhoto = (ImageView) convertView.findViewById(R.id.reportPhoto);
        // Populate the data into the template view using the data object
        // update view
        reportTitle.setText(report.title);
        reportDescription.setText(report.description);
        reportPhoto.setImageBitmap(this.getBitmapFromURL(report.photo));
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