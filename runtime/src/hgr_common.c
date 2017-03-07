#include "hgr.h"

int ROUND_UP(unsigned long int a, unsigned long b){
	log("\n");
	int div=a/b;
	if(((a ^ b) >=0) && (a%b !=0))
		div++;
	return div;
}

long int calc_data_size(long int size,char * data_granularity){
	log("\n");
	long int data_size=size;
	if(!strcmp(data_granularity,"KB"))
		data_size=data_size*1024;
	else if(!strcmp(data_granularity,"MB"))
		data_size=data_size*1024*1024;
	else if(!strcmp(data_granularity,"GB"))
		data_size=data_size*1024*1024*1024;
	return data_size;
}

SPARSE_node_t * hgr_load_SPARSE_node(long int data_size, char *data_granularity, int type,int granularity,int stride,long double wcet,double FREQUENCY,int nodeID){
	log("\n");
	SPARSE_node_t *node=(SPARSE_node_t *) malloc (sizeof(SPARSE_node_t));
	(*node).granularity = granularity; 
	(*node).data_size = calc_data_size(data_size,data_granularity);
	(*node).stride = stride;
	(*node).wcet=wcet;
	(*node).phi=(*node).data_size/granularity;
	(*node).nodeID=nodeID;
	strcpy((*node).data_granularity,data_granularity);

	
	long double wcet_cycles = ((wcet*1000*1000*FREQUENCY)-4);
	long double ro=(wcet_cycles/((*node).data_size/granularity));
	unsigned long int beta = ceil(ro); /* num iterations */ 
	(*node).wcet_cycles=beta;
	if(granularity==1){
		(*node).char_data_ptr   = (char *)malloc(sizeof(char)*((*node).data_size/granularity));
	}else if(granularity==4){
		(*node).int_data_ptr    = (int *)malloc(sizeof(int)*((*node).data_size/granularity));
	}else if(granularity==8){
		(*node).double_data_ptr   = (double *)malloc(sizeof(double)*((*node).data_size/granularity));
	}else if(granularity==16){
		(*node).long_double_data_ptr = (long double *)malloc(sizeof(long double)*((*node).data_size/granularity));
	}
	
	return node;
}

PREM_node_t * hgr_load_PREM_node(long int data_size, char *data_granularity, int type,int granularity,long double wcet,double FREQUENCY,int nodeID){
	log("\n");
	PREM_node_t *node=(PREM_node_t *) malloc (sizeof(PREM_node_t));
	(*node).granularity = granularity; 
	(*node).data_size = calc_data_size(data_size,data_granularity);
	(*node).wcet=wcet;
	(*node).nodeID=nodeID;
	strcpy((*node).data_granularity,data_granularity);

	(*node).char_data_ptr   = (char *)malloc(sizeof(char)*((*node).data_size));
	long double wcet_cycles = ((wcet*1000*1000*FREQUENCY));
	unsigned long int beta = ceil(wcet_cycles); /* num iterations */ 
	(*node).wcet_cycles=beta;

	int C_SIZE=cache_size();

	/* Mem data_size + Exec cycles */
	int i=0;
	(*node).num_copies=ROUND_UP((*node).data_size,C_SIZE);
	(*node).data_size_pointer= (unsigned long int *)malloc(sizeof(unsigned long int)*((*node).num_copies));

	if((*node).num_copies<=1){
		(*node).data_size_pointer[0]=(*node).data_size;
		(*node).wcet_per_cycle=(*node).wcet_cycles;
	}else{
		for(i=0;i<(*node).num_copies-1;i++)
			(*node).data_size_pointer[i]=C_SIZE;
		(*node).data_size_pointer[i++]=(*node).data_size-(C_SIZE*((*node).num_copies-1));
		(*node).wcet_per_cycle=(*node).wcet_cycles/((*node).num_copies);
	}

	return node;
}

//This function generates an index randomly with size number of randoms within limit value (limit&size = data_size/step)
unsigned long int * random_generator(long int limit, long int size) {
	log("\n");
	srand((unsigned)time(NULL));
	unsigned long int *random_pointer=(unsigned long int *) malloc (sizeof(unsigned long int)*size);
	long int divisor = RAND_MAX/(limit+1);

	int randVal,i;
	for(i=0;i<size;i++){
		do { 
			randVal = rand() / divisor;
		} while (randVal > limit);
		random_pointer[i]=randVal;
	}

	return random_pointer;
}

//This function generates the stride values (size = data_size/stride)
unsigned long int * stride_generator(int stride, long int size) {
	log("\n");
	unsigned long int i;
	unsigned long int *strides=(unsigned long int *) malloc (sizeof(unsigned long int)*size);

	for(i=0;i<size;i++)
		strides[i]=(i*stride)%size;

	return strides;
}
