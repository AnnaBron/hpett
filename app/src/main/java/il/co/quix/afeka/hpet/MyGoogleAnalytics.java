package il.co.quix.afeka.hpet;

import android.content.Context;

import com.google.android.gms.analytics.GoogleAnalytics;
import com.google.android.gms.analytics.HitBuilders;
import com.google.android.gms.analytics.Tracker;

public class MyGoogleAnalytics {
    public static GoogleAnalytics analytics;
    public static Tracker tracker;
int test;



    public MyGoogleAnalytics(Context context, String ScreenName) {
        analytics = GoogleAnalytics.getInstance(context);
        analytics.setLocalDispatchPeriod(60);

        tracker = analytics.newTracker("UA-74913737-1");  // UA-71434680-2
        tracker.enableExceptionReporting(true);
        tracker.enableAdvertisingIdCollection(true);
        tracker.enableAutoActivityTracking(true);

        tracker.setScreenName(ScreenName);
        tracker.send(new HitBuilders.ScreenViewBuilder().build());
        test = 1+1;

    }

    public void sendEvent(String category, String action, String label) {
        tracker.send(new HitBuilders.EventBuilder()
                .setCategory(category)
                .setAction(action)
                .setLabel(label)
                .build());
    }


}
