package org.modogthedev.fabric.client;

import net.fabricmc.api.ClientModInitializer;
import org.modogthedev.VoiceLibClient;

public final class VoiceLibFabricClient implements ClientModInitializer {
    @Override
    public void onInitializeClient() {
        VoiceLibClient.init();
    }
}
