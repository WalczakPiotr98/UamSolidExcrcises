package speakerrecognition.impl.GMM;

import speakerrecognition.impl.KMeans.KMeans;
import speakerrecognition.impl.Matricies;
import speakerrecognition.impl.Statistics;

public class GMM {
    private static final double EPS = 2.2204460492503131e-16;
    private int n_init = 10;
    private int n_iter = 10;
    public static int numOfComponents;
    private double[][] observations;
    private double min_covar = 0.001;
    private boolean converged = false;
    private double current_log_likelihood = 0;
    private double prev_log_likelihood = Double.NaN;
    private double tol = 0.001;
    private double[] log_likelihoods = null;
    private double[][] responsibilities = null;
    private double[][] means = null;
    private double[] weights = null;
    private double[][] covars = null;
    private double[][] best_means = null;
    private double[] best_weights = null;
    private double[][] best_covars = null;


    public GMM(double[][] data, int compNum) {
        this.observations = data;

        this.numOfComponents = compNum;
        this.means = new double[compNum][data[0].length];
        this.weights = new double[data.length];
        this.covars = new double[compNum][data[0].length];
    }

    public void fit() {
        double change = 0;

        try {

            double[][] cv;
            double max_log_prob = Double.NEGATIVE_INFINITY;

            for (int i = 0; i < this.n_init; i++) {
                KMeans kMeans = new KMeans(this.observations, this.numOfComponents);
                kMeans.fit();
                this.means = kMeans.get_centers();
                this.weights = Matricies.fillWith(this.weights, (double) 1 / this.numOfComponents);

                this.covars = Matricies.cov(Matricies.transpose(this.observations)); //np.cov(X.T), gmm.py line 450
                cv = Matricies.eye(this.observations[0].length, this.min_covar); //self.min_covar * np.eye(X.shape[1])
                this.covars = Matricies.addMatrixes(this.covars, cv);
                this.covars = Matricies.duplicate(Matricies.chooseDiagonalValues(this.covars), this.numOfComponents);

                for (int j = 0; j < this.n_iter; j++) {
                    prev_log_likelihood = current_log_likelihood;
                    ScoreSamples score_samples = new ScoreSamples(this.observations, this.means, this.covars, this.weights);
                    this.log_likelihoods = score_samples.getLogprob();
                    this.responsibilities = score_samples.getResponsibilities();
                    current_log_likelihood = Statistics.getMean(log_likelihoods);

                    if (!Double.isNaN(prev_log_likelihood)) {
                        change = Math.abs(current_log_likelihood - prev_log_likelihood);
                        if (change < this.tol) {
                            this.converged = true;
                            break;
                        }

                    }
                    do_mstep(this.observations, this.responsibilities);
                }

                if (current_log_likelihood > max_log_prob) {
                    max_log_prob = current_log_likelihood;
                    this.best_means = this.means;
                    this.best_covars = this.covars;
                    this.best_weights = this.weights;

                }
            }

            if (Double.isInfinite(max_log_prob))
                System.out.println("EM algorithm was never able to compute a valid likelihood given initial parameters");
        } catch (Exception myEx) {
            myEx.printStackTrace();
            System.exit(1);
        }

    }
    public double[][] get_means() {
        return this.best_means;
    }

    public double[][] get_covars() {
        return this.best_covars;
    }

    public double[] get_weights() {
        return this.best_weights;
    }

    private void do_mstep(double[][] data, double[][] responsibilities) {
        try {
            double[] weights = Matricies.sum(responsibilities, 0);
            double[][] weighted_X_sum = Matricies.multiplyByMatrix(Matricies.transpose(responsibilities), data);
            double[] inverse_weights = Matricies.invertElements(Matricies.addValue(weights, 10 * EPS));
            this.weights = Matricies.addValue(Matricies.multiplyByValue(weights, 1.0 / (Matricies.sum(weights) + 10 * EPS)), EPS);
            this.means = Matricies.multiplyByValue(weighted_X_sum, inverse_weights);
            this.covars = covar_mstep_diag(this.means, data, responsibilities, weighted_X_sum, inverse_weights, this.min_covar);
        } catch (Exception myEx) {
            myEx.printStackTrace();
            System.exit(1);
        }

    }

    private double[][] covar_mstep_diag(double[][] means, double[][] X, double[][] responsibilities, double[][] weighted_X_sum, double[] norm, double min_covar) {
        double[][] temp = null;
        try {
            double[][] avg_X2 = Matricies.multiplyByValue(Matricies.multiplyByMatrix(Matricies.transpose(responsibilities), Matricies.multiplyMatrixesElByEl(X, X)), norm);
            double[][] avg_means2 = Matricies.power(means, 2);
            double[][] avg_X_means = Matricies.multiplyByValue(Matricies.multiplyMatrixesElByEl(means, weighted_X_sum), norm);
            temp = Matricies.addValue(Matricies.addMatrixes(Matricies.substractMatrixes(avg_X2, Matricies.multiplyByValue(avg_X_means, 2)), avg_means2), min_covar);
        } catch (Exception myEx) {
            System.out.println("An exception encourred: " + myEx.getMessage());
            myEx.printStackTrace();
            System.exit(1);
        }
        return temp;
    }
}
