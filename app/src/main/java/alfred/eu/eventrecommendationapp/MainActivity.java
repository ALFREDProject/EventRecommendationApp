package alfred.eu.eventrecommendationapp;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import alfred.eu.eventrecommendationapp.actions.GetRecommendationsForUserAction;
import eu.alfred.api.PersonalAssistantConnection;
import eu.alfred.api.event.model.Event;
import eu.alfred.api.event.webservice.RecommendationManager;
import eu.alfred.ui.AppActivity;
import eu.alfred.ui.CircleButton;

public class MainActivity extends AppActivity {

    private static final String LOGTAG = MainActivity.class.getSimpleName();

    //Action
    private static final String GET_RECOMMENDATIONS_FOR_USER = "GetRecommendationsForUser";

    private RecommendationManager recommendationManager;
    private List<Event> recommendations;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Log.d(LOGTAG, "Event recommendations app created");

        personalAssistant.setOnPersonalAssistantConnectionListener(new PersonalAssistantConnection() {
            @Override
            public void OnConnected() {
                recommendationManager = new RecommendationManager(personalAssistant.getMessenger());
                onNewIntent(getIntent());
            }

            @Override
            public void OnDisconnected() {
                // Do some cleanup stuff
            }
        });

        //Build list of alfred recommendations

        // *********** Simulated ****************
        recommendations = new ArrayList<>();
        Event event = new Event();
        event.setTitle("Recommendation 1");
        recommendations.add(event);
        event = new Event();
        event.setTitle("Recommendation 2");
        recommendations.add(event);
        event = new Event();
        event.setTitle("Recommendation 3");
        recommendations.add(event);
        displayRecommendations();
        Log.d(LOGTAG, "Alfred simulated recommendations: " + recommendations.size());
        // **************************************

        //Change your view contents. Note, the the button has to be included last.
        setContentView(alfred.eu.eventrecommendationapp.R.layout.activity_main);

        circleButton = (CircleButton) findViewById(R.id.voiceControlBtn);
        circleButton.setOnTouchListener(new CircleTouchListener());
    }

    @Override
    public void performAction(String command, Map<String, String> map) {
        Log.d(LOGTAG, "Action performed!");
        switch (command) {
            case (GET_RECOMMENDATIONS_FOR_USER):
                GetRecommendationsForUserAction cta = new GetRecommendationsForUserAction(this, cade,recommendationManager);
                cta.performAction(command, map);
                break;
            default:
                break;
        }
    }

    public void setRecommendations(List<Event> recommendations) {
        this.recommendations = recommendations;
    }

    public void displayRecommendations() {
        ListView listViewRecommendations = (ListView) findViewById(R.id.listViewRecommendations);
        listViewRecommendations.setAdapter(new ListAdapter(this, R.layout.every_item_recommendations_list, recommendations) {
            @Override
            public void onEntry(Object entry, View view) {
                final Event event = (Event) entry;
                TextView textViewTitle = (TextView) view.findViewById(R.id.textViewTitle);
                TextView textViewWhen = (TextView) view.findViewById(R.id.textViewWhen);
                TextView textViewFriendsGoing = (TextView) view.findViewById(R.id.textViewFriendsGoing);

                //TODO fill recommendations display
                textViewTitle.setText("TITLE: " + event.getTitle());

                //TODO setOnClickListener

            }
        });

    }
}
