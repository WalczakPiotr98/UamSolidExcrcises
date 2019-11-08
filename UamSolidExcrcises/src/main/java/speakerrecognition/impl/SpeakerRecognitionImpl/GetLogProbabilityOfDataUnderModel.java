package speakerrecognition.impl.SpeakerRecognitionImpl;

import speakerrecognition.impl.MyException;
import speakerrecognition.impl.SpeakerModel;

public class GetLogProbabilityOfDataUnderModel {
    public double getLogProbabilityOfDataUnderModel(SpeakerModel model, double[][] dataToBeTested) throws MyException {
        return model.getScore(dataToBeTested);
    }
}
