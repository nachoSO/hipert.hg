//Implicit task
#include <stdlib.h>
#include <unistd.h>
#include <stdio.h>
#include "hgr.h"
#include "hgr_dependencies.h"

hgr_dependency_t Angle_Sync_0_1,Angle_Sync_1_2,Angle_Sync_2_3,Angle_Sync_3_4,Angle_Sync_4_5,Angle_Sync_5_6,Angle_Sync_6_7,Angle_Sync_7_8,Angle_Sync_8_9,Angle_Sync_9_10,Angle_Sync_10_11,lock_start_Angle_Sync;
PREM_node_t *Angle_Sync_data[12];

volatile long int var_condition_Angle_Sync=0;
long double wcet_per_task[12];

void Angle_Sync_pre_load_tasks(double FREQUENCY){
	int granularity=0;
 	
	
	wcet_per_task[0]=0.0;
	Angle_Sync_data[0]=hgr_load_PREM_node(7704,"B",granularity,wcet_per_task[0],FREQUENCY,0);
 	
	
	wcet_per_task[1]=1.9400000000000008;
	Angle_Sync_data[1]=hgr_load_PREM_node(0,"B",granularity,wcet_per_task[1],FREQUENCY,1);
 	
	
	wcet_per_task[2]=13.75666666666667;
	Angle_Sync_data[2]=hgr_load_PREM_node(0,"B",granularity,wcet_per_task[2],FREQUENCY,2);
 	
	
	wcet_per_task[3]=1.5066666666666666;
	Angle_Sync_data[3]=hgr_load_PREM_node(0,"B",granularity,wcet_per_task[3],FREQUENCY,3);
 	
	
	wcet_per_task[4]=4.4099999999999975;
	Angle_Sync_data[4]=hgr_load_PREM_node(0,"B",granularity,wcet_per_task[4],FREQUENCY,4);
 	
	
	wcet_per_task[5]=2.6799999999999993;
	Angle_Sync_data[5]=hgr_load_PREM_node(0,"B",granularity,wcet_per_task[5],FREQUENCY,5);
 	
	
	wcet_per_task[6]=5.559999999999998;
	Angle_Sync_data[6]=hgr_load_PREM_node(0,"B",granularity,wcet_per_task[6],FREQUENCY,6);
 	
	
	wcet_per_task[7]=2.633333333333334;
	Angle_Sync_data[7]=hgr_load_PREM_node(0,"B",granularity,wcet_per_task[7],FREQUENCY,7);
 	
	
	wcet_per_task[8]=14.596666666666671;
	Angle_Sync_data[8]=hgr_load_PREM_node(0,"B",granularity,wcet_per_task[8],FREQUENCY,8);
 	
	
	wcet_per_task[9]=4.236666666666665;
	Angle_Sync_data[9]=hgr_load_PREM_node(0,"B",granularity,wcet_per_task[9],FREQUENCY,9);
 	
	
	wcet_per_task[10]=2.276666666666667;
	Angle_Sync_data[10]=hgr_load_PREM_node(0,"B",granularity,wcet_per_task[10],FREQUENCY,10);
 	
	
	wcet_per_task[11]=0.0;
	Angle_Sync_data[11]=hgr_load_PREM_node(7704,"B",granularity,wcet_per_task[11],FREQUENCY,11);
	
}

void Angle_Sync_create_dependency(){	
	hgr_init_dependency(&Angle_Sync_0_1,NULL);
	hgr_wait_dependency(&Angle_Sync_0_1);	
	hgr_init_dependency(&Angle_Sync_1_2,NULL);
	hgr_wait_dependency(&Angle_Sync_1_2);	
	hgr_init_dependency(&Angle_Sync_2_3,NULL);
	hgr_wait_dependency(&Angle_Sync_2_3);	
	hgr_init_dependency(&Angle_Sync_3_4,NULL);
	hgr_wait_dependency(&Angle_Sync_3_4);	
	hgr_init_dependency(&Angle_Sync_4_5,NULL);
	hgr_wait_dependency(&Angle_Sync_4_5);	
	hgr_init_dependency(&Angle_Sync_5_6,NULL);
	hgr_wait_dependency(&Angle_Sync_5_6);	
	hgr_init_dependency(&Angle_Sync_6_7,NULL);
	hgr_wait_dependency(&Angle_Sync_6_7);	
	hgr_init_dependency(&Angle_Sync_7_8,NULL);
	hgr_wait_dependency(&Angle_Sync_7_8);	
	hgr_init_dependency(&Angle_Sync_8_9,NULL);
	hgr_wait_dependency(&Angle_Sync_8_9);	
	hgr_init_dependency(&Angle_Sync_9_10,NULL);
	hgr_wait_dependency(&Angle_Sync_9_10);	
	hgr_init_dependency(&Angle_Sync_10_11,NULL);
	hgr_wait_dependency(&Angle_Sync_10_11);	
	hgr_init_dependency(&lock_start_Angle_Sync,NULL);
	hgr_wait_dependency(&lock_start_Angle_Sync);
}

