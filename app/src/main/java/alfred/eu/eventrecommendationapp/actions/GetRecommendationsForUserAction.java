package alfred.eu.eventrecommendationapp.actions;

import android.util.Log;

import java.util.List;
import java.util.Map;

import alfred.eu.eventrecommendationapp.MainActivity;
import eu.alfred.api.event.model.Event;
import eu.alfred.api.event.webservice.RecommendationManager;
import eu.alfred.api.personalization.model.UserProfile;
import eu.alfred.api.proxies.interfaces.ICadeCommand;
import eu.alfred.api.speech.Cade;

/**
 * Created by Tobias on 12/02/2016.
 */
public class GetRecommendationsForUserAction implements ICadeCommand {

    private static final String LOGTAG = GetRecommendationsForUserAction.class.getSimpleName();

    private MainActivity main;
    private Cade cade;
    private RecommendationManager recommendationManager;

    public GetRecommendationsForUserAction(MainActivity main, Cade cade, RecommendationManager recommendationManager) {
        this.main = main;
        this.cade = cade;
        this.recommendationManager = recommendationManager;
    }

    @Override
    public void performAction(String s, Map<String, String> map) {
        Log.i(LOGTAG, map.get("Recommendations_for_users"));
        // Get recommendations

        //TODO get user profile
        UserProfile userProfile = null;

        List<Event> recommendations = recommendationManager.getEventRecommendationForUser(userProfile);
        try {
            Log.d(LOGTAG, "Received recommendations: " + recommendations.size());
            main.setRecommendations(recommendations);
            main.displayRecommendations();
        }catch (NullPointerException npe) {
            Log.d(LOGTAG, "Received recommendations: null");
        }



        throw new UnsupportedOperationException("Here is some logic missing, but it should return data ");
    }
}
