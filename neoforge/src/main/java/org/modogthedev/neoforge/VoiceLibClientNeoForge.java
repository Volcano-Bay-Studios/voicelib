package org.modogthedev.neoforge;

import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.common.Mod;
import org.modogthedev.VoiceLibClient;

@Mod(value = "voicelib", dist = Dist.CLIENT)
public class VoiceLibClientNeoForge {
    public VoiceLibClientNeoForge(IEventBus modBus) {
        VoiceLibClient.init();
    }
}
