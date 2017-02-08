#include <stdlib.h>
#include <unistd.h>
#include <stdio.h>
#include "_hgr.h"
#include "hgr_dependencies.h"

hgr_dependency_t G1_1_2, G1_1_3, lock_start1;
PREM_node_t *G1_data[3];
long int var_condition1=0;
long double wcet_per_task[3];
double FREQUENCY;

void G1_pre_load_tasks(){
	log("\n");

	int granularity=1;
	wcet_per_task[0]=500.0;
	G1_data[0]=hgr_load_PREM_node(10,"MB",0,granularity,wcet_per_task[0],FREQUENCY,0);

	wcet_per_task[1]=600.0;
	G1_data[1]=hgr_load_PREM_node(10,"MB",0,granularity,wcet_per_task[1],FREQUENCY,1);

	wcet_per_task[2]=300.0;
	G1_data[2]=hgr_load_PREM_node(10,"MB",0,granularity,wcet_per_task[2],FREQUENCY,2);
	
}
// 
void G1_create_dependency(){
	hgr_init_dependency(&G1_1_2,NULL);
	hgr_wait_dependency(&G1_1_2);	
	hgr_init_dependency(&G1_1_3,NULL);
	hgr_wait_dependency(&G1_1_3);	
	hgr_init_dependency(&lock_start1,NULL);
	hgr_wait_dependency(&lock_start1);
}

void G1_destroy_dependency(){
	hgr_destroy_dependency(&G1_1_2);
	hgr_destroy_dependency(&G1_1_3);
	hgr_destroy_dependency(&lock_start1);
}

// void *G1_wait_finish(){
// 	unsupported();
// 	struct sched_param sp;
// 	memset(&sp,0,sizeof(sp));
// 	sp.sched_priority=90;
// 	sched_setscheduler(0,SCHED_FIFO,&sp);
// // 	while(var_condition1!=3); //_POL_
// 	while(var_condition1!=1);
// 	return 0;
// }

void G1_start(){
	hgr_release_dependency(&lock_start1);
}
// 
void G1_finish(){
	unsupported();
// 	pthread_t t;
// 	hgr_pthread_create(&t, NULL, &G1_wait_finish, (void *)3);	
// 	hgr_pthread_join(t);	
}

void *G1_1(void * param){
	printf("Hi im the node G1_1\n");  //1 [miet="10.381", meet="10.381", maet="10.75", mem="1", unit="mb"]
	
	hgr_wait_dependency(&lock_start1);
	printf("Hi im the node G1_1, and I go!\n");  //1 [miet="10.381", meet="10.381", maet="10.75", mem="1", unit="mb"]
// 
// 	volatile char *ptr_dst = (char *)malloc(G1_data[0]->data_size);
// 	hgr_PREM_compute_node(G1_data[0],ptr_dst); //Mem phase
// 	
	hgr_release_dependency(&G1_1_2);
	hgr_release_dependency(&G1_1_3);
// 
// 	var_condition1++;
// 	free(ptr_dst);
	hgr_exit();

	return 0;
}

void *G1_2(){
	printf("Hi im the node G1_2\n");  //2 [miet="10.98", meet="10.0", maet="10.92", mem="1", unit="mb"]
	
// 	hgr_wait_dependency(&lock_start1);
	hgr_wait_dependency(&G1_1_2);
	printf("Hi im the node G1_2, and I go!\n");  //1 [miet="10.381", meet="10.381", maet="10.75", mem="1", unit="mb"]
// 
// 	volatile char *ptr_dst = (char *)malloc(G1_data[1]->data_size);
// 
// 	hgr_PREM_compute_node(G1_data[1],ptr_dst); 
// 
// 	var_condition1++;
// 	free(ptr_dst);
	hgr_exit();

	return 0;
}

void *G1_3(){
	printf("Hi im the node G1_3\n");  //3 [miet="10.85", meet="10.53", maet="10.66", mem="1", unit="mb"]
// 	
// 	pthread_mutex_trylock(&lock_start1);
	hgr_wait_dependency(&G1_1_3);
	printf("Hi im the node G1_3, and I go!\n");  //1 [miet="10.381", meet="10.381", maet="10.75", mem="1", unit="mb"]
// 
// 	volatile char *ptr_dst = (char *)malloc(G1_data[2]->data_size);
// 
// 	hgr_PREM_compute_node(G1_data[2],ptr_dst); 
// 	
// 	var_condition1++;
// 	free(ptr_dst);
	hgr_exit();

	return 0;
}

void G1(){
	log("\n");
//  	struct timespec tstart={0,0}, tend={0,0};
//  	double timeTaken;
	FREQUENCY=(double)(mhz()/1000.0);

	printf("FREQ: %f \n",FREQUENCY);
	G1_pre_load_tasks();
	hgr_prepare_task(0,3);
	while(1) {

		var_condition1=0;

		G1_create_dependency();

		hgr_thread_create(0,0, NULL, &G1_1, (void *)0);  
		hgr_thread_create(0,1, NULL, &G1_2, (void *)1);  
		hgr_thread_create(0,2, NULL, &G1_3, (void *)2);  

// 		clock_gettime(CLOCK_MONOTONIC, &tstart);	

		G1_start();
		G1_finish();

// 		clock_gettime(CLOCK_MONOTONIC, &tend);
// 		timeTaken=(( ((double)tend.tv_sec + 1.0e-9*tend.tv_nsec) - ((double)tstart.tv_sec + 1.0e-9*tstart.tv_nsec)))*1000;
// 		printf("PREM | Total execution time: %f miliseconds\n",timeTaken);
// 
		hgr_thread_join(0,0);
		hgr_thread_join(0,1);  
		hgr_thread_join(0,2);

		hgr_free_task(0);
		G1_destroy_dependency();
		
// 		hgr_wait_for_period(0);
		break; // TODO to remove
	}
	hgr_free_task(0);
}