void Angle_Sync_destroy_dependency(){
	hgr_destroy_dependency(&Angle_Sync_0_1);
	hgr_destroy_dependency(&Angle_Sync_1_2);
	hgr_destroy_dependency(&Angle_Sync_2_3);
	hgr_destroy_dependency(&Angle_Sync_3_4);
	hgr_destroy_dependency(&Angle_Sync_4_5);
	hgr_destroy_dependency(&Angle_Sync_5_6);
	hgr_destroy_dependency(&Angle_Sync_6_7);
	hgr_destroy_dependency(&Angle_Sync_7_8);
	hgr_destroy_dependency(&Angle_Sync_8_9);
	hgr_destroy_dependency(&Angle_Sync_9_10);
	hgr_destroy_dependency(&Angle_Sync_10_11);
	hgr_destroy_dependency(&lock_start_Angle_Sync);
}

void *Angle_Sync_wait_finish() {
	while(var_condition_Angle_Sync!=12)
		; 
	return 0;
}

void Angle_Sync_start() {
	hgr_release_dependency(&lock_start_Angle_Sync);
}

void Angle_Sync_finish() {
	finish_work(Angle_Sync_wait_finish);
}

void *Angle_Sync_Copy_In(){
	//printf("Hi im the node Angle_Sync_Copy_In\n");  //
	
	pthread_mutex_trylock(&lock_start_Angle_Sync);
 	volatile char *ptr_dst = (char *)malloc(Angle_Sync_data[0]->data_size);
		
	hgr_IMPLICIT_memory_node(Angle_Sync_data[0],ptr_dst); //Mem+Computation phase
	
	hgr_release_dependency(&Angle_Sync_0_1);
	
	var_condition_Angle_Sync++;
 	free(ptr_dst);
	hgr_exit();

	return 0;

}
void *Runnable_6660us_0(){
	//printf("Hi im the node Runnable_6660us_0\n");  //
	
	pthread_mutex_trylock(&lock_start_Angle_Sync);
 	volatile char *ptr_dst = (char *)malloc(Angle_Sync_data[1]->data_size);
		
	hgr_wait_dependency(&Angle_Sync_0_1);
	hgr_IMPLICIT_execution_node(Angle_Sync_data[1],ptr_dst); //Mem+Computation phase
	
	hgr_release_dependency(&Angle_Sync_1_2);
	
	var_condition_Angle_Sync++;
 	free(ptr_dst);
	hgr_exit();

	return 0;

}
void *Runnable_6660us_1(){
	//printf("Hi im the node Runnable_6660us_1\n");  //
	
	pthread_mutex_trylock(&lock_start_Angle_Sync);
 	volatile char *ptr_dst = (char *)malloc(Angle_Sync_data[2]->data_size);
		
	hgr_wait_dependency(&Angle_Sync_1_2);
	hgr_IMPLICIT_execution_node(Angle_Sync_data[2],ptr_dst); //Mem+Computation phase
	
	hgr_release_dependency(&Angle_Sync_2_3);
	
	var_condition_Angle_Sync++;
 	free(ptr_dst);
	hgr_exit();

	return 0;

}
void *Runnable_6660us_2(){
	//printf("Hi im the node Runnable_6660us_2\n");  //
	
	pthread_mutex_trylock(&lock_start_Angle_Sync);
 	volatile char *ptr_dst = (char *)malloc(Angle_Sync_data[3]->data_size);
		
	hgr_wait_dependency(&Angle_Sync_2_3);
	hgr_IMPLICIT_execution_node(Angle_Sync_data[3],ptr_dst); //Mem+Computation phase
	
	hgr_release_dependency(&Angle_Sync_3_4);
	
	var_condition_Angle_Sync++;
 	free(ptr_dst);
	hgr_exit();

	return 0;

}
void *Runnable_6660us_3(){
	//printf("Hi im the node Runnable_6660us_3\n");  //
	
	pthread_mutex_trylock(&lock_start_Angle_Sync);
 	volatile char *ptr_dst = (char *)malloc(Angle_Sync_data[4]->data_size);
		
	hgr_wait_dependency(&Angle_Sync_3_4);
	hgr_IMPLICIT_execution_node(Angle_Sync_data[4],ptr_dst); //Mem+Computation phase
	
	hgr_release_dependency(&Angle_Sync_4_5);
	
	var_condition_Angle_Sync++;
 	free(ptr_dst);
	hgr_exit();

	return 0;

}
void *Runnable_6660us_4(){
	//printf("Hi im the node Runnable_6660us_4\n");  //
	
	pthread_mutex_trylock(&lock_start_Angle_Sync);
 	volatile char *ptr_dst = (char *)malloc(Angle_Sync_data[5]->data_size);
		
	hgr_wait_dependency(&Angle_Sync_4_5);
	hgr_IMPLICIT_execution_node(Angle_Sync_data[5],ptr_dst); //Mem+Computation phase
	
	hgr_release_dependency(&Angle_Sync_5_6);
	
	var_condition_Angle_Sync++;
 	free(ptr_dst);
	hgr_exit();

	return 0;

}
void *Runnable_6660us_5(){
	//printf("Hi im the node Runnable_6660us_5\n");  //
	
	pthread_mutex_trylock(&lock_start_Angle_Sync);
 	volatile char *ptr_dst = (char *)malloc(Angle_Sync_data[6]->data_size);
		
	hgr_wait_dependency(&Angle_Sync_5_6);
	hgr_IMPLICIT_execution_node(Angle_Sync_data[6],ptr_dst); //Mem+Computation phase
	
	hgr_release_dependency(&Angle_Sync_6_7);
	
	var_condition_Angle_Sync++;
 	free(ptr_dst);
	hgr_exit();

	return 0;

}
void *Runnable_6660us_6(){
	//printf("Hi im the node Runnable_6660us_6\n");  //
	
	pthread_mutex_trylock(&lock_start_Angle_Sync);
 	volatile char *ptr_dst = (char *)malloc(Angle_Sync_data[7]->data_size);
		
	hgr_wait_dependency(&Angle_Sync_6_7);
	hgr_IMPLICIT_execution_node(Angle_Sync_data[7],ptr_dst); //Mem+Computation phase
	
	hgr_release_dependency(&Angle_Sync_7_8);
	
	var_condition_Angle_Sync++;
 	free(ptr_dst);
	hgr_exit();

	return 0;

}
void *Runnable_6660us_7(){
	//printf("Hi im the node Runnable_6660us_7\n");  //
	
	pthread_mutex_trylock(&lock_start_Angle_Sync);
 	volatile char *ptr_dst = (char *)malloc(Angle_Sync_data[8]->data_size);
		
	hgr_wait_dependency(&Angle_Sync_7_8);
	hgr_IMPLICIT_execution_node(Angle_Sync_data[8],ptr_dst); //Mem+Computation phase
	
	hgr_release_dependency(&Angle_Sync_8_9);
	
	var_condition_Angle_Sync++;
 	free(ptr_dst);
	hgr_exit();

	return 0;

}
void *Runnable_6660us_8(){
	//printf("Hi im the node Runnable_6660us_8\n");  //
	
	pthread_mutex_trylock(&lock_start_Angle_Sync);
 	volatile char *ptr_dst = (char *)malloc(Angle_Sync_data[9]->data_size);
		
	hgr_wait_dependency(&Angle_Sync_8_9);
	hgr_IMPLICIT_execution_node(Angle_Sync_data[9],ptr_dst); //Mem+Computation phase
	
	hgr_release_dependency(&Angle_Sync_9_10);
	
	var_condition_Angle_Sync++;
 	free(ptr_dst);
	hgr_exit();

	return 0;

}
void *Runnable_6660us_9(){
	//printf("Hi im the node Runnable_6660us_9\n");  //
	
	pthread_mutex_trylock(&lock_start_Angle_Sync);
 	volatile char *ptr_dst = (char *)malloc(Angle_Sync_data[10]->data_size);
		
	hgr_wait_dependency(&Angle_Sync_9_10);
	hgr_IMPLICIT_execution_node(Angle_Sync_data[10],ptr_dst); //Mem+Computation phase
	
	hgr_release_dependency(&Angle_Sync_10_11);
	
	var_condition_Angle_Sync++;
 	free(ptr_dst);
	hgr_exit();

	return 0;

}
void *Angle_Sync_Copy_Out(){
	//printf("Hi im the node Angle_Sync_Copy_Out\n");  //
	
	pthread_mutex_trylock(&lock_start_Angle_Sync);
 	volatile char *ptr_dst = (char *)malloc(Angle_Sync_data[11]->data_size);
		
	hgr_wait_dependency(&Angle_Sync_10_11);
	hgr_IMPLICIT_memory_node(Angle_Sync_data[11],ptr_dst); //Mem+Computation phase
	
	
	var_condition_Angle_Sync++;
 	free(ptr_dst);
	hgr_exit();

	return 0;

}

