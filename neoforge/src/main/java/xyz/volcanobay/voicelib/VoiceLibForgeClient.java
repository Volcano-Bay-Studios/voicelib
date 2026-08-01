package xyz.volcanobay.voicelib;

import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.Mod;
import net.neoforged.neoforge.client.event.RegisterKeyMappingsEvent;

@Mod(value = VoiceLib.MODID, dist = Dist.CLIENT)
public class VoiceLibForgeClient {
    @SubscribeEvent
    public static void registerKeyMappings(RegisterKeyMappingsEvent event) {
        event.register(VoiceLibClient.PUSH_TO_TALK);
    }

}
