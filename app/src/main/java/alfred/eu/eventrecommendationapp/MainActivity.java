package alfred.eu.eventrecommendationapp;

import android.app.AlertDialog;
import android.net.Uri;
import android.os.Bundle;
import android.widget.ListView;

import com.google.android.gms.appindexing.Action;
import com.google.android.gms.appindexing.AppIndex;
import com.google.android.gms.common.api.GoogleApiClient;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.EnumSet;
import java.util.List;
import java.util.Map;

import alfred.eu.eventrecommendationapp.adapters.ArrayAdapterItem;
import alfred.eu.eventrecommendationapp.web.Event;
import alfred.eu.eventrecommendationapp.web.EventRecommendationResponse;
import alfred.eu.eventrecommendationapp.web.RecommendationReason;
import eu.alfred.ui.AppActivity;

public class MainActivity extends AppActivity {
    private static final String GET_RECOMMENDATIONS_FOR_USER = "GetRecommendationsForUser";
    final static String HELP_TO_POSTURE_ACTION = "HowToPostureAction";

    private GoogleApiClient client;
    public AlertDialog alertDialogStores;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        /*super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        circleButton = (CircleButton) findViewById(R.id.voiceControlBtn);
        client = new GoogleApiClient.Builder(this).addApi(AppIndex.API).build();*/

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        try {
            showPopUp();
        } catch (ParseException e) {
            e.printStackTrace();
        }
        // a button to show the pop up with a list view


      //  findViewById(R.id.buttonShowPopUp).setOnClickListener(handler);


    }

    private Event getEvent() throws ParseException {

        SimpleDateFormat sd =  new SimpleDateFormat("dd.MM.yyyy");
        Event e = new Event();
        String title = "Dummyevent";
        e.setDescription("Description: "+title);
        e.setCreated(new Date());
        e.setCapacity("10");
        e.setTitle(title);
        e.setCategories(Arrays.asList(new String[] {"sports","golf"}));//Change
        Date d = sd.parse("26.04.2016");
        e.setStart_date(d);
        return  e;
    }

    public void showPopUp() throws ParseException {

        // add your items, this can be done programatically
        // your items can be from a database
        EventRecommendationResponse []resp = new EventRecommendationResponse[8];
        Event e = getEvent();
        resp[0] = new EventRecommendationResponse(e, EnumSet.of(RecommendationReason.FRIENDS_GOING),1);
        resp[1] = new EventRecommendationResponse(e, EnumSet.of(RecommendationReason.FRIENDS_GOING),2);
        resp[2] = new EventRecommendationResponse(e, EnumSet.of(RecommendationReason.FRIENDS_GOING),3);
        resp[3] = new EventRecommendationResponse(e, EnumSet.of(RecommendationReason.FRIENDS_GOING),4);
        resp[4] = new EventRecommendationResponse(e, EnumSet.of(RecommendationReason.FRIENDS_GOING),5);
        resp[5] = new EventRecommendationResponse(e, EnumSet.of(RecommendationReason.FRIENDS_GOING),6);
        resp[6] = new EventRecommendationResponse(e, EnumSet.of(RecommendationReason.FRIENDS_GOING),7);
        resp[7] = new EventRecommendationResponse(e, EnumSet.of(RecommendationReason.FRIENDS_GOING),8);

        ArrayAdapterItem adapter = null;
        // our adapter instance
        try
        {
            adapter = new ArrayAdapterItem(this, R.layout.list_view_row_item, resp);
        }
        catch (Exception except)
        {
            except.printStackTrace();
        }
        ListView list = (ListView)findViewById(R.id.lwitem);
        list.setAdapter(adapter);

    ;}

    @Override
    public void performAction(String command, Map<String, String> map) {

        //Add custom events here
        switch (command) {
            case (GET_RECOMMENDATIONS_FOR_USER):
              /*  GetRecommendationsForUserAction cta = new GetRecommendationsForUserAction(this, cade,recommendationManager);
                cta.performAction(command, map);*/
                break;
            default:
                break;
        }
    }

  /*  @Override
    public void onStart() {
        super.onStart();

        client.connect();
        Action viewAction = Action.newAction(
                Action.TYPE_VIEW,
                "Main Page",

                Uri.parse("http://host/path"),
                Uri.parse("android-app://alfred.eu.eventrecommendationapp/http/host/path")
        );
        AppIndex.AppIndexApi.start(client, viewAction);
    }*/

    @Override
    public void onStop() {
        super.onStop();

        // ATTENTION: This was auto-generated to implement the App Indexing API.
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
        client.disconnect();
    }
}
