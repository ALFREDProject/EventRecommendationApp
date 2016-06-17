package alfred.eu.eventrecommendationapp;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONObject;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.Map;

import alfred.eu.eventrecommendationapp.actions.GetRecommendationsForUserAction;
import alfred.eu.eventrecommendationapp.adapters.ArrayAdapterItem;
import alfred.eu.eventrecommendationapp.adapters.UserIdAdapter;
import eu.alfred.api.personalization.helper.eventrecommendation.EventHelper;
import eu.alfred.api.personalization.helper.eventrecommendation.EventRatingTransfer;
import eu.alfred.api.personalization.model.eventrecommendation.Event;
import eu.alfred.api.personalization.model.eventrecommendation.EventRecommendationResponse;
import eu.alfred.api.personalization.model.eventrecommendation.GlobalsettingsKeys;
import eu.alfred.api.personalization.responses.PersonalizationResponse;
import eu.alfred.api.proxies.interfaces.ICadeCommand;
import eu.alfred.ui.AppActivity;
import eu.alfred.ui.BackToPAButton;
import eu.alfred.ui.CircleButton;

public class MainActivity extends AppActivity implements ICadeCommand {
    private static final String GET_RECOMMENDATIONS_FOR_USER = "ShowEventRecommendationAction";
    private String userId;
    private View loadingProgress ;
    private MainActivity instance;
    private ArrayList<EventRecommendationResponse> resp;
    private AlertDialog alertDialogStores;

    private Event getEvent() {
        Event e = new Event();
        String title = "MzFandzEvent";
        e.setDescription("Description: "+title);
        e.setCreated(new Date());
        e.setCapacity("10");
        e.setTitle(title);
        //SimpleDateFormat sd =  new SimpleDateFormat("dd.MM.yyyy HH:mm");
        e.setCategories(Arrays.asList(new String[] {"sports","golf"}));//Change
        Date d = null;
        d = new Date(System.currentTimeMillis() % 1000+2000);
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
        getSharedPreferences("global_settings", MODE_ENABLE_WRITE_AHEAD_LOGGING);
        String userId = prefs.getString(GlobalsettingsKeys.userId,"");
        this.userId = "573043c7e4b0bd6603c8a9fe";//TODO Remove this shit
        /*SharedPreferences.Editor edit = prefs.edit();
        edit.putString(GlobalsettingsKeys.userEventsAccepted,"");
        edit.commit();
        globalSettings.setGlobalSetting(GlobalsettingsKeys.userEventsAccepted,"");
        edit.apply();*/
         if(userId=="")
        {
            new AlertDialog.Builder(this)
                    .setTitle("Not logged in")
                    .setMessage("Please login to use eventrecommendations by using the ProfileEditorApp")
                    .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            System.exit(0);
                        }
                    })

                    /*.setNegativeButton(android.R.string.no, new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            // do nothing
                        }
                    })*/
                    .setIcon(android.R.drawable.ic_dialog_alert)
                    .show();
        }
        //  userId= "572312a8e4b0d25de0692eea";
      /*  String[] ids = new String[10];

        ids[0] = "573043c7e4b0bd6603c8a9fe";
        ids[1] = "573043c7e4b0bd6603c8a9ff";
        ids[2] = "573043c7e4b0bd6603c8aa00";
        ids[3] = "573043c7e4b0bd6603c8aa01";
        ids[4] = "573043c7e4b0bd6603c8aa02";
        ids[5] = "573043c7e4b0bd6603c8aa03";
        ids[6] = "573043c7e4b0bd6603c8aa04";
        ids[7] = "573043c7e4b0bd6603c8aa05";
        ids[8] = "573043c7e4b0bd6603c8aa06";
        ids[9] = "573043c7e4b0bd6603c8aa07";*/

/*        UserIdAdapter adapter = new UserIdAdapter(this, R.layout.list_view_row_item_userid, ids);
        ListView listViewItems = new ListView(this);
        listViewItems.setAdapter(adapter);
        listViewItems.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Context context = view.getContext();
                TextView textViewItem = ((TextView) view.findViewById(R.id.textViewItem));
                SharedPreferences.Editor edit = prefs.edit();
                if(textViewItem != null && textViewItem.getText()!="")
                {   edit.putString(GlobalsettingsKeys.userId,textViewItem.getText().toString());
                    userId = textViewItem.getText().toString();
                }
                edit.commit();
                globalSettings.setGlobalSetting(GlobalsettingsKeys.userId+"","");
                edit.apply();
                getRecommendations();
                ((MainActivity) context).alertDialogStores.cancel();
            }
        });*/

 /*       // put the ListView in the pop up
        alertDialogStores=  new AlertDialog.Builder(MainActivity.this)
                .setView(listViewItems)
                .setTitle("Available userIds")
                .show();*/


        getRecommendations();

        loadingProgress = findViewById(R.id.loadingAnimation);
        loadingProgress.setVisibility(View.VISIBLE);
        instance = this;

    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);

        circleButton = (CircleButton) findViewById(R.id.voiceControlBtn);
        circleButton.setOnTouchListener(new MicrophoneTouchListener());

        backToPAButton = (BackToPAButton) findViewById(R.id.backControlBtn);
        backToPAButton.setOnTouchListener(new BackTouchListener());
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

    public void getRecommendations() {
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
                    getSharedPreferences("global_settings", MODE_PRIVATE);
                    try
                    {
                        String json = prefs.getString(GlobalsettingsKeys.userEventsAccepted,"");
                        ArrayList<EventRatingTransfer> eventsTobeRated = EventHelper.jsonToEventTransferList(json);


                        resp =EventHelper.jsonToEventList(s);
                        if(resp.size()!=0)
                        {
                            for(EventRatingTransfer ert: eventsTobeRated)
                            {
                                for (EventRecommendationResponse r : resp) {
                                    if(r.getEvent().getEventID() == ert.getEventID())
                                    {
                                        resp.remove(r);
                                        break;
                                    }
                                }
                            }
                        }
                        loadingProgress.setVisibility(View.INVISIBLE);
                        ArrayAdapterItem adapter = null;
                        try
                        {
                            if(resp.size()==0)
                            {
                                runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        findViewById(R.id.noEvents).setVisibility(View.VISIBLE);
                                        findViewById(R.id.loadingAnimation).setVisibility(View.INVISIBLE);
                                        findViewById(R.id.lwitem).setVisibility(View.INVISIBLE);
                                    }
                                });
                            }
                            else
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
                            String json = prefs.getString(GlobalsettingsKeys.userEventsAccepted,"");
                            ArrayList<Event> e = null;
                            if(json.equals(""))
                            {
                                e = new ArrayList<Event>();
                            }
                            else
                            {
                                //    e= jsonToEventList(prefs.getString(GlobalsettingsKeys.userEventsAccepted,""));
                                if(e.contains(r.getEvent()))
                                    continue;
                            }
                            e.add(r.getEvent());
                            SharedPreferences.Editor edit = prefs.edit();
                            //edit.putString(GlobalsettingsKeys.userEventsAccepted,eventListToJson(e));
                            //edit.commit();
                            // edit.apply();
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
}
