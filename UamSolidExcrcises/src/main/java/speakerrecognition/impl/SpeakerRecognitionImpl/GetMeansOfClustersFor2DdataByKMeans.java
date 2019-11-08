package speakerrecognition.impl.SpeakerRecognitionImpl;

import speakerrecognition.impl.KMeans.KMeans;

public class GetMeansOfClustersFor2DdataByKMeans {
    public double[][] getMeansOfClustersFor2DdataByKMeans(double[][] data, int numOfClusters) {
        KMeans kMeans = new KMeans(data, numOfClusters);
        kMeans.fit();
        return kMeans.get_centers();
    }
}
