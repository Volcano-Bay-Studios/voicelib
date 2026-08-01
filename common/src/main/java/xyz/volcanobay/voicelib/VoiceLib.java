package xyz.volcanobay.voicelib;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import xyz.volcanobay.voicelib.api.VoiceLibExample;
import xyz.volcanobay.voicelib.networking.VoiceLibPackets;

public class VoiceLib {
    public static final String MODID = "voicelib";
    public static final String MOD_NAME = "voicelib";
    public static final Logger LOGGER = LoggerFactory.getLogger(MOD_NAME);
    public static final boolean exampleEnabled = true;

    public static void init() {
        VoiceLibPackets.register();
        VoiceLibExample.init();
    }
}
