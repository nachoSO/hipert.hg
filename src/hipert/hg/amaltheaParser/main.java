/**
 * Purpose: Waters Challenge
 *
 * @author Unimore
 * @version 1.0 25/05/16
 */

package hipert.hg.amaltheaParser;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.io.Writer;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;

import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpressionException;
import javax.xml.xpath.XPathFactory;
import javax.xml.parsers.DocumentBuilder;
import org.w3c.dom.Document;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

public class main {
   static int nShared=0; //num of shared labels
   public static void main(String[] args) throws ParserConfigurationException, SAXException, IOException, XPathExpressionException{

	   FileInputStream file = new FileInputStream(new File("ChallengeModel_2017.amxmi"));
       DocumentBuilderFactory builderFactory = DocumentBuilderFactory.newInstance();
       DocumentBuilder builder =  builderFactory.newDocumentBuilder();
       Document xmlDocument = builder.parse(file);
	   XPath xPath =  XPathFactory.newInstance().newXPath();
	   
	   //load all XML information of specific core
	   System.out.println("Loading cores...");
	   Core core0=load_core("Scheduler_CORE0?type=os.TaskScheduler",xmlDocument,xPath); //all preempptive |  U: 0.9701929364654952
	   Core core1=load_core("Scheduler_CORE1?type=os.TaskScheduler",xmlDocument,xPath); //all preempptive | U: 1.3357246246246244
	   Core core2=load_core("Scheduler_CORE2?type=os.TaskScheduler",xmlDocument,xPath); //preempptive & cooperative | U: 1.0685265349999995
	   Core core3=load_core("Scheduler_CORE3?type=os.TaskScheduler",xmlDocument,xPath); //all preempptive | U: 1.1793503684210531
	   //Core core0=load_manual_core();
	   //order tasks by priority
	   Collections.sort(core0.tasks);
	   Collections.sort(core1.tasks);
	   Collections.sort(core2.tasks);
	   Collections.sort(core3.tasks);
	   System.out.println("Cores already loaded");
	   
	   LinkedList<Core> cores=new LinkedList<Core>();
	   cores.add(core0);
	   cores.add(core1);
	   cores.add(core2);
	   cores.add(core3);
	   matrixCommunication(cores,xmlDocument,xPath);
	   System.out.println("Matrix communication computed");
	   taskDependencies(cores);
	   
	   //this function balance the utilization	
	   //increaseWCET(core0,core1,core2,core3);
	   //calculate response time of all core tasks
	   /* System.out.println("Computing RTA");
	   core0.RTAcore(); 
	   core1.RTAcore(); 
	   core2.RTAcore(); 
	   core3.RTAcore(); 
	   

	   //print results
	   for(int j=0;j<cores.size();j++){
		   double executionTime=0.0;
		   double period=0.0;
		   double utilization=0.0;
		   System.out.println("******** CORE"+j);
		   for(int i=0;i<cores.get(j).tasks.size();i++){
			   executionTime=cores.get(j).tasks.get(i).executionTime;
			   period=cores.get(j).tasks.get(i).period;
			   utilization+=executionTime/period;
			   System.out.println(cores.get(j).tasks.get(i) + ", utilization: "+utilization);
		   }
	   }
	   computeEffectChain(cores,"EffectChain_1",xmlDocument,xPath);
	   computeEffectChain(cores,"EffectChain_2",xmlDocument,xPath);
	   computeEffectChain(cores,"EffectChain_3",xmlDocument,xPath);
	   */
	   
   }

   //this function shows the task dependencies between tasks
   private static void taskDependencies(LinkedList<Core> cores) {

   }

