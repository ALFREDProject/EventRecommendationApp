package alfred.eu.eventrecommendationapp;

import android.app.Notification;
import android.app.NotificationManager;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.support.v7.app.AlertDialog;
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
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

import alfred.eu.eventrecommendationapp.actions.GetRecommendationsForUserAction;
import alfred.eu.eventrecommendationapp.adapters.ArrayAdapterItem;
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
    @Override
    public void onNewIntent(Intent intent) { super.onNewIntent(intent);
        getSharedPreferences("global_settings", MODE_ENABLE_WRITE_AHEAD_LOGGING);
        String userId = prefs.getString(GlobalsettingsKeys.userId,"");
        this.userId = "573043c8e4b0bd6603c8aa05";//TODO Reasdasdmove this shit

      if(this.userId.equals(""))
        {

            new AlertDialog.Builder(this)
                    .setTitle("Not logged in")
                    .setMessage("Please login to use eventrecommendations by using the ProfileEditorApp")
                    .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            System.exit(0);
                        }
                    })
                    .setIcon(android.R.drawable.ic_dialog_alert)
                    .show();
        }
        getRecommendations(false);
        instance = this;
        loadingProgress = findViewById(R.id.loadingAnimation);
        loadingProgress.setVisibility(View.VISIBLE);

        /*this.runOnUiThread(new Runnable() {
            public void run() {
                Timer timer = new Timer ();
                TimerTask hourlyTask = new TimerTask() {
                    @Override
                    public void run () {
                        getRecommendations(true);
                    }
                };
                timer.schedule (hourlyTask, 0l, 1000*60*60);   // 1000*10*60 every 10 minut
            }
        });*/
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
    }

    public void getRecommendations(final boolean isFriendsOnly) {
        eventrecommendationManager.getRecommendations(userId,isFriendsOnly, new PersonalizationResponse() {
            @Override
            public void OnSuccess(JSONObject jsonObject) {

            }

            @Override
            public void OnSuccess(JSONArray jsonArray) {}

            @Override
            public void OnSuccess(Object o) {}
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
                        EventRecommendationResponse[] r =gson.fromJson(s,EventRecommendationResponse[].class);
                        Log.i("fertig",r.length+"");
                        resp = new ArrayList<>(Arrays.asList(r));
                        if(resp.size()==0)
                        {
                            loadingProgress = findViewById(R.id.loadingAnimation);

                            instance.runOnUiThread(new Runnable(){
                                @Override
                                                                                                                                                                                                                                                                                                                                                                 public void run() {
                                    View noEvent = findViewById(R.id.noEvents);
                                    View lwitem = findViewById(R.id.lwitem);
                                    loadingProgress.setVisibility(View.INVISIBLE);
                                    lwitem.setVisibility(View.INVISIBLE);
                                    noEvent.setVisibility(View.VISIBLE);
                                } });
                            return;
                        }
                        if(isFriendsOnly)
                        {
                            showEventNotification();
                        }
                        ArrayAdapterItem adapter = null;
                        try
                        {
                            EventRecommendationResponse[] array = new EventRecommendationResponse[resp.toArray().length];
                            int i = 0;
                            for (Object o : resp.toArray()) {
                                array[i] = (EventRecommendationResponse)o;
                                i++;
                            }
                            Log.i("Done","i is "+ i);
                            adapter = new ArrayAdapterItem(instance, R.layout.list_view_row_item,array);
                        }
                        catch (Exception except)
                        {
                            except.printStackTrace();
                        }
                        ListView list = (ListView)instance.findViewById(R.id.lwitem);
                        list.setAdapter(adapter);
                        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                            @Override
                            public void onItemClick(AdapterView<?> parent, View view, int position,long id) {
                                EventRecommendationResponse entry = (EventRecommendationResponse) parent.getItemAtPosition(position);
                                Intent i = new Intent(instance, EventDetailsActivity.class);
                                DateFormat format = new SimpleDateFormat("dd.MM.yyyy");
                                i.putExtra("userId",instance.userId);
                                i.putExtra("eventTitle",entry.getEvent().getTitle());
                                i.putExtra("eventStartDate",format.format(entry.getEvent().getStart_date()));
                                i.putExtra("eventEndDate",format.format(entry.getEvent().getEnd_date()));
                                i.putExtra("eventLocale",entry.getEvent().getLocale());
                                i.putExtra("eventDescription",entry.getEvent().getDescription());
                                i.putExtra("eventId",entry.getEvent().getEventID());
                                i.putExtra("reasons",entry.getReasons());
                                i.putExtra("weight",entry.getWeight());
                                instance.startActivity(i);
                            }
                        });
                        loadingProgress = findViewById(R.id.loadingAnimation);
                        instance.runOnUiThread(new Runnable(){
                            @Override
                            public void run() {
                                loadingProgress.setVisibility(View.INVISIBLE);
                            } });
                        Log.i("fertig","Response gebaut -- sollte jetzt was sichtbar sein...");
                    }
                    catch(Exception e)
                    {
                        e.printStackTrace();
                    }
                }
            }
            @Override
            public void OnError(Exception e) {}
        });
    }

    private void showEventNotification() {
        Notification n  = new Notification.Builder(instance)
                .setContentTitle("One of your friend is participating an event")
                .setContentText("New social event")
                .setSmallIcon(R.drawable.event_ico)
                .build();
        NotificationManager notificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        // hide the notification after its selected
        n.flags |= Notification.FLAG_AUTO_CANCEL;

        notificationManager.notify(0, n);
    }
}