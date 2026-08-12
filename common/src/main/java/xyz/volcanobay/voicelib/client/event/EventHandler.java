package xyz.volcanobay.voicelib.client.event;


import foundry.veil.api.network.VeilPacketManager;
import net.minecraft.client.Minecraft;
import net.minecraft.network.chat.Component;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.FilenameUtils;
import org.apache.commons.io.IOUtils;
import org.vosk.Model;
import xyz.volcanobay.voicelib.VoiceLibClient;
import xyz.volcanobay.voicelib.VoiceLibConstants;
import xyz.volcanobay.voicelib.VoiceLib;
import xyz.volcanobay.voicelib.api.VoiceLibApi;
import xyz.volcanobay.voicelib.api.events.ClientTalkEvent;
import xyz.volcanobay.voicelib.networking.packets.PlayerSpeakPacket;
import xyz.volcanobay.voicelib.networking.packets.PlayerSpeakPartialPacket;
import xyz.volcanobay.voicelib.speech.MicrophoneHandler;
import xyz.volcanobay.voicelib.speech.SpeechRecognizer;

import javax.sound.sampled.AudioFormat;
import java.io.*;
import java.net.URL;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Enumeration;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;

public class EventHandler {
    /**
     * The following variables are used to store the speech recognizer
     */
    private static MicrophoneHandler microphoneHandler;

    /**
     * The following variables are used to store the microphone handler
     */
    private static SpeechRecognizer speechRecognizer;

    /**
     * The following variables are used to store the last recognized result
     */
    private static String lastResult = "";
    /**
     * The following variables are used to store the last partial recognized result
     */
    private static String lastPartialResult = "";

    /**
     * The following variables are used to store the thread that listens to the microphone
     */
    private static Thread listenThread;
    private static boolean recordingLastTick = false;
    private static String modelType = "vosk-model-en-us-0.22-lgraph";

//    public static void register() {
//        ClientTickEvent.CLIENT_PRE.register(minecraft -> handleStartClientTickEvent());
//        ClientTickEvent.CLIENT_POST.register(minecraft -> handleEndClientTickEvent());
//        ClientLifecycleEvent.CLIENT_STARTED.register(minecraft -> handelClientStartEvent());
//        ClientLifecycleEvent.CLIENT_STOPPING.register(minecraft -> handleClientStopEvent());
//    }

    /**
     * Sets the Model, this will set the path for the model to be downloaded.
     * Different models can be used to change the language, although the
     * mod would also have to be configured for that language.
     * I do NOT recommend changing this as the other models are more than 1GB
     * See <a href="https://alphacephei.com/vosk/models">Vosk Models</a>}
     * The default is "vosk-model-small-en-us-0.15"
     * @param string
     */
    public static void setModel(String string) {
        modelType = string;
    }

    private static void listenThreadTask() {
        while (true) {
            try {
                if (speechRecognizer == null) {         // wait 10 seconds and try to initialize the speech recognizer again
//                    if (Minecraft.getInstance().player != null) {
//                        Minecraft.getInstance().player.sendSystemMessage(Component.literal("§cAcoustic Model Load Failed"));
//                    }
                    // listenThread.wait(10000);
                    try {
                        Thread.sleep(10000);
                    } catch (InterruptedException ie) {
                        continue;
                    }

                    speechRecognizer = new SpeechRecognizer(new Model(getPathAndDownload()), VoiceLibConstants.sampleRate);
                } else if (microphoneHandler == null) {  // wait 10 seconds and try to initialize the microphone handler again
                    listenThread.wait(10000);
                    microphoneHandler = new MicrophoneHandler(new AudioFormat(VoiceLibConstants.sampleRate, 16, 1, true, false));
                    microphoneHandler.startListening();  // Try to restart microphone
                } else {                                 // If the speech recognizer and the microphone handler are initialized successfully
                    String tmp = speechRecognizer.getStringMsg(microphoneHandler.readData());
                    if (!tmp.isEmpty() && !tmp.equals(lastResult) &&
                            VoiceLibClient.recordingSpeech) {   // Read audio data from the microphone and send it to the speech recognizer for recognition
                        if (VoiceLibConstants.encoding_repair) {
                            lastResult = SpeechRecognizer.repairEncoding(tmp, VoiceLibConstants.srcEncoding, VoiceLibConstants.dstEncoding);
                        } else {                                        // default configuration without encoding repair
                            lastResult = tmp;                           // restore the recognized text
                        }
                    }

                    tmp = speechRecognizer.getPartialResult();
                    if (!tmp.isEmpty() && !tmp.equals(lastPartialResult) &&
                            VoiceLibClient.recordingSpeech) {   // Read audio data from the microphone and send it to the speech recognizer for recognition
                        if (VoiceLibConstants.encoding_repair) {
                            lastPartialResult = SpeechRecognizer.repairEncoding(tmp, VoiceLibConstants.srcEncoding, VoiceLibConstants.dstEncoding);
                        } else {                                        // default configuration without encoding repair
                            lastPartialResult = tmp;                           // restore the recognized text
                        }
                    }
                }
            } catch (Exception e) {
                VoiceLib.LOGGER.error(e.getMessage());
            }
        }
    }

