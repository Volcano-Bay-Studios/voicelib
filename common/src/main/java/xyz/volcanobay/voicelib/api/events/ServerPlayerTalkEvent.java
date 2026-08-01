package xyz.volcanobay.voicelib.api.events;

import net.minecraft.server.level.ServerPlayer;

public interface ServerPlayerTalkEvent {
    String getText();
    ServerPlayer getPlayer();
}
