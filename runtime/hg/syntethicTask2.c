#include <stdlib.h>
#include <unistd.h>
#include <stdio.h>
#include "_hgr.h"

// pthread_mutex_t G6_1_2,G6_1_3,lock_start2;
// SPARSE_node_t *G6_data[3];
// long double wcet_per_task[3];
// int var_condition2=0;
// unsigned long int * pointer[3]; //points to the memory access stride
// double FREQUENCY;
// 
// void G6_pre_load_tasks(){
// 	int stride=1;
// 	int granularity=1;
// 	wcet_per_task[0]=500.0;
// 	//create task_t struct (data_size, data_granularity, task_t type (PREM,sparse), granularity, stride)
// 	G6_data[0]=hgr_load_SPARSE_node(10,"MB",1,granularity,stride,wcet_per_task[0],FREQUENCY,0);
//  	//pointer[0]=stride_generator(stride,G6_data[0]->data_size/1); //seq
//  	pointer[0]=random_generator(G6_data[0]->data_size/1,G6_data[0]->data_size/1); //rand
// 
// 	wcet_per_task[1]=600.0;
// 	//create task_t struct (data_size, data_granularity, task_t type (PREM,sparse), granularity, stride)
// 	G6_data[1]=hgr_load_SPARSE_node(10,"MB",1,granularity,stride,wcet_per_task[1],FREQUENCY,1);
//  	//pointer[1]=stride_generator(stride,G6_data[1]->data_size/1); //seq
//  	pointer[1]=random_generator(G6_data[1]->data_size/1,G6_data[1]->data_size/1); //rand
// 
// 	wcet_per_task[2]=300.0;
// 	//create task_t struct (data_size, data_granularity, task_t type (PREM,sparse), granularity, stride)
// 	G6_data[2]=hgr_load_SPARSE_node(10,"MB",1,granularity,stride,wcet_per_task[2],FREQUENCY,2);
//  	//pointer[2]=stride_generator(stride,G6_data[2]->data_size/1); //seq
//  	pointer[2]=random_generator(G6_data[2]->data_size/1,G6_data[2]->data_size/1); //rand
//  	
// 	//random_generator(phi,phi);
// }
// 
// void G6_create_dependency(){	
// 	hgr_init_dependency(&G6_1_2,NULL);
// 	hgr_wait_dependency(&G6_1_2);	
// 	hgr_init_dependency(&G6_1_3,NULL);
// 	hgr_wait_dependency(&G6_1_3);	
// 	hgr_init_dependency(&lock_start2,NULL);
// 	hgr_wait_dependency(&lock_start2);
// }
// 
// void G6_destroy_dependency(){
// 	hgr_destroy_dependency(&G6_1_2);
// 	hgr_destroy_dependency(&G6_1_3);
// }
// 
// void *G6_wait_finish(){
// 	struct sched_param sp;
// 	memset ( &sp,0,sizeof(sp));
// 	sp.sched_priority=90;
// 	sched_setscheduler(0,SCHED_FIFO,&sp);
// 	while(var_condition2!=3);
// 	return 0;
// }
// 
// void G6_start(){
// 	hgr_release_dependency(&lock_start2);
// }
// 
// void G6_finish(){
// 	pthread_t t;
// 	hgr_pthread_create(&t, NULL, &G6_wait_finish, (void *)3);	
// 	hgr_pthread_join(t);	
// }
// 
// void *G6_1(){
// 	//printf("Hi im the node G6_1\n");  //1 [miet="10.381", meet="10.381", maet="10.75", mem="1", unit="mb"]
// 	pthread_mutex_trylock(&lock_start2);
// 
// 	hgr_SPARSE_compute_node(G6_data[0],pointer[0]); //Mem & Computation phase
// 	
// 	hgr_release_dependency(&G6_1_2);
// 	hgr_release_dependency(&G6_1_3);
// 	var_condition2++;
// 	hgr_exit();
// 	return 0;
// }
// void *G6_2(){
// 	//printf("Hi im the node G6_2\n");  //2 [miet="10.98", meet="10.0", maet="10.92", mem="1", unit="mb"]
// 	pthread_mutex_trylock(&lock_start2);
// 	hgr_wait_dependency(&G6_1_2); 
// 
// 	hgr_SPARSE_compute_node(G6_data[1],pointer[1]); //Mem & Computation phase
// 	var_condition2++;
// 	hgr_exit();
// 	return 0;
// }
// void *G6_3(){
// 	//printf("Hi im the node G6_3\n");  //3 [miet="10.85", meet="10.53", maet="10.66", mem="1", unit="mb"]
// 	pthread_mutex_trylock(&lock_start2);
// 	hgr_wait_dependency(&G6_1_3); 
// 	
// 	hgr_SPARSE_compute_node(G6_data[2],pointer[2]); //Mem & Computation phase
// 	var_condition2++;
// 	hgr_exit();
// 	return 0;
// }

void G6(){
	log("\n");
// 	struct timespec tstart={0,0}, tend={0,0};
// 	double timeTaken;
// 	FREQUENCY=2.553000;
// 	FREQUENCY=(double)(mhz()/1000.0);
// 	printf("FREQ: %f \n",FREQUENCY);		
// 	G6_pre_load_tasks();
// 	static int count = 100;
// 	
// 	hgr_prepare_task(1,3);
// 	while(--count){
// 
// 		var_condition2=0;
// 
// 		G6_create_dependency();
// 
// 		hgr_thread_create(1,0, NULL, &G6_1, (void *)0);  
// 		hgr_thread_create(1,1, NULL, &G6_2, (void *)1);  
// 		hgr_thread_create(1,2, NULL, &G6_3, (void *)2);   
// 
// 		clock_gettime(CLOCK_MONOTONIC, &tstart);	
// 
// 		G6_start();
// 		G6_finish();
// 
// 		clock_gettime(CLOCK_MONOTONIC, &tend);
// 		timeTaken=(( ((double)tend.tv_sec + 1.0e-9*tend.tv_nsec) - ((double)tstart.tv_sec + 1.0e-9*tstart.tv_nsec)))*1000;
// 		printf("SPARSE | Total execution time: %f miliseconds\n",timeTaken);
// 
// 		hgr_thread_join(1,0);  
// 		hgr_thread_join(1,1);  
// 		hgr_thread_join(1,2);
// 		
// 		G6_destroy_dependency();
// 		hgr_wait_for_period(1);
// 	}
// 	hgr_free_task();
}


