package alfred.eu.eventrecommendationapp;

import alfred.eu.eventrecommendationapp.web.WebServiceClient;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ListView;
import android.widget.TextView;

import com.google.gson.Gson;

import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import alfred.eu.eventrecommendationapp.actions.SelectRecommendationAction;

import eu.alfred.api.PersonalAssistantConnection;
import eu.alfred.api.personalization.model.UserProfile;
import eu.alfred.api.personalization.model.eventrecommendation.Event;
import eu.alfred.api.personalization.model.eventrecommendation.Venue;
import eu.alfred.api.proxies.interfaces.ICadeCommand;
import eu.alfred.ui.AppActivity;
import eu.alfred.ui.CircleButton;

public class MainActivity extends AppActivity  implements ICadeCommand {

  private static final String LOGTAG = MainActivity.class.getSimpleName();

  //Action
  private static final String GET_RECOMMENDATIONS_FOR_USER = "GetRecommendationsForUser";
  private static final String SELECT_RECOMENDATION_XXX = "SelectRecommendationXXX";

  // TODO RecommendationManager has changed
//  private RecommendationManager recommendationManager;

  private List<Event> recommendations;

  private String url = "http://alfred.eu:8080/recommendation-engine/services/recommendationServices/";

  private WebServiceClient wbClient;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);

    Log.d(LOGTAG, "Event recommendations app created");

    // View contents
    setContentView(alfred.eu.eventrecommendationapp.R.layout.activity_main);

    // *********** Old way, RecommendationManager is not accesible ****************
    personalAssistant.setOnPersonalAssistantConnectionListener(new PersonalAssistantConnection() {
      @Override
      public void OnConnected() {
        // INTEGRATION WITH ALFRED PROFILE and RECOMMENDATIONS SERVICES

        // Some help here to get the recomendations of the user
        // and the needed information about the user profile
        // recommendations = list of events

        //TODO get userId
        // get userProfile ?
        UserProfile userProfile = new UserProfile();
        String userId = userProfile.getId();

        //Maybe an AsyncTask for this...
        wbClient = new WebServiceClient();
        List<Object> result;
        result = wbClient.doGetRequest(url + "users/" + userId + "/events", HashMap.class);
        HashMap<Event,Integer> map = (HashMap<Event,Integer>) result.get(0);
        recommendations = new ArrayList<>();
        for (Event e : map.keySet()) {
          recommendations.add(e);
        }
        onNewIntent(getIntent());

        // *********** Simulated ****************
        recommendations = getSimulatedEvents();
        Log.d(LOGTAG, "Alfred simulated recommendations: " + recommendations.size());
        // **************************************

        displayRecommendations();
      }

      @Override
      public void OnDisconnected() {
        // Do some cleanup stuff
      }
    });

    // PA Buttons
    circleButton = (CircleButton) findViewById(R.id.voiceControlBtn);
    circleButton.setOnTouchListener(new CircleTouchListener());
  }

  @Override
  public void performAction(String command, Map<String, String> map) {
    Log.d(LOGTAG, "Action performed!");
    switch (command) {
//      case (GET_RECOMMENDATIONS_FOR_USER):
//        GetRecommendationsForUserAction cta = new GetRecommendationsForUserAction(this, cade, recommendationManager);
//        cta.performAction(command, map);
//        break;
      case (SELECT_RECOMENDATION_XXX):
        // TODO in some way the title of the event must go to CADE, and the event
        SelectRecommendationAction sra = new SelectRecommendationAction(this, cade, recommendations);
        sra.performAction(command, map);
        break;
      default:
        break;
    }
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


  /**
   * Display list of recommendations with a listener on each row
   */
  public void displayRecommendations() {
    ListView listViewRecommendations = (ListView) findViewById(R.id.listViewRecommendations);
    listViewRecommendations.setAdapter(new ListAdapter(this, R.layout.every_item_recommendations_list, recommendations) {
      @Override
      public void onEntry(Object entry, View view) {
        final Event event = (Event) entry;
        TextView textViewTitle = (TextView) view.findViewById(R.id.textViewTitle);
        TextView textViewWhen = (TextView) view.findViewById(R.id.textViewWhen);
        TextView textViewFriendsGoing = (TextView) view.findViewById(R.id.textViewFriendsGoing);

        //Fill recommendations display
        textViewTitle.setText(event.getTitle());
        textViewWhen.setText(DateFormat.getDateTimeInstance().format(event.getStart_date()));
        //Fill which friends are going
        textViewFriendsGoing.setText(event.getParticipants().size() + " " + getResources().getString(R.string.friends_participating));

        view.setOnClickListener(new View.OnClickListener() {
          @Override
          public void onClick(View v) {
            Intent recommendationDetails = new Intent(MainActivity.this, EventDetailsActivity.class);
            recommendationDetails.putExtra(EventDetailsActivity.PARAM_EVENT_DETAILS, (new Gson()).toJson(event));
            startActivity(recommendationDetails);
          }
        });

      }
    });
  }


  /**
   * Only for testing with fake data
   * @return
   */
  private List<Event> getSimulatedEvents() {
    List<Event> result = new ArrayList<>();
    Event event;
    Venue venue = new Venue();
    venue.setAddress("Main street, Amsterdam");
    venue.setName("People centre");
    List<String> participants = new ArrayList<>();
    participants.add("Johan");
    participants.add("Hasibur");
    participants.add("Margarita");
    List<String> accesibilities = new ArrayList<>();
    accesibilities.add("Foot");
    accesibilities.add("Wheelchair");

    event = new Event();
    event.setStart_date(new Date());
    event.setEnd_date(new Date((new Date()).getTime() + 360000));
    event.setParticipants(participants);
    event.setDescription("Learn about the origin and history of tai chi, a martial art designed for self-defense, and discover the health benefits");
    event.setVenue(venue);
    event.setAccessibility(accesibilities);
    event.setTitle("Tai chi");
    result.add(event);

    event = new Event();
    event.setStart_date(new Date());
    event.setEnd_date(new Date((new Date()).getTime() + 360000));
    event.setParticipants(participants);
    event.setDescription("Aquagym courses are given under the supervision of a lifeguard");
    event.setVenue(venue);
    event.setAccessibility(accesibilities);
    event.setTitle("Aquagym");
    result.add(event);

    event = new Event();
    event.setStart_date(new Date());
    event.setEnd_date(new Date((new Date()).getTime() + 360000));
    event.setParticipants(participants);
    event.setDescription("Regualar walking, like most aerobic activities, is good for you because cardio-vascular exercise strengthens the heart and lungs, increasing overall fitness");
    event.setVenue(venue);
    event.setAccessibility(accesibilities);
    event.setTitle("Walking");
    result.add(event);
    return result;
  }
}
