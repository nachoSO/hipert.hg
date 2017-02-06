#include <stdio.h>

int main (int argc, char **argv)
{
	int a[1], b[1];

	GOMP_init(0 /* device id */);

	printf("==== EXECUTING ON COMPUTATION CLUSTERS ====\n");
	
	#pragma omp target map(to:a[0:1]) map(from:b[0:1])
	{
		#pragma omp parallel
 		#pragma omp single
 		#pragma omp task shared(a, b)
		{
			printf("Inside task\n");
			b[0] = a[0] + 1;
		}
	} // target
	
	printf("\n--------------------------------------\n\n");
	
	GOMP_deinit(0);

	return 0;
}
