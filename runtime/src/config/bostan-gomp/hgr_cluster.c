// #include <unistd.h>
#include "_hgr.h"
#include "hgr_dependencies.h"
// #include <math.h>

int mhz(){
	log("\n");
	return 500; // FIXME
}

// char* cmd_system(const char* command){
// 	log("\n");
// //     char* result = "";
// //     FILE *fpRead;
// //     fpRead = popen(command, "r");
// //     char buf[1024];
// //     memset(buf,'\0',sizeof(buf));
// //     while(fgets(buf,1024-1,fpRead)!=NULL)
// //     {
// //         result = buf;
// //     }
// //     if(fpRead!=NULL)
// //        pclose(fpRead);
// // 
// //     return result;
// 	return "";
// }

int cache_size()
{
	int cache_size=0;
	int L1 = 32;
	int L2 = 0;
	int L3 = 0;

	//printf("L3 %d | l2 %d | l1 %d\n",L3,L2,L1);
	if(L3!=0)
		cache_size=L3*1024;
	else if(L2!=0)
		cache_size=L2*1024;
	else if(L1!=0)
		cache_size=L1*1024;

	return cache_size;
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
void hgr_prepare_task(int taskID, int numNodes)
{
	log("\n");
}

void hgr_free_task(int taskID)
{
	log("\n");
}

struct gomp_ol_args_0_t
{
};

extern void GOMP_task(void (*fn) (void *), void *data, void (*cpyfn) (void *, void *), long arg_size, long arg_align, int if_clause, unsigned flags __attribute__((unused)));

int hgr_thread_create(	int taskID, int nodeID, const void *attr,
			void *(*start_routine) (void *), void *arg)
{
	log("\n");
	static int thrID = 0;
	if(arg)
		warning("args passing to thread is not supported\n");
	
	struct gomp_ol_args_0_t ol_data_0;
// 	ol_data_0.a = (int *const *) a;
	unsigned long int gomp_task_flags_0 = 0x0;
	
	GOMP_task((void (*)(void *))start_routine, &ol_data_0,
					 (void (*)(void *, void *))0, // cpyfn
						0, 4, 1, // long arg_size, long arg_align, int if_clause
						gomp_task_flags_0
						//, 0, 1U // , void **depend, unsigned id
						);
	
 	return thrID++;
}

int hgr_thread_join(int taskID, int nodeID){
	unsupported();
// 	return hgr_pthread_join(threads[taskID][nodeID]);
	return -1;
}

void hgr_wait_for_period (int taskID){
	unsupported();
// 	log("Task %d waiting for period...\n",taskID);
// 	ptask_wait_for_period();
}

void hgr_init_dependency(hgr_dependency_t *dep, const void *attr){
	// log("\n");
}

void hgr_destroy_dependency(hgr_dependency_t *dep){
	// log("\n");
}

void hgr_wait_dependency(hgr_dependency_t *dep){
	// log("\n");
}

void hgr_release_dependency(hgr_dependency_t *dep){
	// log("\n");
}

void hgr_exit() {
	log("\n");
}


