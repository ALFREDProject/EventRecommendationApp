package alfred.eu.eventrecommendationapp;

import android.os.Bundle;

import java.util.Map;

import alfred.eu.eventrecommendationapp.actions.GetRecommendationsForUserAction;
import eu.alfred.api.PersonalAssistantConnection;
import eu.alfred.api.event.webservice.RecommendationManager;
import eu.alfred.ui.AppActivity;
import eu.alfred.ui.CircleButton;

public class MainActivity extends AppActivity {
    RecommendationManager recommendationManager;
    private static final String GET_RECOMMENDATIONS_FOR_USER = "GetRecommendationsForUser";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

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


        //Change your view contents. Note, the the button has to be included last.
        setContentView(alfred.eu.eventrecommendationapp.R.layout.activity_main);

        circleButton = (CircleButton) findViewById(R.id.voiceControlBtn);
        circleButton.setOnTouchListener(new CircleTouchListener());
    }



    @Override
    public void performAction(String command, Map<String, String> map) {

        //Add custom events here
        switch (command) {
            case (GET_RECOMMENDATIONS_FOR_USER):
                GetRecommendationsForUserAction cta = new GetRecommendationsForUserAction(this, cade,recommendationManager);
                cta.performAction(command, map);
                break;
            default:
                break;

        }
    }
}
