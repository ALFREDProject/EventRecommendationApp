package alfred.eu.eventrecommendationapp.actions;

import android.content.Intent;
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
import java.util.List;
import java.util.Map;

import alfred.eu.eventrecommendationapp.CustomDeserializer;
import alfred.eu.eventrecommendationapp.EventDetailsActivity;
import alfred.eu.eventrecommendationapp.MainActivity;
import alfred.eu.eventrecommendationapp.R;
import alfred.eu.eventrecommendationapp.adapters.ArrayAdapterItem;
import eu.alfred.api.personalization.model.eventrecommendation.EventRecommendationResponse;
import eu.alfred.api.personalization.responses.PersonalizationResponse;
import eu.alfred.api.personalization.webservice.eventrecommendation.EventrecommendationManager;
import eu.alfred.api.proxies.interfaces.ICadeCommand;
import eu.alfred.api.speech.Cade;

/**
 * Created by Tobias on 12/02/2016.
 */
public class GetRecommendationsForUserAction implements ICadeCommand {

    MainActivity main;
    Cade cade;
    private List<EventRecommendationResponse> resp;
    EventrecommendationManager eventrecommendationManager;
    public GetRecommendationsForUserAction(MainActivity main, Cade cade, EventrecommendationManager eventrecommendationManager) {
        this.main = main;
        this.cade = cade;
        this.eventrecommendationManager = eventrecommendationManager;
    }

    @Override
    public void performAction(String s, Map<String, String> map) {
        String userId= "571494fbe4b0d25de0692e40";
        Integer daysToShow= Integer.parseInt(map.get("selected_event_list_size"));
        Log.i("sel_days", daysToShow+"");

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
                        ArrayAdapterItem adapter = null;
                        try
                        {
                            EventRecommendationResponse[] array = new EventRecommendationResponse[resp.toArray().length];
                            int fuck = 0;
                            for (Object o : resp.toArray()) {
                                array[fuck] = (EventRecommendationResponse)o;
                                fuck++;
                            }
                            Log.i("fertig","Fuck is "+fuck);
                            adapter = new ArrayAdapterItem(main, R.layout.list_view_row_item,array);
                        }
                        catch (Exception except)
                        {
                            except.printStackTrace();
                        }
                        ListView list = (ListView)main.findViewById(R.id.lwitem);
                        list.setAdapter(adapter);
                        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                            @Override
                            public void onItemClick(AdapterView<?> parent, View view, int position,long id) {
                                EventRecommendationResponse entry = (EventRecommendationResponse) parent.getItemAtPosition(position);
                                Intent i = new Intent(main, EventDetailsActivity.class);
                                DateFormat format = new SimpleDateFormat("dd.MM.yyyy");
                                i.putExtra("eventTitle",entry.getEvent().getTitle());
                                i.putExtra("eventStartDate",format.format(entry.getEvent().getStart_date()));
                                i.putExtra("eventEndDate",format.format(entry.getEvent().getEnd_date()));
                                i.putExtra("eventLocale",entry.getEvent().getLocale());
                                i.putExtra("eventDescription",entry.getEvent().getDescription());
                                i.putExtra("eventId",entry.getEvent().getEventID());
                                i.putExtra("reasons",entry.getReasons());
                                i.putExtra("weight",entry.getWeight());
                                main.startActivity(i);
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
    public void performWhQuery(String s, Map<String, String> map) {

    }

    @Override
    public void performValidity(String s, Map<String, String> map) {

    }

    @Override
    public void performEntityRecognizer(String s, Map<String, String> map) {

    }
   /* @Override
    public void performAction(String s, Map<String, String> map) {
        Log.i("Recommendations-Log", map.get("Recommendations_for_users"));
        UserProfile hereIsSomethingMissing = null;
        recommendationManager.getEventRecommendationForUser(hereIsSomethingMissing);
        throw new UnsupportedOperationException("Here is some logic missing, but it should return data ");
    }

    private void reload() {

    }


    private class GetRecommendationsAsync extends AsyncTask<Void, Void, Void> {
        public Void doInBackground(Void... params) {
            for (long i=0; i < 100000; i++) {
                System.out.println(i);
            }
            return null;
        }

        protected void onPostExecute(Void result) {
            reload();
        }


    }
*/
}
