package hipert.hg.XMLParser;

import java.io.File;

public class DAG {
	private int period;
	private int priority;
	private int deadline;
	private int map;
	String comment;
	String dagName;
	File dagPath;
	String mem_access;
	int step=0;
	
	//prem constructor
	public DAG(String dagPath, String mem_access){
		this.dagPath=new File(dagPath);
		this.mem_access=mem_access;
	}
	
	//scatter constructor
	public DAG(String dagPath, String mem_access,int step){
		this.dagPath=new File(dagPath);
		this.mem_access=mem_access;
		this.step=step;
	}

	public DAG() {
		// TODO Auto-generated constructor stub
	}

	public int getPeriod() {
		return period;
	}

	public void setPeriod(int period) {
		this.period = period;
	}

	public int getDeadline() {
		return deadline;
	}

	public void setDeadline(int deadline) {
		this.deadline = deadline;
	}

	public int getPriority() {
		return priority;
	}

	public void setPriority(int priority) {
		this.priority = priority;
	}

	public int getMap() {
		return map;
	}

	public void setMap(int map) {
		this.map = map;
	}
}