void Angle_Sync(){
	struct timespec tstart={0,0}, tend={0,0};
	double timeTaken;
	double FREQUENCY=(double)(mhz()/1000.0);

	printf("FREQ: %f \n",FREQUENCY);
	Angle_Sync_pre_load_tasks(FREQUENCY);
	hgr_prepare_task(1,12);

	while(1){
		var_condition_Angle_Sync=0;
		Angle_Sync_create_dependency();

		
		hgr_thread_create(1,0, NULL, &Angle_Sync_Copy_In, (void *)0);		
		hgr_thread_create(1,1, NULL, &Runnable_6660us_0, (void *)1);		
		hgr_thread_create(1,2, NULL, &Runnable_6660us_1, (void *)2);		
		hgr_thread_create(1,3, NULL, &Runnable_6660us_2, (void *)3);		
		hgr_thread_create(1,4, NULL, &Runnable_6660us_3, (void *)4);		
		hgr_thread_create(1,5, NULL, &Runnable_6660us_4, (void *)5);		
		hgr_thread_create(1,6, NULL, &Runnable_6660us_5, (void *)6);		
		hgr_thread_create(1,7, NULL, &Runnable_6660us_6, (void *)7);		
		hgr_thread_create(1,8, NULL, &Runnable_6660us_7, (void *)8);		
		hgr_thread_create(1,9, NULL, &Runnable_6660us_8, (void *)9);		
		hgr_thread_create(1,10, NULL, &Runnable_6660us_9, (void *)10);		
		hgr_thread_create(1,11, NULL, &Angle_Sync_Copy_Out, (void *)11);  
 		clock_gettime(CLOCK_MONOTONIC, &tstart);	

		Angle_Sync_start();
		Angle_Sync_finish();
		
 		clock_gettime(CLOCK_MONOTONIC, &tend);
 		timeTaken=(( ((double)tend.tv_sec + 1.0e-9*tend.tv_nsec) - ((double)tstart.tv_sec + 1.0e-9*tstart.tv_nsec)))*1000;
 		printf("Total execution time TASK Angle_Sync: %f miliseconds\n",timeTaken);
		
		hgr_release_dependency(&lock_start_Angle_Sync);
		
  
		hgr_thread_join(1,0);  
		hgr_thread_join(1,1);  
		hgr_thread_join(1,2);  
		hgr_thread_join(1,3);  
		hgr_thread_join(1,4);  
		hgr_thread_join(1,5);  
		hgr_thread_join(1,6);  
		hgr_thread_join(1,7);  
		hgr_thread_join(1,8);  
		hgr_thread_join(1,9);  
		hgr_thread_join(1,10);  
		hgr_thread_join(1,11);		
		//hgr_free_task(0);
		Angle_Sync_destroy_dependency();
		hgr_wait_for_period(1);
	}
}








