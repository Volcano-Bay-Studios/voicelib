package org.modogthedev.api;

import net.minecraft.world.damagesource.DamageType;

public class VoiceLibExample {
    public static void init() {
        VoiceLibApi.registerServerPlayerSpeechListener((serverPlayerTalkEvent -> {
            if (serverPlayerTalkEvent.getText().contains("ouch"))
                serverPlayerTalkEvent.getPlayer().igniteForSeconds(2);
        }));
    }
}
