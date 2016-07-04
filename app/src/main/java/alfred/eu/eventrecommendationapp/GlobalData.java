package alfred.eu.eventrecommendationapp;

import java.util.ArrayList;

import eu.alfred.api.personalization.model.eventrecommendation.EventRecommendationResponse;

/**
 * Created by hardes on 04/07/2016.
 */
public class GlobalData {
    private  ArrayList<EventRecommendationResponse> resp;
    private static  GlobalData instance;

    private GlobalData() {

    }
    public static GlobalData getInstance()
    {
        if(instance == null)
            return new GlobalData();
        else
            return instance;
    }

    public  ArrayList<EventRecommendationResponse> getResp() {
        return resp;
    }

    public void setResp(ArrayList<EventRecommendationResponse> resp) {
        this.resp = resp;
    }
}
