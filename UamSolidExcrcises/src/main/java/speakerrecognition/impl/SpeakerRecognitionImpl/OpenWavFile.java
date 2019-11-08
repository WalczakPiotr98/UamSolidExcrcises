package speakerrecognition.impl.SpeakerRecognitionImpl;

import speakerrecognition.impl.MyException;
import speakerrecognition.impl.WavFile;

import java.io.IOException;

public class OpenWavFile {
    public int[] openWavFile(String resourcePath) throws IOException, MyException {
        WavFile wavFile = new WavFile(resourcePath);
        wavFile.open();
        return wavFile.getSamples();
    }
}
