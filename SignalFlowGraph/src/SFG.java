

import java.awt.font.NumericShaper;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class SFG implements ISFG{
	
	private int numberOfNode = 0, start , end;
	private Double[][] segGain = new Double[50][50];
	private Double mul = 1.0;
	
	private ArrayList<ArrayList<Integer>> forwardPaths;
	private ArrayList<boolean[]> forwardPathsMarked;
	private ArrayList<Double> forwardPathsGain;
	private ArrayList<ArrayList<Integer>> loops;
	private ArrayList<boolean[]> loopsMarked;
	private ArrayList<Double> loopsGain;
	private ArrayList<Integer[]> nonTouchingLoops;
	private ArrayList<Double> nonTouchingLoopGains;

	@Override
	public void setNumberOfNode(int numberOfNode) {
		// TODO Auto-generated method stub
		this.numberOfNode = numberOfNode;
	}
	
	public void setNodesSegGain(Double[][] segGain) {
		// TODO Auto-generated method stub
		for (int i = 0 ; i < 50 ; i++){
			for (int j = 0 ; j < 50 ; j++){
				this.segGain[i][j] = 0.0;
			}
		}
		for (int i = 0 ; i < numberOfNode ; i++){
			for (int j = 0; j < numberOfNode ; j++){
				this.segGain[i][j] = segGain[i][j];
			}
		}
	}
	
	public void intial(){
		forwardPaths = new ArrayList<ArrayList<Integer>>();
		loops = new ArrayList<ArrayList<Integer>>();
		forwardPathsMarked = new ArrayList<boolean[]>();
		loopsMarked = new ArrayList<boolean[]>();
		forwardPathsGain = new ArrayList<Double>();
		loopsGain = new ArrayList<Double>();
		nonTouchingLoopGains = new ArrayList<Double>();
		nonTouchingLoops = new ArrayList<Integer[]>();
	}	
	public void start(int start, int end){
		this.start = start;
		this.end = end;
		intial();
		if (start != 0){
			segGain[start][numberOfNode] = 1.0;
			numberOfNode++;
			dfs(new ArrayList<Integer>() , new boolean[numberOfNode] , 0);
			ArrayList<ArrayList<Integer>> loop = new ArrayList<ArrayList<Integer>>();
			for (int i = 0; i < loops.size(); i++) {
				loop.add(new ArrayList<Integer>());
				loop.get(loop.size() - 1).add(i);
			}
			generateNonTouching(loop, 1);
			mul = getOvalAllTF();
			intial();
			numberOfNode--;
			segGain[start][numberOfNode] = 0.0;
		}
		// insert new node in the C(s)
		int flag = 0;
		if (end != numberOfNode - 1){
			segGain[end][numberOfNode] = 1.0;
			numberOfNode++;
			flag = 1;
		}
		dfs(new ArrayList<Integer>() , new boolean[numberOfNode] , 0);
		ArrayList<ArrayList<Integer>> loop = new ArrayList<ArrayList<Integer>>();
		for (int i = 0; i < loops.size(); i++) {
			loop.add(new ArrayList<Integer>());
			loop.get(loop.size() - 1).add(i);
		}
		generateNonTouching(loop, 1);
		if (flag == 1){
			for (int i = 0 ; i < forwardPaths.size() ; i++){
		          int s = forwardPaths.get(i).size();
		          forwardPaths.get(i).remove(s-1);
				}	
		}
	}
	public void dfs(ArrayList<Integer> path , boolean [] vis , int index){
		path.add(index);
		vis[index] = true;
		if (path.size() > 1 && index == numberOfNode - 1){
			path = new ArrayList<>(path);
			forwardPaths.add(path);
			boolean [] mark = new boolean[numberOfNode]; 
			for (int i = 0 ; i < path.size() ; i++){
				mark[path.get(i)] = true;
			}
			forwardPathsMarked.add(mark);
			calculateGain(path , 0);
			return;
		}
		for (int node = 0 ; node < numberOfNode ; node++){
			if (segGain[index][node] != 0){
				if(!vis[node]){
					dfs(path , vis , node);
					path.remove(path.size() - 1);
					vis[node] = false;
				}else{
					int location = path.indexOf(node);
					if (location != -1){
						List<Integer> l = path.subList(location, path.size());
						ArrayList<Integer> loop = new ArrayList<>(l);
						loop.add(loop.get(0));
						boolean [] mark = new boolean[numberOfNode]; 
						for (int i = 0 ; i < loop.size() ; i++){
							mark[loop.get(i)] = true;
						}
						int flag = 0;
						for (int i = 0 ; i < loops.size() ; i++){
							if (loops.get(i).size() == loop.size() &&
									check(mark , loopsMarked.get(i))){
								flag = 1;
								break;
							}
						}
						if (flag == 0){
							loops.add(loop);
							loopsMarked.add(mark);
							calculateGain(loop , 1);
						}
						
					}
				}
			}
		}
	}
	public void calculateGain(ArrayList<Integer> path , int flag){
		double gain = 1;
		for(int i = 0 ; i < path.size() - 1; i++){
			gain *= segGain[path.get(i)][path.get(i+1)];
		}
		if (flag == 0){forwardPathsGain.add(gain);}
		else{
			if (flag == 1 && path.size() == 1){ // self loop
				gain = segGain[path.get(0)][path.get(0)];
			}
			loopsGain.add(gain);
		}
	}
	public boolean check(boolean [] newLoop , boolean [] oldLoop){
		for (int i = 0 ; i < newLoop.length ; i++){
			if (newLoop[i] != oldLoop[i])return false;
		}
		return true;
	}
	private void generateNonTouching(ArrayList<ArrayList<Integer>> arrList,
			int nth) {
		Set<List<Integer>> foundbefore = new HashSet<List<Integer>>();
		boolean moveOnFlag = false;
		ArrayList<ArrayList<Integer>> nextArrList = new ArrayList<ArrayList<Integer>>();
		for (int i = 0; i < arrList.size(); i++) {
			for (int j = i + 1; j < arrList.size(); j++) {
				for (int k = 0; k < arrList.get(j).size(); k++) {
					int cand = arrList.get(j).get(k);
					ArrayList<Integer> temp = new ArrayList<Integer>();
					temp.addAll(arrList.get(i));
					temp.add(cand);
					if (isNonTouching(temp)) {
						Collections.sort(temp);
						if (!foundbefore.contains(temp)) {
							foundbefore.add(temp);
							moveOnFlag = true;
							nextArrList.add(new ArrayList<Integer>());
							nextArrList.get(nextArrList.size() - 1)
									.addAll(temp);
							nonTouchingLoops.add(temp.toArray(new Integer[temp.size()]));
							double gain = 1;
							for (int f = 0; f < temp.size(); f++)
								{gain *= loopsGain.get(temp.get(f));}
							nonTouchingLoopGains.add(gain);
						}
					}
				}
			}

		}
		if (moveOnFlag) {
			generateNonTouching(nextArrList, ++nth);
		}
	}

	private boolean isNonTouching(ArrayList<Integer> arr) {
		int flag;
		for (int i = 0; i < numberOfNode; i++) {
			flag = 0;
			for (int j = 0; j < arr.size(); j++) {
				if (loopsMarked.get(arr.get(j))[i])
					flag++;
			}
			if (flag > 1)
				return false;
		}
		return true;
	}
	private double Delta(){
		double individualLoopGain = 0;
		for (int i = 0 ; i < loopsGain.size() ; i++){
			individualLoopGain += loopsGain.get(i);
		}
		double toutching = 0;
		int i = 0;
		for (Integer[] arr : nonTouchingLoops) {
			ArrayList<ArrayList<Integer>> loop = new ArrayList<ArrayList<Integer>>();
			if (arr.length > 0)
				    loop.add(loops.get(arr[0]));
			for (int j = 1; j < arr.length; j++)
				{
			    loop.add(loops.get(arr[j]));
			    }
		  if(loop.size() % 2 == 0){
			  toutching += nonTouchingLoopGains.get(i);
		  }else{
			  toutching -= nonTouchingLoopGains.get(i);
		  }
		  i++;
		}
		double delta = ( 1 - individualLoopGain + toutching); 
		return delta;
	}
	public double calcNom(){
		
		ArrayList<Double> gain = getForwardPathsDeltas();
		
		double nem = 0;
		for (int i = 0 ; i < forwardPaths.size() ; i++){
			nem += (forwardPathsGain.get(i) * gain.get(i));
		}
		return nem;
	}
	
	public double getOvalAllTF() {
		double temp = calcNom() / Delta(); 
		if (mul != 1.0){
			return temp/mul;
		}
		return temp;
	}

	@Override
	public ArrayList<Double> getNonTouchingLoopGains() {
		// TODO Auto-generated method stub
		return nonTouchingLoopGains;
	}

	@Override
	public ArrayList<Integer[]> getNonTouchingLoops() {
		// TODO Auto-generated method stub
		return nonTouchingLoops;
	}

	@Override
	public ArrayList<Double> getLoopsGain() {
		// TODO Auto-generated method stub
		return loopsGain;
	}

	@Override
	public ArrayList<ArrayList<Integer>> getLoops() {
		// TODO Auto-generated method stub
		return loops;
	}

	@Override
	public ArrayList<ArrayList<Integer>> getForwardPaths() {
		// TODO Auto-generated method stub
		return forwardPaths;
	}

	@Override
	public ArrayList<Double> getForwardPathsGain() {
		// TODO Auto-generated method stub
		return forwardPathsGain;
	}

	@Override
	public double getMainDelta() {
		// TODO Auto-generated method stub
		return Delta();
	}

	@Override
	public ArrayList<Double> getForwardPathsDeltas() {
		// TODO Auto-generated method stub
		boolean [][] loop = new boolean[loops.size()][numberOfNode];
		for (int i = 0 ; i < loops.size() ; i++){
			for (int j = 0 ; j < loops.get(i).size() ; j++){
				loop[i][loops.get(i).get(j)] = true;
			}
		}
		ArrayList<Double> gain = new ArrayList<Double>();
		for (int i = 0 ; i < forwardPaths.size() ; i++){
			boolean [] temp = new boolean[numberOfNode];
			for (int j = 0 ; j < forwardPaths.get(i).size() ; j++){
				temp[forwardPaths.get(i).get(j)] = true;
			}
			double s = 0;
		  for (int k = 0 ; k < loops.size() ; k++){
			  int flag = 0;
			  for (int f = 0 ; f < numberOfNode ; f++){
				  if (loop[k][f] == temp[f] && temp[f] == true){
					  flag = 1;
					  break;
				  }
			  }
			  if (flag == 0){
				  s += loopsGain.get(k);
			  }
		  }
		  gain.add(1 - s);
		}
		return gain;
	}
	

}