   //This function prints the matrix communication
   static void matrixCommunication(LinkedList<Core> cores, Document xmlDocument,XPath xPath) throws FileNotFoundException, UnsupportedEncodingException, XPathExpressionException{
	   PrintWriter  writer_IMPLICIT= new PrintWriter("label_memory_mapping_implicit.txt", "UTF-8");
	   PrintWriter  writer_LET= new PrintWriter("label_memory_mapping_LET_h.txt", "UTF-8");

	   System.out.println("Creating matrix communication");
	   for(Core core_src:cores){
		   for(Task t_src: core_src.tasks){
			   writer_IMPLICIT.println("Task "+t_src.name+" communicates with...");
			   writer_LET.println("Task "+t_src.name+" communicates with...");
			   
			   int IMPLICIT_footprint=0;
			   int LET_footprint=0;
			   int IMPLICIT_TcostCopyInPerTask=0;
			   int IMPLICIT_TcostCopyOutPerTask=0;
			   
			   for(Core core_dst:cores){

				   for(Task t_dst: core_dst.tasks){
					   
					   int IMPLICIT_costCopyInPerTask=0;
					   int IMPLICIT_costCopyOutPerTask=0;

					   int LET_costTransactionPerTask=0;
					   
					   if(!t_src.name.equals(t_dst.name)){
						   for(String label_src: t_src.labels){
							  for(String label_dest:t_dst.labels){
								  String src_operation=label_src.substring(label_src.length() - 1);
								  String dst_operation=label_dest.substring(label_dest.length() - 1);
								  if(label_src.substring(0,label_src.length()-2).equals(label_dest.substring(0,label_dest.length()-2))
										  && !src_operation.equals(dst_operation)){
									  	  
									  String label_src_allocation=labelAllocation(label_src.substring(0,label_src.length()-2),xmlDocument,xPath);
									  String label_dst_allocation=labelAllocation(label_dest.substring(0,label_dest.length()-2),xmlDocument,xPath);
									  String core_source=core_of_task(t_src.name, xmlDocument, xPath).replace("?type=hw.Core","");
									  String core_dest=core_of_task(t_dst.name, xmlDocument, xPath).replace("?type=hw.Core","");
									  
									  //IMPLICIT
									  int IMPLICIT_costCopyIn=calculateCostCopyIn_IMPLICIT(label_src_allocation,core_source);
									  int IMPLICIT_costCopyOut=0;
									  if(src_operation.equals("w")) //cost copy out only for writers
										  IMPLICIT_costCopyOut=calculateCostCopyOut_IMPLICIT(label_src_allocation,core_source);

									  writer_IMPLICIT.println("\t Task_src: "+t_src.name+"("+core_source+")"+" label: "+label_src+ "("
									  + label_src_allocation +")" +
									  " || Task_dst: "+t_dst.name+"("+core_dest+")"+" label: "+label_dest 
									  + "("+ label_dst_allocation +")" + " || Cost copy in: "+ IMPLICIT_costCopyIn + " - Cost copy out: "+ IMPLICIT_costCopyOut);
									  IMPLICIT_costCopyInPerTask+=IMPLICIT_costCopyIn;
									  IMPLICIT_costCopyOutPerTask+=IMPLICIT_costCopyOut;
									  IMPLICIT_footprint+=size_of_label(label_src.substring(0,label_src.length()-2),xmlDocument,xPath);
									  
									  //LET
									  int LET_costTransaction=0;
									  if(src_operation.equals("w")) //cost Transaction out only for writers
										  LET_costTransaction=calculateCostTransaction_LET(label_src_allocation,core_dest);

									  writer_LET.println("\t Task_src: "+t_src.name+"("+core_source+")"+" label: "+label_src+ "("
									  + label_src_allocation +")" +
									  " || Task_dst: "+t_dst.name+"("+core_dest+")"+" label: "+label_dest 
									  + "("+ label_dst_allocation +")" + " || Cost transaction: "+ LET_costTransaction);
									  LET_costTransactionPerTask+=LET_costTransaction;
									  if(src_operation.equals("r")){
										  int footprint=size_of_label(label_src.substring(0,label_src.length()-2),xmlDocument,xPath);							  
										  if(typeOfCommunication(t_src.name,t_dst.name).equals("nh")){ //non-harmonic tasks use the double of footprint
											  if(t_src.name.equals("Task_20ms"))
												  footprint=footprint*3;
											  else if(t_src.name.equals("Task_50ms"))
												  footprint=footprint*2;
										  }else if(typeOfCommunication(t_src.name,t_dst.name).equals("a")){
											  footprint=footprint*3;
										  }
										  LET_footprint+=footprint;
									  }
								  }
							  }
						   }
					   }
					   IMPLICIT_TcostCopyInPerTask+=IMPLICIT_costCopyInPerTask;
					   IMPLICIT_TcostCopyOutPerTask+=IMPLICIT_costCopyOutPerTask;
					   if(IMPLICIT_costCopyInPerTask!=0)
						   writer_IMPLICIT.println("\t \t Cost copy in "+t_src.name+" to "+t_dst.name+" is: "+IMPLICIT_costCopyInPerTask+ " cycles");
					   if(IMPLICIT_costCopyOutPerTask!=0)
						   writer_IMPLICIT.println("\t \t Cost copy out "+t_src.name+" to "+t_dst.name+" is: "+IMPLICIT_costCopyOutPerTask+ " cycles");
					   if(LET_costTransactionPerTask!=0)
						   writer_LET.println("\t \t Total cost transaction "+t_src.name+" to "+t_dst.name+ " is: "+LET_costTransactionPerTask+ " cycles");
				   }
				   
			   }
			   writer_IMPLICIT.println("******** Cost copy in task: "+t_src.name+" is: "+IMPLICIT_TcostCopyInPerTask+ " cycles");
			   writer_IMPLICIT.println("******** Cost copy out task: "+t_src.name+" is: "+IMPLICIT_TcostCopyOutPerTask+ " cycles");
			   writer_IMPLICIT.println("******** Total footprint used for shared_labels task: "+t_src.name+" is: "+IMPLICIT_footprint+ " bits");
			   
			   writer_LET.println("******** Total footprint used for shared_labels task: "+t_src.name+" is: "+LET_footprint+ " bits");

		   }
	   }
	   writer_IMPLICIT.close();
	   writer_LET.close();

   }

