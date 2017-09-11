#include <stdio.h>
#include "hgr.h"
#include "syntethicTask.h"
#include <errno.h>
//#define PARAM_SIZE 7
//#define TEST_SIZE 1
//double FREQUENCY=0;


int gen_main(int argc, char *argv[]){
	int index=0;

	hgr_init(SCHED_FIFO, GLOBAL, PRIO_CEILING);
 	//period,deadline,priority,proccessor,task_name
	index=hgr_task_creator(0,tspec_from(700, MILLI),tspec_from(700, MILLI),60,1,NOW,G4);
	
	hgr_task_joinPTask(index);
	
	hgr_destroy();
	return 0;
}	


