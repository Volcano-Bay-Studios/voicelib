package xyz.volcanobay.voicelib.networking;
import foundry.veil.api.network.VeilPacketManager;
import xyz.volcanobay.voicelib.VoiceLib;
import xyz.volcanobay.voicelib.networking.packets.PlayerSpeakPacket;
import xyz.volcanobay.voicelib.networking.packets.PlayerSpeakPartialPacket;

public class VoiceLibPackets {

    private static final VeilPacketManager INSTANCE = VeilPacketManager.create(VoiceLib.MODID, "1");

    public static void register() {
        INSTANCE.registerServerbound(PlayerSpeakPacket.TYPE, PlayerSpeakPacket.CODEC, PlayerSpeakPacket::handleServerSide);
        INSTANCE.registerServerbound(PlayerSpeakPartialPacket.TYPE, PlayerSpeakPartialPacket.CODEC, PlayerSpeakPartialPacket::handleServerSide);
    }

}