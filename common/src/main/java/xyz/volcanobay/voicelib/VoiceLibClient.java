package xyz.volcanobay.voicelib;

import com.mojang.blaze3d.platform.InputConstants;
import net.minecraft.client.KeyMapping;

public class VoiceLibClient {
    public static boolean recordingSpeech = false;
    public static boolean alwaysOnRecording = true;
    public static boolean printToChat = false;
    public static boolean printToConsole = false;
    public static KeyMapping PUSH_TO_TALK = new KeyMapping(
            "key.voicelib.push_to_talk", // The translation key of the name shown in the Controls screen
            InputConstants.Type.KEYSYM, // This key mapping is for Keyboards by default
            InputConstants.KEY_V, // The default keycode
            "category.voicelib.voicelib" // The category translation key used to categorize in the Controls screen
    );

    public static void clientTick() {
        if (alwaysOnRecording) {
            recordingSpeech = !PUSH_TO_TALK.isDown();
        } else {
            recordingSpeech = PUSH_TO_TALK.isDown();
        }
    }
}
