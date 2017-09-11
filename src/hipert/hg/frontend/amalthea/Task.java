package hipert.hg.frontend.amalthea;

import java.io.Serializable;
import java.util.LinkedList;


public class Task implements Comparable<Task>, Serializable {
	String name;
	double period;
	double deadline;
	int priority;
	int pts; //all tasks have a threshold defined by max. task priority of cooperative tasks
	String preemptionType;
	LinkedList<Runnable> runnables=new LinkedList<Runnable>();
	double responseTime=0;
	double executionTime=0;
	//publishing and reading point costs
	double costReading; 
	double costPublishing;
	int core=-1;
	LinkedList<String> labels = new LinkedList<String>();
	
	public void addLabel(String label){
		labels.add(label);
	}
	
	public void addRunnable(Runnable r){
		runnables.add(r);
	}
	
	public Task(String name,double period,int priority,String preemptionType, int core){
		this.name=name;
		this.period=period;
		this.deadline=this.period;
		this.priority=priority;
		this.preemptionType=preemptionType;
		this.pts=10;
		this.core=core;
	}
	
	//This function returns the total task execution time of this task
	public double processInstructions(){
		double totalExecutionTime=0;
		for(int i=0;i<runnables.size();i++)
			totalExecutionTime+=runnables.get(i).WCET;
		return totalExecutionTime;
	} 

	public int compareTo(Task t) {
		int comparedSize = t.priority;
		if (this.priority > comparedSize) {
			return -1;
		} else if (this.priority == comparedSize) {
			return 0;
		} else {
			return 1;
		}
	}

	public String toString(){
		return "Task name: "+name+", period: "+period+"us, priority: "+priority+", preemptionType: "
				+preemptionType+", response time: "+responseTime+"us, wcet: "+executionTime;
	}
}
