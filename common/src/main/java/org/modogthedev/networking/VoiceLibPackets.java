package org.modogthedev.networking;
import dev.architectury.networking.NetworkManager;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerPlayer;
import org.jetbrains.annotations.NotNull;
import org.modogthedev.VoiceLib;
import org.modogthedev.networking.packets.PlayerSpeakPacket;

import java.nio.channels.NetworkChannel;

public class VoiceLibPackets {
    private static NetworkChannel INSTANCE;

    public static void register() {
        NetworkManager.registerReceiver(NetworkManager.c2s(), PlayerSpeakPacket.PLAYER_SPEAK_PACKET_TYPE,
                PlayerSpeakPacket.STREAM_CODEC, PlayerSpeakPacket::handleServerSide);
    }

    public static void sendToServer(@NotNull CustomPacketPayload message) {
        if (NetworkManager.canServerReceive(message.type())) {
            NetworkManager.sendToServer(message);
        }
    }
}