package alfred.eu.eventrecommendationapp.actions;

import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.Map;

import eu.alfred.api.personalization.model.eventrecommendation.Event;
import eu.alfred.api.proxies.interfaces.ICadeCommand;
import eu.alfred.api.speech.Cade;
import eu.alfred.ui.AppActivity;

public class RecommendationDecisionAction implements ICadeCommand {

    private static final String LOGTAG = RecommendationDecisionAction.class.getSimpleName();

    private AppActivity parent;
    private Cade cade;

    public RecommendationDecisionAction(AppActivity parent, Cade cade) {
        this.parent = parent;
        this.cade = cade;
    }

    @Override
    public void performAction(String s, Map<String, String> map) {
        Log.i(LOGTAG, "Recommendations action to display one of them");

        //TODO which is the one selected?
        //Assuming it comes from a Json parameter in the map
        Type responseType = new TypeToken<Event>(){}.getType();
        Event event = (new Gson()).fromJson(map.get("event"), responseType);
        //Set Go / Don't go
        //TODO event.set...?
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
