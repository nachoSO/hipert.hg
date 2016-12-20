package hipert.hg.XMLParser;
import java.awt.List;
import java.io.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.LinkedList;

public class XMLModifier {
	
	private LinkedList<Edge> edges=new LinkedList<Edge>();
	private LinkedList<Node> nodes=new LinkedList<Node>();
	private DAG dag=new DAG();

	private String dagMemAccess="prem";
	
	public void setDagMemAccess(String dagMemAccess){
		this.dagMemAccess=dagMemAccess;
	}
	
	public DAG getDag(){
		return this.dag;
	}
	
	public LinkedList<Node> getNodes(){
		return this.nodes;
	}
	
    @SuppressWarnings("resource")
	public void readDOTFile(String filename){
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
			dag.dagName=line.split(" ")[1];
			
			//Process file
			while( (line = br.readLine()) != null ){
				processString(line);
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
    private void processString(String line) {
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
				dag.comment=line;
				line=line.replace("\"", "");
				dag.comment=dag.comment.replace("\"", "&quot;");
				double miet=Double.parseDouble(line.split("miet=")[1].split(",")[0]);
				double meet=Double.parseDouble(line.split("meet=")[1].split(",")[0]);
				double maet=Double.parseDouble(line.split("maet=")[1].split(",")[0]);
				int mem_acess=Integer.parseInt(line.split("mem=")[1].split(",")[0]);
				String mem_unit=line.split("unit=")[1].split(",")[0].replace("]","").toUpperCase();
				int nodeId=Integer.parseInt(line.split(" ")[0]);
				
				nodes.add(new Node(nodeId,miet,meet,maet,mem_acess,mem_unit,dag.comment));
			}	
		}else if(line.contains("period")){ //process period priority deadline information
			dag.setPeriod(Integer.parseInt(line.split("period=")[1].split(",")[0]));
			dag.setPriority(Integer.parseInt(line.split("priority=")[1].split(",")[0]));
			dag.setDeadline(Integer.parseInt(line.split("deadline=")[1].split(",")[0]));
			dag.setMap(Integer.parseInt(line.split("map=")[1].split(",")[0].replace("]", "")));

		}
	}
    
    
    public void modifyXML(File path, String nodeName,String period, String deadline, String priority,
			String map, String MIET, String MEET, String MAET, String MEM, String MEM_UNIT,int index) throws IOException {
		ArrayList<String> newLines = new ArrayList<String>();

		BufferedReader br = new BufferedReader(new FileReader(path));

		String line;
		String QM="\"";
		while ((line = br.readLine()) != null) {
			if (line.contains("0 [period")){
				line= "\t"+"0 [period="+period+", priority="+priority+", deadline="+deadline+", map="+map+"]";
			}
			if (line.contains(nodeName +" [MIET")){
				line = "\t"+nodeName+" [MIET="+QM+MIET+QM+", MEET="+QM+MEET+QM+", "
						+ "MAET="+QM+MAET+QM+", MEM="+QM+MEM+QM+", UNIT="+QM+MEM_UNIT+QM+"]";
			}
			newLines.add(line+"\n");
		}
		br.close();
		
		FileWriter writer = new FileWriter(path); 
		for(String str: newLines) {
		  writer.write(str);
		}
		writer.close();
		
		this.nodes.get(index).setWcet(Double.parseDouble(MIET));
		this.nodes.get(index).setIwcet(Double.parseDouble(MIET));
		this.nodes.get(index).setMaet(Double.parseDouble(MAET));
		this.nodes.get(index).setMem_access(Integer.parseInt(MEM));
		this.nodes.get(index).setMem_unit(MEM_UNIT);
		this.nodes.get(index).setNodeID(Integer.parseInt(nodeName));
		this.dag.setPeriod(Integer.parseInt(period));
		this.dag.setDeadline(Integer.parseInt(deadline));
		this.dag.setMap(Integer.parseInt(map));
		this.dag.setPriority(Integer.parseInt(priority));
	}

	public void modifyXML() {
		// TODO Auto-generated method stub
		
	}
}