	   private static String typeOfCommunication(String t_src, String t_dst) {
		   String communicationType="h";
		   if(t_src.contains("ISR") || t_dst.contains("ISR") || t_src.contains("Angle") || t_dst.contains("Angle")){
			   communicationType="a";
		   }else{
			   if(t_src.contains("Task_") && t_dst.contains("Task_")){
				   int t1=Integer.parseInt(t_src.replace("Task_","").replace("ms",""));
				   int t2=Integer.parseInt(t_dst.replace("Task_","").replace("ms",""));
				   if(t1>t2){
					   int mod=t1%t2;
					   if(mod!=0)
						   communicationType="nh";
				   }else{
					   int mod=t2%t1;
					   if(mod!=0)
						   communicationType="nh";
				   }
		   	   }else{
		   		   communicationType="nh";
		   	   }
		   }
		   return communicationType;
	   }

//here we pay in function of where source core and LRAM dest location
   private static int calculateCostTransaction_LET(String label_src_allocation,String core_source) {
	   int cost=9;
	   if(label_src_allocation.substring(label_src_allocation.length() - 1).equals(core_source.substring(core_source.length() - 1)))
		   cost=1;
	   
	   return cost;
   }
   
   private static int calculateCostCopyIn_IMPLICIT(String label_src_allocation, String core_source) {
	   int cost=9;
	   if(label_src_allocation.substring(label_src_allocation.length() - 1).equals(core_source.substring(core_source.length() - 1)))
		   cost=1;
	   
	   return cost;
   }
   
   private static int calculateCostCopyOut_IMPLICIT(String label_src_allocation, String core_source) {
	   int cost=9;
	   if(label_src_allocation.substring(label_src_allocation.length() - 1).equals(core_source.substring(core_source.length() - 1)))
		   cost=1;
	   
	   return cost;
   }

   //This function returns in which RAM is allocated the label
   private static String labelAllocation(String label, Document xmlDocument,XPath xPath) throws XPathExpressionException{
	   label=label+"?type=sw.Label";
       String expression = "*/mappingModel/mapping[@abstractElement='" + label + "']/@mem";
       NodeList labelNodeList = (NodeList) xPath.compile(expression).evaluate(xmlDocument, XPathConstants.NODESET);
       String labelAllocation=labelNodeList.item(0).getFirstChild().getNodeValue().replace("?type=hw.Memory", "");
       return labelAllocation;
   }
   

   //This function decreases the utilization of all cores
   public static void increaseWCET(Core core0,Core core1, Core core2, Core core3){
	   for(int i=0;i<core0.tasks.getLast().runnables.size();i++){ //core 0 last task
		   double wcetR=core0.tasks.getLast().runnables.get(i).executionTime;
		   double pR=(wcetR*50)/100;
		   core0.tasks.getLast().runnables.get(i).executionTime=wcetR-pR;
	   }
	   
	   core0.tasks.getLast().executionTime=core0.tasks.getLast().processInstructions();
	   
	   for(int i=0;i<core1.tasks.getLast().runnables.size();i++){ //core 1 last task
		   double wcetR=core1.tasks.getLast().runnables.get(i).executionTime;
		   double pR=(wcetR*63)/100;
		   core1.tasks.getLast().runnables.get(i).executionTime=wcetR-pR;
	   }
	   core1.tasks.getLast().executionTime=core1.tasks.getLast().processInstructions();
	   
	   for(int i=0;i<core2.tasks.getFirst().runnables.size();i++){ //core 2 first task
		   double wcetR=core2.tasks.getFirst().runnables.get(i).executionTime;
		   double pR=(wcetR*35)/100;
		   core2.tasks.getFirst().runnables.get(i).executionTime=wcetR-pR;
	   }
	   core2.tasks.getFirst().executionTime=core2.tasks.getFirst().processInstructions();
	   
	   for(int i=0;i<core3.tasks.getLast().runnables.size();i++){ //core 3 last task
		   double wcetR=core3.tasks.getLast().runnables.get(i).executionTime;
		   double pR=(wcetR*16)/100;
		   core3.tasks.getLast().runnables.get(i).executionTime=wcetR-pR;
	   }
	   core3.tasks.getLast().executionTime=core3.tasks.getLast().processInstructions();
   }
   
