#include <stdio.h>
#include "hgr.h"
#include "syntethicTask.h"

int main(int argc, char *argv[]){
	int index=0;
	
	hgr_init_dependency(&premMutex, NULL);
	hgr_ptask_init(SCHED_OTHER, PARTITIONED, PRIO_CEILING);
 	//period,deadline,priority,proccessor,task_name
	index=hgr_task_creator(tspec_from(1000, MILLI),tspec_from(1000, MILLI),50,0,NOW,G1);
	index=hgr_task_creator(tspec_from(2000, MILLI),tspec_from(2000, MILLI),60,1,NOW,G2);
	
	hgr_pthread_join(ptask_get_threadid(index));
	hgr_destroy_dependency(&premMutex);

	return 0;
}



