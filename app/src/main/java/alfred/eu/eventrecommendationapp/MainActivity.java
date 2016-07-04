package alfred.eu.eventrecommendationapp;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.lang.reflect.Type;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

import alfred.eu.eventrecommendationapp.adapters.ArrayAdapterItem;
import eu.alfred.api.personalization.model.eventrecommendation.EventRecommendationResponse;
import eu.alfred.api.personalization.model.eventrecommendation.GlobalsettingsKeys;
import eu.alfred.api.personalization.responses.PersonalizationResponse;
import eu.alfred.api.proxies.interfaces.ICadeCommand;
import eu.alfred.ui.AppActivity;
import eu.alfred.ui.BackToPAButton;
import eu.alfred.ui.CircleButton;

public class MainActivity extends AppActivity implements ICadeCommand {
    private int cade_SizeItems;
    private static final String GET_RECOMMENDATIONS_FOR_USER = "ShowEventRecommendationAction";
    private static final long MILLISECONDS = 6000;//TODO 600000;
    private String userId;
    private View loadingProgress ;
    private Timer timer = new Timer();
    private MainActivity instance;
    private ArrayList<EventRecommendationResponse> resp;
    private int cadeEventNumber;//If we get #1 - it is 0 in the list
    @Override
    public void onNewIntent(Intent intent) { super.onNewIntent(intent);
        this.appendLog("new intent");
        getSharedPreferences("global_settings", MODE_ENABLE_WRITE_AHEAD_LOGGING);
        //String userId = prefs.getString(GlobalsettingsKeys.userId,"");
        this.userId  = prefs.getString(GlobalsettingsKeys.userId,"");
        // this.userId = "57726a806f2bcd8b2abef5bb";//TODO remove this shit
       // this.userId = "57711ce93d107c060ba855b4";
        //this.userId = "";
        this.appendLog("new intent - Got userid: "+this.userId);
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
        getRecommendations(false,false);
        instance = this;
        loadingProgress = findViewById(R.id.loadingAnimation);
        loadingProgress.setVisibility(View.VISIBLE);

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        this.appendLog("onCreate");
        super.onCreate(savedInstanceState);
        cade_SizeItems = -1;
        setContentView(R.layout.activity_main);

        this.appendLog("activity_main loaded");
        final Button button = (Button) findViewById(R.id.btn_reload);
        button.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                View noEvent = findViewById(R.id.noEvents);
                View lwitem = findViewById(R.id.lwitem);
                loadingProgress.setVisibility(View.VISIBLE);
                lwitem.setVisibility(View.INVISIBLE);
                noEvent.setVisibility(View.INVISIBLE);
                getRecommendations(false,false);
            }
        });
        circleButton = (CircleButton) findViewById(R.id.voiceControlBtn);
        circleButton.setOnTouchListener(new MicrophoneTouchListener());

        backToPAButton = (BackToPAButton) findViewById(R.id.backControlBtn);
        backToPAButton.setOnTouchListener(new BackTouchListener());
        this.appendLog(" timer.schedule");
        timer.schedule(new MyTimerTask(), 60 * 60, MILLISECONDS); //Ask again after 600000  milliseconds (10 minutes)
        this.appendLog(" timer.schedule done");
    }
    @Override
    public void performAction(String command, Map<String, String> map) {

        instance.appendLog("Cade: performAction!!!");
        //Add custom events here
        switch (command) {
            /*case (GET_RECOMMENDATIONS_FOR_USER):
                GetRecommendationsForUserAction cta = new GetRecommendationsForUserAction(this, cade,eventrecommendationManager);
                cta.performAction(command, map);
                break;
*/

            case ("ShowEventRecommendationAction"):

                try
                {
                    instance.appendLog("map.get(\"selected_event_list_size\")"+map.get("selected_event_list_size"));
                    cade_SizeItems = Integer.parseInt(map.get("selected_event_list_size").replace("event_list_size_",""));
                    instance.appendLog("cade_SizeItems"+cade_SizeItems);
                }
                catch(Exception ysdlkjf)
                {
                    instance.appendLog("CADE: Exception"+ysdlkjf.toString());
                    cade_SizeItems = -1;
                    instance.appendLog("cade_SizeItems"+cade_SizeItems);
                }
                getRecommendations(false,false);
                instance.appendLog("cade.sendActionResult");
                cade.sendActionResult(true);
                break;
            case ("ShowEventDetailsAction"):
                String sNumber = map.get("selected_event_number").replace("event_number","").replace("event_number_","");

                cadeEventNumber = Integer.parseInt(sNumber.replace("_",""));

                resp = GlobalData.getInstance().getResp();
                EventRecommendationResponse rs = resp.get(cadeEventNumber-1);
                Intent i = new Intent(instance, EventDetailsActivity.class);
                DateFormat format = new SimpleDateFormat("dd.MM.yyyy");
                i.putExtra("userId",instance.userId);
                i.putExtra("eventTitle",rs.getEvent().getTitle());
                i.putExtra("eventStartDate",format.format(rs.getEvent().getStart_date()));
                i.putExtra("eventEndDate",format.format(rs.getEvent().getEnd_date()));
                i.putExtra("eventLocale",rs.getEvent().getVenue());
                i.putExtra("eventDescription",rs.getEvent().getDescription());
                i.putExtra("eventId",rs.getEvent().getEventID());
                i.putExtra("reasons",rs.getReasons());
                i.putExtra("weight",rs.getWeight());
                instance.startActivity(i);


                break;

            case ("GoToEventAction"):
                if(resp.size()==0)
                    System.out.println("aksjhda");

                break;

            case ("DontGoToEventAction"):
                if(resp.size()==0)
                    System.out.println("aslkdjalskjd");

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

    public void getRecommendations(final boolean isFriendsOnly,final boolean isCadeDetails) {
        this.appendLog("getRecommendations - download  ");
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
                instance.appendLog("getRecommendations: OnSuccess");
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
                        EventRecommendationResponse[] r = null;
                        if(s.equals(""))
                            r = new EventRecommendationResponse[0];
                        else
                            r =gson.fromJson(s,EventRecommendationResponse[].class);
                        Log.i("fertig",r.length+"");
                        resp = new ArrayList<>(Arrays.asList(r));
                        GlobalData.getInstance().setResp(resp);
                        instance.appendLog("getRecommendations: resp.size()="+resp.size());
                        if(resp.size()==0)
                        {
                            if(!isFriendsOnly)
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
                            else
                                return;
                        }
                        if(isFriendsOnly)
                        {
                            instance.appendLog("getRecommendations: isFriendsOnly="+isFriendsOnly);
                            showEventNotification();
                            return;
                        }
                        ArrayAdapterItem adapter = null;
                        try
                        {
                            EventRecommendationResponse[] array;
                            if(cade_SizeItems==-1)
                                array = new EventRecommendationResponse[resp.toArray().length];
                            else
                                array = new EventRecommendationResponse[cade_SizeItems];
                            int i = 0;
                            for (Object o : resp.toArray()) {
                                array[i] = (EventRecommendationResponse)o;
                                i++;
                                if(cade_SizeItems!=-1 && i == cade_SizeItems)
                                    break;
                            }
                            Log.i("Done","i is "+ i);
                            adapter = new ArrayAdapterItem(instance, R.layout.list_view_row_item,array);
                        }
                        catch (Exception except)
                        {
                            instance.appendLog("getRecommendations: error!!!");
                            instance.appendLog(except.toString());
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
                                i.putExtra("eventLocale",entry.getEvent().getVenue());
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

                                View noEvent = findViewById(R.id.noEvents);
                                View lwitem = findViewById(R.id.lwitem);
                                loadingProgress.setVisibility(View.INVISIBLE);
                                lwitem.setVisibility(View.VISIBLE);
                                noEvent.setVisibility(View.INVISIBLE);
                                getWindow().getDecorView().findViewById(android.R.id.content).invalidate();//Rebuild ui!
                            } });
                        instance.appendLog("getRecommendations: Response done - should be visible now");
                        Log.i("fertig","Response gebaut -- sollte jetzt was sichtbar sein...");
                        if(isCadeDetails)
                        {

                            resp = GlobalData.getInstance().getResp();
                            EventRecommendationResponse rs = resp.get(cadeEventNumber-1);
                            Intent i = new Intent(instance, EventDetailsActivity.class);
                            DateFormat format = new SimpleDateFormat("dd.MM.yyyy");
                            i.putExtra("userId",instance.userId);
                            i.putExtra("eventTitle",rs.getEvent().getTitle());
                            i.putExtra("eventStartDate",format.format(rs.getEvent().getStart_date()));
                            i.putExtra("eventEndDate",format.format(rs.getEvent().getEnd_date()));
                            i.putExtra("eventLocale",rs.getEvent().getVenue());
                            i.putExtra("eventDescription",rs.getEvent().getDescription());
                            i.putExtra("eventId",rs.getEvent().getEventID());
                            i.putExtra("reasons",rs.getReasons());
                            i.putExtra("weight",rs.getWeight());
                            instance.startActivity(i);

                        }
                    }
                    catch(Exception e)
                    {
                        instance.appendLog("getRecommendations: error!!!");
                        instance.appendLog(e.toString());
                        e.printStackTrace();
                    }
                }
            }
            @Override
            public void OnError(Exception e) {
                instance.appendLog("getRecommendations: onError!!!");
                instance.appendLog(e.toString());
            }
        });
    }

    private void showEventNotification() {
        System.out.println("hello");
       /* Notification n  = new Notification.Builder(instance)
                .setContentTitle("One of your friend is participating an event")
                .setContentText("New social event")
                .setSmallIcon(R.drawable.event_ico)
                .build();
        NotificationManager notificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        // hide the notification after its selected
        n.flags |= Notification.FLAG_AUTO_CANCEL;
        notificationManager.notify(0, n);*/
    }
    private class MyTimerTask extends TimerTask{
        @Override
        public void run() {
            //System.out.println("hello");
          /*  runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    Log.i("quarterTask",new Date().toString()+" - GetRecommendationsForFriends");
                    getRecommendations(true);
                }
            });*/
        }
    }
    public void appendLog(String text)
    {
        String LOG_ROOT_DIR = Environment.getExternalStorageDirectory().toString()+ "/alfred-logs/";

        File logFile = new File(LOG_ROOT_DIR+"recommendation.csv");
        try {
           /* FileOutputStream out = new FileOutputStream(logFile);
            String content = text;
            byte[] contentInBytes = content.getBytes();
            out.write(contentInBytes);
            out.flush();
            out.close();*/

            BufferedWriter buf = new BufferedWriter(new FileWriter(logFile, true));
            buf.append(text);
            buf.newLine();
            buf.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}