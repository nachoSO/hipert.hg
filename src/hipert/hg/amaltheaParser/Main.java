/**
 * Purpose: Waters Challenge
 *
 * @author Unimore
 * @version 1.0 25/05/16
 */

package hipert.hg.amaltheaParser;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.Collections;
import java.util.LinkedList;

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

public class Main {
   static int nShared=0; //num of shared labels
   public static void main(String[] args) throws ParserConfigurationException, SAXException, IOException, XPathExpressionException{

	   FileInputStream file = new FileInputStream(new File("./amalthea/ChallengeModel.amxmi"));

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
	   
	   //this function balance the utilization	
	   increaseWCET(core0,core1,core2,core3);
	   
	   //calculate response time of all core tasks
	   System.out.println("Computing RTA");
	   core0.RTAcore(); 
	   core1.RTAcore(); 
	   core2.RTAcore(); 
	   core3.RTAcore(); 
	   
	   LinkedList<Core> cores=new LinkedList<Core>();
	   cores.add(core0);
	   cores.add(core1);
	   cores.add(core2);
	   cores.add(core3);

	   //private_and_shared_labels(xmlDocument,xPath);

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
   
   //This function prints all private&shared labelss between cores
   public static void private_and_shared_labels(Document xmlDocument,XPath xPath) throws XPathExpressionException, FileNotFoundException, UnsupportedEncodingException{
	   System.out.println("Checking labels...");
	   int nLabels=10000;
	   Core c0=new Core("CORE0"),c1=new Core("CORE1"),c2=new Core("CORE2"),c3=new Core("CORE3");
	   LinkedList<Core> cores=new LinkedList<Core>();
	   cores.add(c0);cores.add(c1);cores.add(c2);cores.add(c3);
 
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
		        		   }
	        		   }
	        	   }
	           }else if(!isShared){ //private label
	        	   for(int p=0;p<cores.size();p++){
	        		   if(cores.get(p).name.equals(coreLabel.get(0))){
	        			   cores.get(p).nPrivateLabels++;
	        			   cores.get(p).sizePrivateLabels+=labelSize;
	        		   }
	        	   }
	           }
           }else if(coreLabel.size()==1){ //only one elemento, therefore is private
        	   for(int p=0;p<cores.size();p++){
        		   if(cores.get(p).name.equals(coreLabel.get(0))){
        			   cores.get(p).nPrivateLabels++;
        			   cores.get(p).sizePrivateLabels+=labelSize;   
        		   }
        	   }
           }
	   }
	   System.out.println("Total shared labels= "+nShared);
	   for(int i=0;i<cores.size();i++){
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
   
}


