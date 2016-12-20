#include <stdlib.h>
#include <unistd.h>
#include <stdio.h>
#include "ptask.h"
#include "pmutex.h"
#include "hgr.h"

pthread_mutex_t G1_1_2,G1_1_3;
long int G1_cycle_per_task[3];
memory_access_t *G1_data[3];


void G1_pre_load_tasks(){
	double wcet_per_task[3];
 	
	wcet_per_task[0]=10.41;
	G1_data[0]=hgr_load_mem(100,"KB",1,8);
 	
	wcet_per_task[1]=10.0;
	G1_data[1]=hgr_load_mem(100,"B",1,8);
 	
	wcet_per_task[2]=10.53;
	G1_data[2]=hgr_load_mem(100,"B",1,8);
		
	hgr_load_computation(wcet_per_task,3,G1_cycle_per_task);
}

void G1_mutexCreate(){	
	hgr_init_dependency(&G1_1_2,NULL);
	hgr_wait_dependency(&G1_1_2);	
	hgr_init_dependency(&G1_1_3,NULL);
	hgr_wait_dependency(&G1_1_3);	
}

void G1_mutexDestroy(){
	hgr_destroy_dependency(&G1_1_2);
	hgr_destroy_dependency(&G1_1_3);
}

void *G1_1(){
	printf("Hi im the node G1_1\n");  //1 [miet="10.381", meet="10.41", maet="10.75", mem="100", unit="kb"]

	
	hgr_compute_sparse_task(G1_data[0],G1_cycle_per_task[0]); //Mem & Computation phase
	
	hgr_release_dependency(&G1_1_2); //release dependency
	hgr_release_dependency(&G1_1_3); //release dependency
	hgr_exit();
}
void *G1_2(){
	printf("Hi im the node G1_2\n");  //2 [miet="10.98", meet="10.00", maet="10.92", mem="100", unit="b"]

	hgr_wait_dependency(&G1_1_2); //wait dependency
	
	hgr_compute_sparse_task(G1_data[1],G1_cycle_per_task[1]); //Mem & Computation phase
	
	hgr_exit();
}
void *G1_3(){
	printf("Hi im the node G1_3\n");  //3 [miet="10.85", meet="10.53", maet="10.66", mem="100", unit="b"]

	hgr_wait_dependency(&G1_1_3); //wait dependency
	
	hgr_compute_sparse_task(G1_data[2],G1_cycle_per_task[2]); //Mem & Computation phase
	
	hgr_exit();
}

void G1(){
	G1_pre_load_tasks();

	while(1){
		pthread_t threads[3];
		G1_mutexCreate();
  
		hgr_pthread_create(&threads[0], NULL, &G1_1, (void *)0);  
		hgr_pthread_create(&threads[1], NULL, &G1_2, (void *)1);  
		hgr_pthread_create(&threads[2], NULL, &G1_3, (void *)2);  
		hgr_pthread_join(threads[0]);  
		hgr_pthread_join(threads[1]);  
		hgr_pthread_join(threads[2]);		
		
		G1_mutexDestroy();
		hgr_wait_for_period();
	}
}


