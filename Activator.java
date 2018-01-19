import java.math.*;

public class Activator {
	
	protected Activators activator;
	
	static final double E = 2.7182818284590452353602875;
	
	public Activator (Activators activator) {
		this.activator = activator;
	}
	
	protected void transform(double[] matrix) {
		switch (this.activator) {
		case TANH:
			for (int i=0; i<matrix.length; i++) {
				matrix[i] = this.tanH(matrix[i]); 
			}
		case SIGMOID:
			this.Sigmoid(5); //CHANGE THIS (OBVIOUSLY)
		}
	}
	
	protected void transformPrime(double[] matrix) {
		switch (this.activator) {
		case TANH:
			for (int i=0; i<matrix.length; i++) {
				matrix[i] = this.tanHPrime(matrix[i]); 
			}
		case SIGMOID:
			// derivative function; SIGMOIDRIME
		}
	}
	
	protected double tanH(double z) {
		double expTerm = Math.pow(E, (2*z));
		return ((expTerm-1)/(expTerm+1));
	}
	
	protected double tanHPrime(double z) {
		double numerator = 2 * (Math.pow(E, (-z)));
		double denominator = 1 + (Math.pow(E, ((-2)*z)));
		return (Math.pow((numerator/denominator), 2));
	}
	
	protected void Sigmoid(double z) {
		// 
	}

}
