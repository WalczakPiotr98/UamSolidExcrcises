package speakerrecognition.impl.KMeans;

import speakerrecognition.impl.Matricies;
import speakerrecognition.impl.Matricies;


public class Kmeans_single{
    private int[] best_labels = null;
    private double[][] best_centers = null;
    private double best_inertia = Double.MAX_VALUE;
    private double[] distances = null;

    Kmeans_single(double[][] data, int n_clusters, double[] x_sq_norms, int max_iter, double tol){

        try{

            double[][] centers = KMeans.init_centroids(data, n_clusters, x_sq_norms);
            this.distances = new double[data.length];

            for(int i=0; i<max_iter;i++){
                double[][] centers_old = centers.clone();
                LabelsInertia labelsInertia = new LabelsInertia(data, x_sq_norms, centers, this.distances);
                int[] labels = labelsInertia.getLabels().clone();
                double inertia = labelsInertia.getInertia();
                this.distances = labelsInertia.getDistances().clone();

                centers = KMeans.centers_dense(data, labels, n_clusters, distances);

                if (inertia<best_inertia){
                    this.best_labels = labels.clone();
                    this.best_centers = centers.clone();
                    this.best_inertia = inertia;
                }

                if (Matricies.squared_norm(Matricies.substractMatrixes(centers_old, centers))<=tol)
                    break;

            }
        }
        catch(Exception myEx)
        {
            myEx.printStackTrace();
            System.exit(1);
        }
    }

    public int[] get_best_labels(){
        return this.best_labels;
    }

    public double[][] get_best_centers(){
        return this.best_centers;
    }

    public double get_best_inertia(){
        return this.best_inertia;
    }
}