   //This function prints the response time of an effect chain as is detailed in the paper
   public static void computeEffectChain(LinkedList<Core> cores,String effectChainName,Document xmlDocument,XPath xPath) throws XPathExpressionException, FileNotFoundException, UnsupportedEncodingException{
	   LinkedList<String> effectChainRunnables=new LinkedList<String>();
	   System.out.println("***************"+effectChainName);

	   //find runnables involved in the effect chain
	   String expressionStimulus = "*/constraintsModel/eventChains[@name='" + effectChainName + "']/segments/eventChain/@stimulus";
       NodeList stimulusNode = (NodeList) xPath.compile(expressionStimulus).evaluate(xmlDocument, XPathConstants.NODESET);
       String expressionResponse = "*/constraintsModel/eventChains[@name='" + effectChainName + "']/segments/eventChain/@response";
       NodeList responseNode = (NodeList) xPath.compile(expressionResponse).evaluate(xmlDocument, XPathConstants.NODESET);
       
       String stimulus=stimulusNode.item(0).getFirstChild().getNodeValue().replace("?type=events.RunnableEvent", "");
	   stimulus=stimulus.replace("RunnableStart_", "");
	   effectChainRunnables.add(stimulus);
	   
	   //load all runnables of the effect chain
       for (int j = 1; j < stimulusNode.getLength(); j++) {
    	   stimulus=stimulusNode.item(j).getFirstChild().getNodeValue().replace("?type=events.RunnableEvent", "");
    	   stimulus=stimulus.replace("RunnableStart_", "");
    	   if(!effectChainRunnables.getLast().equals(stimulus))
    		   effectChainRunnables.add(stimulus);
    	   String response=responseNode.item(j).getFirstChild().getNodeValue().replace("?type=events.RunnableEvent", "");
    	   response=response.replace("RunnableStart_", "");
    	   if(!effectChainRunnables.getLast().equals(response))
    		   effectChainRunnables.add(response);
       }
       
       //find task-core to get the wcet of the runnable in the previous list
       //two types of effect chain: 1) all runnables belongs to the same task, 2) runnables belongs to different task (conservative approach)
       int effectChainType=1;
       String SauxFirstValue=core_of_task(task_of_runnable(effectChainRunnables.get(0),xmlDocument,xPath),xmlDocument,xPath).replace("?type=hw.Core", "").replace("CORE", "");
       int IauxFirstValue=Integer.parseInt(SauxFirstValue);
       for(int i=1;i<effectChainRunnables.size();i++){
    	   SauxFirstValue=core_of_task(task_of_runnable(effectChainRunnables.get(i),xmlDocument,xPath),xmlDocument,xPath).replace("?type=hw.Core", "").replace("CORE", "");
    	   if(IauxFirstValue!=Integer.parseInt(SauxFirstValue)){
    		   effectChainType=2;
    		   break;
    	   }
       }	
       
	   String taskRunnable;
	   String coreTask; //index core
	   int coreIndex;
	   double rtRunnable,period;
	   double totalRT=0;    
       switch (effectChainType) {
	       case 1:   //EC TYPE 1
	    	   taskRunnable=task_of_runnable(effectChainRunnables.getLast(),xmlDocument,xPath);
	    	   coreTask=core_of_task(taskRunnable,xmlDocument,xPath).replace("?type=hw.Core", "");
	    	   coreTask=coreTask.replace("CORE", "");
	    	   coreIndex=Integer.parseInt(coreTask);
	    	   for(int j=0;j<cores.get(coreIndex).tasks.size();j++){
        		   if(cores.get(coreIndex).tasks.get(j).name.equals(taskRunnable)){ //we find task, now we need the runnable
        			   for(int k=0;k<cores.get(coreIndex).tasks.get(j).runnables.size();k++){
        				   if(cores.get(coreIndex).tasks.get(j).runnables.get(k).name.equals(effectChainRunnables.getLast())){
        					   rtRunnable=cores.get(coreIndex).tasks.get(j).runnables.get(k).responseTime;
    						   period=cores.get(coreIndex).tasks.get(j).period;
    						   totalRT+=(rtRunnable+period);
    						   System.out.println("Effect chain 1: "+(rtRunnable+period)+"us");
        				   }
        			   }
        		   }
        	   }
               break;
	       case 2:  //EC TYPE 2
	           for(int i=0;i<effectChainRunnables.size();i++){
	        	   taskRunnable=task_of_runnable(effectChainRunnables.get(i),xmlDocument,xPath);
	        	   coreTask=core_of_task(taskRunnable,xmlDocument,xPath).replace("?type=hw.Core", "");
	        	   coreTask=coreTask.replace("CORE", "");
	        	   coreIndex=Integer.parseInt(coreTask);
	        	   //task to runnable runnable relation
	        	   for(int j=0;j<cores.get(coreIndex).tasks.size();j++){
	        		   if(cores.get(coreIndex).tasks.get(j).name.equals(taskRunnable)){ //we find task, now we need the runnable
	        			   for(int k=0;k<cores.get(coreIndex).tasks.get(j).runnables.size();k++){
	        				   if(cores.get(coreIndex).tasks.get(j).runnables.get(k).name.equals(effectChainRunnables.get(i))){
	        					   rtRunnable=cores.get(coreIndex).tasks.get(j).runnables.get(k).responseTime;
	    						   period=cores.get(coreIndex).tasks.get(j).period;
	    						   totalRT+=(rtRunnable+period);
	        					   System.out.println(effectChainRunnables.get(i)+" response time+period: "
	        							   +(rtRunnable+period)+"us");
	        				   }
	        			   }
	        		   }
	        	   }
	           }
	    	   System.out.println("Total RT of the effect chain: "+totalRT);
               break;
       }

   }
   
