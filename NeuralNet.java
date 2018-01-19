
public class NeuralNet {

	//an mxn matrix where m is the number of data points in the input data set
	//and n is the number of input variables in each data point
	protected inputLayer input;
	
	//an mxn matrix where ???
	protected hiddenLayer next;
	
	//an mx1 matrix where m is the number of output variables to be predicted
	protected outputLayer output;
	
	//a String specifying the function to be called during forward propagation
	protected Activators activator;
	
	//ADD IMPLEMENTATION for changing how many layers are in middle (how many 'cols' of nodes)
	public NeuralNet(String fileName, int numInputVars, int nodes, Activators activator, int numOutputVars, int numHiddenLayers) {
		
		//creates an mxn matrix from user data, where m is the number of data points in the input and m is the number of input variables
		this.input = new inputLayer(numInputVars, numOutputVars, fileName);
		
		//DIAGNOSTIC PRINT LINES BEGIN
		System.out.println("done creating input layer");
		System.out.println();
		//DIAGNOSTIC PRINT LINES END
		
		//creates an mx1 matrix such that m is the desired number of output variables
		//matrix is mx1 because it predicts the output for one data point at a time
		this.output = new outputLayer(numOutputVars, nodes, activator, this.input);
		
		//DIAGNOSTIC PRINT LINES BEGIN
		System.out.println("done creating output layer");
		System.out.println();
		//DIAGNOSTIC PRINT LINES END
		
		//creates an mxn matrix where m is the number of input variables 
		//and m is the number of nodes in the hidden layer
		this.input.setNext(new hiddenLayer(nodes, numInputVars, activator, this.output));
		
		layer current = this.input.nextLayer;
		
		for (int i=1; i<numHiddenLayers; i++) { // for the number of hidden layers specified, not including the first one
			layer next = new hiddenLayer(nodes, numInputVars, activator, this.output); // create a hidden layer
			current.setNext(next); // set the current layer's next attribute to the new layer
			current = next; //reassign the alias current
		}
		
		current.setNext(this.output); // set the next layer of the final HL to the output layer
		
		System.out.println("done creating hidden layer(s)");
		System.out.println();
		
		//the activation function specified by the user
		this.activator = activator;
		
		this.train(); //SHOULD THIS BE CALLED HERE OR IN MAIN? Does creating a network automatically train it?
		
	}
	
	public void train() { // currently executes once for each dataset/sample in input data
		for (int i=0; i<this.input.getSampleSize(); i++) { //repeats for every pair/set of input variables
			this.input.nextLayer.forward(this.input.getInputSet(i)); //calls the forward method within hiddenLayer on one set of input variables (one row of the input matrix)
			this.output.backward(this.input.getTargetVals(i), i); //calls the backward method within hiddenLayer on one set of input variables
			System.out.println("this is the end of trainging round " + (i+1));
			System.out.println();
		}
	}
	
	public void forward() { //IS THIS NEEDED? Should train call a separate forward and backward method? what customization could that add?
		
		System.out.println("this is the beginning of forward (NN)");
		
		for (int i=0; i<this.input.getSampleSize(); i++) { //repeats for every pair/set of input variables
			this.next.forward(this.input.getInputSet(i)); //calls the forward method within hiddenLayer on one pair/set of input variables
		}
		
	}
	
	
	
	
}
