import java.util.Random;

public class weightMatrix {
	
	protected double[][] weights; //the nxc matrix where n is the number of input variables and c is the number of nodes; initialized with random weights
	protected int rows;
	protected int cols;
	
	//CHECK THE ORDER OF ALL MATRICES
	public weightMatrix(int numVars, int nodes){
		this.rows = nodes;
		this.cols = numVars;
		Random randGaussian = new Random(); // a random number generator with an approximately Gaussian distribution
		this.weights = new double[nodes][numVars]; // a mxn matrix where m is the number of relevant variables (number of connecting nodes) and m is the number of nodes in each hidden layer
		for(int i=0; i<nodes; i++) {
			for(int x=0; x<numVars; x++) {
				this.weights[i][x] = randGaussian.nextGaussian();
			}
		}
		
		//PRINTS DATA TO TERMINAL FOR CONFIRMATION
		System.out.println();
		System.out.println("printing the weights");
	    for(int i=0; i<nodes; i++) {
	    	for (int x=0; x<numVars; x++) {
	    		System.out.print(weights[i][x]);
	    		System.out.print(" ");
	    	}
	    	System.out.println();
	    }
		
	}
	
	public double[][] getWeights() {
		return this.weights;
	}
	
	public double getWeight(int row, int col) {
		return this.weights[row][col];
	}
	
	public void setWeight(int row, int col, double value) {
		this.weights[row][col] = value;
	}
	
	public weightMatrix deepCopy() {
		weightMatrix deepCopy = new weightMatrix(this.cols, this.rows);
		for (int i=0; i<this.rows; i++) {
			for (int x=0; x<this.cols; x++) {
				deepCopy.setWeight(i, x, this.weights[i][x]);
			}
		}
		return deepCopy;
	}
	
	public void print(){
		for (int i=0; i<this.rows; i++) {
			for (int x=0; x<this.cols; x++) {
				System.out.print(this.weights[i][x] + " ");
			}
			System.out.println();
		}
	}
	
}
