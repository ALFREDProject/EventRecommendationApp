package alfred.eu.eventrecommendationapp.adapters;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.EnumSet;

import alfred.eu.eventrecommendationapp.R;
import alfred.eu.eventrecommendationapp.web.EventRecommendationResponse;
import alfred.eu.eventrecommendationapp.web.RecommendationReason;

/**
 * Created by thardes on 25/04/2016.
 */
public class ArrayAdapterItem extends ArrayAdapter<EventRecommendationResponse>{

    Context mContext;
    int layoutResourceId;
    EventRecommendationResponse data[] = null;

    public ArrayAdapterItem(Context mContext, int layoutResourceId, EventRecommendationResponse[] data) {

        super(mContext, layoutResourceId, data);

        this.layoutResourceId = layoutResourceId;
        this.mContext = mContext;
        this.data = data;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if(convertView==null){
            LayoutInflater inflater = ((Activity) mContext).getLayoutInflater();
            convertView = inflater.inflate(layoutResourceId, parent, false);
        }
        EventRecommendationResponse objectItem = data[position];

        TextView txt_title = (TextView) convertView.findViewById(R.id.txt_eventTitle);
        txt_title.setText(objectItem.getEvent().getTitle());

        TextView txt_time = (TextView) convertView.findViewById(R.id.txt_time);
        SimpleDateFormat sd =  new SimpleDateFormat("dd.MM.yyyy HH:mm");
        txt_time.setText("26.04.2016 13:37");//sd.format(objectItem.getEvent().getStart_date())

        String basedOn = generateBasedOnText(objectItem.getReasons());
        TextView txt_basedOn = (TextView) convertView.findViewById(R.id.txt_basedOn);
        txt_basedOn.setText(basedOn);

        return convertView;
    }

    private String generateBasedOnText(EnumSet<RecommendationReason> reasons)
    {
        String reason = "";

        if(reasons.contains(RecommendationReason.FRIENDS_GOING))
        {
            reason += "Friends are going - ";
        }
        if(reasons.contains(RecommendationReason.HISTORY))
        {
            reason += "Based on history - ";
        }
        if(reasons.contains(RecommendationReason.INTERESTS)||reasons.contains(RecommendationReason.TAGS))
        {
            reason += "Based on interests - ";
        }

        if(reasons.contains(RecommendationReason.SIMILAR_PARTICIPANTS))
        {
            reason += "Similar participants are joining - ";
        }

        return reason;
    }
}