   //This method loads a simple tasks to test 
   public static Core load_manual_core(){
	   Core core=new Core("Core0");
	   
	   //Task0
	   Runnable r0T0=new Runnable("Runnable 0",2000),r1T0=new Runnable("Runnable 1",2000),r2T0=new Runnable("Runnable 2",2000);
	   Task T0=new Task("Task 0",100.0,11,"preemptive");
	   T0.addRunnable(r0T0);T0.addRunnable(r1T0);T0.addRunnable(r2T0);
	   core.addTask(T0);
	  
	   //Task1
	   Runnable r0T1=new Runnable("Runnable 0",12000),r1T1=new Runnable("Runnable 1",1000);
	   Task T1=new Task("Task 1",150.0,9,"preemptive");
	   T1.addRunnable(r0T1);T1.addRunnable(r1T1);
	   core.addTask(T1);
	   
	   //Task2
	   Runnable r0T2=new Runnable("Runnable 0",100),r1T2=new Runnable("Runnable 1",89100),r2T2=new Runnable("Runnable 2",100);
	   Task T2=new Task("Task 2",550.0,8,"preemptive");
	   T2.addRunnable(r0T2);T2.addRunnable(r1T2);T2.addRunnable(r2T2);
	   core.addTask(T2);
	   
	   return core;
   }
   
   /*---------------------------------------------------------------------------------------------*/
   /**
    * The following functions are used to query the XML
  	*/
   
   //This method loads a core "coreName" from the challenge model
   public static Core load_core(String coreName,Document xmlDocument,XPath xPath) throws XPathExpressionException, FileNotFoundException, UnsupportedEncodingException{
	   
	   Core core=new Core(coreName.replace("?type=os.TaskScheduler", ""));	   
	   String expression = "*/mappingModel/processAllocation[@scheduler='" + coreName + "']/@process";
	   NodeList allTasks = (NodeList) xPath.compile(expression).evaluate(xmlDocument, XPathConstants.NODESET);
	   boolean readDependency=false;
	   boolean writeDependency=false;
	   
	   for (int i = 0; i < allTasks.getLength(); i++) { //get all tasks
		   String taskName=allTasks.item(i).getFirstChild().getNodeValue().replace("?type=sw.Task", "");
		   
		   double period=period_of_task(taskName,xmlDocument,xPath);
		   int priority=priority_of_task(taskName,xmlDocument,xPath);
		   String preemptionType=preemptionType_of_task(taskName,xmlDocument,xPath);
		   Task t=new Task(taskName,period,priority,preemptionType); //create a task
		   
		   expression = "*/swModel/tasks[@name='" + taskName + "']/callGraph/graphEntries/calls/@runnable";
		   NodeList allRunnables = (NodeList) xPath.compile(expression).evaluate(xmlDocument, XPathConstants.NODESET);
		   for (int j = 0; j < allRunnables.getLength(); j++) {
			   String runnableName=allRunnables.item(j).getFirstChild().getNodeValue().replace("?type=sw.Runnable", ""); //get one specific runnable
			   expression="*/swModel/runnables[@name= '" + runnableName + "']/runnableItems/deviation/upperBound/@value";
			   NodeList instructionUpperBound = (NodeList) xPath.compile(expression).evaluate(xmlDocument, XPathConstants.NODESET); //get instructionUpperBound
			   int nInstructions=Integer.parseInt(instructionUpperBound.item(0).getFirstChild().getNodeValue());
			   Runnable r=new Runnable(runnableName,nInstructions);
			   t.addRunnable(r);
		   }
		   core.addTask(t);
	   }
	  
	   //assign label to tasks
	   for(Task t: core.tasks){
		   for(Runnable r: t.runnables){
			   
			   String expression_label_name="*/swModel/runnables[@name= '" + r.name + "']/runnableItems/@data";
			   NodeList allLabels = (NodeList) xPath.compile(expression_label_name).evaluate(xmlDocument, XPathConstants.NODESET); 
			   
			   for (int j = 0; j < allLabels.getLength(); j++) {
				   String label=allLabels.item(j).getFirstChild().getNodeValue();
				   String expression_access="*/swModel/runnables[@name= '" + r.name + "']/runnableItems[@data= '" + label + "']/@access";
				   NodeList label_access = (NodeList) xPath.compile(expression_access).evaluate(xmlDocument, XPathConstants.NODESET); 
				   String access=label_access.item(0).getFirstChild().getNodeValue();

				   if(access.equals("read")){
					   t.addLabel(label.replace("?type=sw.Label", "")+"_r");
				   }
				   else{
					   t.addLabel(label.replace("?type=sw.Label", "")+"_w");   
				   }
			   }
		   }
		   
	   }

	   return core;
   }

