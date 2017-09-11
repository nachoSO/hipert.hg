//Implicit task
#include <stdlib.h>
#include <unistd.h>
#include <stdio.h>
#include "hgr.h"
#include "hgr_dependencies.h"

hgr_dependency_t Task_1ms_0_1,Task_1ms_1_2,Task_1ms_2_3,Task_1ms_3_4,Task_1ms_4_5,Task_1ms_5_6,Task_1ms_6_7,Task_1ms_7_8,Task_1ms_8_9,Task_1ms_9_10,lock_start_Task_1ms;
PREM_node_t *Task_1ms_data[11];

volatile long int var_condition_Task_1ms=0;
long double wcet_per_task[11];

void Task_1ms_pre_load_tasks(double FREQUENCY){
	int granularity=0;
 	
	
	wcet_per_task[0]=0.0;
	Task_1ms_data[0]=hgr_load_PREM_node(2688,"B",granularity,wcet_per_task[0],FREQUENCY,0);
 	
	
	wcet_per_task[1]=7.463333333333334;
	Task_1ms_data[1]=hgr_load_PREM_node(0,"B",granularity,wcet_per_task[1],FREQUENCY,1);
 	
	
	wcet_per_task[2]=2.0933333333333337;
	Task_1ms_data[2]=hgr_load_PREM_node(0,"B",granularity,wcet_per_task[2],FREQUENCY,2);
 	
	
	wcet_per_task[3]=3.5066666666666664;
	Task_1ms_data[3]=hgr_load_PREM_node(0,"B",granularity,wcet_per_task[3],FREQUENCY,3);
 	
	
	wcet_per_task[4]=3.3833333333333333;
	Task_1ms_data[4]=hgr_load_PREM_node(0,"B",granularity,wcet_per_task[4],FREQUENCY,4);
 	
	
	wcet_per_task[5]=10.35666666666667;
	Task_1ms_data[5]=hgr_load_PREM_node(0,"B",granularity,wcet_per_task[5],FREQUENCY,5);
 	
	
	wcet_per_task[6]=1.38;
	Task_1ms_data[6]=hgr_load_PREM_node(0,"B",granularity,wcet_per_task[6],FREQUENCY,6);
 	
	
	wcet_per_task[7]=4.556666666666667;
	Task_1ms_data[7]=hgr_load_PREM_node(0,"B",granularity,wcet_per_task[7],FREQUENCY,7);
 	
	
	wcet_per_task[8]=10.10666666666667;
	Task_1ms_data[8]=hgr_load_PREM_node(0,"B",granularity,wcet_per_task[8],FREQUENCY,8);
 	
	
	wcet_per_task[9]=4.543333333333334;
	Task_1ms_data[9]=hgr_load_PREM_node(0,"B",granularity,wcet_per_task[9],FREQUENCY,9);
 	
	
	wcet_per_task[10]=0.0;
	Task_1ms_data[10]=hgr_load_PREM_node(2688,"B",granularity,wcet_per_task[10],FREQUENCY,10);
	
}

void Task_1ms_create_dependency(){	
	hgr_init_dependency(&Task_1ms_0_1,NULL);
	hgr_wait_dependency(&Task_1ms_0_1);	
	hgr_init_dependency(&Task_1ms_1_2,NULL);
	hgr_wait_dependency(&Task_1ms_1_2);	
	hgr_init_dependency(&Task_1ms_2_3,NULL);
	hgr_wait_dependency(&Task_1ms_2_3);	
	hgr_init_dependency(&Task_1ms_3_4,NULL);
	hgr_wait_dependency(&Task_1ms_3_4);	
	hgr_init_dependency(&Task_1ms_4_5,NULL);
	hgr_wait_dependency(&Task_1ms_4_5);	
	hgr_init_dependency(&Task_1ms_5_6,NULL);
	hgr_wait_dependency(&Task_1ms_5_6);	
	hgr_init_dependency(&Task_1ms_6_7,NULL);
	hgr_wait_dependency(&Task_1ms_6_7);	
	hgr_init_dependency(&Task_1ms_7_8,NULL);
	hgr_wait_dependency(&Task_1ms_7_8);	
	hgr_init_dependency(&Task_1ms_8_9,NULL);
	hgr_wait_dependency(&Task_1ms_8_9);	
	hgr_init_dependency(&Task_1ms_9_10,NULL);
	hgr_wait_dependency(&Task_1ms_9_10);	
	hgr_init_dependency(&lock_start_Task_1ms,NULL);
	hgr_wait_dependency(&lock_start_Task_1ms);
}

void Task_1ms_destroy_dependency(){
	hgr_destroy_dependency(&Task_1ms_0_1);
	hgr_destroy_dependency(&Task_1ms_1_2);
	hgr_destroy_dependency(&Task_1ms_2_3);
	hgr_destroy_dependency(&Task_1ms_3_4);
	hgr_destroy_dependency(&Task_1ms_4_5);
	hgr_destroy_dependency(&Task_1ms_5_6);
	hgr_destroy_dependency(&Task_1ms_6_7);
	hgr_destroy_dependency(&Task_1ms_7_8);
	hgr_destroy_dependency(&Task_1ms_8_9);
	hgr_destroy_dependency(&Task_1ms_9_10);
	hgr_destroy_dependency(&lock_start_Task_1ms);
}

