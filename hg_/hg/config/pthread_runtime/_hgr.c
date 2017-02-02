#include <unistd.h>
#include "_hgr.h"

int taskID;

int hgr_task_creator(int _taskID, tspec period,tspec rdline,int priority,int processor,int act_flag,void* name_task){
	taskID=_taskID;
	tpars tparam = TASK_SPEC_DFL;
	tparam = (tpars) {
		.period=period, 
		.rdline=rdline,
		.priority = priority, .processor = processor,
		.act_flag = act_flag,
		.measure_flag=1
	};
	int index = ptask_create_param(name_task, &tparam);;
	return index;
}

int ROUND_UP(unsigned long int a, unsigned long b){

	int div=a/b;
	if(((a ^ b) >=0) && (a%b !=0))
		div++;
	return div;
}

long int calc_data_size(long int size,char * data_granularity){

	long int data_size=size;
	if(!strcmp(data_granularity,"KB"))
		data_size=data_size*1024;
	else if(!strcmp(data_granularity,"MB"))
		data_size=data_size*1024*1024;
	else if(!strcmp(data_granularity,"GB"))
		data_size=data_size*1024*1024*1024;
	return data_size;
}

int mhz(){

	FILE *fp;
	char path[1035];

	/* Open the command for reading. */
	fp = popen(LMPATH, "r");
	if (fp == NULL) {
	printf("Failed to run command\n" );
	exit(1);
	}

	while (fgets(path, sizeof(path)-1, fp) != NULL);

	pclose(fp);
	int res=(int) strtol(path, (char **)NULL, 10);

	return res;
}

char* cmd_system(const char* command){

    char* result = "";
    FILE *fpRead;
    fpRead = popen(command, "r");
    char buf[1024];
    memset(buf,'\0',sizeof(buf));
    while(fgets(buf,1024-1,fpRead)!=NULL)
    {
        result = buf;
    }
    if(fpRead!=NULL)
       pclose(fpRead);

    return result;
}

int cache_size()
{
	int cache_size=0;
	int L1=atoi(cmd_system("cat /sys/devices/system/cpu/cpu0/cache/index1/size | sed 's/\K//'"));
	int L2=atoi(cmd_system("cat /sys/devices/system/cpu/cpu0/cache/index2/size | sed 's/\K//'"));
	int L3=atoi(cmd_system("cat /sys/devices/system/cpu/cpu0/cache/index3/size | sed 's/\K//'"));

	//printf("L3 %d | l2 %d | l1 %d\n",L3,L2,L1);
	if(L3!=0)
		cache_size=L3*1024;
	else if(L2!=0)
		cache_size=L2*1024;
	else if(L1!=0)
		cache_size=L1*1024;

	return cache_size;
}

SPARSE_node_t * hgr_load_SPARSE_node(long int data_size, char *data_granularity, int type,int granularity,int stride,long double wcet,double FREQUENCY,int nodeID){

	SPARSE_node_t *node=(SPARSE_node_t *) malloc (sizeof(SPARSE_node_t));
	(*node).granularity = granularity; 
	(*node).data_size = calc_data_size(data_size,data_granularity);
	(*node).stride = stride;
	(*node).wcet=wcet;
	(*node).phi=(*node).data_size/granularity;
	(*node).nodeID=nodeID;
	strcpy((*node).data_granularity,data_granularity);

	
	long double wcet_cycles = ((wcet*1000*1000*FREQUENCY)-4);
	long double ro=(wcet_cycles/((*node).data_size/granularity));
	unsigned long int beta = ceil(ro); /* num iterations */ 
	(*node).wcet_cycles=beta;
	if(granularity==1){
		(*node).char_data_ptr   = (char *)malloc(sizeof(char)*((*node).data_size/granularity));
	}else if(granularity==4){
		(*node).int_data_ptr    = (int *)malloc(sizeof(int)*((*node).data_size/granularity));
	}else if(granularity==8){
		(*node).double_data_ptr   = (double *)malloc(sizeof(double)*((*node).data_size/granularity));
	}else if(granularity==16){
		(*node).long_double_data_ptr = (long double *)malloc(sizeof(long double)*((*node).data_size/granularity));
	}
	
	return node;
}

