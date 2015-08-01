package il.co.quix.afeka.hpet;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by nisomazuz on 7/18/15.
 */
public class Report {
    public String record_id;
    public String title;
    public String lat;
    public String lng;
    public String description;
    public String dogId;
    public String photo;
    public String address;



    // Constructor to convert JSON object into a Java class instance
    public Report(JSONObject object){
        try {
            this.record_id = object.getString("id");
            this.title= object.getString("title");
            this.description= object.getString("description");
            this.photo= object.getString("photo");
            this.lat= object.getString("lat");
            this.lng= object.getString("lng");
            this.dogId = object.getString("dog_id");
            this.address = object.getString("address");

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    // Factory method to convert an array of JSON objects into a list of objects
    // User.fromJson(jsonArray);
    public static ArrayList<Report> fromJson(JSONArray jsonObjects) {
        ArrayList<Report> dogs = new ArrayList<Report>();
        for (int i = 0; i < jsonObjects.length(); i++) {
            try {
                dogs.add(new Report(jsonObjects.getJSONObject(i)));
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        return dogs;
    }
}
