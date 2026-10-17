package xyz.volcanobay.voicelib.api.util;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import java.nio.charset.StandardCharsets;

/**
 * Helpers for the raw Vosk JSON carried by talk events.
 */
public final class SpeechJson {
    private SpeechJson() {}

    /**
     * Parse Vosk JSON; returns an empty object if it isn't a JSON object
     */
    public static JsonObject parse(String json) {
        try {
            JsonElement element = JsonParser.parseString(json);
            return element.isJsonObject() ? element.getAsJsonObject() : new JsonObject();
        } catch (RuntimeException e) {
            return new JsonObject();
        }
    }

    /**
     * The recognized text: "text" for final results, "partial" for partial results
     */
    public static String text(String json) {
        JsonObject object = parse(json);
        JsonElement text = object.has("text") ? object.get("text") : object.get("partial");
        return text != null && text.isJsonPrimitive() ? text.getAsString() : "";
    }

    /**
     * Returns the json unchanged if it fits in maxBytes of UTF-8, otherwise only its text field
     */
    public static String fit(String json, int maxBytes) {
        if (json.getBytes(StandardCharsets.UTF_8).length <= maxBytes) return json;
        JsonObject object = parse(json);
        JsonObject trimmed = new JsonObject();
        String key = object.has("text") ? "text" : "partial";
        trimmed.addProperty(key, text(json));
        return trimmed.toString();
    }
}
