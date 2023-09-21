package app.fiveminchange;

import app.fiveminchange.model.RootObject;
import com.fasterxml.jackson.databind.ObjectMapper;

public class JsonMapper {

    public RootObject getRootObj (String json) {
        ObjectMapper objectMapper = new ObjectMapper();
        try {
            return objectMapper.readValue(json, RootObject.class);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}
