package hipert.hg.XMLParser;
import java.io.*;
import java.util.LinkedList;

import org.eclipse.epsilon.common.util.StringUtil;

public class XMLGenerator {
	
	private LinkedList<Edge> edges=new LinkedList<Edge>();
	private LinkedList<Node> nodes=new LinkedList<Node>();
	private LinkedList<DAG> dags=new LinkedList<DAG>();
	
	private String filename = null;

//	// _POL_ for debug purposes
//	public void setFilename(String filename) {
//		this.filename = filename;
//	}

	public String getFilename() {
		return filename;
	}

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
			dags.get(index).setDagName(line.split(" ")[1]);
			
			//Process file
			while( (line = br.readLine()) != null ){
				processString(line,index);
			}
		} 
		catch (IOException e) 
		{  
		    e.printStackTrace();
		}
		this.filename = filename;
    }

    private static double GetValDouble(String line, String attrName, double defaultValue) {
    	try { return Double.parseDouble(MaybeGetVal(line, attrName)); }
    	catch(Exception ex) { return defaultValue;}
    }
    private static int GetValInt(String line, String attrName, int defaultValue) {
    	try { return Integer.parseInt(MaybeGetVal(line, attrName)); }
    	catch(Exception ex) { return defaultValue;}
    }
    
    private static String MaybeGetVal(String line, String attrName) {
    	String [] split = line.toLowerCase().split(attrName+"=");
    	if(split.length < 2) // No way
    		return "";
    	split = split[1].split(",");
    	if(split.length < 2) // No way
    		return "";
    	return split[0];
    }
    
    /*This function process a line from the file and prepare the structure
    to be written in the file
	A line may be:
 	1) 0 && not label with period = Ignored label
    2) Edge (containing source and destination node)
    3) Node (containing)
    */
    private void processString(String line,int index) {
    	if(line == null || StringUtil.isEmpty(line))
    		return;
    	
    	// If it's a comment
    	if(line.trim().startsWith("//"))
    			return;
    	
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
				dags.get(index).setComment(line);
				line=line.replace("\"", "");
				dags.get(index).setComment(dags.get(index).getComment().replace("\"", "&quot;"));
				
				double miet = GetValDouble(line, "miet", -1);
				double meet = GetValDouble(line, "meet", -1);
				double maet = GetValDouble(line, "maet", -1);
				int mem_acess = GetValInt(line, "mem", 0);
				
				//String mem_unit=line.split("unit=")[1].split(",")[0].replace("]","").toUpperCase();
				String mem_unit = MaybeGetVal(line, "unit");
				mem_unit = mem_unit.replace("]","").toUpperCase();
				if(StringUtil.isEmpty(mem_unit))
					mem_unit = "B";
				
				int nodeId=Integer.parseInt(line.split(" ")[0]);
				nodes.add(new Node(nodeId,miet,meet,maet,mem_acess,mem_unit,dags.get(index).getComment()));
				
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
		    writer.println("<dagCollection xmi:id=\""+dags.get(index).getDagName()+"\" name=\""+dags.get(index).getDagName()+"\" period=\""+dags.get(index).getPeriod()+"\" priority=\""
		    			+dags.get(index).getPriority()+"\" deadline=\""+dags.get(index).getDeadline()+"\" processor=\""+dags.get(index).getMap()+"\" mem_type=\""+dags.get(index).getMem_access()
		    			+"\" step=\""+dags.get(index).getStep()+"\" stride=\""+dags.get(index).getStride()+"\">");
		    
		    for(Node node : nodes){
		    	String nodeID=dags.get(index).getDagName()+"_"+node.getNodeID();
			    writer.println("<node xmi:id=\""+nodeID+"\" name=\""+nodeID+"\" "
			    		+ "comment=\""+node.getComment()+"\" miet=\""+node.getIwcet()+"\" meet=\""+node.getWcet()+"\" "
			    		+ "maet=\""+node.getMaet()+"\" mem_access=\""+node.getMem_access()+"\" mem_unit=\""+node.getMem_unit()+"\"/>");
		    }
		    for(Edge edge : edges){
		    	String edgeName=dags.get(index).getDagName()+"_"+edge.name;
		    	String dagSrc=dags.get(index).getDagName()+"_"+edge.src.getNodeID();
		    	String dagTrg=dags.get(index).getDagName()+"_"+edge.trg.getNodeID();
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
		createModelFile(fileDst,dagsParam[0]);
		int index=0;
		for (DAG dag : dagsParam){
			nodes.clear();
			edges.clear();
		    dags.add(dag);
			readDOTFile(dag.getDagPath().toString(),index);
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

	private void createModelFile(String fileDst,DAG d) {
		try {
			PrintWriter writer = new PrintWriter(fileDst, "UTF-8");
			writer.println("<?xml version=\"1.0\" encoding=\"ASCII\"?>");

		    writer.println("<GraphMetamodel:DagSet xmi:version=\"2.0\" xmlns:xmi=\"http://www.omg.org/XMI\" xmlns:GraphMetamodel=\"GraphMetamodel\" "
		    		+ "xmi:id=\"dagPSocrates\" partitioning_policy=\""+d.getPartitioning_policy()+"\" semaphore_protocol=\"PRIO_CEILING\" "
    				+ "sched_alg=\""+d.getSched_policy()+"\" output_dir=\""+d.getOutput_dir()+"/"+"\">");

			writer.close();   		

		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
	}
}

