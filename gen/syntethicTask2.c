#include <stdlib.h>
#include <unistd.h>
#include <stdio.h>
#include "ptask.h"
#include "pmutex.h"
#include "hgr.h"

pthread_mutex_t G2_1_2,G2_1_3,G2_2_4,G2_3_4,G2_5_4;
long int G2_cycle_per_task[5];
memory_access_t *G2_data[5];


void G2_pre_load_tasks(){
	double wcet_per_task[5];
 	
	wcet_per_task[0]=10.65;
	G2_data[0]=hgr_load_mem(100,"B",0,0);
 	
	wcet_per_task[1]=10.91;
	G2_data[1]=hgr_load_mem(100,"B",0,0);
 	
	wcet_per_task[2]=10.82;
	G2_data[2]=hgr_load_mem(100,"B",0,0);
 	
	wcet_per_task[3]=10.12;
	G2_data[3]=hgr_load_mem(100,"B",0,0);
 	
	wcet_per_task[4]=10.86;
	G2_data[4]=hgr_load_mem(100,"B",0,0);
		
	hgr_load_computation(wcet_per_task,5,G2_cycle_per_task);
}

void G2_mutexCreate(){	
	hgr_init_dependency(&G2_1_2,NULL);
	hgr_wait_dependency(&G2_1_2);	
	hgr_init_dependency(&G2_1_3,NULL);
	hgr_wait_dependency(&G2_1_3);	
	hgr_init_dependency(&G2_2_4,NULL);
	hgr_wait_dependency(&G2_2_4);	
	hgr_init_dependency(&G2_3_4,NULL);
	hgr_wait_dependency(&G2_3_4);	
	hgr_init_dependency(&G2_5_4,NULL);
	hgr_wait_dependency(&G2_5_4);	
}

void G2_mutexDestroy(){
	hgr_destroy_dependency(&G2_1_2);
	hgr_destroy_dependency(&G2_1_3);
	hgr_destroy_dependency(&G2_2_4);
	hgr_destroy_dependency(&G2_3_4);
	hgr_destroy_dependency(&G2_5_4);
}

void *G2_1(){
	printf("Hi im the node G2_1\n");  //1 [miet="10.48", meet="10.65", maet="10.63", mem="100", unit="b"]
	
	hgr_prem_memphase(G2_data[0]); //Mem phase

	
	hgr_compute_task(G2_cycle_per_task[0]); //Computation phase
	hgr_release_dependency(&G2_1_2);
	hgr_release_dependency(&G2_1_3);
	hgr_exit();
}
void *G2_2(){
	printf("Hi im the node G2_2\n");  //2 [miet="10.21", meet="10.91", maet="10.34", mem="100", unit="b"]
	
	hgr_prem_memphase(G2_data[1]); //Mem phase

	
	hgr_wait_dependency(&G2_1_2);
	hgr_compute_task(G2_cycle_per_task[1]); //Computation phase
	hgr_release_dependency(&G2_2_4);
	hgr_exit();
}
void *G2_3(){
	printf("Hi im the node G2_3\n");  //3 [miet="10.42", meet="10.82", maet="10.66", mem="100", unit="b"]
	
	hgr_prem_memphase(G2_data[2]); //Mem phase

	
	hgr_wait_dependency(&G2_1_3);
	hgr_compute_task(G2_cycle_per_task[2]); //Computation phase
	hgr_release_dependency(&G2_3_4);
	hgr_exit();
}
void *G2_4(){
	printf("Hi im the node G2_4\n");  //4 [miet="10.38", meet="10.12", maet="10.89", mem="100", unit="b"]
	
	hgr_prem_memphase(G2_data[3]); //Mem phase

	
	hgr_wait_dependency(&G2_2_4);
	hgr_wait_dependency(&G2_3_4);
	hgr_wait_dependency(&G2_5_4);
	hgr_compute_task(G2_cycle_per_task[3]); //Computation phase
	hgr_exit();
}
void *G2_5(){
	printf("Hi im the node G2_5\n");  //5 [miet="10.84", meet="10.86", maet="10.21", mem="100", unit="b"]
	
	hgr_prem_memphase(G2_data[4]); //Mem phase

	
	hgr_compute_task(G2_cycle_per_task[4]); //Computation phase
	hgr_release_dependency(&G2_5_4);
	hgr_exit();
}

void G2(){
	G2_pre_load_tasks();

	while(1){
		pthread_t threads[5];
		G2_mutexCreate();
  
		hgr_pthread_create(&threads[0], NULL, &G2_1, (void *)0);  
		hgr_pthread_create(&threads[1], NULL, &G2_2, (void *)1);  
		hgr_pthread_create(&threads[2], NULL, &G2_3, (void *)2);  
		hgr_pthread_create(&threads[3], NULL, &G2_4, (void *)3);  
		hgr_pthread_create(&threads[4], NULL, &G2_5, (void *)4);  
		hgr_pthread_join(threads[0]);  
		hgr_pthread_join(threads[1]);  
		hgr_pthread_join(threads[2]);  
		hgr_pthread_join(threads[3]);  
		hgr_pthread_join(threads[4]);		
		
		G2_mutexDestroy();
		hgr_wait_for_period();
	}
}


