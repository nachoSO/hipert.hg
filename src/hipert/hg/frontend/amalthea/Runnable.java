package hipert.hg.frontend.amalthea;

import java.io.Serializable;
import java.util.LinkedList;

public class Runnable implements Serializable {
	String name;
	int nWInstructions; //Worst-case instructions
	int nBInstructions; //Best-case instructions
	int nAInstructions; //Average-case instructions

	double WCET;
	double ICET; //WCET without memory

	double BCET;
	double ACET;
	
	double BCST;
	double WCST;

	double WCRT; //reactions
	double readLatency;
	double writeLatency;
	LinkedList<Label> labels = new LinkedList<Label>();
	double frequency=300000000.0;
	public void addLabel(Label label){
		labels.add(label);
	}
	
	public Runnable(String name, int nWInstructions,int nBInstructions,int nAInstructions){
		this.name=name;
		this.nWInstructions=nWInstructions;
		this.nBInstructions=nBInstructions;
		this.nAInstructions=nAInstructions;

		this.WCET=(this.nWInstructions/frequency)*(1000000.0); // (n/f)*usec;
		this.BCET=(this.nBInstructions/frequency)*(1000000.0); // (n/f)*usec;
		this.ACET=(this.nAInstructions/frequency)*(1000000.0); // (n/f)*usec;
		this.ICET=this.WCET;
		this.WCRT=0;
	}
	
	//For copy in and copy out runnables
	public Runnable(String name, double cost){
		this.name=name;
		this.WCET=(cost/frequency)*(1000000.0);
		this.BCET=(cost/frequency)*(1000000.0);
		this.ACET=(cost/frequency)*(1000000.0);

	}
	
	public void printLabels(){
		for (Label l:labels){
			System.out.println(l.name+" location: "+l.labelLocation+" frequency access: "+l.frequencyAccess);
		}
	}

}
