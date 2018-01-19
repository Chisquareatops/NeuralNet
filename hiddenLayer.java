
public class hiddenLayer implements layer {
	
	protected layer nextLayer;
	protected layer prevLayer;

	//the matrix that holds the weights by which the input layer matrix will be multiplied to create the hidden layer
	protected weightMatrix weights;
	
	//the number of nodes in the hidden layer
	protected int nodes; 
	
	protected double[] hiddenMatrix;
	protected double[] hiddenSumsOriginal;
	protected Activator activator;
	
	protected outputLayer output; //SHOULD BE GENERALIZED SO CAN BE ANY LAYER???
	protected int numInputVars;
	
	//SHOULD THERE BE A FIELD FOR THE INPUTS? sometimes inputs will be the user inputs, and sometimes they will be the previous middle layer?

	public hiddenLayer(int nodes, int numInputVars, Activators activator, outputLayer output) {
		
		//DIAGNOSTIC PRINT LINES BEGIN
		System.out.print("CREATING THE HIDDEN LAYER");
		//DIAGNOSTIC PRINT LINES END
		this.numInputVars = numInputVars;
		this.weights = new weightMatrix(numInputVars, nodes); //creates the weight matrix with random values
		this.hiddenMatrix = new double[nodes]; //creates a matrix to hold the result of multiplying the inputs by the weights
		this.nodes = nodes;
		this.activator = new Activator(activator);
		this.output = output;
		
		//INSERT CODE TO MULTIPLY inputs by weights ?? or this goes in the forward method?
	}
	

	public void forward(double[] input) {
		
		//DIAGNOSTIC PRINT LINE BEGIN
		System.out.println("this is the beginning of forward (HL)");
		//DIAGNOSTIC PRINT LINE END
		
		for (int i=0; i<this.nodes; i++) { //loops through each column in the weight matrix (the number of nodes in hidden layer)
			double sum = 0;
			for (int x=0; x<input.length; x++) { //multiplies each row of the input matrix with each column of the hidden layer
				sum += input[x] * this.weights.getWeights()[i][x]; //add the results together
			}
			this.hiddenMatrix[i] = sum; // place the results of matrix multiplication into the hiddenMatrix
			
		}
		
		//AT THIS POINT WE HAVE AN mx1 MATRIX WHERE m IS THE NUMBER OF NODES IN EACH HIDDEN LAYER
		//AND EACH VALUE IS THE SUM OF EACH INPUT VARIABLE MULTIPLIED BY A WEIGHT
		
		//PRINTS DATA TO TERMINAL FOR CONFIRMATION
		System.out.println("the hidden layer sum");
	    for(int i=0; i<this.nodes; i++) {
    		System.out.print(i + ": " + this.hiddenMatrix[i]);
    		System.out.print(" ");
    	}
	    
	    //COPY HIDDEN LAYER SUMS BEFORE TRANSFORMATION (original sums needed for further calculations)
	    this.hiddenSumsOriginal = this.matrixDeepCopy();
		
		//NEEDS TO APPLY ACTIVATOR TO THE MATRIX:
		this.activator.transform(hiddenMatrix);
		
		//WE NOW HAVE THE SAME mx1 MATRIX EXCEPT EACH VALUE HAS BEEN TRANSFORMED BY THE TRANSFORMATION FUNCTION
		
		//DIAGNOSTIC PRINT LINES BEGIN
		System.out.println();
		System.out.println("the hidden layer sum after transformation");
	    for(int i=0; i<this.nodes; i++) {
	    		System.out.print(i + ": " + this.hiddenMatrix[i]);
	    		System.out.print(" ");
	    	}
    	System.out.println();
      	System.out.println();
      	//DIAGNOSTIC PRINT LINES END
		
      	//CALLS THE FORWARD METHOD IN THE OUTPUT LAYER
      	//PROBABLY CAN'T CALL THIS HERE IF I WANT TO ADD HIDDEN LAYERS ARBITRARILY
      	this.output.forward(this.hiddenMatrix);

    }


