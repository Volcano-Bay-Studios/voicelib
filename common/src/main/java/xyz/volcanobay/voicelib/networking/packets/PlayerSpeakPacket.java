package xyz.volcanobay.voicelib.networking.packets;

import foundry.veil.api.network.handler.ServerPacketContext;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;
import org.jetbrains.annotations.NotNull;
import xyz.volcanobay.voicelib.VoiceLib;
import xyz.volcanobay.voicelib.api.VoiceLibApi;
import xyz.volcanobay.voicelib.api.events.ServerPlayerTalkEvent;
import xyz.volcanobay.voicelib.api.util.SpeechJson;


public class PlayerSpeakPacket implements CustomPacketPayload {

    public static final Type<PlayerSpeakPacket> TYPE =
            new Type<>(ResourceLocation.fromNamespaceAndPath(VoiceLib.MODID, "player_speak"));
    public static final StreamCodec<RegistryFriendlyByteBuf, PlayerSpeakPacket> CODEC = StreamCodec.composite(
            ByteBufCodecs.STRING_UTF8, p -> p.json,
            PlayerSpeakPacket::new);

    private final String json;   // Vosk JSON; the text is read from it on the server

    public PlayerSpeakPacket(String json) {
        this.json = json;
    }

    @NotNull
    @Override
    public Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }

    public static void handleServerSide(PlayerSpeakPacket msg, ServerPacketContext ctx) {
        VoiceLibApi.fireServerPlayerTalkEvent(ServerPlayerTalkEvent.of(SpeechJson.text(msg.json), msg.json, ctx.player()));
    }

}