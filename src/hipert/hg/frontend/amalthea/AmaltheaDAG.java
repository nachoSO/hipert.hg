package hipert.hg.frontend.amalthea;

import java.util.LinkedList;

public class AmaltheaDAG{
	LinkedList<Core> cores;
	String paradigm;
	public AmaltheaDAG(LinkedList<Core> cores, String paradigm) {
		this.cores=(LinkedList) cores.clone();
		this.paradigm=paradigm;
	}

}