void *Task_1ms_wait_finish() {
	while(var_condition_Task_1ms!=11)
		; 
	return 0;
}

void Task_1ms_start() {
	hgr_release_dependency(&lock_start_Task_1ms);
}

void Task_1ms_finish() {
	finish_work(Task_1ms_wait_finish);
}

void *Task_1ms_Copy_In(){
	//printf("Hi im the node Task_1ms_Copy_In\n");  //
	
	pthread_mutex_trylock(&lock_start_Task_1ms);
 	volatile char *ptr_dst = (char *)malloc(Task_1ms_data[0]->data_size);
		
	hgr_IMPLICIT_memory_node(Task_1ms_data[0],ptr_dst); //Mem+Computation phase
	
	hgr_release_dependency(&Task_1ms_0_1);
	
	var_condition_Task_1ms++;
 	free(ptr_dst);
	hgr_exit();

	return 0;

}
void *Runnable_1ms_0(){
	//printf("Hi im the node Runnable_1ms_0\n");  //
	
	pthread_mutex_trylock(&lock_start_Task_1ms);
 	volatile char *ptr_dst = (char *)malloc(Task_1ms_data[1]->data_size);
		
	hgr_wait_dependency(&Task_1ms_0_1);
	hgr_IMPLICIT_execution_node(Task_1ms_data[1],ptr_dst); //Mem+Computation phase
	
	hgr_release_dependency(&Task_1ms_1_2);
	
	var_condition_Task_1ms++;
 	free(ptr_dst);
	hgr_exit();

	return 0;

}
void *Runnable_1ms_1(){
	//printf("Hi im the node Runnable_1ms_1\n");  //
	
	pthread_mutex_trylock(&lock_start_Task_1ms);
 	volatile char *ptr_dst = (char *)malloc(Task_1ms_data[2]->data_size);
		
	hgr_wait_dependency(&Task_1ms_1_2);
	hgr_IMPLICIT_execution_node(Task_1ms_data[2],ptr_dst); //Mem+Computation phase
	
	hgr_release_dependency(&Task_1ms_2_3);
	
	var_condition_Task_1ms++;
 	free(ptr_dst);
	hgr_exit();

	return 0;

}
void *Runnable_1ms_2(){
	//printf("Hi im the node Runnable_1ms_2\n");  //
	
	pthread_mutex_trylock(&lock_start_Task_1ms);
 	volatile char *ptr_dst = (char *)malloc(Task_1ms_data[3]->data_size);
		
	hgr_wait_dependency(&Task_1ms_2_3);
	hgr_IMPLICIT_execution_node(Task_1ms_data[3],ptr_dst); //Mem+Computation phase
	
	hgr_release_dependency(&Task_1ms_3_4);
	
	var_condition_Task_1ms++;
 	free(ptr_dst);
	hgr_exit();

	return 0;

}
void *Runnable_1ms_3(){
	//printf("Hi im the node Runnable_1ms_3\n");  //
	
	pthread_mutex_trylock(&lock_start_Task_1ms);
 	volatile char *ptr_dst = (char *)malloc(Task_1ms_data[4]->data_size);
		
	hgr_wait_dependency(&Task_1ms_3_4);
	hgr_IMPLICIT_execution_node(Task_1ms_data[4],ptr_dst); //Mem+Computation phase
	
	hgr_release_dependency(&Task_1ms_4_5);
	
	var_condition_Task_1ms++;
 	free(ptr_dst);
	hgr_exit();

	return 0;

}
void *Runnable_1ms_4(){
	//printf("Hi im the node Runnable_1ms_4\n");  //
	
	pthread_mutex_trylock(&lock_start_Task_1ms);
 	volatile char *ptr_dst = (char *)malloc(Task_1ms_data[5]->data_size);
		
	hgr_wait_dependency(&Task_1ms_4_5);
	hgr_IMPLICIT_execution_node(Task_1ms_data[5],ptr_dst); //Mem+Computation phase
	
	hgr_release_dependency(&Task_1ms_5_6);
	
	var_condition_Task_1ms++;
 	free(ptr_dst);
	hgr_exit();

	return 0;

}
void *Runnable_1ms_5(){
	//printf("Hi im the node Runnable_1ms_5\n");  //
	
	pthread_mutex_trylock(&lock_start_Task_1ms);
 	volatile char *ptr_dst = (char *)malloc(Task_1ms_data[6]->data_size);
		
	hgr_wait_dependency(&Task_1ms_5_6);
	hgr_IMPLICIT_execution_node(Task_1ms_data[6],ptr_dst); //Mem+Computation phase
	
	hgr_release_dependency(&Task_1ms_6_7);
	
	var_condition_Task_1ms++;
 	free(ptr_dst);
	hgr_exit();

	return 0;

}
void *Runnable_1ms_6(){
	//printf("Hi im the node Runnable_1ms_6\n");  //
	
	pthread_mutex_trylock(&lock_start_Task_1ms);
 	volatile char *ptr_dst = (char *)malloc(Task_1ms_data[7]->data_size);
		
	hgr_wait_dependency(&Task_1ms_6_7);
	hgr_IMPLICIT_execution_node(Task_1ms_data[7],ptr_dst); //Mem+Computation phase
	
	hgr_release_dependency(&Task_1ms_7_8);
	
	var_condition_Task_1ms++;
 	free(ptr_dst);
	hgr_exit();

	return 0;

}
void *Runnable_1ms_7(){
	//printf("Hi im the node Runnable_1ms_7\n");  //
	
	pthread_mutex_trylock(&lock_start_Task_1ms);
 	volatile char *ptr_dst = (char *)malloc(Task_1ms_data[8]->data_size);
		
	hgr_wait_dependency(&Task_1ms_7_8);
	hgr_IMPLICIT_execution_node(Task_1ms_data[8],ptr_dst); //Mem+Computation phase
	
	hgr_release_dependency(&Task_1ms_8_9);
	
	var_condition_Task_1ms++;
 	free(ptr_dst);
	hgr_exit();

	return 0;

}
void *Runnable_1ms_8(){
	//printf("Hi im the node Runnable_1ms_8\n");  //
	
	pthread_mutex_trylock(&lock_start_Task_1ms);
 	volatile char *ptr_dst = (char *)malloc(Task_1ms_data[9]->data_size);
		
	hgr_wait_dependency(&Task_1ms_8_9);
	hgr_IMPLICIT_execution_node(Task_1ms_data[9],ptr_dst); //Mem+Computation phase
	
	hgr_release_dependency(&Task_1ms_9_10);
	
	var_condition_Task_1ms++;
 	free(ptr_dst);
	hgr_exit();

	return 0;

}
void *Task_1ms_Copy_Out(){
	//printf("Hi im the node Task_1ms_Copy_Out\n");  //
	
	pthread_mutex_trylock(&lock_start_Task_1ms);
 	volatile char *ptr_dst = (char *)malloc(Task_1ms_data[10]->data_size);
		
	hgr_wait_dependency(&Task_1ms_9_10);
	hgr_IMPLICIT_memory_node(Task_1ms_data[10],ptr_dst); //Mem+Computation phase
	
	
	var_condition_Task_1ms++;
 	free(ptr_dst);
	hgr_exit();

	return 0;

}

