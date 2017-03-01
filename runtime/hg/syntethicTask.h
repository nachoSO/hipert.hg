
#ifndef synth_H
#define synth_H
// void *G1_1();
// void *G1_2();
// void *G1_3();
// 
// //This function calculates
// void G1_pre_load_tasks();
// void G1_create_dependency();
// void G1_destroy_dependency();
void G1(void *);
// void *G5_1();
// void *G5_2();
// void *G5_3();
// 
// //This function calculates
// void G5_pre_load_tasks();
// void G5_mutexCreate();
// void G5_mutexDestroy();
// void G5(void *);
// void *G6_1();
// void *G6_2();
// void *G6_3();
// 
// //This function calculates
// void G6_pre_load_tasks();
// void G6_create_dependency();
// void G6_destroy_dependency();
// void G6(void *);
// void *G7_1();
// void *G7_2();
// void *G7_3();
// 
// //This function calculates
// void G7_pre_load_tasks();
// void G7_mutexCreate();
// void G7_mutexDestroy();
// void G7();

typedef void (*task_fn)(void *);

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
	{ .fn_name = "G1", .fn_ptr = G1 }/*,
	{ .fn_name = "G6", .fn_ptr = G6 }*/
	};


#endif

