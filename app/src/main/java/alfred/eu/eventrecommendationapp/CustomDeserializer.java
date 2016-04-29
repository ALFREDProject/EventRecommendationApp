package alfred.eu.eventrecommendationapp;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;

import java.lang.reflect.Type;

/**
 * Created by thardes on 29/04/2016.
 */
public class CustomDeserializer  implements JsonDeserializer<MainActivity> {

    @Override
    public MainActivity deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
        if (json == null)
            return null;

        JsonObject jo = json.getAsJsonObject();
        return  null;
    }
}
