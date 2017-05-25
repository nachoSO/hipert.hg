package hipert.hg.frontend.rtdot;

public class Node {
	private double wcet; //worst case execution time (MIET), isolated wcet (MEET), average execution time
	private double iwcet;
	private double maet;
	private int nodeID;
	private String comment;
	private int mem_access; //num memory acess
	private String mem_unit;
	
	public Node(int nodeID){
		this.setNodeID(nodeID);
	}
	
	public Node(int nodeID,double iwcet,double wcet,double maet,int mem_access, String mem_unit,String comment ){
		this.setWcet(wcet);
		this.setIwcet(iwcet);
		this.setMaet(maet);
		this.setNodeID(nodeID);
		this.setMem_access(mem_access);
		this.setMem_unit(mem_unit);
		this.setComment(comment);
	}

	public int getNodeID() {
		return nodeID;
	}

	public void setNodeID(int nodeID) {
		this.nodeID = nodeID;
	}

	public String getComment() {
		return comment;
	}

	public void setComment(String comment) {
		this.comment = comment;
	}

	public int getMem_access() {
		return mem_access;
	}

	public void setMem_access(int mem_access) {
		this.mem_access = mem_access;
	}

	public String getMem_unit() {
		return mem_unit;
	}

	public void setMem_unit(String mem_unit) {
		this.mem_unit = mem_unit;
	}

	public double getIwcet() {
		return iwcet;
	}

	public void setIwcet(double iwcet) {
		this.iwcet = iwcet;
	}

	public double getWcet() {
		return wcet;
	}

	public void setWcet(double wcet) {
		this.wcet = wcet;
	}

	public double getMaet() {
		return maet;
	}

	public void setMaet(double maet) {
		this.maet = maet;
	}
}
