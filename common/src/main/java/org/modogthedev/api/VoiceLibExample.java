package org.modogthedev.api;

import net.minecraft.world.damagesource.DamageType;
import org.modogthedev.VoiceLib;

public class VoiceLibExample {
    public static void init() {
        VoiceLibApi.registerServerPlayerSpeechListener((serverPlayerTalkEvent -> {
            if (VoiceLib.exampleEnabled && serverPlayerTalkEvent.getText().contains("ouch"))
                serverPlayerTalkEvent.getPlayer().igniteForSeconds(2);
        }));
    }
}
