import java.io.File;

public class Main {

	 public static void main(String[] args) {
	        String fileName = args[0];
	        int inputs =  Integer.parseInt(args[1]);
	        //File file= new File(fileName);
	        
	        NeuralNet nn = new NeuralNet(fileName, inputs, 3, Activators.TANH, 1, 1, true);
	 }
	
}
