import java.io.*;
import java.util.*;


public class inputLayer implements layer {
	
	protected outputLayer output;
	protected layer nextLayer;
	
	protected int numInputVars;
	protected int numOutputVars;
	// an mxn matrix where m is the number of data points and n is the number of input variables in each data point
	protected double[][] inputData;
	// an mxn matrix where m is the number of data points and n is the number of output variables in each data point; the correct output values associated with the inputs
	protected double[][] targetVals;

	public inputLayer(int numInputVars, int numOutputVars, String fileName) {
		
		//DIAGNOSTIC PRINT LINES BEGIN
		System.out.println("CREATING THE INPUT LAYER");
		//DIAGNOSTIC PRINT LINES END
		this.numInputVars = numInputVars;
		this.numOutputVars = numOutputVars;
		//System.out.println(numOutputVars);

		ArrayList<String> lines = new ArrayList<String>(); // an array for the lines of the file
		Scanner inputStream;
		
		try{
			File file= new File(fileName);
			inputStream = new Scanner(file); // try to attach an input stream to the file

			while(inputStream.hasNext()){ // while there is still data
				String line = inputStream.next();
				lines.add(line); // add each line to a String array
			}
			
			this.inputData = new double[lines.size()][numInputVars]; // an mxn matrix where m is the number of data points (lines in file) and n is the number of variables
			this.targetVals = new double[lines.size()][numOutputVars]; // an mxn matrix where m is the number of data points (lines in file) and n is number of output variables
			
			for(int i=0; i<lines.size(); i++) { //goes down lines of file
				String[] values = lines.get(i).split(","); //parses a line of the file into multiple values
				for(int x=0; x<values.length; x++) { //goes along each line
					//System.out.println(values[x]); TEST
					//System.out.println(Double.parseDouble(values[x])); TEST
					if (x<numInputVars) { // if the current value represents an input variable
						this.inputData[i][x]= Double.parseDouble(values[x]); //adds the input variables to a 2 dimensional array
						//System.out.println("the added INP values is: " + this.inputData[i][x]);
					}
					else { // if the current value represents an output variable
						this.targetVals[i][x-numInputVars]= Double.parseDouble(values[x]); //putting the target values in their own array
						//DIAGNOSTIC PRINT LINES BEGIN
						//System.out.println("the added SOLN values is: " + this.targetVals[i][x-numInputVars]);
						//System.out.println();
						//DIAGNOSTIC PRINT LINES END
					}
				}	
			}
			inputStream.close();
			
			//PRINTS DATA TO TERMINAL FOR CONFIRMATION
	        testPrint(lines.size());
	        //END TEST PRINT
            
		
		}
        catch (FileNotFoundException e) {
        	System.out.println("File not found. Check file name and directory.");
            e.printStackTrace();
        }

	}
	
	public void testPrint(int lineNum) { //PRINTS DATA TO TERMINAL FOR CONFIRMATION
		System.out.println("input data: ");
        for(int i=0; i<lineNum; i++) {
        	for (int n=0; n<this.numInputVars; n++) {
        		System.out.print(this.inputData[i][n]);
        		System.out.print(" ");
        	}
        	System.out.println();
        }
        System.out.println("desired output: ");
        for(int i=0; i<lineNum; i++) {
        	for (int x=0; x<this.numOutputVars; x++) {
        		System.out.print(this.targetVals[i][x]);
        		System.out.print(" ");
        	}
        	System.out.println();
        }
	}
	
	public int getSampleSize(){
		return this.inputData.length;
	}
	
	public double[] getInputSet(int x){
		return this.inputData[x];
	}
	
	public double[] getTargetVals(int x){
		return this.targetVals[x];
	}
	
	public void setOutput(outputLayer output) {
		this.output = output;
	}

	@Override
	public void setNext(layer nextLayer) {
		this.nextLayer = nextLayer;
		nextLayer.setPrev(this);
		
	}

	@Override
	public void setPrev(layer prevLayer) {
		//this.prevLayer = prevLayer;
		
	}
	
	public double[] getInputData(int dataSet) {
		return this.inputData[dataSet]; // returns the input data for a specific dataset/pair of inputs
	}

	@Override
	public void forward(double[] inputSet) {
		// TODO Auto-generated method stub
		
	}

}
