#include <unistd.h>
#include <math.h>

#include "_hgr.h"
#include "shared_defs.h"

/*Wrappers*/

void hgr_init(int policy, global_policy global, sem_protocol protocol){
	log("\n");
	//system("cpufreq-set -g performance");
	//system("echo 1 > /sys/devices/system/cpu/intel_pstate/no_turbo");
} // hgr_init

void hgr_destroy(){
	log("\n");
}

struct mppa_target_param_1;
struct  mppa_target_param_1
{
  int n;
  omp_param_t params[6];
};
static void _host_fn_1(omp_param_t *_params)
{
	printf("FATAL ERROR\n");
}

int hgr_task_creator(int _taskID, tspec period,tspec rdline,int priority,int processor,int act_flag)
{
	log("\n");
	log("taskID is %d\n", _taskID);
	
	struct mppa_target_param_1 *mppa_data_1;
		/* Offloaded data structure is freed in the Runtime. */ 
	mppa_data_1 = malloc(sizeof(struct mppa_target_param_1));
	(*mppa_data_1).params[0].ptr = &_taskID;
	(*mppa_data_1).params[0].size = sizeof(int);
	(*mppa_data_1).params[0].type = 0;
	
	(*mppa_data_1).params[1].ptr = &period;
	(*mppa_data_1).params[1].size = sizeof(tspec);
	(*mppa_data_1).params[1].type = 0;
	
	(*mppa_data_1).params[2].ptr = &rdline;
	(*mppa_data_1).params[2].size = sizeof(tspec);
	(*mppa_data_1).params[2].type = 0;
	
	(*mppa_data_1).params[3].ptr = &priority;
	(*mppa_data_1).params[3].size = sizeof(int);
	(*mppa_data_1).params[3].type = 0;
	
	(*mppa_data_1).params[4].ptr = &processor;
	(*mppa_data_1).params[4].size = sizeof(int);
	(*mppa_data_1).params[4].type = 0;
	
	(*mppa_data_1).params[5].ptr = &act_flag;
	(*mppa_data_1).params[5].size = sizeof(int);
	(*mppa_data_1).params[5].type = 0;
	
	(*mppa_data_1).n = 6;
	
	GOMP_target(0, (void (*)(void *))_host_fn_1, (void *)1, (void *)mppa_data_1, 0, 65535U);
	GOMP_target_wait(0, (void *)mppa_data_1, 0);
	static index = 0;
	return index++;
}
// 
// void hgr_prepare_task(int taskID, int numNodes){
// 	unsupported();
// // 	threads[taskID]=(pthread_t *) malloc (numNodes*sizeof(pthread_t));
// }
// 
// void hgr_free_task(int taskID){
// 	unsupported();
// // 	free(threads[taskID]);
// }
// 
// int hgr_thread_create(int taskID, int nodeID, const pthread_attr_t *attr,
//                           void *(*start_routine) (void *), void *arg){
// 	unsupported();
// // 	return hgr_pthread_create(&threads[taskID][nodeID], NULL, start_routine, (void *)nodeID);
// 	return -1;
// }
// 
// int hgr_thread_join(int taskID, int nodeID){
// 	unsupported();
// // 	return hgr_pthread_join(threads[taskID][nodeID]);
// 	return -1;
// }
// 
// int hgr_pthread_join(pthread_t thread){
// 	unsupported();
// // 	return pthread_join(thread, 0);
// 	return -1;
// }
// 
// int hgr_pthread_create(pthread_t *thread, const pthread_attr_t *attr,
//                           void *(*start_routine) (void *), void *arg){
// 	unsupported();
// // 	return pthread_create(thread, NULL, start_routine, arg);
// 	return -1;
// }
// 
// void hgr_wait_for_period (int taskID){
// 	unsupported();
// // 	log("Task %d waiting for period...\n",taskID);
// // 	ptask_wait_for_period();
// }
// 
// void hgr_init_dependency(pthread_mutex_t *mutex, const pthread_mutexattr_t *attr){
// 	unsupported();
// // 	pthread_mutex_init(mutex, NULL);
// }
// 
// void hgr_destroy_dependency(pthread_mutex_t *mutex){
// 	unsupported();
// // 	pthread_mutex_destroy(mutex);
// }
// 
// void hgr_wait_dependency(pthread_mutex_t *mutex){
// 	unsupported();
// // 	pthread_mutex_lock(mutex);	
// }
// 
// void hgr_release_dependency(pthread_mutex_t *mutex){
// 	unsupported();
// // 	pthread_mutex_unlock(mutex);	
// }
// 
// void hgr_exit(){
// 	unsupported();
// // 	pthread_exit(NULL);
// }


