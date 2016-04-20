package alfred.eu.eventrecommendationapp.actions;

import android.content.Intent;
import android.util.Log;

import com.google.gson.Gson;

import java.util.List;
import java.util.Map;

import alfred.eu.eventrecommendationapp.EventDetailsActivity;
import eu.alfred.api.personalization.model.eventrecommendation.Event;
import eu.alfred.api.proxies.interfaces.ICadeCommand;
import eu.alfred.api.speech.Cade;
import eu.alfred.ui.AppActivity;

public class SelectRecommendationAction implements ICadeCommand {

    public static final String EVENTTITLE_KEY = "eventTitle";

    private static final String LOGTAG = SelectRecommendationAction.class.getSimpleName();

    private AppActivity parent;
    private Cade cade;
    private List<Event> recommendations;


    public SelectRecommendationAction(AppActivity parent, Cade cade, List<Event> recommendations) {
        this.parent = parent;
        this.cade = cade;
        this.recommendations = recommendations;
    }

    @Override
    public void performAction(String s, Map<String, String> map) {
        Log.i(LOGTAG, "Recommendations action to display one of them");

        //TODO which is the one selected?
        //Assuming the title comes from a parameter in the map
        Event event = new Event();
        for (Event e : recommendations) {
            if (e.getTitle().equalsIgnoreCase(map.get(EVENTTITLE_KEY))) {
                event = e;
                break;
            }
        }
        // Display recommendation
        Intent recommendationDetails = new Intent(parent, EventDetailsActivity.class);
        recommendationDetails.putExtra("event", (new Gson()).toJson(event));
        parent.startActivity(recommendationDetails);
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
}
