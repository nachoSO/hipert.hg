#ifndef hgr_H
#define hgr_H
#include <stdio.h>
#include <ptask.h>
#include <string.h>
// #include "pmutex.h"
#include <stdint.h>
#include <stdlib.h>
#include <math.h>
#include <errno.h>

#define log printf
#define DIV_ROUND_CLOSEST(n, d) ((((n) < 0) ^ ((d) < 0)) ? (((n) - (d)/2)/(d)) : (((n) + (d)/2)/(d)))

// extern pthread_mutex_t premMutex;
// pthread_t ** threads;

typedef struct 
{
	char data_granularity[2];
	int granularity;
	int nodeID;
	int num_copies;
	long int data_size;
	long double wcet;
	unsigned long int wcet_per_cycle;
	unsigned long int wcet_cycles;
	long int *data_size_pointer;
	volatile char *char_data_ptr;
} PREM_node_t;

typedef struct 
{
	char data_granularity[2];
	int granularity;
	int stride;
	int nodeID;
	long int data_size;
	long double wcet;
	unsigned long int phi;
	unsigned long int wcet_cycles;
	volatile char *char_data_ptr;     //1B
	volatile int  *int_data_ptr;      //4B
	volatile double *double_data_ptr;     //8B
	volatile long double *long_double_data_ptr; //16B
} SPARSE_node_t;

unsigned long int * random_generator(long int limit, long int size);

unsigned long int * stride_generator(int stride, long int size);

long int calc_data_size(long int size,char * data_granularity);

int mhz();

long double hgr_PREM_compute_node_clock(PREM_node_t *node);

//This function calculates the num. of computation cycles
void hgr_load_computation(double wcet_per_task[],int nElements,long int cycle_per_task[]);

//This function loads all the information into the structure
PREM_node_t * hgr_load_PREM_node(long int data_size, char *data_granularity, int type,int granularity,long double wcet,double FREQUENCY,int nodeID); 
SPARSE_node_t * hgr_load_SPARSE_node(long int data_size, char *data_granularity, int type,int granularity,int stride,long double wcet,double FREQUENCY,int nodeID); 

//PREM: Memory & Computation phase
void hgr_PREM_compute_node (PREM_node_t *node, void *ptr_dst);

//SPARSE: Memory & Computation phase
void hgr_SPARSE_compute_node (SPARSE_node_t *node,unsigned long int * pointer);
void hgr_mem_ex(SPARSE_node_t *node,unsigned long int * pointer);

/******************* Wrappers ***************************/

int hgr_task_creator(int taskID, tspec period,tspec rdline,int priority,int processor,int act_flag,void* name_task);

void hgr_wait_for_period(int taskID);

void hgr_prepare_task(int taskID,int numNodes);

int hgr_thread_create(int taskID, int nodeID, const pthread_attr_t *attr, void *(*start_routine) (void *), void *arg);

int hgr_thread_join(int taskID, int numNode);

//int hgr_pthread_join(pthread_t thread);

//int hgr_pthread_create(pthread_t *thread, const pthread_attr_t *attr, void *(*start_routine) (void *), void *arg);

void hgr_ptask_init(int policy, global_policy global, sem_protocol protocol);

void hgr_exit();

#endif