	public void backward(double[][] deltaOutputSumToWeights, weightMatrix origWeights, int numOutputVars, int sample) {
		
		System.out.println("this is the beginning of backward: HL");
				
		//FIND THE DELTA HIDDEN SUM	
		//WILL NOT!!!! WORK FOR MULTIPLE OUTPUT VARS... NOT SURE HOW TO BALANCE FEEDBACK FROM BOTH OUTPUTS SINCE THERE IS ONLY ONE SUM PER NODE IN HL
	
		this.activator.transformPrime(this.hiddenSumsOriginal); // take the integral of the activation function at the original hidden layer sums
		double[][] deltaHiddenSums_preavg = new double[numOutputVars][this.nodes]; // create a matrix to hold the needed changes to the hidden sums
		for (int i=0; i<numOutputVars; i++) { // for each output variable
			for (int x=0; x<this.nodes; x++) { // for each connecting node
				deltaHiddenSums_preavg[i][x] = deltaOutputSumToWeights[i][x] * hiddenSumsOriginal[x];
			}
		}
		//THE ABOVE WILL GIVE A 2D ARRAY! needs to be collapsed to a single array before proceeding.
		
		double[] deltaHiddenSums_postavg = null;
		if (numOutputVars > 1) {
			// FIGURE OUT SOME KIND OF WEIGHTED AVG I GUESS??? needs to be collapsed to a single array, but how?
		}
		else {
			deltaHiddenSums_postavg = deltaHiddenSums_preavg[0]; // if only one output var, collapse into a single array of needed changes
		}
		
		//DIAGNOSTIC PRINT LINE
		System.out.println("the delta hidden sums are: ");
		for (int i=0; i<deltaHiddenSums_postavg.length; i++) {
			System.out.print(deltaHiddenSums_postavg[i] +" ");
		}
		System.out.println();
		//END DIAGNOSTIC PRINT LINE
		
		
		// CURRENTLY WRITTEN TO CONNECT WITH INPUT LAYER
		//WILL NOT!!! WORK WITH MULTIPLE HIDDEN LAYERS... not yet generalized. how to do this? will need some concept of 'inputs' in general,
		//which will be the number of input vars in this case but would be the number of nodes per hidden layer if there were another HL
		//behind this one
		// so at this point basically I have the DELTA HIDDEN SUMS which are the needed changes to the hidden sums:
		
		//FIND THE NEEDED CHANGES IN THE WEIGHTS
		
		double[][] deltaWeights = new double[this.numInputVars][this.nodes]; // really shitty workaround, this layer needs to know how many inputs
		// this seems like it would be solved by the issue above though (creating general concept of connectors coming in) so ignoring for now
		
		for (int i=0; i<this.numInputVars; i++) {
			for (int x=0; x<this.nodes; x++) {
				deltaWeights[i][x] = deltaHiddenSums_postavg[x] / ((inputLayer)this.prevLayer).inputData[sample][i]; //divide the needed change in hidden sums by the appropriate input
				if (((inputLayer)this.prevLayer).inputData[sample][i] == 0) {
					deltaWeights[i][x] = deltaHiddenSums_postavg[x] / .0000000000000000000000000000000000001; // this is such a stupid workaround but I don't know what to do
				}
			}
		}
		
		//DIAGNOSTIC PRINT LINE
		System.out.println("the delta weights are: ");
		for (int i=0; i<this.numInputVars; i++) {
			for (int x=0; x<this.nodes; x++) {
				System.out.print(deltaWeights[i][x] + " ");
				System.out.println();
			}
		}
		System.out.println();
		//END DIAGNOSTIC PRINT LINE
		
		//CHANGE THE WEIGHTS
		weightMatrix oldWeights = this.weights.deepCopy();
		
		for (int i=0; i<this.numInputVars; i++) {
			for (int x=0; x<this.nodes; x++) {
				double newWeight = oldWeights.getWeight(x, i) + deltaWeights[i][x]; // add the change in weight to each weight
				this.weights.setWeight(x, i, newWeight); // place the new weight into the weight matrix
			}
		}
		
		//DIANOSTIC PRINT LINES
		System.out.println("the new weights are: ");
		this.weights.print();
		
	}
	
	public void setNext(layer nextLayer) {
		this.nextLayer = nextLayer;
		nextLayer.setPrev(this);
		
	}
	
	public void setPrev(layer prevLayer) {
		this.prevLayer = prevLayer;
		System.out.println("THIS WAS ASSIGNED A PREV LAYER");
		
	}
	
	public double[] matrixDeepCopy() {
		double[] matrixDeepCopy = new double[this.nodes];
	    for(int i=0; i<this.nodes; i++) {
	    	matrixDeepCopy[i] = this.hiddenMatrix[i];
    	}
	    return matrixDeepCopy;
	}


}
