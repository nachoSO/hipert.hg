#include <unistd.h>
#include <stdlib.h>
#include <stdio.h>
#include "_hgr.h"

//PREM: Memory phase
void hgr_prem_memphase(memory_access_t *data){
	int i;
	int step= (*data).step; 
	long int phi=(*data).data_size/step;
	hgr_wait_dependency(&premMutex);
	for(i=0;i<phi;i++){
		if(step==1){
			volatile char a   = (*data).char_data_ptr[i];
		}else if(step==4){
			volatile int a    = (*data).int_data_ptr[i];
		}else if(step==8){
			volatile double a   = (*data).double_data_ptr[i];
		}else if(step==16){
			volatile long double a = (*data).long_double_data_ptr[i];
		}
	}
	hgr_release_dependency(&premMutex);
}

//PREM: Computation phase
void hgr_compute_task (long int ncycles){
	//ncycles is calculated based on the tame taken to execute the benchComputer function
	long int i=0;
	for(i=0;i<ncycles;i++);
}

//SPARSE: Memory & Computation phase
void hgr_compute_sparse_task (memory_access_t *data, unsigned long int ncycles){
	long int i,j;
	unsigned long int * pointer; //points to the memory access stride
	int step=(*data).step;
	unsigned long int phi=(*data).data_size/step;
	int stride=(*data).stride;
	unsigned long int ro=ncycles/phi;

	printf("ncycles= %lu ,phi=%lu, ro=%lu, step=%d \n",ncycles,phi,ro,step);
	if(stride==0){ //random
		pointer=random_generator(phi,phi); //number limit, size array
	}else{ //sequential + stride
		pointer=stride_generator(stride,phi); //stride, size array
	}
	
	for(i=0;i<phi;i++){
		//memory phase
		if(step>0 && step<=8){
			volatile char a   = (*data).char_data_ptr[pointer[i]];
		}else if(step>=9 && step<=16){
			volatile int a    = (*data).int_data_ptr[pointer[i]];
		}else if(step>=17 && step<=32){
			volatile long a   = (*data).double_data_ptr[pointer[i]];
		}else if(step>=33 && step<=64){
			volatile double a = (*data).long_double_data_ptr[pointer[i]];
		}
		//execution phase
		for(j=0;j<ro;j++);
	}
}
