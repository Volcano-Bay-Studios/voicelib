package org.modogthedev.api.events;

import net.minecraft.server.level.ServerPlayer;

public interface ServerPlayerTalkEvent {
    String getText();
    ServerPlayer getPlayer();
}
