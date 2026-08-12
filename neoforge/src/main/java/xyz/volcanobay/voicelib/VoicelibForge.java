package xyz.volcanobay.voicelib;

import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.common.Mod;

@Mod(VoiceLib.MODID)
public class VoicelibForge {
    public VoicelibForge(IEventBus eventBus) {
        VoiceLib.init();
    }
}
