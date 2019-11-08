package speakerrecognition.impl;
import java.io.Serializable;

public class SpeakerModel implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private double[][] means=null;
	private double[][] covars=null;
	private double[] weights = null;
	private String name = null;
	
	public SpeakerModel(double[][] means, double[][] covars, double[] weights, String name){
		this.means = means;
		this.covars=covars;
		this.weights=weights;
		this.name = name;
	}

	
	public String getName(){
		return this.name;
	}
	
	public double getScore(double[][] data) throws MyException{
		double score = 0;
		double[] logprob = null;
		double[][] lpr = log_multivariate_normal_density(data, this.means, this.covars);
		lpr = Matricies.addValue(lpr, Matricies.makeLog(this.weights));
		logprob = Matricies.logsumexp(lpr);
		score = Statistics.getMean(logprob);
		return score;
	}
	
	private double[][] log_multivariate_normal_density(double[][] data, double[][] means, double[][] covars) throws MyException{
		double[][] lpr;
		int n_dim = data[0].length;
		
		double[] sumLogCov = Matricies.sum(Matricies.makeLog(covars), 1); //np.sum(np.log(covars), 1)
		double[] sumDivMeanCov = Matricies.sum(Matricies.divideElements(Matricies.power(this.means, 2), this.covars),1); //np.sum((means ** 2) / covars, 1)
		double[][] dotXdivMeanCovT = Matricies.multiplyByValue(Matricies.multiplyByMatrix(data, Matricies.transpose(Matricies.divideElements(means, covars))), -2); //- 2 * np.dot(X, (means / covars).T)
		double[][] dotXdivOneCovT = Matricies.multiplyByMatrix(Matricies.power(data,  2), Matricies.transpose(Matricies.invertElements(covars)));
		
		
		sumLogCov = Matricies.addValue(sumLogCov,n_dim * Math.log(2*Math.PI)); //n_dim * np.log(2 * np.pi) + np.sum(np.log(covars), 1)
		sumDivMeanCov = Matricies.addMatrixes(sumDivMeanCov, sumLogCov); // n_dim * np.log(2 * np.pi) + np.sum(np.log(covars), 1) + np.sum((means ** 2) / covars, 1)
		dotXdivOneCovT = Matricies.sum(dotXdivOneCovT, dotXdivMeanCovT); //- 2 * np.dot(X, (means / covars).T) + np.dot(X ** 2, (1.0 / covars).T)
		dotXdivOneCovT = Matricies.addValue(dotXdivOneCovT, sumDivMeanCov); // (n_dim * np.log(2 * np.pi) + np.sum(np.log(covars), 1) + np.sum((means ** 2) / covars, 1) - 2 * np.dot(X, (means / covars).T) + np.dot(X ** 2, (1.0 / covars).T))
		lpr = Matricies.multiplyByValue(dotXdivOneCovT, -0.5);
		
		return lpr;
	}

}
