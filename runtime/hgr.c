#include <unistd.h>
#include <stdlib.h>
#include <stdio.h>
#include "hgr.h"

pthread_mutex_t premMutex;

int hgr_task_creator(tspec period,tspec rdline,int priority,int processor,int act_flag,void* name_task){
	tpars tparam = TASK_SPEC_DFL;
	tparam = (tpars) {
		.period=period, 
		.rdline=rdline,
		.priority = priority, .processor = processor,
		.act_flag = act_flag,
		.measure_flag=1
	};
	int index = ptask_create_param(name_task, &tparam);;
	return index;
}

long int calc_data_size(long int size,char * data_granularity){
	long int data_size=size;
	if(strcmp(data_granularity,"KB")==1)
		data_size=data_size*1024;
	else if(strcmp(data_granularity,"MB")==1)
		data_size=data_size*1024*1024;
	else if(strcmp(data_granularity,"GB")==1)
		data_size=data_size*1024*1024*1024;
	return data_size;
}

/*This function returns the time taken to execute a 'for' for MAXCYCLES*/
double hgr_bench_computer(){
	double time[nExperiments];
	int j=0,i=0;
	struct timespec tstart={0,0}, tend={0,0};
	double avgExecutionTime=0;
	
	for(j=0;j<nExperiments;j++){
		clock_gettime(CLOCK_MONOTONIC, &tstart);
		for (i=0;i<MAXCYCLES;i++);
		clock_gettime(CLOCK_MONOTONIC, &tend);
		time[j]=(( ((double)tend.tv_sec + 1.0e-9*tend.tv_nsec) - ((double)tstart.tv_sec + 1.0e-9*tstart.tv_nsec)))*1000;
		avgExecutionTime+=time[j];
	}
	avgExecutionTime=avgExecutionTime/nExperiments;
	//printf("AVG Execution time - For %d cycles: %.5f miliseconds\n",MAXCYCLES,avgExecutionTime);
	return avgExecutionTime;
}

//This function calculates the num. of computation cycles
void hgr_load_computation(double wcet_per_task[],int nElements,long int cycle_per_task[]){
	int i;
	double avg=hgr_bench_computer();

	for(i=0;i<nElements;i++){
		cycle_per_task[i]=(long int)(((double)wcet_per_task[i]*(double)MAXCYCLES)/(double)avg);
	}
}

memory_access_t * hgr_load_mem(long int data_size, char *data_granularity, int type,int step){

	memory_access_t *mem_data=(memory_access_t *) malloc (sizeof(memory_access_t));
	(*mem_data).access_type = type; 
	(*mem_data).step = step; 
	(*mem_data).data_size = calc_data_size(data_size,data_granularity);
	strcpy((*mem_data).data_granularity,data_granularity);
	if(step>0 && step<=8){
		(*mem_data).char_data_ptr   = (char *)malloc(((*mem_data).data_size)/step);
	}else if(step>=9 && step<=16){
		(*mem_data).int_data_ptr    = (int *)malloc(((*mem_data).data_size)/step);
	}else if(step>=17 && step<=32){
		(*mem_data).long_data_ptr   = (long *)malloc(((*mem_data).data_size)/step);
	}else if(step>=33 && step<=64){
		(*mem_data).double_data_ptr = (double *)malloc(((*mem_data).data_size)/step);
	}else{	
		(*mem_data).char_data_ptr   = (char *)malloc((*mem_data).data_size); //PREM
	}
	
	return mem_data;
}

//PREM: Memory phase
void hgr_prem_memphase(memory_access_t *data){
	int i;
	
	hgr_wait_dependency(&premMutex);
	for(i=0; i<(*data).data_size; i++){
		volatile char a = (*data).char_data_ptr[i];
	}
	hgr_release_dependency(&premMutex);
}


//PREM: Computation phase
void hgr_compute_task (long int ncycles){
	//ncycles is calculated based on the tame taken to execute the benchComputer function
	//struct timespec tstart={0,0}, tend={0,0};
	long int i=0;
	//clock_gettime(CLOCK_MONOTONIC, &tstart);
	for(i=0;i<ncycles;i++);
	//clock_gettime(CLOCK_MONOTONIC, &tend);
	//printf("Execution time %.5f miliseconds\n",
	//	(( ((double)tend.tv_sec + 1.0e-9*tend.tv_nsec) - ((double)tstart.tv_sec + 1.0e-9*tstart.tv_nsec)))*1000);
}


//SPARSE: Memory & Computation phase
 void hgr_compute_sparse_task (memory_access_t *data, long int ncycles){

	long int i,j,k;
	int step=(*data).step;
	long int phi=(*data).data_size/step;
	for(i=0;i<phi;i++){
		//memory phase
		if(step>0 && step<=8){
			volatile char a   = (*data).char_data_ptr[i];
		}else if(step>=9 && step<=16){
			volatile int a    = (*data).int_data_ptr[i];
		}else if(step>=17 && step<=32){
			volatile long a   = (*data).long_data_ptr[i];
		}else if(step>=33 && step<=64){
			volatile double a = (*data).double_data_ptr[i];
		}
		//execution phase
		for(j=0;j<(ncycles/phi);j++);
	}
}

int hgr_pthread_join(pthread_t thread){
	pthread_join(thread, 0);
}

int hgr_pthread_create(pthread_t *thread, const pthread_attr_t *attr,
                          void *(*start_routine) (void *), void *arg){
	pthread_create(thread, NULL, start_routine, arg);
}

void  hgr_ptask_init(int policy, global_policy global, sem_protocol protocol){
	ptask_init(policy, global, protocol);
}

void hgr_exit(){
	pthread_exit(NULL);
}

void hgr_wait_for_period (){
	ptask_wait_for_period();
}

void hgr_init_dependency(pthread_mutex_t *mutex, const pthread_mutexattr_t *attr){
	pthread_mutex_init(mutex, NULL);
}

void hgr_destroy_dependency(pthread_mutex_t *mutex){
	pthread_mutex_destroy(mutex);
}

void hgr_wait_dependency(pthread_mutex_t *mutex){
	pthread_mutex_lock(mutex);	
}

void hgr_release_dependency(pthread_mutex_t *mutex){
	pthread_mutex_unlock(mutex);	
}

