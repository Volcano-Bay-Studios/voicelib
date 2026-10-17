package xyz.volcanobay.voicelib.api.events;

import com.google.gson.JsonObject;
import net.minecraft.server.level.ServerPlayer;
import xyz.volcanobay.voicelib.api.util.SpeechJson;

public interface ServerPlayerTalkEvent {
    String getText();
    ServerPlayer getPlayer();

    /**
     * Raw Vosk JSON, including per-word confidence and timing. See {@link SpeechJson} for the format.
     * Word data is dropped if the utterance is too large for one packet; the text is always present.
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

    static ServerPlayerTalkEvent of(String text, String json, ServerPlayer player) {
        return new ServerPlayerTalkEvent() {
            @Override
            public String getText() {
                return text;
            }

            @Override
            public ServerPlayer getPlayer() {
                return player;
            }

            @Override
            public String getJson() {
                return json;
            }
        };
    }
}
