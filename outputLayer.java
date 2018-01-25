
public class outputLayer implements layer {
	
	protected inputLayer input;
	protected hiddenLayer prevLayer;
	
	protected weightMatrix weights; //the matrix of weights, one for each connecting node
	protected int nodes; //the number of nodes that connect to this output layer
	protected int outputVars; //the number of output variables
	// an mx1 matrix where m is the number of output variables and each entry is the calculated output for one output variable (this is the outputSums matrix after transformation by the activation function)
	protected double[] output;
	private Activator activator;
	protected double[] outputSums; 

	public outputLayer(int numOutputVars, int nodes, Activators activator, inputLayer input) {
		
		System.out.print("CREATING THE OUTPUT LAYER");
		
		this.outputVars = numOutputVars;
		
		this.nodes = nodes;
		
		this.weights = new weightMatrix(nodes, numOutputVars); //creates the weight matrix with random values; one weight for each connecting node
	
		this.output = new double[outputVars];
		
		this.activator = new Activator(activator);
		
		this.input = input; // connect this output to the input
		input.setOutput(this);// also make this output the output in input
		
		
		
	}
	
	public void forward(double[] input) {
		
		System.out.println("this is the beginning of forward (OL)");
		
		for (int i=0; i<this.outputVars; i++) { //loops through each column of the output layer weights (one column for each output variable)
			double sum = 0;
			//System.out.println("this is col " + i + "and it has this many members: " + this.weights.getWeights()[i].length); TEST
			for(int x=0; x<this.nodes; x++) { //loops through each value in col (one for each connecting node from the hidden layer)
				sum += (input[x] * this.weights.getWeight(i,x)); //multiplies each member of the input vector by each weight in the col and sums the results
				//DIAGNOSTIC PRINT LINES BEGIN
				System.out.println("this is col " + i + ", row " + x);
				System.out.println("the HL sum is: " + input[x] + " and the weight is: " + this.weights.getWeight(i,x) + " and the sum so far is: " + sum);
				//DIAGNOSTIC PRINT LINES END
			}
			output[i] = sum;
		}
			
		//PRINTS DATA TO TERMINAL FOR CONFIRMATION
		System.out.println("the output layer sum");
		for(int i=0; i<this.outputVars; i++) {
			System.out.print(this.output[i]);
		}
		
		//NEEDS TO APPLY ACTIVATOR TO THE MATRIX:
		this.outputSums = new double[this.outputVars];
		for(int i=0; i<this.outputVars; i++) { // for each output variable in the output vector
			this.outputSums[i] = this.output[i]; // copy the raw output (the sum) to a matrix of sums
			System.out.print(this.outputSums[i] + " ");
		}
		this.activator.transform(output); // transform the output vector with the activation function
		
		//DIAGNOSTIC PRINT START
		System.out.println();
		System.out.println("the output layer sum after transformation: ");
	    for(int i=0; i<this.outputVars; i++) {
	    		System.out.print(i + ": " + this.output[i]);
	    	}
    	System.out.println();
      	System.out.println();
      	//DIAGOSTIC PRINT END
      	
      	double totalError = 

    }
	
	public void backward(double[] targetVals, int sample) {
		
		System.out.println("this is the beginning of backward (OL)");
		
		double[] hiddenLayerVals = this.prevLayer.hiddenMatrix;
		double[] OutputMOEs = new double[this.outputVars]; // create an empty array for the output value margins of error; one entry per output variable
		for (int i=0; i<outputVars; i++) { // for each output variable
			OutputMOEs[i] = targetVals[i] - output[i]; // subtract the calculated output from the correct target output
		}
		
		//DIAGNOSTIC PRINT LINES BEGIN
		System.out.println("the Margins of Error: ");
		for (int i=0; i<outputVars; i++) {
			System.out.print(OutputMOEs[i] + " ");
		}
		System.out.println();
		System.out.println();
		//DIAGNOSTIC PRINT LINES END
		
		this.activator.transformPrime(this.outputSums); // transform the output layer sum(s) using the derivative of the activation function
		// this will give the rate of change of the sum with respect to each weight (i.e. the weight of each connecting node in the hidden layer)
		
		double[] deltaOutputSums = new double[this.outputVars]; // create an empty array for the delta output sum(s); the necessary change in the output sum(s)
		for (int i=0; i<outputVars; i++) { // for each output variable
			deltaOutputSums[i] = OutputMOEs[i] * this.outputSums[i]; // multiply the margin of error by the derivative of the sum of with respect to x for that output variable
		}
		
		//DIAGNOSTIC PRINT LINES BEGIN
		System.out.println("the delta output sums: ");
		for (int i=0; i<outputVars; i++) {
			System.out.print(deltaOutputSums[i] + " ");
		}
		System.out.println();
		System.out.println();
		//DIAGNOSTIC PRINT LINES END
		
		//COPY THE WEIGHTS BEFORE CHANGING THEM (original weights needed in additional calculations)
		weightMatrix origWeights = this.weights.deepCopy();
		
      	//CHANGE THE WEIGHTS
		double deltaWeight;
		double newWeight;
		//DIAGNOSTIC PRINT LINE
		System.out.println("the delta weight values are: ");
		
      	for (int i=0; i<this.outputVars; i++) { // for each column in the weight matrix (i.e. for each output variable)
      		for (int x=0; x<this.nodes; x++) { // for each row in the weight matrix (i.e. for each connecting node)
      			deltaWeight = deltaOutputSums[i] / hiddenLayerVals[x]; // the needed change in the weights is the needed change in the output sum divided by the hidden layer result
      			if (deltaOutputSums[i] == 0) {
      				deltaWeight = 0;
      			}
      			System.out.print(deltaWeight + " ");
      			newWeight = this.weights.getWeight(i, x) + deltaWeight; // the new weight is the current weight at this location in the weight matrix
      			this.weights.setWeight(i, x, newWeight); // set weight to new weight
      		}
      	}
      	//DIAGNOSTIC PRINT LINES BEGIN
    	System.out.println();
      	System.out.println();
      	System.out.println("the new weights: ");
      	this.weights.print();
    	System.out.println();
      	System.out.println();
      	//DIAGNOSTIC PRINT LINES END
      	
      	double[][] deltaOutputSumToWeights = new double[outputVars][nodes]; // a quotient of the delta output sum to the original weights/neurons between hidden layer and output layer	
		for (int i=0; i<outputVars; i++) { // for each output variable
			for (int x=0; x<nodes; x++) { // for each connecting node
				deltaOutputSumToWeights[i][x] = deltaOutputSums[i]/ origWeights.getWeight(i, x); // divide the delta output sum for each output variable by all the connecting synapses/weights
			}
		}
		
      	this.prevLayer.backward(deltaOutputSumToWeights, origWeights, outputVars, sample); // call the backward method in the hidden layer connecting to this output layer
      	
	}
	
	public void backward2() {
		
	}

	@Override
	public void setNext(layer nextLayer) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void setPrev(layer prevLayer) {
		this.prevLayer = (hiddenLayer) prevLayer;
		
	}

}
