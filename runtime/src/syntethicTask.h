#ifndef synth_H
#define synth_H

void *G1(void *);
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
	{.fn_name = "G1", .fn_ptr = G1 },
};


#endif

