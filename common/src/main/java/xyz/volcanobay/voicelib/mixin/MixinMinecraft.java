package xyz.volcanobay.voicelib.mixin;

import net.minecraft.client.Minecraft;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import xyz.volcanobay.voicelib.VoiceLibClient;
import xyz.volcanobay.voicelib.client.event.EventHandler;

@Mixin(Minecraft.class)
public class MixinMinecraft {
    @Inject(at = @At("TAIL"), method = "<init>")
    private void init(CallbackInfo info) {
        EventHandler.handelClientStartEvent();
    }

    @Inject(method = "tick", at = @At("HEAD"))
    public void startTick(CallbackInfo info) {
        EventHandler.handleStartClientTickEvent();
        VoiceLibClient.clientTick();
    }

    @Inject(method = "tick", at = @At("TAIL"))
    public void endTick(CallbackInfo info) {
        EventHandler.handleEndClientTickEvent();
    }

    @Inject(method = "close", at = @At("HEAD"))
    public void close(CallbackInfo info) {
        EventHandler.handleClientStopEvent();
    }
}
