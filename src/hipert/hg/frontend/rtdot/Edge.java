package hipert.hg.frontend.rtdot;

public class Edge {
	Node src;
	Node trg;
	String name;
	
	public Edge(String name,int srcID,int trgID){
		this.name=name;
		this.src=new Node(srcID);
		this.trg=new Node(trgID);
	}
}
