package xyz.volcanobay.voicelib;

import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.client.keybinding.v1.KeyBindingHelper;

public class VoicelibFabric implements ModInitializer {

    @Override
    public void onInitialize() {
        VoiceLib.init();
        KeyBindingHelper.registerKeyBinding(VoiceLibClient.PUSH_TO_TALK);

    }
}
