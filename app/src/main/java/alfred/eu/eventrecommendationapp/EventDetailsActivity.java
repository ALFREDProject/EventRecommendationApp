package alfred.eu.eventrecommendationapp;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.text.DateFormat;
import java.util.Map;

import alfred.eu.eventrecommendationapp.actions.RecommendationDecisionAction;
import eu.alfred.api.personalization.model.eventrecommendation.Event;
import eu.alfred.ui.AppActivity;
import eu.alfred.ui.CircleButton;

public class EventDetailsActivity extends AppActivity {

  public static final String PARAM_EVENT_DETAILS = "eventdetails";

  private static final String LOGTAG = EventDetailsActivity.class.getSimpleName();

  private static final String RECOMENDATION_DECISION = "RecommendationDecision";

  private TextView textViewTitle;
  private TextView textViewDate;
  private TextView textViewStartInfo;
  private TextView textViewEndInfo;
  private TextView textViewLocationInfo;
  private TextView textViewLocationExtraInfo;
  private TextView textViewFriendsInfo;
  private TextView textViewMobilityInfo;
  private TextView textViewDescriptionInfo;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_event_details);

    textViewTitle = (TextView) findViewById(R.id.textViewTitle);
    textViewDate = (TextView) findViewById(R.id.textViewDate);
    textViewStartInfo = (TextView) findViewById(R.id.textViewStartInfo);
    textViewEndInfo = (TextView) findViewById(R.id.textViewEndInfo);
    textViewLocationInfo = (TextView) findViewById(R.id.textViewLocationInfo);
    textViewLocationExtraInfo = (TextView) findViewById(R.id.textViewLocationExtraInfo);
    textViewFriendsInfo = (TextView) findViewById(R.id.textViewFriendsInfo);
    textViewMobilityInfo = (TextView) findViewById(R.id.textViewMobilityInfo);
    textViewDescriptionInfo = (TextView) findViewById(R.id.textViewDescriptionInfo);

    try {
      Type responseType = new TypeToken<Event>(){}.getType();
      Event receivedEvent = (new Gson()).fromJson(getIntent().getStringExtra(PARAM_EVENT_DETAILS), responseType);

      textViewTitle.setText(receivedEvent.getTitle());
      textViewDate.setText(DateFormat.getDateTimeInstance().format(receivedEvent.getStart_date()));
      textViewStartInfo.setText(DateFormat.getDateTimeInstance().format(receivedEvent.getStart_date()));
      textViewEndInfo.setText(DateFormat.getDateTimeInstance().format(receivedEvent.getEnd_date()));
      textViewLocationInfo.setText(receivedEvent.getVenue().getName());
      textViewLocationExtraInfo.setText(receivedEvent.getVenue().getAddress());
      String participants = "";
      for (String s : receivedEvent.getParticipants()) {
        if ("".equalsIgnoreCase(participants)) {
          participants = s;
        } else {
          participants = participants + ", " + s;
        }
      }
      textViewFriendsInfo.setText(participants);
      String accessibilities = "";
      for (String s : receivedEvent.getAccessibility()) {
        if ("".equalsIgnoreCase(accessibilities)) {
          accessibilities = s;
        } else {
          accessibilities = accessibilities + ", " + s;
        }
      }
      textViewMobilityInfo.setText(accessibilities);
      textViewDescriptionInfo.setText(receivedEvent.getDescription());
    }catch (Exception e) {
      Log.e(LOGTAG, "Failed to get param " + PARAM_EVENT_DETAILS);
      e.printStackTrace();
    }

    // PA Buttons
    circleButton = (CircleButton) findViewById(R.id.voiceControlBtn);
    circleButton.setOnTouchListener(new AppActivity.CircleTouchListener());
  }


  public void onClickGo(View v) {
    Toast.makeText(EventDetailsActivity.this, "Go! TODO", Toast.LENGTH_SHORT).show();
    //TODO inform the recommendation manager
  }

  public void onClickDontGo(View v) {
    Toast.makeText(EventDetailsActivity.this, "Don't go! TODO", Toast.LENGTH_SHORT).show();
    //TODO inform the recommendation manager
  }

  @Override
  public void performAction(String command, Map<String, String> map) {
    Log.d(LOGTAG, "Action performed!");
    switch (command) {
      case (RECOMENDATION_DECISION):
        RecommendationDecisionAction rda = new RecommendationDecisionAction(this, cade);
        rda.performAction(command, map);
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
}