   //This function returns the preemption type of task "taskName"
   public static String preemptionType_of_task(String taskName, Document xmlDocument,XPath xPath) throws XPathExpressionException, FileNotFoundException, UnsupportedEncodingException{
	   String preemptionType;
       String expression2 = "*/swModel/tasks[@name='" + taskName + "']/@preemption";
       NodeList priorityN = (NodeList) xPath.compile(expression2).evaluate(xmlDocument, XPathConstants.NODESET);
       preemptionType=priorityN.item(0).getFirstChild().getNodeValue(); 
       return preemptionType;
   }
   
   //This method returns the priority of the task "taskName"
   public static int priority_of_task(String taskName, Document xmlDocument,XPath xPath) throws XPathExpressionException, FileNotFoundException, UnsupportedEncodingException{
	   int priority=0;
       String expression2 = "*/swModel/tasks[@name='" + taskName + "']/@priority";
       NodeList priorityN = (NodeList) xPath.compile(expression2).evaluate(xmlDocument, XPathConstants.NODESET);
       priority=Integer.parseInt(priorityN.item(0).getFirstChild().getNodeValue()); 
       return priority;
   }
   
   //This function returns the period and compute to us of the task "taskName"
   public static double period_of_task(String taskName,Document xmlDocument,XPath xPath) throws XPathExpressionException{
	   String unitPeriod;
	   double periodTime;
	   String expression = "*/swModel/tasks[@name= '" + taskName + "']/@stimuli";
	   NodeList nodeList = (NodeList) xPath.compile(expression).evaluate(xmlDocument, XPathConstants.NODESET);
	   String stimuli=nodeList.item(0).getFirstChild().getNodeValue();

	   if(stimuli.contains("stimuli.Sporadic")){
		   String expression2 = "*/stimuliModel/stimuli[@name= '" + stimuli.replace("?type=stimuli.Sporadic","") + "']/stimulusDeviation/lowerBound/@value";
		   NodeList lowerBound = (NodeList) xPath.compile(expression2).evaluate(xmlDocument, XPathConstants.NODESET);
		   periodTime=Double.parseDouble(lowerBound.item(0).getFirstChild().getNodeValue());
		   
		   String expression3 = "*/stimuliModel/stimuli[@name= '" + stimuli.replace("?type=stimuli.Sporadic","") + "']/stimulusDeviation/lowerBound/@unit";
		   NodeList unit = (NodeList) xPath.compile(expression3).evaluate(xmlDocument, XPathConstants.NODESET);
		   unitPeriod=unit.item(0).getFirstChild().getNodeValue();
		   if(unitPeriod=="ms"){
			   periodTime=periodTime*1000.0;
		   }
	   }
	   else{
		   String expression1 = "*/stimuliModel/stimuli[@name= '" + stimuli.replace("?type=stimuli.Periodic","") + "']/recurrence/@value";
		   NodeList period = (NodeList) xPath.compile(expression1).evaluate(xmlDocument, XPathConstants.NODESET);
		   periodTime=Double.parseDouble(period.item(0).getFirstChild().getNodeValue());
		   
		   String expression2 = "*/stimuliModel/stimuli[@name= '" + stimuli.replace("?type=stimuli.Periodic","") + "']/recurrence/@unit";
		   NodeList unit = (NodeList) xPath.compile(expression2).evaluate(xmlDocument, XPathConstants.NODESET);
		   unitPeriod=unit.item(0).getFirstChild().getNodeValue();

		   if(unitPeriod.contains("ms")){
			   periodTime=periodTime*1000.0;
		   }
	   }

	   return periodTime;
   }
   
