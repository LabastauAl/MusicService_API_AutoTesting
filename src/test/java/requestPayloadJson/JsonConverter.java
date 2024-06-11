package requestPayloadJson;

import io.restassured.path.json.JsonPath;

public class JsonConverter {
    public static JsonPath stringToJson (String payloadString){
        JsonPath json = new JsonPath(payloadString);
        return json;
    }
}
