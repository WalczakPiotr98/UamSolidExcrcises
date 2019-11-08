package speakerrecognition.impl.GMM;

import speakerrecognition.impl.Matricies;

public class ScoreSamples{
    private double[][] data = null;
    private double[] log_likelihoods = null;
    private double[][] means = null;
    private double[][] covars = null;
    private double[] weights = null;
    private double[] logprob = null;
    private double[][] responsibilities = null;


    ScoreSamples(double[][] X, double[][] means, double[][] covars, double[] weights){
        this.data = X;
        this.log_likelihoods = new double[X.length];
        this.responsibilities = new double[X.length][GMM.numOfComponents];
        this.means = means;
        this.covars = covars;
        this.weights = weights;


        try{
            double[][] lpr = log_multivariate_normal_density(this.data, this.means, this.covars);
            lpr = Matricies.addValue(lpr, Matricies.makeLog(this.weights));
            this.logprob = Matricies.logsumexp(lpr);
            this.responsibilities = Matricies.exp(Matricies.substractValue(lpr, logprob));
        }
        catch(Exception myEx)
        {
            myEx.printStackTrace();
            System.exit(1);
        }

    }

    public double[] getLogprob(){
        return this.logprob;
    }

    public double[][] getResponsibilities(){
        return this.responsibilities;
    }

    private double[][] log_multivariate_normal_density(double[][] data, double[][] means, double[][] covars){
        double[][] lpr = new double[data.length][means.length];
        int n_dim = data[0].length;

        try{
            double[] sumLogCov = Matricies.sum(Matricies.makeLog(covars), 1); //np.sum(np.log(covars), 1)
            double[] sumDivMeanCov = Matricies.sum(Matricies.divideElements(Matricies.power(this.means, 2), this.covars),1); //np.sum((means ** 2) / covars, 1)
            double[][] dotXdivMeanCovT = Matricies.multiplyByValue(Matricies.multiplyByMatrix(data, Matricies.transpose(Matricies.divideElements(means, covars))), -2); //- 2 * np.dot(X, (means / covars).T)
            double[][] dotXdivOneCovT = Matricies.multiplyByMatrix(Matricies.power(data,  2), Matricies.transpose(Matricies.invertElements(covars)));


            sumLogCov = Matricies.addValue(sumLogCov,n_dim * Math.log(2*Math.PI)); //n_dim * np.log(2 * np.pi) + np.sum(np.log(covars), 1)
            sumDivMeanCov = Matricies.addMatrixes(sumDivMeanCov, sumLogCov); // n_dim * np.log(2 * np.pi) + np.sum(np.log(covars), 1) + np.sum((means ** 2) / covars, 1)
            dotXdivOneCovT = Matricies.sum(dotXdivOneCovT, dotXdivMeanCovT); //- 2 * np.dot(X, (means / covars).T) + np.dot(X ** 2, (1.0 / covars).T)
            dotXdivOneCovT = Matricies.addValue(dotXdivOneCovT, sumDivMeanCov); // (n_dim * np.log(2 * np.pi) + np.sum(np.log(covars), 1) + np.sum((means ** 2) / covars, 1) - 2 * np.dot(X, (means / covars).T) + np.dot(X ** 2, (1.0 / covars).T))
            lpr = Matricies.multiplyByValue(dotXdivOneCovT, -0.5);
        }
        catch(Exception myEx)
        {
            System.out.println("An exception encourred: " + myEx.getMessage());
            myEx.printStackTrace();
            System.exit(1);
        }

        return lpr;
    }
}