   //This function prints all private&shared labels between cores
   public static void private_and_shared_labels(Document xmlDocument,XPath xPath) throws XPathExpressionException, FileNotFoundException, UnsupportedEncodingException{
	   System.out.println("Checking labels...");
	   Core c0=new Core("CORE0"),c1=new Core("CORE1"),c2=new Core("CORE2"),c3=new Core("CORE3");
	   LinkedList<Core> cores=new LinkedList<Core>();
	   cores.add(c0);cores.add(c1);cores.add(c2);cores.add(c3);
	   
	   int nLabels=10000;
	   for(int z=0;z<nLabels;z++){
		   LinkedList<String> coreLabel=new LinkedList<String>();
		   String labelName="Label_"+z;
		   String labelToQuery=labelName+"?type=sw.Label";
		   String expression = "*/swModel/runnables[runnableItems[@data='" + labelToQuery + "']]/@name";
	       NodeList runnables = (NodeList) xPath.compile(expression).evaluate(xmlDocument, XPathConstants.NODESET);
	       for (int i = 0; i < runnables.getLength(); i++) {
	           String runnableName=runnables.item(i).getFirstChild().getNodeValue()+"?type=sw.Runnable";
	           expression = "*/swModel/tasks[callGraph/graphEntries/calls[@runnable= '" + runnableName + "']]/@name";
	           NodeList tasks = (NodeList) xPath.compile(expression).evaluate(xmlDocument, XPathConstants.NODESET);
	           for (int j = 0; j < tasks.getLength(); j++) {
	        	   String taskName=tasks.item(j).getFirstChild().getNodeValue();
	        	   coreLabel.add(core_of_task(taskName, xmlDocument, xPath).replace("?type=hw.Core", ""));
	           }
	       }

	       //verify if the label is shared or private (TO DO: CLEAN CODE)
    	   int labelSize=size_of_label(labelName, xmlDocument, xPath);
           if(coreLabel.size()>1){ //more than one element could be shared or just private on different tasks
        	   boolean isShared = false;
	           for(int k=1;k<coreLabel.size() && !isShared;k++){
	        	   if(!coreLabel.get(k).equals(coreLabel.get(0))){
	        		   nShared++;
	        		   isShared=true;
	        	   }
	           }
	           if(isShared){ //count shared labels and size shared label
	        	   for(int k=0;k<coreLabel.size();k++){
	        		   for(int i=0;i<cores.size();i++){
		        		   if(cores.get(i).name.equals(coreLabel.get(k))){
		        			   cores.get(i).nSharedLabels++;
		        			   cores.get(i).sizeSharedLabels+=labelSize;
		        			   cores.get(i).labels.add(labelName);
		        		   }
	        		   }
	        	   }
	           }else if(!isShared){ //private label
	        	   for(int p=0;p<cores.size();p++){
	        		   if(cores.get(p).name.equals(coreLabel.get(0))){
	        			   cores.get(p).nPrivateLabels++;
	        			   cores.get(p).sizePrivateLabels+=labelSize;
	        			   cores.get(p).labels.add(labelName);
	        		   }
	        	   }
	           }
           }else if(coreLabel.size()==1){ //only one element, therefore is private
        	   for(int p=0;p<cores.size();p++){
        		   if(cores.get(p).name.equals(coreLabel.get(0))){
        			   cores.get(p).nPrivateLabels++;
        			   cores.get(p).sizePrivateLabels+=labelSize;  
        			   cores.get(p).labels.add(labelName);
        		   }
        	   }
           }
	   }
	   System.out.println("Total shared labels= "+nShared);
	   PrintWriter writer = new PrintWriter("label_memory_mapping", "UTF-8");

	   for(int i=0;i<cores.size();i++){
		   for(Core c: cores){
			   writer.println("CORE"+c.name);
			   for(String label:c.labels){
				   writer.println("\t"+label);
			   }
		   }
		   
		   writer.close();
		   System.out.println("**************************");
		   System.out.println("Num. private labels "+cores.get(i).name+": "+cores.get(i).nPrivateLabels+", size of private labels (in bits): "+cores.get(i).sizePrivateLabels);
		   System.out.println("Num. shared labels "+cores.get(i).name+": "+cores.get(i).nSharedLabels+", size of shared labels (in bits): "+cores.get(i).sizeSharedLabels);
	   }
   }
   
   //This function returns the size of the label "labelName"
   public static int size_of_label(String labelName, Document xmlDocument,XPath xPath) throws XPathExpressionException, FileNotFoundException, UnsupportedEncodingException{
	   int numberBits=0;
       String expression = "*/swModel/labels[@name='" + labelName + "']/size/@numberBits";
       NodeList label = (NodeList) xPath.compile(expression).evaluate(xmlDocument, XPathConstants.NODESET);
       numberBits=Integer.parseInt(label.item(0).getFirstChild().getNodeValue()); 
       return numberBits;
   }
   
