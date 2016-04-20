package alfred.eu.eventrecommendationapp;

import alfred.eu.eventrecommendationapp.recommendationengine.model.EventRecommendationResponse;
import alfred.eu.eventrecommendationapp.recommendationengine.model.RecommendationReason;
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
import java.util.EnumSet;
import java.util.HashMap;
import java.util.Iterator;
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

  //private List<Event> recommendations;
  private List<EventRecommendationResponse> recommendationsWithMoreData;

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
        List<EventRecommendationResponse> recommendationsWithMoreData = (List<EventRecommendationResponse>) result.get(0);

        onNewIntent(getIntent());

        // *********** Simulated ****************
        recommendationsWithMoreData = getSimulatedEvents();
        Log.d(LOGTAG, "Alfred simulated recommendations: " + recommendationsWithMoreData.size());
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
        SelectRecommendationAction sra = new SelectRecommendationAction(this, cade, recommendationsWithMoreData);
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
    listViewRecommendations.setAdapter(new ListAdapter(this, R.layout.every_item_recommendations_list, recommendationsWithMoreData) {
      @Override
      public void onEntry(Object entry, View view) {
        final EventRecommendationResponse eventRecommendationResponse = (EventRecommendationResponse) entry;
        TextView textViewTitle = (TextView) view.findViewById(R.id.textViewTitle);
        TextView textViewWhen = (TextView) view.findViewById(R.id.textViewWhen);
        TextView textViewReason = (TextView) view.findViewById(R.id.textViewReason);

        //Fill recommendations display
        textViewTitle.setText(eventRecommendationResponse.getEvent().getTitle());
        textViewWhen.setText(DateFormat.getDateTimeInstance().format(eventRecommendationResponse.getEvent().getStart_date()));

        String reason = "";
        for (RecommendationReason rr : eventRecommendationResponse.getReasons()) {
          if (!"".equalsIgnoreCase(reason)) {
            reason = reason + "\n";
          }
          /*  FRIENDS_GOING, TAGS, HISTORY, INTERESTS, SIMILAR_PARTICIPANTS, DISTANCE   */
          switch (rr) {
            case FRIENDS_GOING:
              reason = reason + eventRecommendationResponse.getEvent().getParticipants().size() + " " + getResources().getString(R.string.reason_friends_participating);
              break;
            case TAGS:
              reason = reason + getResources().getString(R.string.reason_based_on_tags);
              break;
            case HISTORY:
              reason = reason + getResources().getString(R.string.reason_history);
              break;
            case INTERESTS:
              reason = reason + getResources().getString(R.string.reason_interests);
              break;
            case SIMILAR_PARTICIPANTS:
              reason = reason + getResources().getString(R.string.reason_similar_participants);
              break;
            case DISTANCE:
              reason = reason + getResources().getString(R.string.reason_distance);
              break;
            default:
          }
        }
        textViewReason.setText(reason);

        view.setOnClickListener(new View.OnClickListener() {
          @Override
          public void onClick(View v) {
            Intent recommendationDetails = new Intent(MainActivity.this, EventDetailsActivity.class);
            recommendationDetails.putExtra(EventDetailsActivity.PARAM_EVENT_DETAILS, (new Gson()).toJson(eventRecommendationResponse.getEvent()));
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
  private List<EventRecommendationResponse> getSimulatedEvents() {
    List<EventRecommendationResponse> result = new ArrayList<>();

    Event event;
    EventRecommendationResponse err;
    EnumSet<RecommendationReason> reasons;
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
    err = new EventRecommendationResponse(event, EnumSet.of(RecommendationReason.DISTANCE, RecommendationReason.HISTORY), 1);
    result.add(err);

    event = new Event();
    event.setStart_date(new Date());
    event.setEnd_date(new Date((new Date()).getTime() + 360000));
    event.setParticipants(participants);
    event.setDescription("Aquagym courses are given under the supervision of a lifeguard");
    event.setVenue(venue);
    event.setAccessibility(accesibilities);
    event.setTitle("Aquagym");
    err = new EventRecommendationResponse(event, EnumSet.of(RecommendationReason.TAGS, RecommendationReason.HISTORY), 1);
    result.add(err);

    event = new Event();
    event.setStart_date(new Date());
    event.setEnd_date(new Date((new Date()).getTime() + 360000));
    event.setParticipants(participants);
    event.setDescription("Regualar walking, like most aerobic activities, is good for you because cardio-vascular exercise strengthens the heart and lungs, increasing overall fitness");
    event.setVenue(venue);
    event.setAccessibility(accesibilities);
    event.setTitle("Walking");
    err = new EventRecommendationResponse(event, EnumSet.of(RecommendationReason.INTERESTS), 1);
    result.add(err);
    return result;
  }
}
