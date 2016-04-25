package alfred.eu.eventrecommendationapp.adapters;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import alfred.eu.eventrecommendationapp.R;
import alfred.eu.eventrecommendationapp.web.EventRecommendationResponse;

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
        TextView textViewItem = (TextView) convertView.findViewById(R.id.textViewItem);
        textViewItem.setText(objectItem.getEvent().getTitle());
        textViewItem.setTag(objectItem.getWeight());

        return convertView;

    }


}