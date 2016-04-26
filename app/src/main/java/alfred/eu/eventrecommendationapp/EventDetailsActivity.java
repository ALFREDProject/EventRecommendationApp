package alfred.eu.eventrecommendationapp;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;

import java.util.EnumSet;
import java.util.Map;

import alfred.eu.eventrecommendationapp.web.RecommendationReason;
import eu.alfred.ui.AppActivity;

/**
 * Created by thardes on 26/04/2016.
 */
public class EventDetailsActivity extends AppActivity  implements GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener {
    private String eventTitle;
    private String eventStartDate;
    private String eventEndDate;
    private String eventLocale;
    private String eventDescription;
    private EnumSet<RecommendationReason> reasons;

    @Override
    public void performAction(String s, Map<String, String> map) {
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.event_details);
        build();
    }

    private void build() {
        Intent i = getIntent();
        Bundle b = i.getExtras();
        eventTitle = b.get("eventTitle").toString();
        eventStartDate = b.get("eventStartDate").toString();
        eventEndDate = b.get("eventEndDate").toString();
        eventLocale = b.get("eventLocale").toString();
        eventDescription = b.get("eventDescription").toString();
        reasons = (EnumSet<RecommendationReason>)b.get("reasons");


        TextView txt_title = (TextView) this.findViewById(R.id.txt_eventTitle);
        txt_title.setText(eventTitle);
        TextView txt_time_start = (TextView) this.findViewById(R.id.txt_startTime);
        txt_time_start.setText(eventStartDate);
        TextView txt_time_end = (TextView) this.findViewById(R.id.txt_endTime);
        txt_time_end.setText(eventEndDate);
        TextView txt_location = (TextView) this.findViewById(R.id.txt_location);
        txt_location.setText(eventLocale);
        TextView txt_description = (TextView) this.findViewById(R.id.txt_description);
        txt_description.setText(eventDescription);

        View.OnClickListener goHandler = new View.OnClickListener() {
            public void onClick(View v) {
                // it was the 1st button
            }
        };
        View.OnClickListener dontGoHandler = new View.OnClickListener() {
            public void onClick(View v) {

            }
        };
    }

    @Override
    public void onConnected(Bundle bundle) {

    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {

    }
}
