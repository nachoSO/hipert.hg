#include <stdlib.h>
#include <unistd.h>
#include <stdio.h>
#include "hgr.h"
#include "hgr_dependencies.h"

hgr_dependency_t G4_1_2,G4_1_3,G4_2_4,G4_3_4,G4_5_3,lock_start_G4;
PREM_node_t *G4_data[5];
volatile long int var_condition_G4=0;
long double wcet_per_task[5];

void G4_pre_load_tasks(double FREQUENCY){
	int granularity=1;
 	
	
	wcet_per_task[0]=94.48;
	G4_data[0]=hgr_load_PREM_node(500,"B",0,granularity,wcet_per_task[0],FREQUENCY,0);
 	
	
	wcet_per_task[1]=58.21;
	G4_data[1]=hgr_load_PREM_node(500,"B",0,granularity,wcet_per_task[1],FREQUENCY,1);
 	
	
	wcet_per_task[2]=14.42;
	G4_data[2]=hgr_load_PREM_node(500,"B",0,granularity,wcet_per_task[2],FREQUENCY,2);
 	
	
	wcet_per_task[3]=35.38;
	G4_data[3]=hgr_load_PREM_node(500,"B",0,granularity,wcet_per_task[3],FREQUENCY,3);
 	
	
	wcet_per_task[4]=12.84;
	G4_data[4]=hgr_load_PREM_node(500,"B",0,granularity,wcet_per_task[4],FREQUENCY,4);
	
}

void G4_create_dependency(){	
	hgr_init_dependency(&G4_1_2,NULL);
	hgr_wait_dependency(&G4_1_2);	
	hgr_init_dependency(&G4_1_3,NULL);
	hgr_wait_dependency(&G4_1_3);	
	hgr_init_dependency(&G4_2_4,NULL);
	hgr_wait_dependency(&G4_2_4);	
	hgr_init_dependency(&G4_3_4,NULL);
	hgr_wait_dependency(&G4_3_4);	
	hgr_init_dependency(&G4_5_3,NULL);
	hgr_wait_dependency(&G4_5_3);	
	hgr_init_dependency(&lock_start_G4,NULL);
	hgr_wait_dependency(&lock_start_G4);
}

void G4_destroy_dependency(){
	hgr_destroy_dependency(&G4_1_2);
	hgr_destroy_dependency(&G4_1_3);
	hgr_destroy_dependency(&G4_2_4);
	hgr_destroy_dependency(&G4_3_4);
	hgr_destroy_dependency(&G4_5_3);
	hgr_destroy_dependency(&lock_start_G4);
}

void *G4_wait_finish() {
	while(var_condition_G4!=5)
		; 
	return 0;
}

void G4_start() {
	hgr_release_dependency(&lock_start_G4);
}

void G4_finish() {
	finish_work(G4_wait_finish);
}

void *G4_1(){
	//printf("Hi im the node G4_1\n");  //1 [miet="94.48", meet="94.48", maet="94.48", mem="500", unit="kb"]
	
	pthread_mutex_trylock(&lock_start_G4);
 	volatile char *ptr_dst = (char *)malloc(G1_data[0]->data_size);
		
	hgr_PREM_compute_node(G4_data[0],ptr_dst); //Mem+Computation phase
	hgr_release_dependency(&G4_1_2);
	hgr_release_dependency(&G4_1_3);
	
	var_condition_G4++;
 	free(ptr_dst);
	hgr_exit();

	return 0;

}
void *G4_2(){
	//printf("Hi im the node G4_2\n");  //2 [miet="58.21", meet="58.21", maet="58.21", mem="500", unit="b"]
	
	pthread_mutex_trylock(&lock_start_G4);
 	volatile char *ptr_dst = (char *)malloc(G1_data[0]->data_size);
		
	hgr_wait_dependency(&G4_1_2);
	hgr_PREM_compute_node(G4_data[1],ptr_dst); //Mem+Computation phase
	hgr_release_dependency(&G4_2_4);
	
	var_condition_G4++;
 	free(ptr_dst);
	hgr_exit();

	return 0;

}
void *G4_3(){
	//printf("Hi im the node G4_3\n");  //3 [miet="14.42", meet="14.42", maet="14.42", mem="500", unit="kb"]
	
	pthread_mutex_trylock(&lock_start_G4);
 	volatile char *ptr_dst = (char *)malloc(G1_data[0]->data_size);
		
	hgr_wait_dependency(&G4_1_3);
	hgr_wait_dependency(&G4_5_3);
	hgr_PREM_compute_node(G4_data[2],ptr_dst); //Mem+Computation phase
	hgr_release_dependency(&G4_3_4);
	
	var_condition_G4++;
 	free(ptr_dst);
	hgr_exit();

	return 0;

}
void *G4_4(){
	//printf("Hi im the node G4_4\n");  //4 [miet="35.38", meet="35.38", maet="35.38", mem="500", unit="b"]
	
	pthread_mutex_trylock(&lock_start_G4);
 	volatile char *ptr_dst = (char *)malloc(G1_data[0]->data_size);
		
	hgr_wait_dependency(&G4_2_4);
	hgr_wait_dependency(&G4_3_4);
	hgr_PREM_compute_node(G4_data[3],ptr_dst); //Mem+Computation phase
	
	var_condition_G4++;
 	free(ptr_dst);
	hgr_exit();

	return 0;

}
void *G4_5(){
	//printf("Hi im the node G4_5\n");  //5 [miet="12.84", meet="12.84", maet="12.84", mem="500", unit="kb"]
	
	pthread_mutex_trylock(&lock_start_G4);
 	volatile char *ptr_dst = (char *)malloc(G1_data[0]->data_size);
		
	hgr_PREM_compute_node(G4_data[4],ptr_dst); //Mem+Computation phase
	hgr_release_dependency(&G4_5_3);
	
	var_condition_G4++;
 	free(ptr_dst);
	hgr_exit();

	return 0;

}

void G4(){
	struct timespec tstart={0,0}, tend={0,0};
	double timeTaken;
	double FREQUENCY=(double)(mhz()/1000.0);

	printf("FREQ: %f \n",FREQUENCY);
	G4_pre_load_tasks(FREQUENCY);
	hgr_prepare_task(0,3);

	while(1){
		var_condition_G4=0;
		G4_create_dependency();

		
		hgr_thread_create(0,0, NULL, &G4_1, (void *)0);		
		hgr_thread_create(0,1, NULL, &G4_2, (void *)1);		
		hgr_thread_create(0,2, NULL, &G4_3, (void *)2);		
		hgr_thread_create(0,3, NULL, &G4_4, (void *)3);		
		hgr_thread_create(0,4, NULL, &G4_5, (void *)4);  
 		clock_gettime(CLOCK_MONOTONIC, &tstart);	

		G4_start();
		G4_finish();
		
 		clock_gettime(CLOCK_MONOTONIC, &tend);
 		timeTaken=(( ((double)tend.tv_sec + 1.0e-9*tend.tv_nsec) - ((double)tstart.tv_sec + 1.0e-9*tstart.tv_nsec)))*1000;
 		printf("Total execution time TASK 0: %f miliseconds\n",timeTaken);
		
		hgr_release_dependency(&lock_start_G4);
		
  
		hgr_thread_join(0,0);  
		hgr_thread_join(0,1);  
		hgr_thread_join(0,2);  
		hgr_thread_join(0,3);  
		hgr_thread_join(0,4);		
		//hgr_free_task(0);
		G4_destroy_dependency();
		hgr_wait_for_period(0);
	}
}


