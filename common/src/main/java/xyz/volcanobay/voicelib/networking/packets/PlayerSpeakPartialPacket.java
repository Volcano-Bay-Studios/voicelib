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


public class PlayerSpeakPartialPacket implements CustomPacketPayload {

    public static final Type<PlayerSpeakPartialPacket> TYPE =
            new Type<>(ResourceLocation.fromNamespaceAndPath(VoiceLib.MODID, "player_speak_partial"));
    public static final StreamCodec<RegistryFriendlyByteBuf, PlayerSpeakPartialPacket> CODEC = StreamCodec.composite(
            ByteBufCodecs.STRING_UTF8, p -> p.json,
            PlayerSpeakPartialPacket::new);

    private final String json;   // Vosk JSON; the text is read from it on the server

    public PlayerSpeakPartialPacket(String json) {
        this.json = json;
    }

    @NotNull
    @Override
    public Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }

    public static void handleServerSide(PlayerSpeakPartialPacket msg, ServerPacketContext ctx) {
        VoiceLibApi.fireServerPlayerPartialTalkEvent(ServerPlayerTalkEvent.of(SpeechJson.text(msg.json), msg.json, ctx.player()));
    }

}