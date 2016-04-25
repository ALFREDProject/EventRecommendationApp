package alfred.eu.eventrecommendationapp.actions;

import android.os.AsyncTask;
import android.util.Log;

import java.util.Map;

import alfred.eu.eventrecommendationapp.MainActivity;
import eu.alfred.api.event.webservice.RecommendationManager;
import eu.alfred.api.personalization.model.UserProfile;
import eu.alfred.api.proxies.interfaces.ICadeCommand;
import eu.alfred.api.speech.Cade;

/**
 * Created by Tobias on 12/02/2016.
 */
public class GetRecommendationsForUserAction implements ICadeCommand {
    MainActivity main;
    Cade cade;
    RecommendationManager recommendationManager;
    public GetRecommendationsForUserAction(MainActivity main, Cade cade, RecommendationManager recommendationManager) {
        this.main = main;
        this.cade = cade;
        this.recommendationManager = recommendationManager;
    }
    @Override
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

}
