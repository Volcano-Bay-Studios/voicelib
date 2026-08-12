package xyz.volcanobay.voicelib.api;

import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.Entity;
import xyz.volcanobay.voicelib.VoiceLib;
import xyz.volcanobay.voicelib.api.util.PhoneticComparison;

public class VoiceLibExample {
    public static void init() {
        VoiceLibApi.registerServerPlayerSpeechListener((serverPlayerTalkEvent -> {
            ServerPlayer player = serverPlayerTalkEvent.getPlayer();
            if (VoiceLib.exampleEnabled && PhoneticComparison.compareSentences(serverPlayerTalkEvent.getText(),"I call upon the, set thy flame here and now") > 0.8) {
                VoiceLib.LOGGER.info("Player " + player.getName().getString() + " said: " + serverPlayerTalkEvent.getText());
                for (Entity entity : player.level().getEntities(player, player.getBoundingBox().inflate(7))) {
                    entity.igniteForSeconds(5);
                }
            }
        }));
    }
}
