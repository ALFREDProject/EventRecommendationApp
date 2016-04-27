package alfred.eu.eventrecommendationapp;

import android.Manifest;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.provider.CalendarContract;
import android.support.v4.app.ActivityCompat;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.EnumSet;
import java.util.Map;
import java.util.TimeZone;

import eu.alfred.api.personalization.model.eventrecommendation.RecommendationReason;
import eu.alfred.ui.AppActivity;

/**
 * Created by thardes on 26/04/2016.
 */
public class EventDetailsActivity extends AppActivity implements GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener {
    private String eventTitle;
    private String eventStartDate;
    private String eventEndDate;
    private String eventLocale;
    private String eventDescription;
    private EnumSet<RecommendationReason> reasons;
    private String eventId;

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
        eventId = b.get("eventId").toString();
        eventDescription = b.get("eventDescription").toString();
        reasons = (EnumSet<RecommendationReason>) b.get("reasons");


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
                Calendar cal = Calendar.getInstance();
                TimeZone tz = cal.getTimeZone();
                Date date = null;
                ContentResolver cr = getContentResolver();
                ContentValues values = new ContentValues();
                DateFormat format = new SimpleDateFormat("dd.MM.yyyy");
                try {
                    date = format.parse(eventStartDate);
                    values.put(CalendarContract.Events.DTSTART, date.getTime());
                    date = format.parse(eventEndDate);
                    values.put(CalendarContract.Events.DTEND, date.getTime());
                } catch (ParseException e) {
                    e.printStackTrace();
                }

                values.put(CalendarContract.Events.TITLE, eventTitle);
                values.put(CalendarContract.Events.DESCRIPTION, eventDescription);
                values.put(CalendarContract.Events.CALENDAR_ID, date.getTime());

                values.put(CalendarContract.Events.EVENT_TIMEZONE, tz.getDisplayName());

                addToCalendar(cr,values);
                Intent i = new Intent(EventDetailsActivity.this, MainActivity.class);
                startActivity(i);
            }
        };
        View.OnClickListener dontGoHandler = new View.OnClickListener() {
            public void onClick(View v) {
                // Toast.makeText(EventDetailsActivity.this,"Hallo Don't go", Toast.LENGTH_SHORT).show();
                Intent i = new Intent(EventDetailsActivity.this, MainActivity.class);
                startActivity(i);
            }
        };
        Button btn_DontGo= (Button) this.findViewById(R.id.dontGoButton);
        Button btn_go= (Button) this.findViewById(R.id.goButton);

        btn_DontGo.setOnClickListener(dontGoHandler);
        btn_go.setOnClickListener(goHandler);
    }
    private void addToCalendar(ContentResolver cr, ContentValues values)
    {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.WRITE_CALENDAR) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        Uri uri = cr.insert(CalendarContract.Events.CONTENT_URI, values);

// get the event ID that is the last element in the Uri
        long eventID = Long.parseLong(uri.getLastPathSegment());
        System.out.println(eventID);
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