PREM_node_t * hgr_load_PREM_node(long int data_size, char *data_granularity, int type,int granularity,long double wcet,double FREQUENCY,int nodeID){

	PREM_node_t *node=(PREM_node_t *) malloc (sizeof(PREM_node_t));
	(*node).granularity = granularity; 
	(*node).data_size = calc_data_size(data_size,data_granularity);
	(*node).wcet=wcet;
	(*node).nodeID=nodeID;
	strcpy((*node).data_granularity,data_granularity);

	(*node).char_data_ptr   = (char *)malloc(sizeof(char)*((*node).data_size));
	long double wcet_cycles = ((wcet*1000*1000*FREQUENCY));
	unsigned long int beta = ceil(wcet_cycles); /* num iterations */ 
	(*node).wcet_cycles=beta;


	int C_SIZE=cache_size();

	/* Mem data_size + Exec cycles */
	int i=0;
	(*node).num_copies=ROUND_UP((*node).data_size,C_SIZE);
	(*node).data_size_pointer= (unsigned long int *)malloc(sizeof(unsigned long int)*((*node).num_copies));

	if((*node).num_copies<=1){
		(*node).data_size_pointer[0]=(*node).data_size;
		(*node).wcet_per_cycle=(*node).wcet_cycles;
	}else{
		for(i=0;i<(*node).num_copies-1;i++)
			(*node).data_size_pointer[i]=C_SIZE;
		(*node).data_size_pointer[i++]=(*node).data_size-(C_SIZE*((*node).num_copies-1));
		(*node).wcet_per_cycle=(*node).wcet_cycles/((*node).num_copies);
	}

	return node;
}

//This function generates a size number of randoms within limit value
unsigned long int * random_generator(long int limit, long int size) {

	srand((unsigned)time(NULL));
	unsigned long int *random_pointer=(unsigned long int *) malloc (sizeof(unsigned long int)*size);
	long int divisor = RAND_MAX/(limit+1);

	int randVal,i;
	for(i=0;i<size;i++){
		do { 
			randVal = rand() / divisor;
		} while (randVal > limit);
		random_pointer[i]=randVal;
	}

	return random_pointer;
}

//This function generates the stride values
unsigned long int * stride_generator(int stride, long int size) {

	unsigned long int i;
	unsigned long int *strides=(unsigned long int *) malloc (sizeof(unsigned long int)*size);

	for(i=0;i<size;i++)
		strides[i]=(i*stride)%size;

	return strides;
}

void dropCaches(){

	FILE *fp;
	fp = fopen("/proc/sys/vm/drop_caches", "w");
	if (fp == NULL) {
	    printf("error %d: %s\n", errno, strerror(errno));
	    // error handling, exit or return
	}
	fprintf(fp, "3"); 
	fclose(fp);
}

/*Wrappers*/
void hgr_init(int policy, global_policy global, sem_protocol protocol){

	system("cpufreq-set -g performance");
	system("echo 1 > /sys/devices/system/cpu/intel_pstate/no_turbo");

	threads=(pthread_t *) malloc (TOTAL_TASK*sizeof(pthread_t));

	hgr_init_dependency(&premMutex, NULL);
	ptask_init(policy, global, protocol);

}

void hgr_destroy(){

	hgr_destroy_dependency(&premMutex);
}

void hgr_prepare_task(int taskID, int numNodes){

	threads[taskID]=(pthread_t *) malloc (numNodes*sizeof(pthread_t));
}

void hgr_free_task(int taskID){

	free(threads[taskID]);
}

int hgr_thread_create(int taskID, int nodeID, const pthread_attr_t *attr,
                          void *(*start_routine) (void *), void *arg){

	return hgr_pthread_create(&threads[taskID][nodeID], NULL, start_routine, (void *)nodeID);
}

int hgr_thread_join(int taskID, int nodeID){

	return hgr_pthread_join(threads[taskID][nodeID]);
}


int hgr_pthread_join(pthread_t thread){

	return pthread_join(thread, 0);
}

int hgr_pthread_create(pthread_t *thread, const pthread_attr_t *attr,
                          void *(*start_routine) (void *), void *arg){

	return pthread_create(thread, NULL, start_routine, arg);
}

void hgr_exit(){

	pthread_exit(NULL);
}

void hgr_wait_for_period (int taskID){
	printf("Task %d waiting for period...\n",taskID);
	ptask_wait_for_period();
}

void hgr_init_dependency(pthread_mutex_t *mutex, const pthread_mutexattr_t *attr){

	pthread_mutex_init(mutex, NULL);
}

void hgr_destroy_dependency(pthread_mutex_t *mutex){

	pthread_mutex_destroy(mutex);
}


void hgr_wait_dependency(pthread_mutex_t *mutex){

	pthread_mutex_lock(mutex);	
}

void hgr_release_dependency(pthread_mutex_t *mutex){

	pthread_mutex_unlock(mutex);	
}




