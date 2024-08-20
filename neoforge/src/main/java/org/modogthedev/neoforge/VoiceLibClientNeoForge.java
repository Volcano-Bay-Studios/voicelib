package org.modogthedev.neoforge;

import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.common.Mod;
import org.modogthedev.VoiceLibClient;

public class VoiceLibClientNeoForge {
    public static void init() {
        VoiceLibClient.init();
    }
}
