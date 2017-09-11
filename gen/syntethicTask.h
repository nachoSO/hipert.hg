#ifndef synth_H
#define synth_H

void *G4(void *);
typedef void *(*task_fn)(void *);

typedef struct _task_table_item_s
{
	char fn_name[256];
	task_fn fn_ptr;
	tspec period;
	tspec rdline;
	int priority;
	int processor;
	int act_flag;
} task_table_item_t;

task_table_item_t functions[] = {
	{.fn_name = "G4", .fn_ptr = G4 },
};


#endif

