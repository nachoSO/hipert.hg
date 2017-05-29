package hipert.hg.amaltheaParser;

import java.util.LinkedList;

public class Core {
	LinkedList<Task> tasks=new LinkedList<Task>();
	String name;
	int nPrivateLabels;
	int nSharedLabels;
	int sizePrivateLabels;
	int sizeSharedLabels;
	LinkedList<String> labels=new LinkedList<String>();
	
	public Core(String name){
		this.name=name;
		this.nPrivateLabels=0;
		this.nSharedLabels=0;
		this.sizePrivateLabels=0;
		this.sizeSharedLabels=0;
	}
	
	public void addTask(Task t){
		t.executionTime=t.processInstructions();
		tasks.add(t);
	}
	
	//This function compute the core response time 
	public void RTAcore(){
		for(int z=0;z<tasks.size();z++){
			if(tasks.get(z).preemptionType.equals("preemptive")){
				responseTimePreemptiveTask(z);
			}
			else{ 
				responseTimeCooperativeTask(z);
			}
		} 
	}
	
	//This function calculate the response time of a preemptive task
	public void responseTimePreemptiveTask(int taskNum){
		double response_n_accum=0;
		for(int i=0;i<tasks.get(taskNum).runnables.size();i++){
			double response=0;
			response_n_accum+=tasks.get(taskNum).runnables.get(i).executionTime;
			double response_n=response_n_accum;
			do{
				response=response_n;
				response_n=response_n_accum;
				for(int j=0;j<taskNum;j++){
					response_n+=myCeil(response, tasks.get(j).period)*tasks.get(j).executionTime;
				}
			}while(response_n>response);
			tasks.get(taskNum).runnables.get(i).responseTime=response_n;
			tasks.get(taskNum).responseTime=response_n;
		}
		//check deadline miss
		for(int j=0;j<tasks.size();j++){
			if(tasks.get(j).responseTime>tasks.get(j).deadline)
				RTADeadlineMiss(taskNum);
		}
	}

	//This function calculate the response time of a deadline miss task
	public void RTADeadlineMiss(int taskNum){
		double executionTimeRunnableAcum=0.0;
		System.out.println(tasks.get(taskNum).responseTime);

		for(int i=0;i<tasks.get(taskNum).runnables.size();i++){
			executionTimeRunnableAcum+=tasks.get(taskNum).runnables.get(i).executionTime;
			int K= (int)myCeil(busy_period(taskNum), tasks.get(taskNum).period); //K
			double jobR=0.0,response=0.00;
			for (int k=1; k<=K; k++) {
				jobR = job_finishDM(taskNum,k,executionTimeRunnableAcum,i) - (k-1)*tasks.get(taskNum).period;
				response = (response>jobR ? response : jobR);
			} 
			tasks.get(taskNum).runnables.get(i).responseTime=response;
			tasks.get(taskNum).responseTime=response;
		}

	}
	
	//This function returns the job finish computation of deadline miss tasks
	public double job_finishDM(int taskIndex, int k, double executionTimeRunnableAcum,int jobIndex){
		double init, finish, finishNew;
		
		init = finishNew = (k-1)*tasks.get(taskIndex).executionTime+executionTimeRunnableAcum;
		do {
			finish = finishNew;
			finishNew = init;
			for(int i=0; i<taskIndex; i++)
				finishNew += myCeil(finish, tasks.get(i).period) * tasks.get(i).executionTime;

		} while (finishNew > finish);
		return finishNew;
	}

	
	//This function calculate the response time of a cooperative task
	public void responseTimeCooperativeTask(int taskNum){
		double executionTimeRunnableAcum=0.0;

		for(int i=0;i<tasks.get(taskNum).runnables.size();i++){
			executionTimeRunnableAcum+=tasks.get(taskNum).runnables.get(i).executionTime;
			//int num= (int)myCeil(busy_period(taskNum,executionTimeRunnableAcum), tasks.get(taskNum).period);
			int K= (int)myCeil(busy_period(taskNum), tasks.get(taskNum).period); //K

			double jobR=0.0,response=0.00;
			for (int k=1; k<=K; k++) {
				jobR = job_finish(taskNum,k,executionTimeRunnableAcum,i) - (k-1)*tasks.get(taskNum).period;
				response = (response>jobR ? response : jobR);
			} 

			tasks.get(taskNum).runnables.get(i).responseTime=response;
			tasks.get(taskNum).responseTime=response;
		}
	}	
	