   //This function returns the core of the task "taskName"
   public static String core_of_task(String taskName, Document xmlDocument,XPath xPath) throws XPathExpressionException{
	   taskName=taskName+"?type=sw.Task";
	   String result="";
       String expression = "*/mappingModel/processAllocation[@process='" + taskName + "']/@scheduler";
       NodeList nodeList = (NodeList) xPath.compile(expression).evaluate(xmlDocument, XPathConstants.NODESET);
       for (int i = 0; i < nodeList.getLength(); i++) {
           String scheduledTask=nodeList.item(i).getFirstChild().getNodeValue();
           expression = "*/mappingModel/coreAllocation[@scheduler='" + scheduledTask + "']/@core";
           NodeList cores = (NodeList) xPath.compile(expression).evaluate(xmlDocument, XPathConstants.NODESET);
           result=cores.item(0).getFirstChild().getNodeValue(); 
       }
       return result;
   }
   
   //This function returns the task of the runnable "runnableName"
   public static String task_of_runnable(String runnableName, Document xmlDocument,XPath xPath) throws XPathExpressionException{
	   runnableName=runnableName+"?type=sw.Runnable";
	   String expression = "*/swModel/tasks[callGraph/graphEntries/calls[@runnable='" + runnableName + "']]/@name";
	   NodeList nodeList = (NodeList) xPath.compile(expression).evaluate(xmlDocument, XPathConstants.NODESET);
	   String result=nodeList.item(0).getFirstChild().getNodeValue();    

	   return result;
   }
   
   
 //This function extracts the label-mapping
   public static void extractLabelMapping(Document xmlDocument,XPath xPath) throws XPathExpressionException, FileNotFoundException, UnsupportedEncodingException{
	   LinkedList<String> GRAM=new LinkedList<String>();
	   LinkedList<String> LRAM0=new LinkedList<String>();
	   LinkedList<String> LRAM1=new LinkedList<String>();
	   LinkedList<String> LRAM2=new LinkedList<String>();
	   LinkedList<String> LRAM3=new LinkedList<String>();
	   for(int i=0;i<10000;i++){
		   String labelName="Label_"+i+"?type=sw.Label";
		   String expression = "*/mappingModel/mapping[@abstractElement='" + labelName + "']/@mem";
		   NodeList nodeList = (NodeList) xPath.compile(expression).evaluate(xmlDocument, XPathConstants.NODESET);
		   String result=nodeList.item(0).getFirstChild().getNodeValue().replace("?type=hw.Memory", "");  
		   labelName=labelName.replace("?type=sw.Label", "");
		   if(result.equals("GRAM")){
			   GRAM.add(labelName);
		   }else if(result.equals("LRAM0")){
			   LRAM0.add(labelName);
		   }else if(result.equals("LRAM1")){
			   LRAM1.add(labelName);
		   }else if(result.equals("LRAM2")){
			   LRAM2.add(labelName);
		   }else if(result.equals("LRAM3")){
			   LRAM3.add(labelName); 
		   }

		   //System.out.println(result.replace("?type=hw.Memory", "")+ " a " +labelName.replace("?type=sw.Label", ""));
		   //labels.add(new Label(i,Integer.parseInt(labelAllocation)));
	   }	
	   try{
		   PrintWriter writer = new PrintWriter("label_memory_mapping", "UTF-8");
		   
		   writer.println("GRAM Labels: ");
		   for(String labelName: GRAM){
			   writer.println("\t"+labelName+" "+Integer.toString(size_of_label(labelName, xmlDocument, xPath)));
		   }
		   writer.println("LRAM0 labels: ");
		   for(String labelName: LRAM0){
			   writer.println("\t"+labelName+" "+Integer.toString(size_of_label(labelName, xmlDocument, xPath)));
		   }	   
		   writer.println("LRAM1 labels: ");
		   for(String labelName: LRAM1){
			   writer.println("\t"+labelName+" "+Integer.toString(size_of_label(labelName, xmlDocument, xPath)));
		   }	   
		   writer.println("LRAM2 labels: ");
		   for(String labelName: LRAM2){
			   writer.println("\t"+labelName+" "+Integer.toString(size_of_label(labelName, xmlDocument, xPath)));
		   }	   
		   writer.println("LRAM3 labels: ");
		   for(String labelName: LRAM3){
			   writer.println("\t"+labelName+" "+Integer.toString(size_of_label(labelName, xmlDocument, xPath)));
		   }
		   writer.close();
	   } catch (IOException e) {
			   // do something
	   }
   }

}


