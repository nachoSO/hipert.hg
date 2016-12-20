package hipert.hg.XMLParser;
import java.io.*;
import java.util.LinkedList;

public class XMLGenerator {
	
	private LinkedList<Edge> edges=new LinkedList<Edge>();
	private LinkedList<Node> nodes=new LinkedList<Node>();
	private LinkedList<DAG> dags=new LinkedList<DAG>();

	private String dagMemAccess="prem";
	
	public void setDagMemAccess(String dagMemAccess){
		this.dagMemAccess=dagMemAccess;
	}
	
	public LinkedList<DAG> getDags(){
		return this.dags;
	}
	
	public LinkedList<Node> getNodes(){
		return this.nodes;
	}
	
    @SuppressWarnings("resource")
	public void readDOTFile(String filename,int index){
	    //Reading dot file
		String line = null;
		File file = new File( filename );
		FileReader fr = null;
		try
		{
		    fr = new FileReader( file );
			BufferedReader br = new BufferedReader( fr );
			
			//dagName first line second string
			line = br.readLine();
			dags.get(index).dagName=line.split(" ")[1];
			
			//Process file
			while( (line = br.readLine()) != null ){
				processString(line,index);
			}
		} 
		catch (IOException e) 
		{  
		    e.printStackTrace();
		}
    }
    
    /*This function process a line from the file and prepare the structure
    to be written in the file
	A line may be:
 	1) 0 && not label with period = Ignored label
    2) Edge (containing source and destination node)
    3) Node (containing)
    */
    private void processString(String line,int index) {
		//1)ignore 0 [label....] && not contains }
		if(!line.startsWith("\t0") && !line.contains("}")){
			line=line.trim(); 
			
			//2) The line is an edge
			if(line.contains("->")){
				int src=Integer.parseInt(line.split("->")[0].trim());
				int dst=Integer.parseInt(line.split("->")[1].trim());
				
				edges.add(new Edge(src+"_"+dst,src,dst));
			}else{//3) The line is a node
				line=line.toLowerCase();
				dags.get(index).comment=line;
				line=line.replace("\"", "");
				dags.get(index).comment=dags.get(index).comment.replace("\"", "&quot;");
				double miet=Double.parseDouble(line.split("miet=")[1].split(",")[0]);
				double meet=Double.parseDouble(line.split("meet=")[1].split(",")[0]);
				double maet=Double.parseDouble(line.split("maet=")[1].split(",")[0]);
				int mem_acess=Integer.parseInt(line.split("mem=")[1].split(",")[0]);
				String mem_unit=line.split("unit=")[1].split(",")[0].replace("]","").toUpperCase();
				int nodeId=Integer.parseInt(line.split(" ")[0]);
				
				nodes.add(new Node(nodeId,miet,meet,maet,mem_acess,mem_unit,dags.get(index).comment));
			}	
		}else if(line.contains("period")){ //process period priority deadline information
			dags.get(index).setPeriod(Integer.parseInt(line.split("period=")[1].split(",")[0]));
			dags.get(index).setPriority(Integer.parseInt(line.split("priority=")[1].split(",")[0]));
			dags.get(index).setDeadline(Integer.parseInt(line.split("deadline=")[1].split(",")[0]));
			dags.get(index).setMap(Integer.parseInt(line.split("map=")[1].split(",")[0].replace("]", "")));

		}
	}

	public void writeModelFile(String filename, int index){
    	try{
			PrintWriter writer = new PrintWriter(new FileOutputStream(new File(filename),true));
		    //write header of the xml
		    writer.println("<dagCollection xmi:id=\""+dags.get(index).dagName+"\" name=\""+dags.get(index).dagName+"\" period=\""+dags.get(index).getPeriod()+"\" priority=\""
		    			+dags.get(index).getPriority()+"\" deadline=\""+dags.get(index).getDeadline()+"\" processor=\""+dags.get(index).getMap()+"\" mem_type=\""+dags.get(index).mem_access
		    			+"\" step=\""+dags.get(index).step+"\">");
		    
		    for(Node node : nodes){
		    	String nodeID=dags.get(index).dagName+"_"+node.getNodeID();
			    writer.println("<node xmi:id=\""+nodeID+"\" name=\""+nodeID+"\" "
			    		+ "comment=\""+node.getComment()+"\" miet=\""+node.getIwcet()+"\" meet=\""+node.getWcet()+"\" "
			    		+ "maet=\""+node.getMaet()+"\" mem_access=\""+node.getMem_access()+"\" mem_unit=\""+node.getMem_unit()+"\"/>");
		    }
		    for(Edge edge : edges){
		    	String edgeName=dags.get(index).dagName+"_"+edge.name;
		    	String dagSrc=dags.get(index).dagName+"_"+edge.src.getNodeID();
		    	String dagTrg=dags.get(index).dagName+"_"+edge.trg.getNodeID();
			    writer.println("<edge xmi:id=\""+edgeName+"\" name=\""+edgeName+"\" src=\""+dagSrc+"\" trg=\""+dagTrg+"\"/>");
		    }
		    writer.println("</dagCollection>");

		    writer.close();
	    
		} catch (IOException e) {
	 	   // do something
	 	}
    }
    
	 public XMLGenerator() {

	 }
    
	 public XMLGenerator(DAG dagsParam[]) {
		this.dagMemAccess=dagMemAccess;
		String fileDst="./modelToCode/dagParsed.model";
		createModelFile(fileDst);
		int index=0;
		for (DAG dag : dagsParam){
			nodes.clear();
			edges.clear();
		    dags.add(dag);
			readDOTFile(dag.dagPath.toString(),index);
		    writeModelFile(fileDst,index);
		    index++;
		}
		
		closeModelFile(fileDst);
		System.out.println("Transformation finished...");
	}

	private void closeModelFile(String fileDst) {
		try {
			PrintWriter writer = new PrintWriter(new FileOutputStream(new File(fileDst),true));
			writer.println("</GraphMetamodel:DagSet>");
			writer.close();  	
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} 
	}

	private void createModelFile(String fileDst) {
		try {
			PrintWriter writer = new PrintWriter(fileDst, "UTF-8");
			writer.println("<?xml version=\"1.0\" encoding=\"ASCII\"?>");
		    writer.println("<GraphMetamodel:DagSet xmi:version=\"2.0\" xmlns:xmi=\"http://www.omg.org/XMI\" xmlns:GraphMetamodel=\"GraphMetamodel\" xmi:id=\"dagPSocrates\" partitioning_policy=\"PARTITIONED\" semaphore_protocol=\"PRIO_CEILING\" sched_alg=\"SCHED_OTHER\">");
			writer.close();   		

		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
	}
}
