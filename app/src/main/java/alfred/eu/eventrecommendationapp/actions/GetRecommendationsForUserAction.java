package alfred.eu.eventrecommendationapp.actions;

import android.os.Messenger;

import java.util.Map;

import alfred.eu.eventrecommendationapp.MainActivity;
import eu.alfred.api.personalization.webservice.eventrecommendation.EventrecommendationManager;
import eu.alfred.api.proxies.interfaces.ICadeCommand;
import eu.alfred.api.speech.Cade;

/**
 * Created by Tobias on 12/02/2016.
 */
public class GetRecommendationsForUserAction implements ICadeCommand {
    MainActivity main;
    Cade cade;
    public GetRecommendationsForUserAction(MainActivity main, Cade cade) {
        this.main = main;
        this.cade = cade;
    }

    @Override
    public void performAction(String s, Map<String, String> map) {

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
