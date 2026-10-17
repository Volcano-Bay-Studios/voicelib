package xyz.volcanobay.voicelib.speech;

import org.vosk.Model;
import org.vosk.Recognizer;

import xyz.volcanobay.voicelib.api.util.SpeechJson;

import java.io.UnsupportedEncodingException;

public class SpeechRecognizer {
    public Recognizer recognizer;

    public SpeechRecognizer(Model model, int sampleRate) throws Exception{
        if (model != null) {
            recognizer = new Recognizer(model, sampleRate);
            recognizer.setWords(true);          // per-word conf/start/end in final results
            recognizer.setPartialWords(true);   // and in partial results
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
        String json = acceptAudio(data);
        return json == null ? "" : SpeechJson.text(json);
    }

    public String getPartialResult() {
        return SpeechJson.text(getPartialJson());
    }

    /**
     * Feed audio data
     * @return the Vosk result JSON when an utterance completes, otherwise null
     */
    public String acceptAudio(byte[] data) {
        return recognizer.acceptWaveForm(data, data.length) ? recognizer.getResult() : null;
    }

    /**
     * @return the Vosk partial JSON for the utterance in progress
     */
    public String getPartialJson() {
        return recognizer.getPartialResult();
    }

    /**
     * Flush the recognizer: the result JSON for any audio not yet returned as a result, then resets
     */
    public String getFinalJson() {
        return recognizer.getFinalResult();
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
