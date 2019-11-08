package speakerrecognition.impl.SpeakerRecognitionImpl;

import speakerrecognition.impl.MFCC;

public class ComputeMFCC {
    public double[][] computeMFCC(int[] soundSamples, int fs) {
        MFCC mfcc = new MFCC(soundSamples, fs);
        double[][] speaker_mfcc = mfcc.getMFCC();
        return speaker_mfcc;
    }
}