void Task_1ms(){
	struct timespec tstart={0,0}, tend={0,0};
	double timeTaken;
	double FREQUENCY=(double)(mhz()/1000.0);

	printf("FREQ: %f \n",FREQUENCY);
	Task_1ms_pre_load_tasks(FREQUENCY);
	hgr_prepare_task(0,11);

	while(1){
		var_condition_Task_1ms=0;
		Task_1ms_create_dependency();

		
		hgr_thread_create(0,0, NULL, &Task_1ms_Copy_In, (void *)0);		
		hgr_thread_create(0,1, NULL, &Runnable_1ms_0, (void *)1);		
		hgr_thread_create(0,2, NULL, &Runnable_1ms_1, (void *)2);		
		hgr_thread_create(0,3, NULL, &Runnable_1ms_2, (void *)3);		
		hgr_thread_create(0,4, NULL, &Runnable_1ms_3, (void *)4);		
		hgr_thread_create(0,5, NULL, &Runnable_1ms_4, (void *)5);		
		hgr_thread_create(0,6, NULL, &Runnable_1ms_5, (void *)6);		
		hgr_thread_create(0,7, NULL, &Runnable_1ms_6, (void *)7);		
		hgr_thread_create(0,8, NULL, &Runnable_1ms_7, (void *)8);		
		hgr_thread_create(0,9, NULL, &Runnable_1ms_8, (void *)9);		
		hgr_thread_create(0,10, NULL, &Task_1ms_Copy_Out, (void *)10);  
 		clock_gettime(CLOCK_MONOTONIC, &tstart);	

		Task_1ms_start();
		Task_1ms_finish();
		
 		clock_gettime(CLOCK_MONOTONIC, &tend);
 		timeTaken=(( ((double)tend.tv_sec + 1.0e-9*tend.tv_nsec) - ((double)tstart.tv_sec + 1.0e-9*tstart.tv_nsec)))*1000;
 		printf("Total execution time TASK Task_1ms: %f miliseconds\n",timeTaken);
		
		hgr_release_dependency(&lock_start_Task_1ms);
		
  
		hgr_thread_join(0,0);  
		hgr_thread_join(0,1);  
		hgr_thread_join(0,2);  
		hgr_thread_join(0,3);  
		hgr_thread_join(0,4);  
		hgr_thread_join(0,5);  
		hgr_thread_join(0,6);  
		hgr_thread_join(0,7);  
		hgr_thread_join(0,8);  
		hgr_thread_join(0,9);  
		hgr_thread_join(0,10);		
		//hgr_free_task(0);
		Task_1ms_destroy_dependency();
		hgr_wait_for_period(0);
	}
}








