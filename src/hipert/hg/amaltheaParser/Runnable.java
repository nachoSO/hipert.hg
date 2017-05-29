package hipert.hg.amaltheaParser;

import java.util.LinkedList;

public class Runnable {
	String name;
	int nInstructions;
	double executionTime;
	double responseTime; //reaction
	LinkedList<Integer> readLabels=new LinkedList<Integer>();
	
	//CAMBIAR CORE FREQUENCY
	public Runnable(String name,int nInstructions){
		this.name=name;
		this.nInstructions=nInstructions;
		this.executionTime=(this.nInstructions/200000000.0)*(1000000.0); // (n/f)*usec;
		this.responseTime=0;
	}
	
	public String toString(){
		return "Runnable name: "+name+", nInstructions: "+nInstructions +", executionTime: "+executionTime;
	}
	
}

