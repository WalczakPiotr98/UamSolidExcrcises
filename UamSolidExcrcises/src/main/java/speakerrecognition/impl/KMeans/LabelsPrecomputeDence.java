package speakerrecognition.impl.KMeans;

import speakerrecognition.impl.Matricies;

public  class LabelsPrecomputeDence{
    private double[][] data = null;
    public static int[] labels = null;
    private double[][] centers = null;
    public static double[] distances = null;
    private double[] x_squared_norms = null;
    public static double inertia = 0;

    LabelsPrecomputeDence(double[][] X, double[] x_squared_norms, double[][] centers, double[] distances){
        this.centers = centers;
        this.x_squared_norms = x_squared_norms;
        this.distances = distances;
        this.data = X;

        int n_samples = data.length;
        int k = centers.length;
        double[][] all_distances = Matricies.euclidean_distances(centers, X, x_squared_norms);
        this.labels = new int[n_samples];
        this.labels = Matricies.addValue(this.labels, -1);
        double[] mindist = new double[n_samples];
        mindist = Matricies.addValue(mindist, Double.POSITIVE_INFINITY);

        for(int center_id=0;center_id<k;center_id++){
            double[] dist = all_distances[center_id];
            for(int i=0;i<labels.length;i++){
                if(dist[i]<mindist[i]){
                    this.labels[i] = center_id;
                }
                mindist[i]=Math.min(dist[i], mindist[i]);
            }
        }
        if(n_samples == this.distances.length)
            this.distances = mindist;
        this.inertia = Matricies.sum(mindist);
    }
}