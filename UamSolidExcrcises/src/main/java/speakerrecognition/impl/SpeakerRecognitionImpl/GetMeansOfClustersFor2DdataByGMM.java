package speakerrecognition.impl.SpeakerRecognitionImpl;

import speakerrecognition.impl.GMM.GMM;

public class GetMeansOfClustersFor2DdataByGMM {
    public double[][] getMeansOfClustersFor2DdataByGMM(double[][] data, int numOfClusters) {
        GMM gmm = new GMM(data, numOfClusters);
        gmm.fit();
        return gmm.get_means();
    }

}
