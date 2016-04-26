package alfred.eu.eventrecommendationapp.web;

/**
 * Created by thardes on 25/04/2016.
 */

import android.os.Parcel;
import android.os.Parcelable;

import java.util.EnumSet;

/**
 * Created by thardes on 20/04/2016.
 */
public class EventRecommendationResponse implements Parcelable {
    private Event event;
    private EnumSet<RecommendationReason> reasons;
    private int weight;

    public EventRecommendationResponse(Event event, EnumSet<RecommendationReason> reasons, int weight)
    {
        this.event = event;
        this.reasons = reasons;
        this.weight = weight;
    }


    protected EventRecommendationResponse(Parcel in) {
        weight = in.readInt();
    }

    public static final Creator<EventRecommendationResponse> CREATOR = new Creator<EventRecommendationResponse>() {
        @Override
        public EventRecommendationResponse createFromParcel(Parcel in) {
            return new EventRecommendationResponse(in);
        }

        @Override
        public EventRecommendationResponse[] newArray(int size) {
            return new EventRecommendationResponse[size];
        }
    };

    public Event getEvent() {
        return event;
    }

    public void setEvent(Event event) {
        this.event = event;
    }

    public EnumSet<RecommendationReason> getReasons() {
        return reasons;
    }

    public void setReasons(EnumSet<RecommendationReason> reasons) {
        this.reasons = reasons;
    }

    public int getWeight() {
        return weight;
    }

    public void setWeight(int weight) {
        this.weight = weight;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel out, int flags) {


    }
}
