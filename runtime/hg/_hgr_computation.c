#include <unistd.h>
#include <stdlib.h>
#include <stdio.h>
#include "_hgr.h"
#include <math.h>

pthread_mutex_t premMutex;

void set_max_priority(){
	struct sched_param sp;
	memset ( &sp,0,sizeof(sp));
	sp.sched_priority=99;
	sched_setscheduler(0,SCHED_FIFO,&sp);
}


//cat /sys/devices/system/cpu/cpu0/cache/index2/size
//PREM: Memory phase (ASM implementation)
inline void hgr_PREM_compute_node(PREM_node_t *node, void *ptr_dst){
	//set_max_priority();

	//printf("ds: %ld | c_s %d | round up: %d| div: %d \n ",(*node).data_size,CACHE_SIZE,ROUND_UP((*node).data_size,CACHE_SIZE),(*node).data_size/CACHE_SIZE);
	struct timespec t_start={0,0}, t_end={0,0};
	long double timeTaken=0;
	clock_gettime(CLOCK_MONOTONIC, &t_start);
	int num_copies=(*node).num_copies;
	long int *data_size_pointer=(*node).data_size_pointer;
	while(num_copies--){
		//Mem phase

		hgr_wait_dependency(&premMutex);
		memcpy(ptr_dst, (*node).char_data_ptr, *(data_size_pointer++));
		hgr_release_dependency(&premMutex);
		
		//Execution phase
		__asm__ __volatile__
		("loop:;"
		"dec %0;"
		"jnz loop;"
		: 
		: "b" ((*node).wcet_per_cycle) 
		);
	}
	
	clock_gettime(CLOCK_MONOTONIC, &t_end);
	
	timeTaken=(( ((double)t_end.tv_sec + 1.0e-9*t_end.tv_nsec) - ((double)t_start.tv_sec + 1.0e-9*t_start.tv_nsec)))*1000;
	printf("P | C+M Phase: %Lf | Node: %d \n",timeTaken,(*node).nodeID);
}


//PREM: Computation phase (clock implementation)
long double hgr_PREM_compute_node_clock (PREM_node_t *node){
	struct timespec tstart={0,0}, tend={0,0};
	clock_gettime(CLOCK_MONOTONIC, &tstart);
	long double timeTaken=(( ((double)tend.tv_sec + 1.0e-9*tend.tv_nsec) - ((double)tstart.tv_sec + 1.0e-9*tstart.tv_nsec)))*1000;
	long int cond=1;

	if (timeTaken>=(*node).wcet)
		cond=0;
	while(cond){
		if (timeTaken>=(*node).wcet)
			cond=0;
		
		timeTaken=(( ((double)tend.tv_sec + 1.0e-9*tend.tv_nsec) - ((double)tstart.tv_sec + 1.0e-9*tstart.tv_nsec)))*1000;
		clock_gettime(CLOCK_MONOTONIC, &tend);
	}
	return timeTaken;
}

void hgr_SPARSE_compute_node2 (SPARSE_node_t *node,unsigned long int * pointer){
	set_max_priority();
	
	struct timespec t_start={0,0}, t_end={0,0};
	long double timeTaken=0;
	clock_gettime(CLOCK_MONOTONIC, &t_start);
	unsigned long int i=(*node).phi;
	__asm__ __volatile__
		("looop:;"
		:
		:
		);
		//memory phase
		if((*node).granularity==1){
			volatile char a   = (*node).char_data_ptr[*pointer++];
		}else if((*node).granularity==4){
			volatile int a    = (*node).int_data_ptr[*pointer++];
		}else if((*node).granularity==8){
			volatile double a   = (*node).double_data_ptr[*pointer++];
		}else if((*node).granularity==16){
			volatile long double a = (*node).long_double_data_ptr[*pointer++];
		}
		//printf("%d\n",i);
		//execution phase
		__asm__ __volatile__
		("loop4:;"
		"dec %0;"
		"jnz loop4;"
		"dec %1;"
		"jnz looop;"
		: "=r" (i)
		: "a" ((*node).wcet_cycles)
		);
	clock_gettime(CLOCK_MONOTONIC, &t_end);
	timeTaken=(( ((double)t_end.tv_sec + 1.0e-9*t_end.tv_nsec) - ((double)t_start.tv_sec + 1.0e-9*t_start.tv_nsec)))*1000;
	printf("C+M Phase: %Lf | Node: %d \n",timeTaken,(*node).nodeID);	

}

void hgr_mem_ex(SPARSE_node_t *node,unsigned long int * pointer){
	long int i=(*node).phi;

	while(i--){
		//memory phase
		if((*node).granularity==1){
			volatile char a   = (*node).char_data_ptr[*pointer++];
		}else if((*node).granularity==4){
			volatile int a    = (*node).int_data_ptr[*pointer++];
		}else if((*node).granularity==8){
			volatile double a   = (*node).double_data_ptr[*pointer++];
		}else if((*node).granularity==16){
			volatile long double a = (*node).long_double_data_ptr[*pointer++];
		}
		//execution phase
		__asm__ __volatile__
		("loop2:;"
		"dec %0;"
		"jnz loop2;"
		: 
		: "b" ((*node).wcet_cycles) 
		);
	}
}


//SPARSE: Memory & Computation phase
void hgr_SPARSE_compute_node (SPARSE_node_t *node,unsigned long int * pointer){

	struct timespec t_start={0,0}, t_end={0,0};
	long double timeTaken=0;
	clock_gettime(CLOCK_MONOTONIC, &t_start);

	hgr_mem_ex(node,pointer);
	
	clock_gettime(CLOCK_MONOTONIC, &t_end);
	timeTaken=(( ((double)t_end.tv_sec + 1.0e-9*t_end.tv_nsec) - ((double)t_start.tv_sec + 1.0e-9*t_start.tv_nsec)))*1000;
	printf("S | C+M Phase: %Lf | Node: %d \n",timeTaken,(*node).nodeID);	

}
