package speakerrecognition.impl.SpeakerRecognitionImpl;

import java.io.IOException;
import java.util.List;

import speakerrecognition.impl.*;


public abstract class SpeakerRecognitionImplementation implements SpeakerRecognition {

    public String recognize(List<SpeakerModel> speakerModels, String resourceSoundSpeechFilePath) throws IOException, MyException {
        double finalScore = Long.MIN_VALUE;
        String bestFittingModelName = "";
        for(SpeakerModel model : speakerModels){
            WavFile wavFile1 = new WavFile(resourceSoundSpeechFilePath);
            wavFile1.open();
            int[] x3 = wavFile1.getSamples();
            int fs3 = wavFile1.getFs();
            MFCC mfcc3 = new MFCC(x3, fs3);
            double[][] speaker_mfcc3 = mfcc3.getMFCC();
            double scoreForTest1 = model.getScore(speaker_mfcc3);
            if(scoreForTest1 > finalScore){
                finalScore = scoreForTest1;
                bestFittingModelName = model.getName();
            }

        }
        String recogResult = "Test speech from file "+resourceSoundSpeechFilePath + " is most similar to model "+ bestFittingModelName;
        return recogResult;
    }
    public void printLogProbsForRecognition(List<SpeakerModel> speakerModels, String resourceSoundSpeechFilePath)
            throws IOException, MyException {
        for(SpeakerModel model : speakerModels){
            WavFile wavFile1 = new WavFile(resourceSoundSpeechFilePath);
            wavFile1.open();
            int[] x3 = wavFile1.getSamples();
            int fs3 = wavFile1.getFs();
            MFCC mfcc3 = new MFCC(x3, fs3);
            double[][] speaker_mfcc3 = mfcc3.getMFCC();
            double scoreForTest1 = model.getScore(speaker_mfcc3);
            System.out.println("Test speech from file "+resourceSoundSpeechFilePath + " is similar to model "+ model.getName()+" with log probability "+scoreForTest1);
        }
    }
}