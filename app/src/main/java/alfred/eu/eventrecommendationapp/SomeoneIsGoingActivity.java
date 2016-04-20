package alfred.eu.eventrecommendationapp;

import android.content.Intent;
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

import eu.alfred.api.personalization.model.eventrecommendation.Event;
import eu.alfred.ui.AppActivity;
import eu.alfred.ui.CircleButton;

public class SomeoneIsGoingActivity extends AppActivity {

  public static final String PARAM_EVENT_DETAILS = "eventdetails";

  private static final String LOGTAG = SomeoneIsGoingActivity.class.getSimpleName();

  private Event receivedEvent;

  private TextView textViewWho;
  private TextView textViewWhen;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_someone_is_going);

    //This activity should be started from a notification

    textViewWho = (TextView) findViewById(R.id.textViewWho);
    textViewWhen = (TextView) findViewById(R.id.textViewWhen);

    try {
      Type responseType = new TypeToken<Event>(){}.getType();
      receivedEvent = (new Gson()).fromJson(getIntent().getStringExtra(PARAM_EVENT_DETAILS), responseType);

      String participants = "";
      for (String s : receivedEvent.getParticipants()) {
        if ("".equalsIgnoreCase(participants)) {
          participants = s;
        } else {
          participants = participants + ", " + s;
        }
      }
      textViewWho.setText(participants + " --> " + receivedEvent.getTitle());
      textViewWhen.setText(DateFormat.getDateTimeInstance().format(receivedEvent.getStart_date()));
    }catch (Exception e) {
      Log.e(LOGTAG, "Failed to get param " + PARAM_EVENT_DETAILS);
    }

    // PA Buttons
    circleButton = (CircleButton) findViewById(R.id.voiceControlBtn);
    circleButton.setOnTouchListener(new AppActivity.CircleTouchListener());
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

  /**
   * Click on the screen
   * @param v
   */
  public void onClickDetails(View v) {
    Toast.makeText(SomeoneIsGoingActivity.this, "View event details", Toast.LENGTH_SHORT).show();
    Intent recommendationDetails = new Intent(SomeoneIsGoingActivity.this, EventDetailsActivity.class);
    recommendationDetails.putExtra("event", (new Gson()).toJson(receivedEvent));
    startActivity(recommendationDetails);
  }
}
