package org.modogthedev.api;

import org.modogthedev.api.events.ClientTalkEvent;
import org.modogthedev.api.events.ServerPlayerTalkEvent;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

public class VoiceLibApi {
    private static List<Consumer<ServerPlayerTalkEvent>> serverPlayerTalkEventListeners = new ArrayList<>();
    private static List<Consumer<ClientTalkEvent>> clientTalkEventListeners = new ArrayList<>();

    /**
     * Register a consumer for a ServerPlayerTalkEvent. Whenever a player speaks,
     * it will be sent to the server and this event will be fired.
     * @param consumer
     */
    public static void registerServerPlayerSpeechListener(Consumer<ServerPlayerTalkEvent> consumer) {
        serverPlayerTalkEventListeners.add(consumer);
    }
    public static void registerClientSpeechListener(Consumer<ClientTalkEvent> consumer) {
        clientTalkEventListeners.add(consumer);
    }

    public static void fireServerPlayerTalkEvent(ServerPlayerTalkEvent event) {
        for (Consumer<ServerPlayerTalkEvent> consumer: serverPlayerTalkEventListeners) {
            consumer.accept(event);
        }
    }

    public static void fireClientTalkEvent(ClientTalkEvent event) {
        for (Consumer<ClientTalkEvent> consumer: clientTalkEventListeners) {
            consumer.accept(event);
        }
    }

}
