package alfred.eu.eventrecommendationapp;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

public class EventDetailsActivity extends AppCompatActivity {

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_event_details);
  }


  public void onClickGo(View v) {
    Toast.makeText(EventDetailsActivity.this, "Go!", Toast.LENGTH_SHORT).show();
  }

  public void onClickDontGo(View v) {
    Toast.makeText(EventDetailsActivity.this, "Don't go!", Toast.LENGTH_SHORT).show();
  }
}
