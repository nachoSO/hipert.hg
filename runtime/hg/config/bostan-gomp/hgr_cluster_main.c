#include "_hgr.h"
#include "shared_defs.h"
#include "syntethicTask.h"

extern task_table_item_t functions[];
extern void (*omp_functions[1 + TOTAL_TASK])(omp_param_t *);

/* Kernels: 0-> init, 1->task creator */
static void _cluster_fn_0(omp_param_t *_params)
{
	log("\n");
}
struct  gomp_ol_args_1_t
{
	int *taskID;
//   int **a;
};

extern int GOMP_single_start(void);
extern void GOMP_barrier();
static void _ol__cluster_fn_0_1(struct gomp_ol_args_1_t *ol_data_1)
{
// 	log("\n");
	if (GOMP_single_start())
  {
		int taskID = (int) ol_data_1->taskID;
// 		log("Inside single taskID %d\n", taskID);
		// Now, we can run the (RT)-Task function
		functions[taskID].fn_ptr(NULL);
  }
	GOMP_barrier();
}

extern void GOMP_parallel_start (void *, void *, int);
extern void GOMP_parallel_end (void);
extern void GOMP_set_tdg_id(unsigned int new_tdg_id);
extern void GOMP_unset_tdg_id(void);

static void _cluster_fn_1(omp_param_t *_params)
{
	int taskID;
	log("\n");
	
  taskID = * (int *) _params[0].ptr;
// 	log("taskID is %d\n", taskID);	
	
	functions[taskID].period = * (tspec *) _params[1].ptr;
	functions[taskID].rdline = * (tspec *) _params[2].ptr;
	functions[taskID].priority = * (int *) _params[3].ptr;
	functions[taskID].processor = * (int *) _params[4].ptr;
	functions[taskID].act_flag = * (int *) _params[5].ptr;	
	
	if(taskID < TOTAL_TASK)
	{
		
		GOMP_set_tdg_id(taskID);
  
    struct gomp_ol_args_1_t ol_data_1;
    ol_data_1.taskID = (int *) taskID;
// 		log("Creating parreg for '%s'\n", functions[taskID].fn_name);
    GOMP_parallel_start((void (*)(void *))_ol__cluster_fn_0_1, &ol_data_1, 0);
    _ol__cluster_fn_0_1(&ol_data_1);
    GOMP_parallel_end();
// 		log("Out of parreg for '%s'\n", functions[taskID].fn_name);
		
		GOMP_unset_tdg_id();
	}
	else
	{
		// TODO fatal error
	}
	
}

void (*omp_functions[1 + TOTAL_TASK])(omp_param_t *) = {
	[0] = _cluster_fn_0,
	[1] = _cluster_fn_1	
};

int main(int argc, char **argv)
{
  return 0;
}
