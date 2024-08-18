package org.modogthedev.neoforge;

import net.neoforged.api.distmarker.Dist;
import net.neoforged.fml.common.Mod;

import org.modogthedev.VoiceLib;
import org.modogthedev.VoiceLibClient;

@Mod(VoiceLib.MOD_ID)
public final class VoiceLibNeoForge {
    public VoiceLibNeoForge() {
        // Run our common setup.
        VoiceLib.init();
    }
}
