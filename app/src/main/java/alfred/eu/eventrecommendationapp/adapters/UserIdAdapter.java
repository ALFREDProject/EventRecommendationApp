package alfred.eu.eventrecommendationapp.adapters;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import alfred.eu.eventrecommendationapp.R;
import eu.alfred.api.personalization.model.eventrecommendation.EventRecommendationResponse;

/**
 * Created by thardes on 03/05/2016.
 */
public class UserIdAdapter extends ArrayAdapter<String> {

     String[] data;
    int layoutResourceId;
    Context mContext;
    public UserIdAdapter(Context context, int resource,String[] data) {
        super(context, resource,data);
        this.layoutResourceId = resource;
        this.mContext = context;
        this.data = data;
    }
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if(convertView==null){
            LayoutInflater inflater = ((Activity) mContext).getLayoutInflater();
            convertView = inflater.inflate(layoutResourceId, parent, false);
        }

        // object item based on the position
        String objectItem = data[position];

        // get the TextView and then set the text (item name) and tag (item ID) values
        TextView textViewItem = (TextView) convertView.findViewById(R.id.textViewItem);
        textViewItem.setText(objectItem);
        return convertView;
    }
}
