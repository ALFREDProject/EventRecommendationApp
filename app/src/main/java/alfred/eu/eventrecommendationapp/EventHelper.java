package alfred.eu.eventrecommendationapp;

import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import eu.alfred.api.personalization.model.eventrecommendation.Event;

/**
 * Created by thardes on 02/05/2016.
 */
public class EventHelper {

    private static Gson g = new Gson();

    public static List<Event> jsonToEventList(String json)
    {
        Event[] events = g.fromJson(json,Event[].class);
        return new ArrayList<>(Arrays.asList(events));
    }
    public static String eventListToJson(ArrayList<Event> list)
    {
        return g.toJson(list);
    }
}
