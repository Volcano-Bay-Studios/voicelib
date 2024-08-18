package org.modogthedev;

import com.mojang.blaze3d.platform.InputConstants;
import dev.architectury.event.events.client.ClientTickEvent;
import dev.architectury.registry.client.keymappings.KeyMappingRegistry;
import net.minecraft.client.KeyMapping;
import org.modogthedev.client.event.EventHandler;

import javax.swing.*;
import javax.swing.text.JTextComponent;

public class VoiceLibClient {
    public static boolean recordingSpeech = false;
    public static boolean alwaysOn = true;
    public static boolean printToChat = true;
    public static KeyMapping vKeyMapping = new KeyMapping(
            "key.voicelib.push_to_talk", // The translation key of the name shown in the Controls screen
            InputConstants.Type.KEYSYM, // This key mapping is for Keyboards by default
            InputConstants.KEY_V, // The default keycode
            "category.voicelib.voicelib" // The category translation key used to categorize in the Controls screen
    );

    public static void init() {
        KeyMappingRegistry.register(vKeyMapping);
        ClientTickEvent.CLIENT_POST.register(minecraft -> {
            if (alwaysOn)
                recordingSpeech = !vKeyMapping.isDown();
            else
                recordingSpeech = vKeyMapping.isDown();
        });

        EventHandler.register();
    }
}
