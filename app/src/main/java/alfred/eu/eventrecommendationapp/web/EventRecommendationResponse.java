package alfred.eu.eventrecommendationapp.web;

/**
 * Created by thardes on 25/04/2016.
 */

import java.util.EnumSet;

/**
 * Created by thardes on 20/04/2016.
 */
public class EventRecommendationResponse {
    private Event event;
    private EnumSet<RecommendationReason> reasons;
    private int weight;

    public EventRecommendationResponse(Event event, EnumSet<RecommendationReason> reasons, int weight)
    {
        this.event = event;
        this.reasons = reasons;
        this.weight = weight;
    }


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
}
