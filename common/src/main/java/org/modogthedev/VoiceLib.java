package org.modogthedev;

import net.minecraft.resources.ResourceLocation;
import org.modogthedev.networking.VoiceLibPackets;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public final class VoiceLib {
    public static final String MOD_ID = "voicelib";

    public static Logger LOGGER = LoggerFactory.getLogger(MOD_ID);
    public static ResourceLocation id(String text) {
        return ResourceLocation.fromNamespaceAndPath(MOD_ID,text);
    }
    public static void init() {
        VoiceLibPackets.register();
        // Write common init code here.
    }
}
