package alfred.eu.eventrecommendationapp;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;

import org.json.JSONArray;
import org.json.JSONObject;

import java.lang.reflect.Type;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.Map;

import alfred.eu.eventrecommendationapp.actions.GetRecommendationsForUserAction;
import alfred.eu.eventrecommendationapp.adapters.ArrayAdapterItem;
import eu.alfred.api.personalization.model.eventrecommendation.Event;
import eu.alfred.api.personalization.model.eventrecommendation.EventRecommendationResponse;
import eu.alfred.api.personalization.model.eventrecommendation.GlobalsettingsKeys;
import eu.alfred.api.personalization.responses.PersonalizationResponse;
import eu.alfred.ui.AppActivity;
import eu.alfred.ui.CircleButton;

public class MainActivity extends AppActivity {
    private static final String GET_RECOMMENDATIONS_FOR_USER = "ShowEventRecommendationAction";
    private SharedPreferences preferences;
    private String userId;
    MainActivity instance;
    private ArrayList<EventRecommendationResponse> resp;
    private Event getEvent() {
        Event e = new Event();
        String title = "MzFandzEvent";
        e.setDescription("Description: "+title);
        e.setCreated(new Date());
        e.setCapacity("10");
        e.setTitle(title);
         SimpleDateFormat sd =  new SimpleDateFormat("dd.MM.yyyy HH:mm");
        e.setCategories(Arrays.asList(new String[] {"sports","golf"}));//Change
        Date d = null;
        try {
            d = sd.parse("22.03.1990 11:11");
        } catch (ParseException e1) {
            e1.printStackTrace();
        }
        e.setStart_date(d);

        Calendar cal = Calendar.getInstance();
        cal.setTime(d);
        cal.add(Calendar.HOUR, 2);
        Date oneHourBack = cal.getTime();
        e.setEnd_date(oneHourBack);

        return e;
    }
    @Override
    public void onNewIntent(Intent intent) { super.onNewIntent(intent);

        userId= "572312a8e4b0d25de0692eea";
        instance = this;
        Event e = getEvent();
        String myjsonevent = new Gson().toJson(e);
        SharedPreferences.Editor edit = prefs.edit();
        edit.putString(GlobalsettingsKeys.userEventsAccepted,myjsonevent);
        edit.commit();
        edit.apply();
        myjsonevent = "";
         myjsonevent = prefs.getString(GlobalsettingsKeys.userEventsAccepted,"");
        e = null;
        e = new Gson().fromJson(myjsonevent,Event.class);
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
                        builder.setDateFormat("yyyy-MM-dd HH:mm:ss").create();
                        // Register an adapter to manage the date types as long values
                        builder.registerTypeAdapter(Date.class, new JsonDeserializer<Date>() {
                            public Date deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) {
                                Date d = new Date(json.getAsLong());
                                return d;
                            }
                        });
                        builder.registerTypeAdapter(MainActivity.class, new CustomDeserializer());
                        Gson gson = builder.create();
                        //Gson gson = new  Gson ();
                        EventRecommendationResponse[] r =gson.fromJson(s,EventRecommendationResponse[].class);
                        Log.i("fertig",r.length+"");
                        resp = new ArrayList<>(Arrays.asList(r));

                        checkPrevious();


