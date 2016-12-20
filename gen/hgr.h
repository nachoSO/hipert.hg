#ifndef hgr_H
#define hgr_H
#include <stdio.h>
#include <ptask.h>
#include <string.h>
#include "pmutex.h"

#define MAXCYCLES 10000
#define nExperiments 10000
#define prem 0
#define sparse

extern pthread_mutex_t premMutex;

static char * rt_task_name[2] = {
    "G1",
    "G2",
};

typedef struct 
{
	int access_type; // 0 = PREM, 1 = scatter access 
	int step;
	long int data_size;
	char data_granularity[2];
	char *prem_data_ptr;
} memory_access_t;

int hgr_task_creator(tspec period,tspec rdline,int priority,int processor,int act_flag,void* name_task);

void hgr_wait_for_period ();

void hgr_init_dependency(pthread_mutex_t *mutex, const pthread_mutexattr_t *attr);

void hgr_destroy_dependency(pthread_mutex_t *mutex);

void hgr_wait_dependency(pthread_mutex_t *mutex);

void hgr_release_dependency(pthread_mutex_t *mutex);

long int calc_data_size(long int size,char * data_granularity);

/*This function returns the time taken to execute a 'for' for MAXCYCLES*/
double hgr_bench_computer();

//This function calculates the num. of computation cycles
void hgr_load_computation(double wcet_per_task[],int nElements,long int cycle_per_task[]);

memory_access_t * hgr_load_mem(long int data_size, char *data_granularity, int type, int step);

//PREM: Memory phase
void hgr_prem_memphase(memory_access_t *data);
  
//PREM: Computation phase
void hgr_compute_task (long int ncycles);

//SPARSE: Memory & Computation phase
void hgr_compute_random_task (memory_access_t *data,long int ncycles);

int hgr_join(pthread_t thread);

int hgr_pthread_create(pthread_t *thread, const pthread_attr_t *attr, void *(*start_routine) (void *), void *arg);

void hgr_ptask_init(int policy, global_policy global, sem_protocol protocol);

#endif
