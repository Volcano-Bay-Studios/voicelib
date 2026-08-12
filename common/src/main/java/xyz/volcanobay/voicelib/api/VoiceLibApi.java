package xyz.volcanobay.voicelib.api;


import xyz.volcanobay.voicelib.VoiceLibClient;
import xyz.volcanobay.voicelib.api.events.ClientTalkEvent;
import xyz.volcanobay.voicelib.api.events.ServerPlayerTalkEvent;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

public class VoiceLibApi {
    private static List<Consumer<ServerPlayerTalkEvent>> serverPlayerTalkEventListeners = new ArrayList<>();
    private static List<Consumer<ClientTalkEvent>> clientTalkEventListeners = new ArrayList<>();
    private static List<Consumer<ServerPlayerTalkEvent>> serverPlayerPartialTalkEventListeners = new ArrayList<>();
    private static List<Consumer<ClientTalkEvent>> clientPartialTalkEventListeners = new ArrayList<>();


    /**
     * Register a consumer for a ServerPlayerTalkEvent. Whenever a player speaks,
     * it will be sent to the server and this event will be fired.
     * @param consumer
     */
    public static void registerServerPlayerSpeechListener(Consumer<ServerPlayerTalkEvent> consumer) {
        serverPlayerTalkEventListeners.add(consumer);
    }

    /**
     * Register a consumer for a ClientTalkEvent. Whenever the user speaks,
     * the event will be fired.
     * @param consumer
     */
    public static void registerClientSpeechListener(Consumer<ClientTalkEvent> consumer) {
        clientTalkEventListeners.add(consumer);
    }


    /**
     * Register a consumer for a ServerPlayerTalkEvent. Whenever partial speech is updated,
     * it will be sent to the server and this event will be fired.
     * @param consumer
     */
    public static void registerServerPlayerPartialSpeechListener(Consumer<ServerPlayerTalkEvent> consumer) {
        serverPlayerPartialTalkEventListeners.add(consumer);
    }

    /**
     * Register a consumer for a ClientTalkEvent. Whenever partial speech is updated,
     * the event will be fired.
     * @param consumer
     */
    public static void registerClientPartialSpeechListener(Consumer<ClientTalkEvent> consumer) {
        clientPartialTalkEventListeners.add(consumer);
    }

    /**
     * Sets if voice messages are printed into the chat
     * @param printToChat
     */
    public static void setPrintToChat(boolean printToChat) {
        VoiceLibClient.printToChat = printToChat;
    }
    /**
     * Sets if voice messages are printed into the console
     * @param printToConsole
     */
    public static void setPrintToConsole(boolean printToConsole) {
        VoiceLibClient.printToConsole = printToConsole;
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

    public static void fireServerPlayerPartialTalkEvent(ServerPlayerTalkEvent event) {
        for (Consumer<ServerPlayerTalkEvent> consumer: serverPlayerPartialTalkEventListeners) {
            consumer.accept(event);
        }
    }

    public static void fireClientPartialTalkEvent(ClientTalkEvent event) {
        for (Consumer<ClientTalkEvent> consumer: clientPartialTalkEventListeners) {
            consumer.accept(event);
        }
    }
}
