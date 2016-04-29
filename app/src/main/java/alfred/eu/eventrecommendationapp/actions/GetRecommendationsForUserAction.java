package alfred.eu.eventrecommendationapp.actions;

import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;

import org.json.JSONArray;
import org.json.JSONObject;

import java.lang.reflect.Type;
import java.text.ParseException;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Map;

import alfred.eu.eventrecommendationapp.MainActivity;
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
                        List<EventRecommendationResponse> variable = (List<EventRecommendationResponse>)(List<?>) resp;*/
                        try {
                            main.showPopUp();
                        } catch (ParseException e) {
                            e.printStackTrace();
                        }
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
