package alfred.eu.eventrecommendationapp;

import android.Manifest;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.provider.CalendarContract;
import android.support.v4.app.ActivityCompat;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.EnumSet;
import java.util.List;
import java.util.Map;
import java.util.TimeZone;

import eu.alfred.api.personalization.helper.eventrecommendation.EventHelper;
import eu.alfred.api.personalization.helper.eventrecommendation.EventRatingTransfer;
import eu.alfred.api.personalization.model.eventrecommendation.Event;
import eu.alfred.api.personalization.model.eventrecommendation.EventRecommendationResponse;
import eu.alfred.api.personalization.model.eventrecommendation.GlobalsettingsKeys;
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
    private String userId;

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
    SharedPreferences sp;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        sp = getSharedPreferences("global_settings",MODE_PRIVATE);
        setContentView(R.layout.event_details);//getSharedPreferences("global_settings",MODE_PRIVATE)
        build();
    }

    private void build() {
        Intent i = getIntent();
        Bundle b = i.getExtras();
        userId = b.get("userId").toString();
        eventTitle = b.get("eventTitle").toString();
        eventStartDate = b.get("eventStartDate").toString();
        eventEndDate = b.get("eventEndDate").toString();
        eventLocale = b.get("eventLocale").toString();//b.get("eventAddress").toString();
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
                storeEventForRating(eventId);
                eventrecommendationManager.acceptRejectEvent(userId,eventId,true);
                Toast.makeText(EventDetailsActivity.this,"Applied", Toast.LENGTH_SHORT).show();

                EventDetailsActivity.this.finish();
            }
        };
        View.OnClickListener dontGoHandler = new View.OnClickListener() {
            public void onClick(View v) {
                Toast.makeText(EventDetailsActivity.this,"Applied", Toast.LENGTH_SHORT).show();
                eventrecommendationManager.acceptRejectEvent(userId,eventId,false);
                Intent i = new Intent(EventDetailsActivity.this, MainActivity.class);
                startActivity(i);
            }
        };
        Button btn_DontGo= (Button) this.findViewById(R.id.dontGoButton);
        Button btn_go= (Button) this.findViewById(R.id.goButton);

        btn_DontGo.setOnClickListener(dontGoHandler);
        btn_go.setOnClickListener(goHandler);
    }
    private void storeEventForRating(String eventId) {
        String json = prefs.getString(GlobalsettingsKeys.userEventsAccepted,"");
        ArrayList<EventRatingTransfer> x  =EventHelper.jsonToEventTransferList(json);
        DateFormat format = new SimpleDateFormat("yyyy.MM.dd");
        boolean found = false;
        for (EventRatingTransfer r: x) {
            if(r.getEventID().equals(eventId))
            {
             found = true;
            }
        }
        if(!found)
        {
            try {
                x.add(new EventRatingTransfer(eventId,format.parse(eventEndDate),format.parse(eventStartDate),format.parse(eventStartDate),eventTitle,eventDescription));
            } catch (ParseException e) {
                e.printStackTrace();
            }
        }
        SharedPreferences.Editor edit = prefs.edit();
        edit.putString(GlobalsettingsKeys.userEventsAccepted,EventHelper.eventTransferListToJson(x));
        edit.commit();
        globalSettings.setGlobalSetting(GlobalsettingsKeys.userEventsAccepted+"",EventHelper.eventTransferListToJson(x));
        edit.apply();
    }
    private void addToCalendar(ContentResolver cr, ContentValues values)
    {
      /*  if (ActivityCompat.checkSelfPermission(this, Manifest.permission.WRITE_CALENDAR) != PackageManager.PERMISSION_GRANTED) {

            return;
        }
        Uri uri = cr.insert(CalendarContract.Events.CONTENT_URI, values);

// get the event ID that is the last element in the Uri
        long eventID = Long.parseLong(uri.getLastPathSegment());
        System.out.println(eventID);*/
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
