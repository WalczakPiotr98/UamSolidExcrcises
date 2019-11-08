package speakerrecognition.impl;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;

import org.junit.Test;

import speakerrecognition.impl.GMM.GMM;
import speakerrecognition.impl.SpeakerRecognitionImpl.SpeakerRecognitionImplementation;


public class TestClass {

	private SpeakerRecognition speakerRecognition = new SpeakerRecognitionImplementation() {
	};

	@Test
	public void testCase() throws IOException, ClassNotFoundException, MyException {

		//given


		WavFile wavFile = new WavFile("src\\test\\resources\\training\\speaker1_2.WAV");
		wavFile.open();
		int[] x = wavFile.getSamples();
		int fs = wavFile.getFs();
		MFCC mfcc = new MFCC(x, fs);
		double[][] speaker_mfcc = mfcc.getMFCC();
		GMM gmm = new GMM(speaker_mfcc, 32);
		gmm.fit();
		SpeakerModel speakerModel1 = new SpeakerModel(gmm.get_means(), gmm.get_covars(), gmm.get_weights(), "speaker1model");

		WavFile wavFile2 = new WavFile("src\\test\\resources\\training\\speaker2_2.WAV");
		wavFile2.open();
		int[] x2 = wavFile2.getSamples();
		int fs2 = wavFile2.getFs();
		MFCC mfcc2 = new MFCC(x2, fs2);
		double[][] speaker_mfcc2 = mfcc2.getMFCC();
		GMM gmm2 = new GMM(speaker_mfcc2, 32);
		gmm2.fit();
		SpeakerModel speakerModel2 = new SpeakerModel(gmm2.get_means(), gmm2.get_covars(), gmm2.get_weights(), "speaker2model");

		List<SpeakerModel> speakerModels = Arrays.asList(speakerModel1, speakerModel2);

		//when

		System.out.println(speakerRecognition.recognize(speakerModels, "src\\test\\resources\\test\\speaker1_1.WAV"));
		System.out.println(speakerRecognition.recognize(speakerModels, "src\\test\\resources\\test\\speaker2_1.WAV"));


		speakerRecognition.printLogProbsForRecognition(speakerModels, "src\\test\\resources\\test\\speaker1_1.WAV");
		speakerRecognition.printLogProbsForRecognition(speakerModels, "src\\test\\resources\\test\\speaker2_1.WAV");

		//then

	}

}
