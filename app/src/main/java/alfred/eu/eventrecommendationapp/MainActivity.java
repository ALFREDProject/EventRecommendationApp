package alfred.eu.eventrecommendationapp;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;
import com.google.gson.reflect.TypeToken;

import org.json.JSONArray;
import org.json.JSONObject;

import java.lang.reflect.Type;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.EnumSet;
import java.util.List;
import java.util.Map;

import alfred.eu.eventrecommendationapp.actions.GetRecommendationsForUserAction;
import alfred.eu.eventrecommendationapp.adapters.ArrayAdapterItem;
import eu.alfred.api.personalization.model.eventrecommendation.*;
import eu.alfred.api.personalization.responses.PersonalizationResponse;
import eu.alfred.ui.AppActivity;
import eu.alfred.ui.CircleButton;

public class MainActivity extends AppActivity {
    private static final String GET_RECOMMENDATIONS_FOR_USER = "GetRecommendationsForUser";

    private SharedPreferences preferences;
    private String loggedUserId;
    private List<EventRecommendationResponse> resp;
    @Override
    public void onNewIntent(Intent intent) { super.onNewIntent(intent);
        String userId= "571494fbe4b0d25de0692e40";

        eventrecommendationManager.getRecommendations(userId, new PersonalizationResponse() {
            @Override
            public void OnSuccess(JSONObject jsonObject) {
                if(jsonObject!=null)
                {

                }
            }

            @Override
            public void OnSuccess(JSONArray jsonArray) {
                if(jsonArray!=null)
                {

                }
            }

            @Override
            public void OnSuccess(Object o) {
                if(o!=null)
                {

                }
            }

            @Override
            public void OnSuccess(String s) {
                if(s!=null)
                {
                    try
                    {
                        // Creates the json object which will manage the information received
                        GsonBuilder builder = new GsonBuilder();

                        // Register an adapter to manage the date types as long values
                        builder.registerTypeAdapter(Date.class, new JsonDeserializer<Date>() {
                            public Date deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) {
                                return new Date();
                            }
                        });
                        /*builder.registerTypeAdapter(Tickets[].class, new JsonDeserializer<Tickets>() {
                            public Tickets deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
                                return null;
                            }
                        });*/

                        Gson gson = builder.create();
                        //Gson gson = new  Gson ();
                        EventRecommendationResponse[] r =gson.fromJson(s,EventRecommendationResponse[].class);
                      /*  TypeToken<List<EventRecommendationResponseWrapper>> token = new TypeToken<List<EventRecommendationResponseWrapper>>(){};
                        List<EventRecommendationResponse> personList = new Gson().fromJson(s, token.getType());
                        List<EventRecommendationResponse> variable = (List<EventRecommendationResponse>)(List<?>) resp;
                        try {
                            showPopUp();
                        } catch (ParseException e) {
                            e.printStackTrace();
                        }*/
                        //r.getRe().size();
                    }
                    catch(Exception e)
                    {
                        e.printStackTrace();
                    }
                }
            }

            public  <T> List<T> stringToArray(String s, Class<T[]> clazz) {
                T[] arr = new Gson().fromJson(s, clazz);
                return Arrays.asList(arr); //or return Arrays.asList(new Gson().fromJson(s, clazz)); for a one-liner
            }
            @Override
            public void OnError(Exception e) {
                e.printStackTrace();
            }
        });
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Gson g = new Gson();
        try {
            EventRecommendationResponse resp = new EventRecommendationResponse(getEvent(),EnumSet.of(RecommendationReason.DISTANCE),2);
            String json = g.toJson(resp);
            EventRecommendationResponse resp2 = g.fromJson(json,EventRecommendationResponse.class);
            resp.getEvent();

            List<EventRecommendationResponse> listof = new ArrayList<>();
            listof.add(resp);
            listof.add(resp);
            listof.add(resp);
            listof.add(resp);
            json = g.toJson(listof);
            EventRecommendationResponse[] resp2List = g.fromJson(json,EventRecommendationResponse[].class);
            resp.getEvent();

        } catch (ParseException e) {
            e.printStackTrace();
        }
        circleButton = (CircleButton) findViewById(R.id.voiceControlBtn); circleButton.setOnTouchListener(new CircleTouchListener());
        circleButton.setOnTouchListener(new CircleTouchListener());
        preferences = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        loggedUserId = preferences.getString("id", "");
        /*try {
            showPopUp();
        } catch (ParseException e) {
            e.printStackTrace();
        }*/
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
    public void showPopUp() throws ParseException {

        // add your items, this can be done programatically
        // your items can be from a database
      /*  EventRecommendationResponse []resp = new EventRecommendationResponse[8];
        Event e = getEvent();
        resp[0] = new EventRecommendationResponse(e, EnumSet.of(RecommendationReason.FRIENDS_GOING),1);
        resp[1] = new EventRecommendationResponse(e, EnumSet.of(RecommendationReason.FRIENDS_GOING),2);
        resp[2] = new EventRecommendationResponse(e, EnumSet.of(RecommendationReason.FRIENDS_GOING),3);
        resp[3] = new EventRecommendationResponse(e, EnumSet.of(RecommendationReason.FRIENDS_GOING),4);
        resp[4] = new EventRecommendationResponse(e, EnumSet.of(RecommendationReason.FRIENDS_GOING),5);
        resp[5] = new EventRecommendationResponse(e, EnumSet.of(RecommendationReason.FRIENDS_GOING),6);
        resp[6] = new EventRecommendationResponse(e, EnumSet.of(RecommendationReason.FRIENDS_GOING),7);
        resp[7] = new EventRecommendationResponse(e, EnumSet.of(RecommendationReason.FRIENDS_GOING),8);
*/
        //List<Object>  result= wbClient.doGetRequest(url+"users/"+userId+"/events", List.class);

        ArrayAdapterItem adapter = null;
        try
        {
            EventRecommendationResponse[] array = (EventRecommendationResponse[]) resp.toArray();
            adapter = new ArrayAdapterItem(this, R.layout.list_view_row_item,array);
        }
        catch (Exception except)
        {
            except.printStackTrace();
        }
        ListView list = (ListView)findViewById(R.id.lwitem);
        list.setAdapter(adapter);
        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position,long id) {
                EventRecommendationResponse entry = (EventRecommendationResponse) parent.getItemAtPosition(position);
                Intent i = new Intent(MainActivity.this, EventDetailsActivity.class);
                DateFormat format = new SimpleDateFormat("dd.MM.yyyy");
                i.putExtra("eventTitle",entry.getEvent().getTitle());
                i.putExtra("eventStartDate",format.format(entry.getEvent().getStart_date()));
                i.putExtra("eventEndDate",format.format(entry.getEvent().getEnd_date()));
                i.putExtra("eventLocale",entry.getEvent().getLocale());
                i.putExtra("eventDescription",entry.getEvent().getDescription());
                i.putExtra("eventId",entry.getEvent().getEventID());
                i.putExtra("reasons",entry.getReasons());
                i.putExtra("weight",entry.getWeight());
                startActivity(i);
            }
        });
    }

    @Override
    public void performAction(String command, Map<String, String> map) {

        //Add custom events here
        switch (command) {
            case (GET_RECOMMENDATIONS_FOR_USER):
                GetRecommendationsForUserAction cta = new GetRecommendationsForUserAction(this, cade);
                cta.performAction(command, map);
                break;

            default:
                break;
        }
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
