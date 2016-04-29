package alfred.eu.eventrecommendationapp;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.Map;

import alfred.eu.eventrecommendationapp.actions.GetRecommendationsForUserAction;
import eu.alfred.api.personalization.model.eventrecommendation.Event;
import eu.alfred.ui.AppActivity;
import eu.alfred.ui.CircleButton;

public class MainActivity extends AppActivity {
    private static final String GET_RECOMMENDATIONS_FOR_USER = "ShowEventRecommendationAction";
    private SharedPreferences preferences;
    private String loggedUserId;

    @Override
    public void onNewIntent(Intent intent) { super.onNewIntent(intent);

    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        circleButton = (CircleButton) findViewById(R.id.voiceControlBtn); circleButton.setOnTouchListener(new CircleTouchListener());
        circleButton.setOnTouchListener(new CircleTouchListener());
        preferences = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        loggedUserId = preferences.getString("id", "");
    }

    private Event getEvent() throws ParseException {

        SimpleDateFormat sd =  new SimpleDateFormat("dd.MM.yyyy HH:mm");
        Event e = new Event();
        String title = "Dummyevent";
        e.setDescription("Description: "+title);
        e.setCreated(new Date());
        e.setCapacity("10");
        e.setTitle(title);
        e.setEventID("pouq3po04u30948");
        e.setCategories(Arrays.asList(new String[] {"sports","golf"}));//Change
        Date d = sd.parse("26.04.2016 13:37");
        e.setStart_date(d);
        e.setEnd_date(sd.parse("26.04.2016 17:37"));
        e.setLocale("Ganderkesee indoor swimming");
        e.setDescription("Swimming is good for you - it keeps you healthy and fit and this is very nice. This is also just a stupid useless text to get the content of the f**** screen filled");
        return  e;
    }

    @Override
    public void performAction(String command, Map<String, String> map) {

        //Add custom events here
        switch (command) {
            case (GET_RECOMMENDATIONS_FOR_USER):
                GetRecommendationsForUserAction cta = new GetRecommendationsForUserAction(this, cade,eventrecommendationManager);
                cta.performAction(command, map);
                break;

            default:
                break;
        }
        cade.sendActionResult(true);
    }

    @Override
    public void performWhQuery(String s, Map<String, String> map) {

    }

    @Override
    public void performValidity(String s, Map<String, String> map) {

    }

    @Override
    public void performEntityRecognizer(String s, Map<String, String> map) {

    }

    @Override
    public void onStop() {
        super.onStop();

      /*  // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        Action viewAction = Action.newAction(
                Action.TYPE_VIEW, // TODO: choose an action type.
                "Main Page", // TODO: Define a title for the content shown.
                // TODO: If you have web page content that matches this app activity's content,
                // make sure this auto-generated web page URL is correct.
                // Otherwise, set the URL to null.
                Uri.parse("http://host/path"),
                // TODO: Make sure this auto-generated app URL is correct.
                Uri.parse("android-app://alfred.eu.eventrecommendationapp/http/host/path")
        );
        AppIndex.AppIndexApi.end(client, viewAction);
        client.disconnect();*/
    }
}
