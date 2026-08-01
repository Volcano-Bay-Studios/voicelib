package xyz.volcanobay.voicelib.networking.packets;

import foundry.veil.api.network.handler.ServerPacketContext;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import org.jetbrains.annotations.NotNull;
import xyz.volcanobay.voicelib.VoiceLib;
import xyz.volcanobay.voicelib.api.VoiceLibApi;
import xyz.volcanobay.voicelib.api.events.ServerPlayerTalkEvent;


public class PlayerSpeakPacket implements CustomPacketPayload {

    public static final Type<PlayerSpeakPacket> TYPE =
            new Type<>(ResourceLocation.fromNamespaceAndPath(VoiceLib.MODID, "player_speak"));
    public static final StreamCodec<RegistryFriendlyByteBuf, PlayerSpeakPacket> CODEC = StreamCodec.composite(
            ByteBufCodecs.STRING_UTF8, p -> p.text,
            PlayerSpeakPacket::new);

    private final String text;

    public PlayerSpeakPacket(String text) {
        this.text = text;
    }

    @NotNull
    @Override
    public Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }

    public static void handleServerSide(PlayerSpeakPacket msg, ServerPacketContext ctx) {
        VoiceLibApi.fireServerPlayerTalkEvent(new ServerPlayerTalkEvent() {
            @Override
            public String getText() {
                return msg.text;
            }

            @Override
            public ServerPlayer getPlayer() {
                return ctx.player();
            }
        });
    }

}