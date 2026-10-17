package xyz.volcanobay.voicelib.api.events;

import com.google.gson.JsonObject;
import xyz.volcanobay.voicelib.api.util.SpeechJson;

public interface ClientTalkEvent {
    String getText();

    /**
     * Raw Vosk JSON, including per-word confidence and timing. See {@link SpeechJson} for the format.
     */
    default String getJson() {
        JsonObject object = new JsonObject();
        object.addProperty("text", getText());
        return object.toString();
    }

    /**
     * {@link #getJson()} parsed; a fresh copy on each call
     */
    default JsonObject getJsonObject() {
        return SpeechJson.parse(getJson());
    }

    static ClientTalkEvent of(String text, String json) {
        return new ClientTalkEvent() {
            @Override
            public String getText() {
                return text;
            }

            @Override
            public String getJson() {
                return json;
            }
        };
    }
}
