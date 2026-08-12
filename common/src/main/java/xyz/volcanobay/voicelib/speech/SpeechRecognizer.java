package xyz.volcanobay.voicelib.speech;

import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
import org.vosk.Model;
import org.vosk.Recognizer;

import java.io.UnsupportedEncodingException;

public class SpeechRecognizer {
    public Recognizer recognizer;

    public SpeechRecognizer(Model model, int sampleRate) throws Exception{
        if (model != null) {
            recognizer = new Recognizer(model, sampleRate);
        } else {
            throw new Exception("Acoustic model not loaded.");
        }
    }

    /**
     * Get the text from the audio data
     * @param data Audio data
     * @return {@link java.lang.String} message
     */
    public String getStringMsg(byte[] data) {
        if (recognizer.acceptWaveForm(data, data.length)) {
            // MicrophoneTextInputMain.LOGGER.info(recognizer.getResult());
            return JsonParser.parseString(recognizer.getResult()).getAsJsonObject().get("text").getAsString();
        } else {
            return "";
        }
    }

    public String getPartialResult() {
        String partialResult = recognizer.getPartialResult();
        if (partialResult == null) return partialResult;
        JsonElement partial = JsonParser.parseString(partialResult).getAsJsonObject().get("partial");
        if (partial == null) return partialResult;
        return partial.getAsString();
    }

    /**
     * Beta function, it may cause some unpredictable errors
     * Repair the error encoding of the string
     * @param str String to be repaired
     * @param srcEncoding Original encoding
     * @param dstEncoding Encoding to be repaired
     * @return {@link java.lang.String} message
     * @throws UnsupportedEncodingException Unsupported encoding
     */
    public static String repairEncoding(String str, String srcEncoding, String dstEncoding) throws UnsupportedEncodingException {
        return new String(str.getBytes(srcEncoding), dstEncoding);
    }
}
