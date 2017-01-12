package hipert.hg.XMLParser;

import java.io.File;

public class DAG {
	private int period;
	private int priority;
	private int deadline;
	private int map;
	private int stride; //if 0 ->random else-> sequential access
	private int step=0;
	private String comment;
	private String dagName;
	private File dagPath;
	private String mem_access;
	
	//prem constructor
	public DAG(String dagPath, String mem_access){
		this.setDagPath(new File(dagPath));
		this.setMem_access(mem_access);
	}
	
	//scatter constructor
	public DAG(String dagPath, String mem_access,int step,int stride){
		this.setDagPath(new File(dagPath));
		this.setMem_access(mem_access);
		this.setStep(step);
		this.setStride(stride);
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

	public int getStride() {
		return stride;
	}

	public void setStride(int stride) {
		this.stride = stride;
	}

	public int getStep() {
		return step;
	}

	public void setStep(int step) {
		this.step = step;
	}

	public String getMem_access() {
		return mem_access;
	}

	public void setMem_access(String mem_access) {
		this.mem_access = mem_access;
	}

	public String getDagName() {
		return dagName;
	}

	public void setDagName(String dagName) {
		this.dagName = dagName;
	}

	public File getDagPath() {
		return dagPath;
	}

	public void setDagPath(File dagPath) {
		this.dagPath = dagPath;
	}

	public String getComment() {
		return comment;
	}

	public void setComment(String comment) {
		this.comment = comment;
	}
}
