package il.co.quix.afeka.hpet;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by nisomazuz on 7/18/15.
 */
public class Dog {
    public String record_id;
    public String name;
    public String description;
    public String photo;
    public String age;
    public String dogId;
    public String gender;
    public String type;


    // Constructor to convert JSON object into a Java class instance
    public Dog(JSONObject object){
        try {
            this.record_id = object.getString("id");
            this.name= object.getString("name");
            this.description= object.getString("description");
            this.photo= object.getString("photo");
            this.age= object.getString("age");
            this.dogId = object.getString("dog_id");
            this.gender = object.getString("gender");
            this.type = object.getString("type");
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public Dog(String record_id, String name){
        try {
            this.record_id = record_id;
            this.name = name;
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // Factory method to convert an array of JSON objects into a list of objects
    // User.fromJson(jsonArray);
    public static ArrayList<Dog> fromJson(JSONArray jsonObjects) {
        ArrayList<Dog> dogs = new ArrayList<Dog>();
        for (int i = 0; i < jsonObjects.length(); i++) {
            try {
                dogs.add(new Dog(jsonObjects.getJSONObject(i)));
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        return dogs;
    }
}
