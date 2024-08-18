package org.modogthedev.networking.packets;

import dev.architectury.networking.NetworkManager;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Player;
import org.jetbrains.annotations.NotNull;
import org.modogthedev.VoiceLib;
import org.modogthedev.api.VoiceLibApi;
import org.modogthedev.api.events.ServerPlayerTalkEvent;

import static org.modogthedev.VoiceLib.MOD_ID;

public class PlayerSpeakPacket implements CustomPacketPayload {

    public static final Type<PlayerSpeakPacket> PLAYER_SPEAK_PACKET_TYPE =
            new Type<>(ResourceLocation.fromNamespaceAndPath(MOD_ID, "player_speak"));
    public static final StreamCodec<RegistryFriendlyByteBuf, PlayerSpeakPacket> STREAM_CODEC = StreamCodec.composite(
            ByteBufCodecs.STRING_UTF8, p -> p.text,
            PlayerSpeakPacket::new);

    private final String text;

    public PlayerSpeakPacket(String text) {
        this.text = text;
    }

    @NotNull
    @Override
    public Type<? extends CustomPacketPayload> type() {
        return PLAYER_SPEAK_PACKET_TYPE;
    }

    public static void handleServerSide(PlayerSpeakPacket msg, NetworkManager.PacketContext ctx) {
        VoiceLibApi.fireServerPlayerTalkEvent(new ServerPlayerTalkEvent() {
            @Override
            public String getText() {
                return msg.text;
            }

            @Override
            public ServerPlayer getPlayer() {
                return (ServerPlayer)ctx.getPlayer();
            }
        });
    }

}