
public interface layer {
	
	public void setNext(layer nextLayer);
	
	public void setPrev(layer prevLayer);

	public void forward(double[] inputSet);
	
	public double[][] getWeights();

}
