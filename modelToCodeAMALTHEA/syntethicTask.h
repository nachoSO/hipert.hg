#ifndef synth_H
#define synth_H

void *Task_1ms(void *);
void *Angle_Sync(void *);
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
	{.fn_name = "Task_1ms", .fn_ptr = Task_1ms },
	{.fn_name = "Angle_Sync", .fn_ptr = Angle_Sync },
};


#endif

