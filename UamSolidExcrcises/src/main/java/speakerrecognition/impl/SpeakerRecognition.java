package speakerrecognition.impl;

import java.io.IOException;
import java.util.List;

public interface SpeakerRecognition {
    String recognize(List<SpeakerModel> speakerModels, String resourceSoundSpeechFilePath) throws IOException, MyException;
    void printLogProbsForRecognition(List<SpeakerModel> speakerModels, String resourceSoundSpeechFilePath) throws IOException, MyException;
}