                        ArrayAdapterItem adapter = null;
                        try
                        {
                            /***Clean response**/
                            for (Object o : resp.toArray()) {
                                EventRecommendationResponse entry = (EventRecommendationResponse)o;
                                /*** START: Remove useless entries ***/
                                if(entry.getEvent().getTitle()==null || entry.getEvent().getTitle()==""||entry.getEvent().getDescription()==null || entry.getEvent().getDescription()==""||entry.getEvent().getVenue().getPostal_code()==null||entry.getEvent().getVenue().getPostal_code()=="")
                                {
                                    Log.i("----Content check----","Something is completely wrong here");
                                    resp.remove(o);
                                    continue;
                                }
                                /*** END: Remove useless entries ***/
                                Log.i("-------Title-------",entry.getEvent().getTitle());
                            }
                            EventRecommendationResponse[] array = new EventRecommendationResponse[resp.toArray().length];
                            int runningIndex = 0;
                            for (Object o : resp.toArray()) {
                                EventRecommendationResponse entry = (EventRecommendationResponse)o;
                                Log.i("-------Title-------",entry.getEvent().getTitle());
                                array[runningIndex] = entry;
                                runningIndex++;
                            }
                            Log.i("done: ","runningIndex is "+ runningIndex);
                            adapter = new ArrayAdapterItem(instance, R.layout.list_view_row_item,array);
                        }
                        catch (Exception except)
                        {
                            except.printStackTrace();
                        }
                        ListView list = (ListView)findViewById(R.id.lwitem);
                        list.setAdapter(adapter);
                        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                            @Override
                            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                                EventRecommendationResponse entry = (EventRecommendationResponse) parent.getItemAtPosition(position);
                                Intent i = new Intent(MainActivity.this, EventDetailsActivity.class);
                                DateFormat format = new SimpleDateFormat("dd.MM.yyyy");

                                i.putExtra("userId",userId);
                                i.putExtra("eventTitle",entry.getEvent().getTitle());
                                i.putExtra("eventStartDate",format.format(entry.getEvent().getStart_date()));
                                i.putExtra("eventEndDate",format.format(entry.getEvent().getEnd_date()));
                                i.putExtra("eventAddress",entry.getEvent().getVenue().getAddress()+", "+entry.getEvent().getVenue().getPostal_code()+" "+entry.getEvent().getVenue().getCity());
                                i.putExtra("eventDescription",entry.getEvent().getDescription());
                                i.putExtra("eventId",entry.getEvent().getEventID());
                                i.putExtra("reasons",entry.getReasons());
                                i.putExtra("weight",entry.getWeight());
                                startActivity(i);
                            }
                        });
                        Log.i("fertig","Response gebaut -- sollte jetzt was sichtbar sein...");
                    }
                    catch(Exception e)
                    {
                        e.printStackTrace();
                    }
                }
            }

            private void checkPrevious() {
                if(getIntent().getExtras()!= null && getIntent().getExtras().get("eventAccept")!=null && getIntent().getExtras().get("eventAccept")!="")
                {
                    final String eventId = getIntent().getExtras().get("eventAccept").toString();
                    for (final EventRecommendationResponse r: resp) {
                        if(r.getEvent().getEventID().equals(eventId))
                        {



                          /*  globalSettings.getGlobalSettings(new GlobalSettingsResponse() {
                                @Override
                                public void OnSuccess(HashMap<String, Object> hashMap) {
                                    Object o = hashMap.get(GlobalsettingsKeys.userEventsAccepted);
                                    List<Event> eventIds = new ArrayList<>();
                                    if(o!= null)
                                        eventIds  =(ArrayList<Event>)o;
                                    eventIds.add(r.getEvent());
                                    try
                                    {
                                        globalSettings.setGlobalSetting(GlobalsettingsKeys.userEventsAccepted,eventIds);

                                    }
                                    catch(Exception e)
                                    {
                                        Log.e("StoreGSettings",e.getLocalizedMessage());
                                    }
                                  /*  globalSettings.getGlobalSettings(new GlobalSettingsResponse() {
                                        @Override
                                        public void OnSuccess(HashMap<String, Object> hashMap) {
                                            Object o = hashMap.get(GlobalsettingsKeys.userEventsAccepted);
                                            o.toString();
                                        }

                                        @Override
                                        public void OnError(Exception e) {

                                        }
                                    });
                                }

                                @Override
                                public void OnError(Exception e) {
                                    e.printStackTrace();
                                }
                            });*/
                        }

                    }
                }
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
        circleButton = (CircleButton) findViewById(R.id.voiceControlBtn); circleButton.setOnTouchListener(new CircleTouchListener());
        circleButton.setOnTouchListener(new CircleTouchListener());
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
