package utils;

import java.util.Base64;

public class Base64coder {
    public static String encodeToBase64(String string1, String string2){
        String initialString = string1 + ":" + string2;
        String encodedString;
        Base64.Encoder encoder = Base64.getEncoder();
        encodedString = encoder.encodeToString(initialString.getBytes());
        return encodedString;
    }
}
