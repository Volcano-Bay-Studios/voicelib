package org.modogthedev.neoforge;

import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.common.Mod;

import net.neoforged.fml.event.lifecycle.FMLClientSetupEvent;
import org.modogthedev.VoiceLib;
import org.modogthedev.VoiceLibClient;
import org.modogthedev.client.event.EventHandler;

@Mod(VoiceLib.MOD_ID)
public final class VoiceLibNeoForge {
    public VoiceLibNeoForge(IEventBus eventBus) {
        eventBus.addListener(this::clientSetup);
        VoiceLib.init();
    }
    public void clientSetup(FMLClientSetupEvent event) {
        VoiceLibClientNeoForge.init();
    }
}
