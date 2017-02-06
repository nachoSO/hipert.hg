// File automatically generated
struct gomp_task;
struct gomp_tdg_t {
	unsigned long id;
	struct gomp_task *task;
	short offin;
	short offout;
	char nin;
	char nout;
	signed char cnt;
	int map;
	long task_counter;
	long task_counter_end;
	long runtime_counter;
};

// TDG extracted from '_cluster_fn_1_target_fn'
struct gomp_tdg_t gomp_tdg_0[4] = {
	{.id = 13, .task = 0, .offin = 0, .offout = 0, .nin = 0, .nout = 0, .cnt = -1, .map = -1, .task_counter=0, .task_counter_end=0, .runtime_counter=0},
	{.id = 25, .task = 0, .offin = 0, .offout = 0, .nin = 0, .nout = 0, .cnt = -1, .map = -1, .task_counter=0, .task_counter_end=0, .runtime_counter=0},
	{.id = 37, .task = 0, .offin = 0, .offout = 0, .nin = 0, .nout = 0, .cnt = -1, .map = -1, .task_counter=0, .task_counter_end=0, .runtime_counter=0},
	{.id = 49, .task = 0, .offin = 0, .offout = 0, .nin = 0, .nout = 0, .cnt = -1, .map = -1, .task_counter=0, .task_counter_end=0, .runtime_counter=0},
};
unsigned short gomp_tdg_ins_0[] = {
	
};
unsigned short gomp_tdg_outs_0[] = {
	
};
// All TDGs are store in a single data structure
unsigned gomp_num_tdgs = 1;

struct gomp_tdg_t *gomp_tdg[1] = {
	gomp_tdg_0,
};

unsigned short *gomp_tdg_ins[1] = {
	gomp_tdg_ins_0,
};

unsigned short *gomp_tdg_outs[1] = {
	gomp_tdg_outs_0,
};

unsigned gomp_tdg_ntasks[1] = {
	4,
};
unsigned gomp_maxI[1] = {
	4,
};
unsigned gomp_maxT[1] = {
	3,
};
