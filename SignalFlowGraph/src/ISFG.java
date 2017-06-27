

import java.util.ArrayList;

public interface ISFG {
	
	/**
	 * set number of node 
	 */
	public void setNumberOfNode(int numberOfNode);

	/**
	 * gain between each two nodes
	 */
	public void setNodesSegGain(Double[][] seg);
	public double getOvalAllTF();
	public double getMainDelta();
	public ArrayList<Double> getNonTouchingLoopGains();
	public ArrayList<Integer[]> getNonTouchingLoops();
	public ArrayList<Double> getLoopsGain();
	public ArrayList<ArrayList<Integer>> getLoops();
	public ArrayList<ArrayList<Integer>> getForwardPaths();
	public ArrayList<Double> getForwardPathsGain();
	public ArrayList<Double> getForwardPathsDeltas();
}
