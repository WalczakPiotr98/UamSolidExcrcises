package speakerrecognition.impl.KMeans;

import speakerrecognition.impl.Matricies;

public  class LabelsInertia{
    public double[][] data = null;
    public int[] labels = null;
    public double[][] centers = null;
    public double[] distances = null;
    public double[] x_squared_norms = null;
    public double inertia = 0;

    public int[] getLabels(){
        return this.labels.clone();
    }

    public double getInertia(){
        return this.inertia;
    }

    public double[] getDistances(){
        return this.distances.clone();
    }

    LabelsInertia(double[][] X, double[] x_squared_norms, double[][] centers, double[] distances){
        this.centers = centers;
        this.x_squared_norms = x_squared_norms;
        this.distances = distances;
        this.data = X;

        int n_samples = data.length;
        int[] labels = new int[n_samples];
        labels = Matricies.addValue(labels, -1);

        LabelsPrecomputeDence result = new LabelsPrecomputeDence(this.data, this.x_squared_norms, this.centers, this.distances);
        this.labels = result.labels.clone();
        this.inertia = result.inertia;
        this.distances = result.distances.clone();

    }

}