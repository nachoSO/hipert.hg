#include <unistd.h>
#include "_hgr.h"
#include <math.h>

int mhz(){
	log("\n");
// 	FILE *fp;
// 	char path[1035];
// 
// 	/* Open the command for reading. */
// 	fp = popen(LMPATH, "r");
// 	if (fp == NULL) {
// 	printf("Failed to run command\n" );
// 	exit(1);
// 	}
// 
// 	while (fgets(path, sizeof(path)-1, fp) != NULL);
// 
// 	pclose(fp);
// 	int res=(int) strtol(path, (char **)NULL, 10);
// 
// 	return res;
	return -1;
}

char* cmd_system(const char* command){
	log("\n");
//     char* result = "";
//     FILE *fpRead;
//     fpRead = popen(command, "r");
//     char buf[1024];
//     memset(buf,'\0',sizeof(buf));
//     while(fgets(buf,1024-1,fpRead)!=NULL)
//     {
//         result = buf;
//     }
//     if(fpRead!=NULL)
//        pclose(fpRead);
// 
//     return result;
	return "";
}

int cache_size()
{
	unsupported();
// 	int cache_size=0;
// 	int L1=atoi(cmd_system("cat /sys/devices/system/cpu/cpu0/cache/index1/size | sed 's/\K//'"));
// 	int L2=atoi(cmd_system("cat /sys/devices/system/cpu/cpu0/cache/index2/size | sed 's/\K//'"));
// 	int L3=atoi(cmd_system("cat /sys/devices/system/cpu/cpu0/cache/index3/size | sed 's/\K//'"));
// 
// 	//printf("L3 %d | l2 %d | l1 %d\n",L3,L2,L1);
// 	if(L3!=0)
// 		cache_size=L3*1024;
// 	else if(L2!=0)
// 		cache_size=L2*1024;
// 	else if(L1!=0)
// 		cache_size=L1*1024;
// 
// 	return cache_size;
	return -1;
}

void dropCaches(){
	unsupported();
// 	FILE *fp;
// 	fp = fopen("/proc/sys/vm/drop_caches", "w");
// 	if (fp == NULL) {
// 	    printf("error %d: %s\n", errno, strerror(errno));
// 	    // error handling, exit or return
// 	}
// 	fprintf(fp, "3"); 
// 	fclose(fp);
}

/*Wrappers*/

void hgr_init(int policy, global_policy global, sem_protocol protocol){
	unsupported();
	//system("cpufreq-set -g performance");
	//system("echo 1 > /sys/devices/system/cpu/intel_pstate/no_turbo");

// 	threads=(pthread_t *) malloc (TOTAL_TASK*sizeof(pthread_t));

// 	hgr_init_dependency(&premMutex, NULL);
// 	ptask_init(policy, global, protocol);

}

void hgr_destroy(){
	unsupported();
// 	hgr_destroy_dependency(&premMutex);
}

int taskID;

int hgr_task_creator(int _taskID, tspec period,tspec rdline,int priority,int processor,int act_flag,void* name_task){
	unsupported();
// 	taskID=_taskID;
// 	tpars tparam = TASK_SPEC_DFL;
// 	tparam = (tpars) {
// 		.period=period, 
// 		.rdline=rdline,
// 		.priority = priority, .processor = processor,
// 		.act_flag = act_flag,
// 		.measure_flag=1
// 	};
// 	int index = ptask_create_param(name_task, &tparam);;
// 	return index;
	return -1;
}

void hgr_prepare_task(int taskID, int numNodes){
	unsupported();
// 	threads[taskID]=(pthread_t *) malloc (numNodes*sizeof(pthread_t));
}

void hgr_free_task(int taskID){
	unsupported();
// 	free(threads[taskID]);
}

int hgr_thread_create(int taskID, int nodeID, const pthread_attr_t *attr,
                          void *(*start_routine) (void *), void *arg){
	unsupported();
// 	return hgr_pthread_create(&threads[taskID][nodeID], NULL, start_routine, (void *)nodeID);
	return -1;
}

int hgr_thread_join(int taskID, int nodeID){
	unsupported();
// 	return hgr_pthread_join(threads[taskID][nodeID]);
	return -1;
}

int hgr_pthread_join(pthread_t thread){
	unsupported();
// 	return pthread_join(thread, 0);
	return -1;
}

int hgr_pthread_create(pthread_t *thread, const pthread_attr_t *attr,
                          void *(*start_routine) (void *), void *arg){
	unsupported();
// 	return pthread_create(thread, NULL, start_routine, arg);
	return -1;
}

void hgr_wait_for_period (int taskID){
	unsupported();
// 	log("Task %d waiting for period...\n",taskID);
// 	ptask_wait_for_period();
}

void hgr_init_dependency(pthread_mutex_t *mutex, const pthread_mutexattr_t *attr){
	unsupported();
// 	pthread_mutex_init(mutex, NULL);
}

void hgr_destroy_dependency(pthread_mutex_t *mutex){
	unsupported();
// 	pthread_mutex_destroy(mutex);
}

void hgr_wait_dependency(pthread_mutex_t *mutex){
	unsupported();
// 	pthread_mutex_lock(mutex);	
}

void hgr_release_dependency(pthread_mutex_t *mutex){
	unsupported();
// 	pthread_mutex_unlock(mutex);	
}

void hgr_exit(){
	unsupported();
// 	pthread_exit(NULL);
}


