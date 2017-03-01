#include <unistd.h>
#include <math.h>

#include "_hgr.h"
#include "shared_defs.h"

/*Wrappers*/

void hgr_init(int policy, global_policy global, sem_protocol protocol){
	log("\n");
}

void hgr_destroy(){
	log("\n");
}

struct mppa_target_param_1;
struct mppa_target_param_1
{
  int n;
  omp_param_t params[6];
};
static void _host_fn_1(omp_param_t *_params)
{
	printf("FATAL ERROR\n");
}

int numtasks = 0;

int hgr_task_creator(int _taskID, tspec period,tspec rdline,int priority,int processor,int act_flag)
{
	log("\n");
	log("taskID is %d\n", _taskID);
	if(numtasks == 2)
	{
		log("The GUMP runtime currently doesn't support more than 2 simulaneous RT-task on the accelerator!\n");
		return -1;
	}
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
	
	GOMP_target(0, (void (*)(void *))_host_fn_1, (void *)1, (void *)mppa_data_1, numtasks, 65535U);
	// log("before invoking GOMP_target_wait\n");
	// GOMP_target_wait(0, (void *)mppa_data_1, numtasks);
	log("returning %d\n", numtasks+1);
	return numtasks++;
}

void hgr_task_joinall()
{
	log("\n");
	int i;
	int end = numtasks;
	if(numtasks >= 2)
	{
		warning("The GUMP runtime currently doesn't support more than 2 simulaneous RT-task on the accelerator!\n");
		end = numtasks;
	}
	
	for(i=0; i<end; i++)
		// data param is unused
		GOMP_target_wait(0, (void *) 0x0, i);
}