#include <stdlib.h>
#include <unistd.h>
#include <stdio.h>
#include "hgr.h"
#include "hgr_dependencies.h"

hgr_dependency_t G1_1_2,G1_1_3,G1_2_4,G1_3_4,lock_start_G1;
PREM_node_t *G1_data[4];
volatile long int var_condition_G1=0;
long double wcet_per_task[4];

void G1_pre_load_tasks(double FREQUENCY){
	int granularity=1;
 	
	
	wcet_per_task[0]=10.381;
	G1_data[0]=hgr_load_PREM_node(1,"B",granularity,wcet_per_task[0],FREQUENCY,0);
 	
	
	wcet_per_task[1]=10.98;
	G1_data[1]=hgr_load_PREM_node(1,"B",granularity,wcet_per_task[1],FREQUENCY,1);
 	
	
	wcet_per_task[2]=10.85;
	G1_data[2]=hgr_load_PREM_node(1,"B",granularity,wcet_per_task[2],FREQUENCY,2);
 	
	
	wcet_per_task[3]=10.85;
	G1_data[3]=hgr_load_PREM_node(1,"B",granularity,wcet_per_task[3],FREQUENCY,3);
	
}

void G1_create_dependency(){	
	hgr_init_dependency(&G1_1_2,NULL);
	hgr_wait_dependency(&G1_1_2);	
	hgr_init_dependency(&G1_1_3,NULL);
	hgr_wait_dependency(&G1_1_3);	
	hgr_init_dependency(&G1_2_4,NULL);
	hgr_wait_dependency(&G1_2_4);	
	hgr_init_dependency(&G1_3_4,NULL);
	hgr_wait_dependency(&G1_3_4);	
	hgr_init_dependency(&lock_start_G1,NULL);
	hgr_wait_dependency(&lock_start_G1);
}

void G1_destroy_dependency(){
	hgr_destroy_dependency(&G1_1_2);
	hgr_destroy_dependency(&G1_1_3);
	hgr_destroy_dependency(&G1_2_4);
	hgr_destroy_dependency(&G1_3_4);
	hgr_destroy_dependency(&lock_start_G1);
}

void *G1_wait_finish() {
	while(var_condition_G1!=4)
		; 
	return 0;
}

void G1_start() {
	hgr_release_dependency(&lock_start_G1);
}

void G1_finish() {
	finish_work(G1_wait_finish);
}

void *G1_1(){
	//printf("Hi im the node G1_1\n");  //1 [miet="10.381", meet="10.381", maet="10.75", mem="1", unit="mb"]
	
	pthread_mutex_trylock(&lock_start_G1);
 	volatile char *ptr_dst = (char *)malloc(G1_data[0]->data_size);
		
	hgr_PREM_compute_node(G1_data[0],ptr_dst); //Mem+Computation phase
	hgr_release_dependency(&G1_1_2);
	hgr_release_dependency(&G1_1_3);
	
	var_condition_G1++;
 	free(ptr_dst);
	hgr_exit();

	return 0;

}
void *G1_2(){
	//printf("Hi im the node G1_2\n");  //2 [miet="10.98", meet="10.0", maet="10.92", mem="1", unit="mb"]
	
	pthread_mutex_trylock(&lock_start_G1);
 	volatile char *ptr_dst = (char *)malloc(G1_data[1]->data_size);
		
	hgr_wait_dependency(&G1_1_2);
	hgr_PREM_compute_node(G1_data[1],ptr_dst); //Mem+Computation phase
	hgr_release_dependency(&G1_2_4);
	
	var_condition_G1++;
 	free(ptr_dst);
	hgr_exit();

	return 0;

}
void *G1_3(){
	//printf("Hi im the node G1_3\n");  //3 [miet="10.85", meet="10.53", maet="10.66", mem="1", unit="mb"]
	
	pthread_mutex_trylock(&lock_start_G1);
 	volatile char *ptr_dst = (char *)malloc(G1_data[2]->data_size);
		
	hgr_wait_dependency(&G1_1_3);
	hgr_PREM_compute_node(G1_data[2],ptr_dst); //Mem+Computation phase
	hgr_release_dependency(&G1_3_4);
	
	var_condition_G1++;
 	free(ptr_dst);
	hgr_exit();

	return 0;

}
void *G1_4(){
	//printf("Hi im the node G1_4\n");  //4 [miet="10.85", meet="10.53", maet="10.66", mem="1", unit="mb"]
	
	pthread_mutex_trylock(&lock_start_G1);
 	volatile char *ptr_dst = (char *)malloc(G1_data[3]->data_size);
		
	hgr_wait_dependency(&G1_2_4);
	hgr_wait_dependency(&G1_3_4);
	hgr_PREM_compute_node(G1_data[3],ptr_dst); //Mem+Computation phase
	
	var_condition_G1++;
 	free(ptr_dst);
	hgr_exit();

	return 0;

}

void G1(){
	struct timespec tstart={0,0}, tend={0,0};
	double timeTaken;
	double FREQUENCY=(double)(mhz()/1000.0);

	printf("FREQ: %f \n",FREQUENCY);
	G1_pre_load_tasks(FREQUENCY);
	hgr_prepare_task(0,4);

	while(1){
		var_condition_G1=0;
		G1_create_dependency();

		
		hgr_thread_create(0,0, NULL, &G1_1, (void *)0);		
		hgr_thread_create(0,1, NULL, &G1_2, (void *)1);		
		hgr_thread_create(0,2, NULL, &G1_3, (void *)2);		
		hgr_thread_create(0,3, NULL, &G1_4, (void *)3);  
 		clock_gettime(CLOCK_MONOTONIC, &tstart);	

		G1_start();
		G1_finish();
		
 		clock_gettime(CLOCK_MONOTONIC, &tend);
 		timeTaken=(( ((double)tend.tv_sec + 1.0e-9*tend.tv_nsec) - ((double)tstart.tv_sec + 1.0e-9*tstart.tv_nsec)))*1000;
 		printf("Total execution time TASK 0: %f miliseconds\n",timeTaken);
		
		hgr_release_dependency(&lock_start_G1);
		
  
		hgr_thread_join(0,0);  
		hgr_thread_join(0,1);  
		hgr_thread_join(0,2);  
		hgr_thread_join(0,3);		
		//hgr_free_task(0);
		G1_destroy_dependency();
		hgr_wait_for_period(0);
	}
}