	/*
	 * The blocking factor Bi to be considered for each task i is
	 * equal to the length of longest subjob (instead of the longest
	 * task) among those with lower priority:
	 */
	double blocking(int taskNum)
	{
		double maxB = 0.0,maxAux=0.0;
		if(taskNum==tasks.size()-1){
			maxB=0.0;
		}else{
			for(int i=taskNum+1;i<tasks.size();i++){
				for(int j=0;j<tasks.get(i).runnables.size();j++){
					maxAux=tasks.get(i).runnables.get(j).executionTime;
					maxB=(maxAux>maxB ? maxAux : maxB);
				}
			}
		}
		return maxB;
	}
	
	//This function returns the "taskNum" busy period
	public double busy_period(int taskNum)
	{
		double init, length, K;
		init = blocking(taskNum);
		K = init + tasks.get(taskNum).executionTime;
		do {
			length = K;
			K = init;
			for(int i=0; i<=taskNum; ++i) {
				K += tasks.get(i).executionTime * myCeil(length,tasks.get(taskNum).period) ;
			}
		} while (K >length);
		return K;
	}
	

	/* (PTS+FPP) The finishing time fi,k can be computed by
	*  summing to the start time si,k the computation time of job Ti,k,
	*  and the interference of the tasks that can preempt Ti,k (those
	*  with priority higher than Oi). That is,
	*/
	double job_finish(int taskIndex, int k, double executionTimeRunnableAcum,int jobIndex)
	{
		double init, finish, finishNew, start;
		start = job_start(taskIndex, k,executionTimeRunnableAcum, jobIndex);
		
		init = finishNew = start + tasks.get(taskIndex).runnables.get(jobIndex).executionTime;
		do {
			finish = finishNew;
			finishNew = init;
			for(int i=0; i<taskIndex; ++i){
				if (tasks.get(i).preemptionType.equals("preemptive"))
					finishNew += (myCeil(finish, tasks.get(i).period) - (myFloor(start, tasks.get(i).period) + 1.0))*tasks.get(i).executionTime;
			
			}
		} while (finishNew > finish);
		return finishNew;
	}

	/*  The start time si,k of the last subjob can be computed considering 
	 *  the blocking time Bi, the computation time of the preceding (k-1) jobs, 
	 *	the subjobs of Ti,k preceding the last one 
	 *	and the interference of the tasks with priority higher than Pi.		
	 */
	double job_start(int taskIndex, int k, double executionTimeRunnableAcum,int jobIndex)
	{
		double init, start, startNew;
		//error
		init = startNew = blocking(taskIndex) + ((k-1)*tasks.get(taskIndex).executionTime) + (executionTimeRunnableAcum-tasks.get(taskIndex).runnables.get(jobIndex).executionTime);
		do {
			start = startNew;
			startNew = init;
			for (int i=0; i<taskIndex; ++i) {
				startNew += (myFloor(start, tasks.get(i).period)+1.0)*tasks.get(i).executionTime;
			}
		} while (startNew>start);

		return startNew;
	}
	
	public double myCeil(double a, double b){
		return (double)(int)Math.ceil(a/b);
	}
	
	public double myFloor(double a, double b){
		return (double)(int)Math.floor(a/b);
	}
	
	//This function computes the TASK response time
	public void RTAcore_per_task(){
		System.out.println("Starting RTA...");

		//response time task n
		for(int z=0;z<tasks.size();z++){
			double response=0;
			double response_n=tasks.get(z).processInstructions();
			do{
				response=response_n;
				response_n=tasks.get(z).processInstructions();
				for(int j=0;j<z;j++){
					int ceil=(int)Math.ceil(response / tasks.get(j).period);
					response_n+=(double)ceil*tasks.get(j).processInstructions();
				}
			}while(response_n>response);
			tasks.get(z).responseTime=response_n;
		}
		
		System.out.println("RTA Finished");
	}

}