    private static void unzip(Path path, Charset charset) throws IOException {
        String fileBaseName = FilenameUtils.getBaseName(path.getFileName().toString());
        Path destFolderPath = Paths.get(path.getParent().toString(), fileBaseName);

        try (ZipFile zipFile = new ZipFile(path.toFile(), ZipFile.OPEN_READ, charset)) {
            Enumeration<? extends ZipEntry> entries = zipFile.entries();
            while (entries.hasMoreElements()) {
                ZipEntry entry = entries.nextElement();
                Path entryPath = destFolderPath.resolve(entry.getName());
                if (entryPath.normalize().startsWith(destFolderPath.normalize())) {
                    if (entry.isDirectory()) {
                        Files.createDirectories(entryPath);
                    } else {
                        Files.createDirectories(entryPath.getParent());
                        try (InputStream in = zipFile.getInputStream(entry)) {
                            try (OutputStream out = new FileOutputStream(entryPath.toFile())) {
                                IOUtils.copy(in, out);
                            }
                        }
                    }
                }
            }
        }
    }

    private static String getPathAndDownload() {
        String path = "";
        try {
            File file = new File("vosk/"+modelType);
            path = new File("vosk/"+modelType).getAbsoluteFile().toString();
            if (!file.exists()) {
                VoiceLib.LOGGER.info("Downloading voice model...");
                File download = new File("vosk.zip");
                FileUtils.copyURLToFile(new URL("https://alphacephei.com/vosk/models/"+modelType+".zip"), download);
                if (download.exists()) {
                    unzip(download.getAbsoluteFile().toPath(), Charset.defaultCharset());
                } else {
                    VoiceLib.LOGGER.error("Failed to download, check for mod update");
                }
                VoiceLib.LOGGER.info("Download complete, loading vosk...");
            }
        } catch (IOException e) {
            VoiceLib.LOGGER.error("Failed to download, check for mod update");
        }
        return path;
    }

    public static void handelClientStartEvent() {     // when the client launch
//        VoiceLib.LOGGER.info("Loading acoustic model from " + getPathAndDownload() + "   ..."); // Log the path of the acoustic model
//        try {                                  // Initialize the speech recognizer
//            speechRecognizer = new SpeechRecognizer(new Model(getPathAndDownload()), VoiceLibConstants.sampleRate);
//            VoiceLib.LOGGER.info("Acoustic model loaded successfully!");
//        } catch (Exception e1) {
//            VoiceLib.LOGGER.error(e1.getMessage());
//        }
        try {                                   // Initialize the microphone handler, single channel, 16 bits per sample, signed, little endian
            microphoneHandler = new MicrophoneHandler(new AudioFormat(VoiceLibConstants.sampleRate, 16, 1, true, false));
            microphoneHandler.startListening();
            VoiceLib.LOGGER.info("Microphone handler initialized successfully!");
        } catch (Exception e2) {
            VoiceLib.LOGGER.error(e2.getMessage());
        }
        if (VoiceLibConstants.encoding_repair) {         // If the encoding repair function is enabled, log a warning
            VoiceLib.LOGGER.warn(
                    String.format("(test function) Trt to resolve error encoding from %s to %s...", VoiceLibConstants.srcEncoding, VoiceLibConstants.dstEncoding));
        }
        listenThread = new Thread(EventHandler::listenThreadTask);
        listenThread.start();
    }

    public static void resetListener() {
        handleClientStopEvent();
    }

    public static void handleClientStopEvent() {
        listenThread.interrupt();                 // Stop the thread that listens to the microphone
        microphoneHandler.stopListening();        // Stop listening to the microphone
        speechRecognizer = null;
        microphoneHandler = null;
        listenThread = null;                      // Clear the thread
    }

    public static void handleEndClientTickEvent() {
        if (VoiceLibClient.recordingSpeech &&           // If the user presses the key V
                microphoneHandler != null ) {
            if (!lastResult.isEmpty()) {
                if (VoiceLibClient.printToConsole) { // If the recognized text is not empty
                    VoiceLib.LOGGER.info(VoiceLibConstants.prefix + lastResult);
                }
                if (Minecraft.getInstance().player != null) {
                    if (VoiceLibClient.printToChat) {
                        Minecraft.getInstance().player.sendSystemMessage(Component.literal(VoiceLibConstants.prefix + lastResult));
                    }
                    VeilPacketManager.server().sendPacket(new PlayerSpeakPacket(lastResult));
                    VoiceLibApi.fireClientTalkEvent(() -> lastResult);
                }
                lastResult = "";                                                   // Clear the recognized text
            }
            if (!lastPartialResult.isEmpty()) {
                if (VoiceLibClient.printToConsole) { // If the recognized text is not empty
                    VoiceLib.LOGGER.info(VoiceLibConstants.partialPrefix + lastPartialResult);
                }
                if (Minecraft.getInstance().player != null) {
                    if (VoiceLibClient.printToChat) {
                        Minecraft.getInstance().player.sendSystemMessage(Component.literal(VoiceLibConstants.partialPrefix + lastResult));
                    }
                    VeilPacketManager.server().sendPacket(new PlayerSpeakPartialPacket(lastPartialResult));
                    VoiceLibApi.fireClientPartialTalkEvent(() -> lastPartialResult);
                }
                lastPartialResult = "";
            }
        }
    }

    public static void handleStartClientTickEvent() {
//        if (listenThread == null)
//            handelClientStartEvent();
        if (VoiceLibClient.recordingSpeech) {  // If the user presses the key V
            if (!recordingLastTick)
                VoiceLib.LOGGER.info("Recording...");
            recordingLastTick = true;
        } else {
            recordingLastTick = false;
            if (lastResult.length() > 0) {
                lastResult = "";
            }
        }
    }
}
