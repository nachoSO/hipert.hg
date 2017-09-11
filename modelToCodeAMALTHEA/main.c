#include <stdio.h>
#include "hgr.h"
#include "syntethicTask.h"
#include <errno.h>
//#define PARAM_SIZE 7
//#define TEST_SIZE 1
//double FREQUENCY=0;


int gen_main(int argc, char *argv[]){
	int index=0;

	hgr_init(SCHED_OTHER, PARTITIONED, PRIO_CEILING);
 	//period,deadline,priority,proccessor,task_name
	index=hgr_task_creator(0,tspec_from(1000, MILLI),tspec_from(1000, MILLI),15,1,NOW,Task_1ms);
	index=hgr_task_creator(1,tspec_from(6660, MILLI),tspec_from(6660, MILLI),14,1,NOW,Angle_Sync);
	
	hgr_task_joinPTask(index);
	
	hgr_destroy();
	return 0;
}	


